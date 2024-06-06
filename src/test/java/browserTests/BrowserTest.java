package browserTests;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.WebDriver;

import common.BrowserSwitch;
import pages.LandingPage;
import pages.ListingPage;
import pages.SearchPage;

class BrowserTest {
	protected static final Logger testLog = LogManager.getLogger(BrowserTest.class);
	protected WebDriver driver;
	
	private String jobTitle = "software engineer";
	private String location = "New York City, NY";
	
	@ParameterizedTest
	@EnumSource(Supported.class)
	void openAllListingsOnPage(Supported browser) {
		driver = BrowserSwitch.get(browser.toString());
		
		LandingPage start = new LandingPage(driver);
		SearchPage results = start.search(jobTitle, location);
		
		int numListings = results.getAllJobs().size();
		for (int j = 0; j < numListings; j++) {
			SearchPage.Listing listing = results.getJob(j);
			
			ListingPage deets = listing.open();
			results = deets.backToResults();		
			Assertions.assertEquals(numListings, results.getAllJobs().size(),
					"different search results after opening a listing");
		}			
	}
	
	@ParameterizedTest
	@EnumSource(Supported.class)
	void openPages(Supported browser) {
		driver = BrowserSwitch.get(browser.toString());
		
		LandingPage start = new LandingPage(driver);
		SearchPage results = start.search(jobTitle, location);
		
		int pageCount = Math.min(10, results.pageSelect.countPages());
		List<SearchPage.Listing> prevListings = null;
		for(int i = 0; i < pageCount; i++) {
			List<SearchPage.Listing> currentListings = results.getAllJobs();
			if(i != 0) {
				Assertions.assertNotEquals(prevListings, currentListings,
						"Listings didn't update when changing pagination");
			}
			
			prevListings = currentListings;
			
			if (i < pageCount - 1) {
				results = results.next();
			}
		}
	}
	
	@AfterEach
	void forceClose() {
		driver.close();
	}
	
	public enum Supported {
		Chrome, Firefox, Edge
	}
}
