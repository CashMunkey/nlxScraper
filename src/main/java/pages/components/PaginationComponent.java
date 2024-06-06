package pages.components;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PaginationComponent extends PageComponent {

	public final By byActive = By.id("activePage");
	
	public PaginationComponent(WebElement rootElement) {
		super(rootElement);
	}
	
	public int getActivePage() {
		return Integer.parseInt(root.findElement(byActive).getText());
	}
	
	public WebElement nextPage() throws IndexOutOfBoundsException {
		if (getActivePage() < countPages())
			return root.findElement(By.cssSelector("#pagi-next > a"));
		else
			throw new IndexOutOfBoundsException("Tried to go to next page, but was on last page");
	}

	public int countPages() {
		List<WebElement> pages = root.findElements(By.cssSelector("li"));
		// second to last pagination option is page [##], last option is next page [►]
		WebElement lastPage = pages.get(pages.size() - 2);
		
		return Integer.parseInt(lastPage.getText());
	}

}
