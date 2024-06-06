package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import pages.components.PageComponent;
import pages.components.PaginationComponent;

public class SearchPage extends WebPage {

	private static final By perPage = By.id("numberOfRecordPerPage");
	private static final By allListings = By
			.cssSelector("#vet-result-section " + "table.cos-table-responsive tbody tr");
	private static final By byPages = By.cssSelector("#pa-pagination > ul");
	
	public final PaginationComponent pageSelect;

	public SearchPage(WebDriver driver) {
		super(driver);

		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofMillis(100)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.and(
				ExpectedConditions.visibilityOfAllElementsLocatedBy(allListings),
				ExpectedConditions.visibilityOfElementLocated(byPages)
		));
		
		pageSelect = new PaginationComponent(driver.findElement(byPages));
	}
	
	public SearchPage next() {
		if(pageSelect.getActivePage() < pageSelect.countPages())
			open(pageSelect.nextPage());
			
		return new SearchPage(driver);
	}

	public SearchPage setMaxPerPage() {
		open(driver.findElement(perPage), dd -> {
			Select dropdown = new Select(dd);
			dropdown.selectByIndex(dropdown.getOptions().size() - 1);
		});

		return new SearchPage(driver);
	}

	public SearchPage filterNewToOld() {
		open(driver.findElement(By.id("ddlVetJobFinder")), sortBy -> {
			Select sort = new Select(sortBy);
			sort.selectByIndex(7);
		});
		
		return new SearchPage(driver);
	}

	public List<Listing> getAllJobs() {
		List<WebElement> listings = driver.findElements(allListings);
		return listings.stream().map(Listing::new).toList();
	}

	public Listing getJob(int index) {
		List<WebElement> listings = driver.findElements(allListings);
		return new Listing(listings.get(index));
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
