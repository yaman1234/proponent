package pageObjects;

import org.openqa.selenium.WebElement;

import utilities.WebElementLib;

public class HappyObjects {

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
	
	
//	settings tab
	public WebElement settings() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/div/nav/div/div[2]/ul/li[2]");
	}
	
//	application setting
	public WebElement appSettings() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/div/div/div/div[1]/div[1]/div/div[2]");
	}
	
//	sign out
	public WebElement signOut() {
		return WebElementLib.findMyElement("xpath", "//*[@id=\"root\"]/div/div/div/nav/div/div[2]/ul/li[3]");
	}
	
	/*********************			***********************************/
//	first table row with data
	public WebElement caseEmailTR() {
		return WebElementLib.findMyElement("xpath", "//div[@class='ant-table-body']/table/tbody/tr[3]");
	}
	
}
