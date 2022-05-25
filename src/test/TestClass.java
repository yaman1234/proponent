package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import pageObjects.PageObject;
import utilities.ExcelRead;
import utilities.InputField;
import utilities.UtilBase;
import utilities.WaitUntil;
import utilities.WebElementLib;

public class TestClass extends UtilBase {

	/*********** Test Cases for Smoke Test ********************/

// 		Read Data from Excel File
//		URL Verification
//		 Login Verification
//		Verify all the links and tabs are working or not
//	 	Verify Search using the different combinations of Filters

	/**********************************************************/

	public ExtentTest test;
	public ExtentReports report;
	String browser = null, baseUrl = null, username = null, password = null;
	public PageObject pageObj;
	public WebDriverWait wait;

	@BeforeSuite
	public void beforeMethod() {

		initialiseDriver();
		driver.manage().window().maximize();

		report = new ExtentReports(System.getProperty("user.dir") + "//testReports//ExtentReport.html");
		pageObj = new PageObject();
	}

	// Read from Excel file
	@Test(priority = 1)
	public void readExcel() throws InterruptedException {
		test = report.startTest("Excel Read Test");
		try {
			browser = ExcelRead.getData(0, 1, 0);
			baseUrl = ExcelRead.getData(1, 1, 0);
			username = ExcelRead.getData(2, 1, 0);
			password = ExcelRead.getData(3, 1, 0);
			test.log(LogStatus.PASS, "successful in reading data from excel file");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Error in reading data from excel file");
			e.printStackTrace();
		}
		report.endTest(test);
	}

	// URL verification
	@Test(priority = 2)
	public void urlTest() throws InterruptedException {
		test = report.startTest("baseUrl test");

		// check if baseURL is null
		if (baseUrl == null) {
			test.log(LogStatus.ERROR, "Empty URL");
		} else if (baseUrl != null) {
			// if URL isnot null go to the URL and get the title
			driver.get(baseUrl);
			String title = driver.getTitle();

			if (title != null) {
				test.log(LogStatus.PASS, "URL : " + baseUrl + " Title : " + title);
			} else {
				test.log(LogStatus.FAIL, "Invalid URL");
			}
		}
		report.endTest(test);
	}

	// Login verification
	@Test(priority = 3)
	public void loginTest() throws InterruptedException {

		test = report.startTest("Login Test");

//		case 1 : Empty username Empty Password
		InputField.inputFieldClear(pageObj.username());
		InputField.inputFieldClear(pageObj.password());

		if (pageObj.submit().isEnabled()) {
			test.log(LogStatus.FAIL, "case 1 : Empty username Empty Password; submit button should be diasbled");
		} else {
			test.log(LogStatus.PASS, "case 1 : Empty username Empty Password; submit button is disabled");
		}

//		case 2 : Empty username valid Password
		// reset
		InputField.inputFieldClear(pageObj.username());
		InputField.inputFieldClear(pageObj.password());

		InputField.enterText(pageObj.password(), password);

		if (pageObj.submit().isEnabled()) {
			test.log(LogStatus.FAIL, "case 2 : Empty username valid Password; submit button should be diasbled");
		} else {
			test.log(LogStatus.PASS, "case 2 : Empty username valid Password; submit button is disabled");
		}

//		case 3 : valid username empty password
//		reset
		InputField.inputFieldClear(pageObj.username());
		InputField.inputFieldClear(pageObj.password());

		InputField.enterText(pageObj.username(), username);

		if (pageObj.submit().isEnabled()) {
			test.log(LogStatus.FAIL, "case 3 : valid username empty password; submit button should be diasbled");
		} else {
			test.log(LogStatus.PASS, "case 3 : valid username empty password; submit button is disabled");
		}

//		case 4 : Wrong username and Wrong Password
		InputField.inputFieldClear(pageObj.username());
		InputField.inputFieldClear(pageObj.password());

		Thread.sleep(2000);
		// wrong credentials
		InputField.enterText(pageObj.username(), password);
		InputField.enterText(pageObj.password(), username);

		if (pageObj.submit().isEnabled()) {
			pageObj.submit().click();

			Thread.sleep(3000);

//			if err-msz element exists it implies wrong credentials

//			WebElement errElement = 	pageObj.errMsz();
			boolean result = WebElementLib.doesElementExist("xpath", "//*[@id=\"root\"]/div/div/form/strong");

			if (result) {
				System.out.println("working");
				test.log(LogStatus.PASS, "case 4 : Invalid Credentials; Couldn't login");
			}
		} else {
			test.log(LogStatus.FAIL, "submit button should be enabled");
		}

//		case 5 : valid username valid password
//		reset
		InputField.inputFieldClear(pageObj.username());
		InputField.inputFieldClear(pageObj.password());

		InputField.enterText(pageObj.username(), username);
		InputField.enterText(pageObj.password(), password);

		if (pageObj.submit().isEnabled()) {
			test.log(LogStatus.PASS, "case 5 : valid username valid password; login sucessful and screenshot");
			pageObj.submit().click();

//			Thread.sleep(4000);
//			wait = new WebDriverWait(driver, 20);
//			wait.until(ExpectedConditions.invisibilityOf(pageObj.loginForm()));
			
//			wait for page to load
			WaitUntil.waitForLoad();
//			jsDriver.executeScript("return document.readyState").equals("complete");
//			capture screenshot
			capture("logged_in");
		} else {
			test.log(LogStatus.FAIL, "case 5 : valid username valid password; submit button should be enabled");
		}

		report.endTest(test);

	}

	// Links and Tabs
	@Test(priority = 4)
	public void linksTest() throws InterruptedException {
		test = report.startTest("Links and Tabs");
		try {
//			wait for load
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ant-table-column-sorters")));
//			verify Case Emails Tab
			WebElement cn = driver.findElement(By.className("ant-table-column-sorters"));
			System.out.println(cn.getText());
			if (cn.getText() != null) {
				test.log(LogStatus.PASS, "Case Email Page loaded sucessfully");
			} else {
				test.log(LogStatus.FAIL, "Failed to Load Case Email");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// click to settings Tab
		try {
			pageObj.settings().click();
			
//			wait for page to load
			jsDriver.executeScript("return document.readyState").equals("complete");
			
		} catch (Exception e) {
			System.out.println("Error in pageObj.settings().click() ");
		}

		report.endTest(test);
	}

	@AfterSuite
	public void afterMethod() throws InterruptedException {
		report.endTest(test);
		report.flush();
		// report will not be generated if flush() is not defined

		Thread.sleep(1000);
//		driver.close();

	}

}
