package components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.ListingPage;

public class Listing {

	private WebElement title;
	private String company;
	private String location;
	private String date;
	
	public Listing(WebElement tr) {
		List<WebElement> cells = tr.findElements(By.cssSelector("td"));
				
		title = cells.get(0);
		company = cells.get(1).getText();
		location = cells.get(2).getText();
		date = cells.get(3).getText();
	}
	
	public ListingPage openListing(WebDriver driver) {
		title.click();
		return new ListingPage(driver);
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
