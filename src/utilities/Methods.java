package utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import test.HappyFlow;

public class Methods extends HappyFlow{


	public void searchWithDate() throws InterruptedException {
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		pageObj.calendarFrom().click();
		pageObj.calendarFrom().sendKeys("2022-05-25");
		pageObj.calendarTo().sendKeys("2022-05-27");
		Thread.sleep(2000);
		pageObj.searchBtn().click();

	}

	public void searchWithCaseNumber() throws InterruptedException {
		String searchCN = "149989";
		pageObj.caseNo().sendKeys(searchCN);
		pageObj.searchBtn().click();

		Thread.sleep(3000);

		WebElement targetTable = driver
				.findElement(By.xpath("//*[@id='root']/div/div/div/div/div/div/div/div/div/div[2]/table"));
		System.out.println(TableData.getRowCount(targetTable));

		Thread.sleep(3000);
		String cellData = TableData.getElement(targetTable, 1, 0).getText();
		if (cellData.equals(searchCN)) {
			System.out.println("Case Number Found");
//			popup alert using JS
//			jsDriver.executeScript("alert('Case number found');");
		} else {
			System.out.println("Case Number not found");
		}

	}

	public void searchWithCustomerNumber(String custNo) throws InterruptedException {
		pageObj.customerNo().sendKeys(custNo);
		pageObj.searchBtn().click();

		/*
		 * Thread.sleep(2000); WebElement targetTable = driver .findElement(By.xpath(
		 * "//*[@id='root']/div/div/div/div/div/div/div/div/div/div[2]/table"));
		 * 
		 * Thread.sleep(2000); String cellData = TableData.getElement(targetTable, 1,
		 * 1).getText(); if (cellData.equals(custNo)) {
		 * System.out.println("CellData : "+ cellData + "		Customer number : " +
		 * custNo); } else { System.out.println("Customer number not found"); }
		 */
	}

	public void searchByStatus() throws InterruptedException {

		WebElement elem = driver.findElement(By.cssSelector("#status"));
		elem.sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		elem.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN);
		elem.sendKeys(Keys.ENTER);
		pageObj.searchBtn().click();

		Thread.sleep(4000);
//		get row count
		System.out.println(" Total no of rows : " + TableData.getRowCount(pageObj.table()));
	}

	public void scroll() throws InterruptedException {
		Thread.sleep(2000);
		// Locating element by link text and store in variable "Element"
		WebElement Element = pageObj.pagination();
		// Scrolling down the page till the element is found
		jsDriver.executeScript("arguments[0].scrollIntoView();", Element);
	}

	public int getTotalLiPagination() throws InterruptedException {

		Thread.sleep(1000);
//		xpath of pagination section
		List<WebElement> pageList = driver.findElements(By.xpath("//*[@id='root']/div/div/div/div/div/div/div/ul/li"));
//		returns the total no of li element 
		return pageList.size();
	}

	public void storeColumn() throws InterruptedException {
//		get table
		WebElement targetTable = pageObj.table();
//		get row count
		int rc = TableData.getRowCount(targetTable);
		System.out.println("RowCount : " + rc);
		colData = new ArrayList<String>();
		String cellData = null;

		for (int i = 1; i <= rc; i++) {
//			colData.add(TableData.getCellText(targetTable, i, 2));
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

			WebElement lastLiElement1 = driver.findElement(By.cssSelector(
					"#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (n - 1) + ")"));
			int count = Integer.parseInt(lastLiElement1.getText());

			for (int i = 1; i < count; i++) {
				Thread.sleep(7000);
//				click next page via pagination
				driver.findElement(By.cssSelector(
						"#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (n) + ")"))
						.click();
				storeColumn();
				Thread.sleep(3000);
			}

		} else {
//			Last page = n-2
			WebElement lastLiElement2 = driver.findElement(By.cssSelector(
					"#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (n - 2) + ")"));

			int count = Integer.parseInt(lastLiElement2.getText());

			for (int i = 2; i <= count; i++) {
				Thread.sleep(7000);
//				click next page via pagination
				driver.findElement(By.cssSelector(
						"#root > div > div > div > div > div > div > div > ul > li:nth-child(" + (i + 1) + ")"))
						.click();
				storeColumn();
				Thread.sleep(3000);
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
