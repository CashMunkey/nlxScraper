package common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserSwitch {

	public static WebDriver get(String driverName) {
		Logger log = LogManager.getLogger(BrowserSwitch.class);
		driverName = driverName.toLowerCase().replace(" ", "");
		
		switch(driverName) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			return new ChromeDriver();
		case "firefox", "gecko":
			WebDriverManager.firefoxdriver().setup();
			return new FirefoxDriver();
		case "edge":
			WebDriverManager.edgedriver().setup();
			return new EdgeDriver();
		default:
			if(driverName.isEmpty()) {
				log.warn("Unrecognized driver: {}; Attempting to use Chrome", driverName);
				WebDriverManager.chromedriver().setup();
				return new ChromeDriver();
			} else {
				log.error("Unrecognized driver: {}", driverName);
				throw new InvalidArgumentException(driverName + " is not a valid driver option");
			}
		}
	}
}
