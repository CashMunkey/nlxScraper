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

public class LandingPage extends WebPage {

	private static final String URL = "https://www.careeronestop.org/JobSearch/job-search.aspx";

	public LandingPage(WebDriver driver) {
		driver.get(URL);

		this.driver = driver;
		
		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofMillis(100)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("end-footer")));
	}

	public SearchPage search(String keyword, String location) {
		setKeyword(keyword);
		setLocation(location);
		return seeJobs();
	}

	public void setKeyword(String word) {
		WebElement keywordBar = driver.findElement(By.id("widget_kw"));
		keywordBar.sendKeys(word);
	}

	public void setLocation(String location) {
		WebElement locBar = driver.findElement(By.id("widget_loc"));
		locBar.sendKeys(location);
	}

	public SearchPage seeJobs() {
		WebElement searchButton = driver.findElement(By.id("widget_searchLanding"));
		searchButton.sendKeys(Keys.RETURN);

		return new SearchPage(driver);
	}
}
