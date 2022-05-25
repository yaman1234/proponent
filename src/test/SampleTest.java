package test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import okio.Timeout;
import pageObjects.PageObject;
import utilities.ExcelRead;
import utilities.InputField;
import utilities.UtilBase;
import utilities.WebElementLib;

public class SampleTest extends UtilBase {
	String browser, baseUrl, username, password;
	PageObject pageObj = new PageObject();

	WebDriverWait wait;
	ExtentTest test;
	ExtentReports report;

	@BeforeTest
	public void beforeTest() {
		initialiseDriver();
		driver.manage().window().maximize();

		wait = new WebDriverWait(driver, 20);
		report = new ExtentReports(System.getProperty("user.dir") + "//testReports//ExtentReport.html");
	}

	@Test(priority = 1)
	public void excelReadTest() {
		test = report.startTest("Excel Read Test");
		try {
			browser = ExcelRead.getData(0, 1, 0);
			baseUrl = ExcelRead.getData(1, 1, 0);
			username = ExcelRead.getData(2, 1, 0);
			password = ExcelRead.getData(3, 1, 0);

			test.log(LogStatus.PASS, "sucessfully read data frpm Excel File");

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Failed to read data frpm Excel File");
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

	@Test(priority = 3)
	public void loginTest() throws InterruptedException {
		test = report.startTest("Login Module Test");
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

//		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div/form/button")));
		if (pageObj.submit().isEnabled()) {
			test.log(LogStatus.FAIL, "case 3 : valid username empty password; submit button should be diasbled");
		} else {
			test.log(LogStatus.PASS, "case 3 : valid username empty password; submit button is disabled");
		}

//		case 4 : Wrong username and Wrong Password
		InputField.inputFieldClear(pageObj.username());
		InputField.inputFieldClear(pageObj.password());

		// wrong credentials
		InputField.enterText(pageObj.username(), password);
		InputField.enterText(pageObj.password(), username);

		if (pageObj.submit().isEnabled()) {
			pageObj.submit().click();
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div/form/strong")));

		WebElement errObj = pageObj.errMsz();
		boolean errStatus = WebElementLib.doesElementExist(errObj);

		if (errStatus) {
			test.log(LogStatus.PASS, "case 4 : Wrong Credentials ");
		} else {
			test.log(LogStatus.FAIL, "case 4 : Logged in with wrong credentials");
		}

//	case 5 : valid username valid password
//	reset
		InputField.inputFieldClear(pageObj.username());
		InputField.inputFieldClear(pageObj.password());

		InputField.enterText(pageObj.username(), username);
		InputField.enterText(pageObj.password(), password);

		if (pageObj.submit().isEnabled()) {
			pageObj.submit().click();

//		WaitUntil.waitForLoad();
//		jsDriver.executeScript("return document.readyState").equals("complete");

			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//div[@class='navbar-collapse collapse']/ul/li[1]")));

//		capture screenshot
			capture("logged_in");
			test.log(LogStatus.PASS, "case 5 : Logged in and screenshot captured");
		} else {
			test.log(LogStatus.FAIL, "case 5 : valid username valid password; submit button should be enabled");
		}

		report.endTest(test);
	}

//	Links and Tabs test
	@Test(priority = 4)
	public void linksTest() {
		test = report.startTest("Links and Tabs Test");
		WebElement caseNoElem = driver.findElement(By.className("ant-table-column-sorters"));
		if (WebElementLib.doesElementExist(caseNoElem)) {
			test.log(LogStatus.PASS, "Case Email Page Loaded sucessfully");
		} else {
			test.log(LogStatus.FAIL, "Case Email Page didnot Load");
		}

//			click to settings
		pageObj.settings().click();

//			table head of user management
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ant-table-thead")));

//			User management
		WebElement setObj = driver.findElement(By.xpath("//*[@id=\"rc-tabs-0-tab-user_management\"]"));
		String value = setObj.getAttribute("aria-selected");

		if (value.equalsIgnoreCase("true")) {
			// User Management tab is selected currently
//		does Add User btn exist
			WebElement addUserBtn = driver.findElement(By.xpath(
					"//*[@id=\"rc-tabs-0-panel-user_management\"]/div/form/div/div[3]/div/div[2]/div[2]/div[1]/div/div/div/div/button"));
			if (WebElementLib.doesElementExist(addUserBtn)) {
				test.log(LogStatus.PASS, "User Management tab loaded sucessfully");
			}
		}

//		click Application setting btn
		pageObj.appSettings().click();
//		table head of user management
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id=\"rc-tabs-0-panel-application_settings\"]/div/form/div/div[2]/div/div[1]")));

		WebElement addSettingBtn = driver.findElement(
				By.xpath("//*[@id=\"rc-tabs-0-panel-application_settings\"]/div/form/div/div[2]/div/div[1]"));
		if (WebElementLib.doesElementExist(addSettingBtn)) {
			test.log(LogStatus.PASS, "Application Setting tab loaded sucessfully");
		}

	

		report.endTest(test);

	}
	
	@Test(priority=5)
	public void logout() {
		test = report.startTest("Logout Module");
		// click signout
		pageObj.signout().click();
//		implicit wait
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		test.log(LogStatus.PASS, "Logged out sucessful");
				report.endTest(test);
	}

	@AfterTest
	public void afterMethod() {
		report.flush();
		driver.close();
	}
}
