package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import pages.components.JobDescription;

public class ListingPage extends WebPage {

	private static By byDesc = By.cssSelector("table.cos-table-detail");
	private static By byPrevPageBottom = By.id("btnBacktoJobSearch");

	public ListingPage(WebDriver driver) {
		super(driver);

		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(45))
				.pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.and(
				ExpectedConditions.elementToBeClickable(byPrevPageBottom), 
				ExpectedConditions.visibilityOfElementLocated(byDesc)));
	}

	public SearchPage backToResults() {
		open(driver.findElement(byPrevPageBottom));

		return new SearchPage(driver);
	}

	public JobDescription getDescription() {
		WebElement desc = driver.findElement(byDesc);
		return new JobDescription(desc);
	}

}
