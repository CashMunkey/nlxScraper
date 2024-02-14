package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LandingPage extends WebPage {

	private static final String URL = "https://www.careeronestop.org/JobSearch/job-search.aspx";
	
	public LandingPage(WebDriver driver) {
		super(driver);
		
		driver.get(URL);
	}
	
	public ResultsPage search(String keyword, String location) {
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
	
	public ResultsPage seeJobs() {
		WebElement searchButton = driver.findElement(By.id("widget_searchLanding"));
		searchButton.sendKeys(Keys.RETURN);
		
		return new ResultsPage(driver);
	}
}
