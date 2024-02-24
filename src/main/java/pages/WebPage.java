package pages;

import java.time.Duration;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public abstract class WebPage {

	protected Logger log = LogManager.getLogger(WebPage.class);
	WebDriver driver;
		
	protected WebPage() { }
	
	protected WebPage(WebDriver driver) {
		this.driver = driver;
		
		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofMillis(100)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("end-footer")));
	}
	
	public WebDriver back() {
		driver.navigate().back();
		return driver;
	}
	
	protected void open(WebElement element) {
		open(element, e -> e.sendKeys(Keys.RETURN));
	}
	
	protected void open(WebElement target, Consumer<WebElement> opener) {
		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(2))
				.pollingEvery(Duration.ofMillis(50));
		opener.accept(target);
		wait.until(ExpectedConditions.stalenessOf(target));
	}
	
}
