import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import common.CsvWriter;
import common.Logger;
import common.RunProperties;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.LandingPage;
import pages.ListingPage;
import pages.SearchPage;

public class Main {

	private static final org.slf4j.Logger LOG = Logger.getInstance();
	
	private static ArrayList<String> lines = new ArrayList<>();
	private static RunProperties properties = RunProperties.getInstance();


	public static void main(String[] args) {
		WebDriver driver = configureDriver();
		 // add headers
		lines.add("Job Title,Company Name,Location,Date Posted,Link,Match Score");

		LandingPage start = new LandingPage(driver);
		SearchPage results = start.search(properties.getJobTitle(), properties.getLocation());

		// go through all pages & listings and return the ones that match our criteria
		results = results.filterNewToOld();
		int pageCount = results.getNumberOfPages();
		
		try {
			for (int i = 0; i < pageCount; i++) {
				SearchPage prev = results;
				results = scrapeListings(results);
				
				if (results.equals(prev)) {
					return;
				} else if (i < pageCount - 1) {
					results = results.goToNextPage();
				}
			}
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		} finally {
			CsvWriter.writeLiteral(properties.getOutputPath(), lines);
			driver.close();
		}		
	}
	
	/*
	public static void main(String[] args) {
		WebDriver driver = configureDriver();
		lines.add("Job Title,Company Name,Location,Date Posted,Match Score,Link"); // add headers

		LandingPage start = new LandingPage(driver);
		SearchPage results = start.search(properties.getJobTitle(), properties.getLocation());

		results = results.filterNewToOld();	
		try {
			scrapeListings(results);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			driver.close();
			CsvWriter.writeLiteral(properties.getOutputPath(), lines);
		}
	}
	*/

	private static WebDriver configureDriver() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		return driver;
	}
	
	private static String listingToCsvRow(SearchPage.Listing listing) {
			StringBuilder row = new StringBuilder();
			
			row.append("\"" + listing.getJobTitle() + "\",");
			row.append("\"" + listing.getCompanyName() + "\",");
			row.append("\"" + listing.getLocation() + "\",");
			row.append(listing.getDatePosted() + ",");
			row.append("\"" + listing.getLink() + "\"");
			
			return row.toString();
	}
	
	public static SearchPage scrapeListings(SearchPage results) {
		int numListings = results.getAllJobs().size();
		for (int j = 0; j < numListings; j++) {
			SearchPage.Listing listing = results.getJob(j);

			if (!listing.getDatePosted().isBefore(properties.getDateFilter())) {
				if (listing.getJobTitle().toLowerCase().contains("intern"))
					continue;
						
				LOG.info("Checking {}'s {} posting for a {}", 
						listing.getCompanyName(),
						listing.getDatePosted(),
						listing.getJobTitle());
				String info = listingToCsvRow(listing);
				
				ListingPage deets = listing.open();
				int score = deets.getDescription().keywordSearch(properties.getKeywords());
				results = deets.backToResults();

				lines.add(info.concat(",").concat(Integer.toString(score)));
			} else {
				break;
			}
		}
		
		return results;
	}
}
