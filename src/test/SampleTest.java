package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import pageObjects.PageObject;
import utilities.ExcelRead;
import utilities.InputField;
import utilities.TableData;
import utilities.UtilBase;
import utilities.WebElementLib;

public class SampleTest extends UtilBase {
	String browser, baseUrl, username, password;
	PageObject pageObj = new PageObject();

	WebDriverWait wait;
	ExtentTest test;
	ExtentReports report;
	WebElement targetTable;

	List<String> caseNoList, customerNoList, colData;

	int rowCountCaseNo, rowCountCustomerNo;

	int counter;

	@BeforeClass(alwaysRun = true)
	public void beforeTest() {
		initialiseDriver();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		wait = new WebDriverWait(driver, 20);
		report = new ExtentReports(System.getProperty("user.dir") + "//testReports//ExtentReport.html");

		try {
			browser = ExcelRead.getData(0, 1, 0).trim();
			baseUrl = ExcelRead.getData(1, 1, 0);
			username = ExcelRead.getData(2, 1, 0);
			password = ExcelRead.getData(3, 1, 0);

		} catch (Exception e) {
			System.out.println("Failed to read data from Excel File");
		}

//		load the case no. to array
		try {
			rowCountCaseNo = ExcelRead.getRowCount("caseNo");
			caseNoList = new ArrayList<String>();

			for (int i = 1; i <= rowCountCaseNo; i++) {
				caseNoList.add(ExcelRead.getData(i, 0, "caseNo"));
			}

		} catch (Exception e) {
			System.out.println("Failed to read case no. from excel sheet");
		}

//		load the customer no. to array
		try {
			rowCountCustomerNo = ExcelRead.getRowCount("customerNo");
			customerNoList = new ArrayList<String>();

			// LOADS THE CONTENET OF EXCEL SHEET INTO ARRAYLIST
			for (int i = 1; i <= rowCountCustomerNo; i++) {
				customerNoList.add(ExcelRead.getData(i, 0, "customerNo"));
			}
		} catch (Exception e) {
			System.out.println("Failed to read customer no. from excel sheet");
		}

		System.out.println("sucessfully read data from Excel File and stored into ArrayList");

	}

