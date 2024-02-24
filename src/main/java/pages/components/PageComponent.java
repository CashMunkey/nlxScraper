package pages.components;

import java.util.function.Function;

import org.openqa.selenium.WebElement;

public class PageComponent {

	protected WebElement root;
	
	protected PageComponent(WebElement rootElement) {
		this.root = rootElement;
	}
	
	/**
	 * Get information from this component
	 * @param <R> The type of information
	 * @param scraper The method to apply to get information
	 * @return Any object
	 */
	public <R> R scrape() {
		return scrape();
	}
	
	protected <R> R scrape(Function<PageComponent, R> getInfo) {
		return getInfo.apply(this);
	}
}
