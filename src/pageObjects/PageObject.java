package pageObjects;

import org.openqa.selenium.WebElement;

import utilities.WebElementLib;

public class PageObject {

	/********************* proponent Log-in page objects **********************/
//	Log-in Form element
	public WebElement loginForm() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/form");
	}

	public WebElement username() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/form/input[1]");
	}

	public WebElement password() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/form/input[2]");
	}

	public WebElement submit() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/form/button");
	}

	public WebElement errMsz() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/form/strong");
	}

	/********************* proponent Loggedin page objects **********************/
//	table
	public WebElement table() {
		return WebElementLib.findMyElement("xpath",
				"//*[@id='root']/div/div/div/div/div/div/div/div/div/div[2]/table");
	}

//	settings tab
	public WebElement settings() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/div/nav/div/div[2]/ul/li[2]");
	}

//	application setting
	public WebElement appSettings() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/div/div/div/div[1]/div[1]/div/div[2]");
	}

//	sign out
	public WebElement signOut() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/div/nav/div/div[2]/ul/li[3]");
	}

	/********************* case email search **********************/

	public WebElement caseNo() {
		return WebElementLib.findMyElement("xpath", "//*[@id='case_number']");
	}

	public WebElement customerNo() {
		return WebElementLib.findMyElement("xpath", "//*[@id='customer_number']");
	}

	public WebElement emailType() {
		return WebElementLib.findMyElement("xpath", "//input[@id='email']");
	}

	public WebElement calendarFrom() {
		return WebElementLib.findMyElement("xpath", "//*[@id='from']");
	}

	public WebElement calendarTo() {
		return WebElementLib.findMyElement("xpath", "//*[@id='to']");
	}

	public WebElement searchBtn() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/div/div/form/div/div[5]/div[2]/button[1]");
	}

	public WebElement pagination() {
		return WebElementLib.findMyElement("xpath", "//*[@id='root']/div/div/div/div/div/div/div/ul");
	}

}
