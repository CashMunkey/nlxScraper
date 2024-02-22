package pages;

import org.openqa.selenium.WebDriver;

public abstract class WebPage {

	WebDriver driver;
	
	protected WebPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebDriver back() {
		driver.navigate().back();
		return driver;
	}
	
}
