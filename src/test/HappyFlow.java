package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import pageObjects.PageObject;
import utilities.TableData;
import utilities.UtilBase;

public class HappyFlow extends UtilBase {

	ExtentTest test;
	ExtentReports report;
	String baseUrl;
	String username, password;
	public PageObject pageObj = new PageObject();
	public List<String> colData = new ArrayList<String>();
	WebDriverWait wait;
	int counter = 0;

	@BeforeSuite
	public void beforeMethod() {
		initialiseDriver();
		driver.manage().window().maximize();
//		implicit wait
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
//		Explicit wait
		wait = new WebDriverWait(driver, 30);
		report = new ExtentReports(System.getProperty("user.dir") + "//testReports//HappyFlowReport.html");
	}

	@Test(priority = 1)
	public void happyFlowTest() throws InterruptedException {
		test = report.startTest("Happy Flow Test");

		baseUrl = "http://10.0.2.248/";
		username = "kathmandu\\yamah022";
		password = "1@work";
		driver.get(baseUrl);

		pageObj.username().sendKeys(username);
		pageObj.password().sendKeys(password);
		pageObj.submit().click();

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id='root']/div/div/form")));
		searchWithCustomerNumber("80688");

		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id='root']/div/div/div/div/div/div/div/div/div/div[2]/table")));
		scroll();

		clickNextPage();
		Thread.sleep(3000);
		System.out.println("Total rows of data : " + colData.size());
//		printList();
		
		report.endTest(test);
	}

	@AfterSuite
	public void afterMethod() {
		report.flush();
		driver.close();

	}

	public void searchWithCustomerNumber(String custNo) throws InterruptedException {
//		send the customerNo and click
		pageObj.customerNo().sendKeys(custNo);
		pageObj.searchBtn().click();
	}

	public void scroll() throws InterruptedException {
//		scroll down to the pagination section
		Thread.sleep(2000);
		WebElement Element = pageObj.pagination();
		jsDriver.executeScript("arguments[0].scrollIntoView();", Element);
	}

	public int getTotalLiPagination() throws InterruptedException {
//returns the total no of li
		Thread.sleep(1000);
		List<WebElement> pageList = driver.findElements(By.xpath("//*[@id='root']/div/div/div/div/div/div/div/ul/li"));
		return pageList.size();
	}

	public void storeColumn() throws InterruptedException {
//		get table
		WebElement targetTable = pageObj.table();
//		get row count
		int rc = TableData.getRowCount(targetTable);

		counter++;
		System.out.println("page : " +counter + " data rows : " + (rc-1) );

		String cellData = null;
		for (int i = 2; i <= rc; i++) {
			cellData = driver.findElement(By.xpath(
					"//*[@id='root']/div/div/div/div/div/div/div/div/div/div[2]/table/tbody/tr[" + (i) + "]/td[3]"))
					.getText();
			colData.add(cellData);
		}
	}

	public void clickNextPage() throws InterruptedException {
//		get the last page no. and loop through the pages

		int n = getTotalLiPagination();
		System.out.println("Total no of li Elements : " + n);
//		get the last li
		WebElement lastLiElement = driver.findElement(
				By.cssSelector("#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (n) + ")"));

		String liTitle = lastLiElement.getAttribute("title");

		if (liTitle.equals("Next Page")) {
			System.out.println("Method 1 called");
			WebElement lastLiElement1 = driver.findElement(By.cssSelector(
					"#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (n - 1) + ")"));
			int count = Integer.parseInt(lastLiElement1.getText());
			System.out.println("Total no of pages :" + count);
			int flag = 0;
			do {
				storeColumn();
//				click the nextPage
				driver.findElement(By.cssSelector(
						"#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (n) + ")")).click();
				Thread.sleep(5000);
				flag++;
			} while (flag != count);

		} else {
			System.out.println("Method 2 called");
//			Last page = n-2
			WebElement lastLiElement2 = driver.findElement(By.cssSelector(
					"#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (n - 2) + ")"));

			int count = Integer.parseInt(lastLiElement2.getText());
			System.out.println("Total no of pages :" + count);
			
			for ( int i = 1; i <= count; i++) {
				storeColumn();
//				System.out.println("page : " +i);
				driver.findElement(By.xpath("//li[@title='Next Page']")).click();
				Thread.sleep(4000);
			}
			
		}
	}

	public void printList() {
		Iterator<String> it = colData.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}

	}

}
