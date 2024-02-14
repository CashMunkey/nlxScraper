package pages;

import org.openqa.selenium.WebDriver;

public abstract class WebPage {

	protected WebDriver driver;
	
	public WebPage(WebDriver driver) {
		this.driver = driver;
	}
	
}
