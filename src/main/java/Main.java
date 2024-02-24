import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import common.CsvWriter;
import common.BrowserSwitch;
import common.RunProperties;
import pages.LandingPage;
import pages.ListingPage;
import pages.SearchPage;
import pages.SearchPage.Listing;

public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class);
	
	private static ArrayList<String> lines = new ArrayList<>();
	private static RunProperties properties = RunProperties.getInstance();
	
	private static WebDriver driver = configureDriver();
	
	public static void main(String[] args) {
		// add headers
		lines.add("Job Title,Company Name,Location,Date Posted,Link,Match Score"); 

		LandingPage start = new LandingPage(driver);
		SearchPage results = start.search(properties.getJobTitle(), properties.getLocation());
		results = results.filterNewToOld();	

		try {
			int pageCount = results.getNumberOfPages();
			
			for (int i = 0; i < pageCount; i++) {
				SearchPage prev = results;

				int numListings = results.getAllJobs().size();
				for (int j = 0; j < numListings; j++) {
					SearchPage.Listing listing = results.getJob(j);

					if (!listing.getDatePosted().isBefore(properties.getDateFilter())) {
						if (!exclude(listing.getJobTitle())) {					
							results = scrape(listing);
						}
					} else {
						break;
					}
				}
				
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

	private static WebDriver configureDriver() {
		WebDriver driver = BrowserSwitch.get(properties.getBrowser());
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		return driver;
	}
	
	private static String listingToCsvRow(Listing listing) {
		StringBuilder row = new StringBuilder();
		
		row.append("\"" + listing.getJobTitle() + "\",");
		row.append("\"" + listing.getCompanyName() + "\",");
		row.append("\"" + listing.getLocation() + "\",");
		row.append(listing.getDatePosted() + ",");
		row.append("\"" + listing.getLink() + "\"");
		
		return row.toString();
	}
	
	private static SearchPage scrape(Listing listing) {
		LOG.info("Checking {}'s {} posting for a(n) {}", 
				listing.getCompanyName(),
				listing.getDatePosted(),
				listing.getJobTitle());
		String info = listingToCsvRow(listing);
		
		ListingPage deets = listing.open();
		Integer score = deets.getDescription().keywordSearch(properties.getKeywords());
		
		lines.add(info.concat(",").concat(score.toString()));
		
		return deets.backToResults();
	}

	private static boolean exclude(String jobTitle) {
		for(String s : properties.getExcluded()) {
			if (jobTitle.toLowerCase().contains(s))
				return true;
		}
		return false;
	}

}
