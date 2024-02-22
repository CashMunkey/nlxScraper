package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import pages.components.PageComponent;

public class SearchPage extends WebPage {

	private static final By perPage = By.id("numberOfRecordPerPage");
	private static final By allListings = By
			.cssSelector("#vet-result-section " + "table.cos-table-responsive tbody tr");

	public SearchPage(WebDriver driver) {
		super(driver);

		try {
			Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofMillis(2000))
					.pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions
					.jsReturnsValue("var scrolling=true; window.scrollend=>scrolling=false; return !scrolling;"));
		} catch (TimeoutException e) {
			log.trace("search page didn't scroll");
		}
	}

	public SearchPage setMaxPerPage() {
		Select dropdown = new Select(driver.findElement(perPage));
		dropdown.selectByIndex(dropdown.getOptions().size() - 1);

		return new SearchPage(driver);
	}

	public SearchPage filterNewToOld() {
		Select sort = new Select(driver.findElement(By.id("ddlVetJobFinder")));
		sort.selectByIndex(7);
		return new SearchPage(driver);
	}

	public List<Listing> getAllJobs() {
		List<WebElement> listings = driver.findElements(allListings);
		return listings.stream()
				.map(tr -> new Listing(tr))
				.toList();
	}

	public Listing getJob(int index) {
		List<WebElement> listings = driver.findElements(allListings);
		return new Listing(listings.get(index));
	}

	public SearchPage goToNextPage() {
		driver.findElement(By.id("last")).sendKeys(Keys.RETURN);
		return new SearchPage(driver);
	}

	public int getNumberOfPages() {
		List<WebElement> pages = driver.findElements(By.cssSelector("#pagination > ul > li"));
		WebElement lastPage = pages.get(pages.size() - 1);

		return Integer.parseInt(lastPage.getText());
	}
	
	public class Listing extends PageComponent {

		private WebElement title;
		private String company;
		private String location;
		private String date;

		public Listing(WebElement tr) {
			super(tr);
			List<WebElement> cells = tr.findElements(By.cssSelector("td"));

			title = cells.get(0);
			company = cells.get(1).getText().replace("\nFederal Contractor", "");
			location = cells.get(2).getText();
			date = cells.get(3).getText();
		}
		
		public ListingPage open() {
			title.findElement(By.tagName("a")).sendKeys(Keys.RETURN);
	        
			return new ListingPage(driver);
		}

		public String getJobTitle() {			
			return title.getText();
		}
		
		public String getLink() {
			return title.findElement(By.tagName("a")).getAttribute("href");
		}
		
		public String getCompanyName() {
			return company;
		}

		public String getLocation() {
			return location;
		}

		public LocalDate getDatePosted() {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			return LocalDate.parse(date, formatter);
		}

	}

}
