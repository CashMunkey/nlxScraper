package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import components.Listing;

public class ResultsPage extends WebPage {

	private static final By perPage = By.id("numberOfRecordPerPage");

	public int pageCount;
	public int currentPage;
	
	public ResultsPage(WebDriver driver) {
		super(driver);
		
		pageCount = getNumberOfPages();
		currentPage = getCurrentPage();
	}

	public ResultsPage setMaxPerPage() {
		Select dropdown = new Select(driver.findElement(perPage));
		dropdown.selectByIndex(dropdown.getOptions().size() - 1);
		
		return new ResultsPage(driver);
	}
	
	public List<Listing> getAllJobs() {
		List<Listing> results = new ArrayList<>();
		List<WebElement> listings = driver.findElements(By.cssSelector("#vet-result-section "
				+ "table.cos-table-responsive tbody tr"));
		
		for(WebElement row : listings) {
			results.add(new Listing(row));
		}
		
		return results;
	}
	
	// TODO this should throw an exception, not return this
	public ResultsPage goToNextPage() {
		if(currentPage != pageCount) {
			driver.findElement(By.id("last")).sendKeys(Keys.RETURN);
			return new ResultsPage(driver);
		} else {
			return this;
		}
	}
	
	private int getNumberOfPages() {
		List<WebElement> pages = driver.findElements(By.cssSelector("#pagination > ul > li"));
		WebElement lastPage = pages.get(pages.size() - 1);
		
		return Integer.parseInt(lastPage.getText());
	}
	
	private int getCurrentPage() {
		WebElement page = driver.findElement(By.cssSelector("#pagination > ul > li.active"));
		return Integer.parseInt(page.getText());
	}
}