	// URL verification
	@Test(priority = 2)
	public void urlTest() throws InterruptedException {
		test = report.startTest("baseUrl test");

		// check if baseURL is null
		if (baseUrl == null) {
			test.log(LogStatus.ERROR, "Empty URL");
			driver.quit();
		} else {
			// if URL isnot null go to the URL and get the title
			driver.get(baseUrl);
			String title = driver.getTitle();
			System.out.println("Title : " + title);
//			Compare the title with the expected string
			if (title.equals("Proponent.")) {
				test.log(LogStatus.PASS, "URL : " + baseUrl + " Title : " + title);
			} else {
				test.log(LogStatus.FAIL, "Invalid URL");
				driver.quit();
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
		driver.navigate().refresh();

		InputField.enterText(pageObj.username(), username);

//		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div/form/button")));
		if (pageObj.submit().isEnabled()) {
			test.log(LogStatus.FAIL, "case 3 : valid username empty password; submit button should be diasbled");
		} else {
			test.log(LogStatus.PASS, "case 3 : valid username empty password; submit button is disabled");
		}

//		case 4 : Wrong username and Wrong Password
		driver.navigate().refresh();

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
		driver.navigate().refresh();

		InputField.enterText(pageObj.username(), username);
		InputField.enterText(pageObj.password(), password);

		if (pageObj.submit().isEnabled()) {
			pageObj.submit().click();

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='navbar-collapse collapse']/ul/li[1]")));

//		capture screenshot
			capture("logged_in");
			test.log(LogStatus.PASS, "case 5 : Logged in and screenshot captured");
			System.out.println("Logged in");
		} else {
			test.log(LogStatus.FAIL, "case 5 : valid username valid password; submit button should be enabled");
		}

		report.endTest(test);
	}

//	Links and Tabs test
	@Test(priority = 4)
	public void linksTest() throws InterruptedException {
		test = report.startTest("Links and Tabs Test");

		try {
			driver.findElement(By.className("ant-table-column-sorters"));
			test.log(LogStatus.PASS, "Case Email Page Loaded sucessfully");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Case Email Page didnot Load");
		}
//			click to settings
		pageObj.settings().click();
		Thread.sleep(3000);

//			User management
		WebElement setObj = driver.findElement(By.xpath("//*[@id=\"rc-tabs-0-tab-user_management\"]"));
		String value = setObj.getAttribute("aria-selected"); // search by attribute

		if (value.equalsIgnoreCase("true")) {
			// User Management tab is selected currently
//		does Add User btn exist
			WebElement addUserBtn = driver
					.findElement(By.xpath("//*[@id='rc-tabs-0-panel-user_management']/div/form/div/div[3]/div/div[2]/div[2]/div[1]/div/div/div/div/button"));
			if (WebElementLib.doesElementExist(addUserBtn)) {
				test.log(LogStatus.PASS, "User Management tab loaded sucessfully");
			}
		}

//		click Application setting btn
		pageObj.appSettings().click();
		Thread.sleep(2000);

		try {
			driver.findElement(By.xpath("//*[@id=\"rc-tabs-0-panel-application_settings\"]/div/form/div/div[2]/div/div[1]"));
			test.log(LogStatus.PASS, "Application Setting tab loaded sucessfully");

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Application Setting tab didnot Load");
		}

		report.endTest(test);

	}

	@Test(priority = 5)
	public void searchCaseEmailCaseNum() throws InterruptedException {
		test = report.startTest("Case Email Search by CASE NO:");
		System.out.println("Case Email Search by CASE NO:");
		String tableCellData;

		driver.navigate().to(baseUrl);
		Thread.sleep(2000);

		for (int i = 0; i < (rowCountCaseNo); i++) {
			driver.navigate().refresh();
			String currentCaseNo = caseNoList.get(i);
			pageObj.caseNo().sendKeys(currentCaseNo);
			pageObj.searchBtn().click();
			Thread.sleep(3000);
System.out.println("<------- "+"case no: "+currentCaseNo+" -------->");
			try {
				driver.findElement(By.xpath("//div[@class='ant-empty ant-empty-normal']"));
//				above stmt searches for the NO DATA FOUND element, if element is found --> Invalid case no else, creates an Exception
				test.log(LogStatus.PASS, "Case Number : " + currentCaseNo + " does not Exist");
				System.out.println(currentCaseNo + " doesnot exists");
			} catch (Exception e) {
				targetTable = pageObj.table();
				tableCellData = TableData.getElement(targetTable, 1, 0).getText();

				if (tableCellData.equals(currentCaseNo)) {
					test.log(LogStatus.PASS, "Case Number: " + currentCaseNo + " matched with result");
					System.out.println(currentCaseNo + " matched with result");
				} else {
					test.log(LogStatus.FAIL, "Case Number: " + currentCaseNo + " didn't matched with result");
				}
			}
		}
		report.endTest(test);
	}

	@Test(priority = 6)
	public void searchCaseEmailCustomerNo() throws InterruptedException {
		test = report.startTest("Search by customer no.");
		System.out.println("Search by customer no.");
		System.out.println("Total data in excel sheet: " + rowCountCustomerNo);

		for (int i = 0; i < rowCountCustomerNo; i++) {
			String currentCustomerNo = customerNoList.get(i);
			System.out.println("<------ customer no: " + currentCustomerNo + " ------> ");
			driver.navigate().refresh();
			pageObj.customerNo().sendKeys(currentCustomerNo);
			pageObj.searchBtn().click();
			Thread.sleep(4000);

			try {
				driver.findElement(By.xpath("//div[@class='ant-empty ant-empty-normal']"));
//				above stmt searches for the NO DATA FOUND element, if element is found --> Invalid cutomer no. else, creates an Exception and executes the catch block
				test.log(LogStatus.PASS, "Customer Number: " + currentCustomerNo + " does not Exist");
				System.out.println("Customer Number:" + currentCustomerNo + " doesnot exists");
			} catch (Exception e) {
//				scroll
				WebElement element = pageObj.pagination();
				jsDriver.executeScript("arguments[0].scrollIntoView();", element);
				Thread.sleep(1000);
//			findElments returns List getTotalLiPagination gives total no of li elements
				List<WebElement> pageList = driver.findElements(By.xpath("//*[@id='root']/div/div/div/div/div/div/div/ul/li"));
				int totalLi = pageList.size();
//				get the last li Element
				WebElement lastLiElement = driver
						.findElement(By.cssSelector("#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (totalLi) + ")"));

				String liTitle = lastLiElement.getAttribute("title");
				colData = new ArrayList<String>();
//				20/pages is not recognized by lastLiElement.getAttribute("title"). so used if method 
				if (liTitle.equals("Next Page")) {
					WebElement lastPage = driver
							.findElement(By.cssSelector("#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (totalLi - 1) + ")"));
					int count = Integer.parseInt(lastPage.getText());
					System.out.println("Total no of pages:" + count);

					for (counter = 1; counter <= count; counter++) {
						storeColumn(2);
						driver.findElement(By.xpath("//li[@title='Next Page']")).click();
						Thread.sleep(4000);
					}

				} else {
//					Last page = n-2
					WebElement lastLiElement2 = driver
							.findElement(By.cssSelector("#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (totalLi - 2) + ")"));

					int count = Integer.parseInt(lastLiElement2.getText());
					System.out.println("Total no of pages: " + count);

					for (counter = 1; counter <= count; counter++) {
						storeColumn(2);
						driver.findElement(By.xpath("//li[@title='Next Page']")).click();
						Thread.sleep(4000);
					}

				}
				
				System.out.println("Total rows of data: " + colData.size());

				/****************** comparing code goes here *******************/
//				i+1 to get the customer dynamically from the excel file
				String firstCustomer = ExcelRead.getData((i+1), 0, "customerNo").trim();
				System.out.println("first customer: " + firstCustomer);

//				counter used to count the total no of error datas
				int counter = 0;
				for (int x = 0; x < colData.size(); x++) {
					String dynamicCustomer = colData.get(x);
					if (firstCustomer.equals(dynamicCustomer)) {
//						System.out.println("success");
					} else {
						System.out.println("ERROR colData index: " + x + " value: " + colData.get(x));
						counter++;
					}
				}
				System.out.println("Total no of error data: " + counter);
				test.log(LogStatus.PASS, "customer no: " + currentCustomerNo + " | Total rows of data: " + colData.size() + " | Error data: " +counter);
			}
		}

		report.endTest(test);
	}

//	setting parameter int tableCol to specify column index to store the data from
	public void storeColumn(int tableColIndex) throws InterruptedException {
//		get table
		WebElement targetTable = pageObj.table();
//		get row count
		int rc = TableData.getRowCount(targetTable);
		System.out.println("page: " + counter + " data rows: " + (rc - 1));

		String cellData = null;
		for (int i = 2; i <= rc; i++) {
			cellData = driver.findElement(By.xpath("//*[@id='root']/div/div/div/div/div/div/div/div/div/div[2]/table/tbody/tr[" + (i) + "]/td["+tableColIndex+"]")).getText()
					.trim();
			colData.add(cellData);
		}
	}

	@Test(priority = 9)
	public void logout() {
		test = report.startTest("Logout Module");
		// click signout
		pageObj.signOut().click();
//		implicit wait
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		test.log(LogStatus.PASS, "Logged out sucessful");
		report.endTest(test);
	}

	@AfterClass(alwaysRun = true)
	public void afterMethod() {
		report.flush();
		driver.close();
	}
}
