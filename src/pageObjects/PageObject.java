package pageObjects;

import org.openqa.selenium.WebElement;

import utilities.WebElementLib;

public class PageObject {

	/********************* proponent Log-in page objects **********************/
//	Log-in Form element
	public WebElement loginForm() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/form");
	}
	
	public WebElement username() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/form/input[1]");
	}
	
	public WebElement password() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/form/input[2]");
	}
	
	public WebElement submit() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/form/button");
	}
	
	public WebElement errMsz(){
		return WebElementLib.findMyElement("xpath","//*[@id=\"root\"]/div/div/form/strong");
	}
	
	/********************* proponent Loggedin page objects **********************/
	
	public WebElement caseEmails() {
		return WebElementLib.findMyElement("xpath", "//div[@class='navbar-collapse collapse']/ul/li[1]");
	}
//	Case Number under the Case Email
	public WebElement caseNumber() {
		return WebElementLib.findMyElement("className", "ant-table-column-sorters");
	}
	
	public WebElement settings() {
		return WebElementLib.findMyElement("xpath", "//div[@class='navbar-collapse collapse']/ul/li[2]");
	}
	
	public WebElement userSettings() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"rc-tabs-4-tab-user_management\"]");
	}
	
	public WebElement appSettings() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/div/div/div/div[1]/div[1]/div/div[2]");
	}
	
	public WebElement signout() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"rc-tabs-0-tab-application_settings\"]");
	}
	
	
}
