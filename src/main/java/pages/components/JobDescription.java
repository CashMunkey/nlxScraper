package pages.components;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class JobDescription extends PageComponent {

	public JobDescription(WebElement rootElement) {
		super(rootElement);
	}

	public int keywordSearch(String... keywords) {
		int score = 0;

		for (String term : keywords) {
			if (root.getText().toLowerCase().contains(term)) {
				score++;
				continue;
			}

			for (WebElement leaf : getLeafNodes()) {
				String excerpt = leaf.getText();

				if (excerpt != null && excerpt.toLowerCase().contains(term)) {
					score++;
					break;
				}
			}
		}

		return score;
	}
	
	private List<WebElement> getLeafNodes() {
		return root.findElements(By.xpath(".//*[not(*)]"));
	}
}
