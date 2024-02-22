package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public abstract class WebPage {

	protected Logger log = LogManager.getLogger(WebPage.class);
	WebDriver driver;
	
	protected WebPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebDriver back() {
		driver.navigate().back();
		return driver;
	}
	
}
