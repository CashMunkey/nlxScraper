package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import pages.components.JobDescription;

public class ListingPage extends WebPage {

	private static By byDesc = By.id("ctl17_lblDesc");
	
	public ListingPage(WebDriver driver) {
		super(driver);
		
	    Wait<WebDriver> wait =
	            new FluentWait<>(driver)
	                .withTimeout(Duration.ofSeconds(30))
	                .pollingEvery(Duration.ofMillis(500))
	                .ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.presenceOfElementLocated(byDesc));
	}

	public SearchPage backToResults() {
		WebElement buttonSideBar = driver.findElement(By.id("btnLeftBacktoJobSearch"));
		if(buttonSideBar.isDisplayed()) {
			buttonSideBar.sendKeys(Keys.RETURN);
		} else {
			driver.findElement(By.id("btnBacktoJobSearch")).sendKeys(Keys.RETURN);
		}
		
		return new SearchPage(driver);
	}

	public JobDescription getDescription() {
		WebElement desc = driver.findElement(byDesc);
		return new JobDescription(desc);
	}
	
}
