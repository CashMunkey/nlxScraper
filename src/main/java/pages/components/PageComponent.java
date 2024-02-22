package pages.components;

import org.openqa.selenium.WebElement;

public class PageComponent {

	protected WebElement root;
	
	protected PageComponent(WebElement rootElement) {
		this.root = rootElement;
	}
	
}
