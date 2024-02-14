import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import common.CsvWriter;
import common.RunProperties;
import components.Listing;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.LandingPage;
import pages.ResultsPage;

public class Main {

	private static HashMap<String, Integer> total = new HashMap<>();
	
	
	public static void main(String[] args) {
		RunProperties properties = RunProperties.getInstance();
		WebDriver driver = configureDriver();
		
		LandingPage start = new LandingPage(driver);
		ResultsPage results = start.search(properties.getJobTitle(), properties.getLocation());
		
		results = results.setMaxPerPage();
		int pageCount = results.pageCount;
		for(int i = 0; i < pageCount; i++) {
			getTally(results.getAllJobs(), properties.getDateFilter());
			
			if( i < pageCount - 1) {
				results = results.goToNextPage();
			}
		}		
		driver.close();
		
		CsvWriter csvFile = null;
		try {
			System.out.println("Writing output to " + System.getProperty("user.dir") + properties.getOutputPath());
			csvFile = new CsvWriter(properties.getOutputPath(), "Company Name", "Open Listings");			
			csvFile.mapToCsv(total, "\nFederal Contractor");
			
			properties.close();
			csvFile.close();
		} catch (IOException e) {
			System.out.println(e);
		} 
	}

	private static WebDriver configureDriver() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		return driver;
	}
	
	private static void getTally(List<Listing> listings, LocalDate excludeBefore) {
		for(Listing listing : listings) {			
			if(!listing.getDatePosted().isBefore(excludeBefore)) {
				String company = listing.getCompanyName();
				Integer numberOfListings = total.get(company);
				
				if(numberOfListings == null) {
					total.put(company, 1);
				} else {
					total.put(company, numberOfListings+1);
				}
			}
		}
	}
	
}
