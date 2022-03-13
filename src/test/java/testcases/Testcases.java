package testcases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import testbase.Testbase;

public class Testcases extends Testbase {
	public Testcases() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static String xLPath, xLResultPath,ActualResult;
	public static String[][] xLTD;
	public static int xRows, xCols, xLRowCount;

	@BeforeMethod
	public void setup() throws Exception {
		xLPath = System.getProperty("user.dir") + "\\src\\main\\java\\testfile\\testdata.xls";
		xLResultPath = System.getProperty("user.dir") + "\\src\\main\\java\\testing_reports\\report.xls";
		xLTD = readXL(xLPath, "testdata");
		xLRowCount = xLTD.length;
		System.out.println("Total rows are-->" + xLRowCount);
		launchbrowser();
		login();
		Thread.sleep(2000);

	}

	@Test(priority=1)
	public void test() throws InterruptedException {
		driver.findElement(By.xpath(prop.getProperty("admin"))).click();
		Thread.sleep(2000);
//		WebElement mainmenu=driver.findElement(By.xpath(prop.getProperty("Admin")));
//		Actions act=new Actions(driver);
//		act.moveToElement(mainmenu).build().perform();
		for (int i = 1; i < xLRowCount; i++) {
			if (xLTD[i][1].equals("Y")) {
				username_box = xLTD[i][0];
				userrole_boxe = xLTD[i][2];
				employeename_boxe = xLTD[i][3];
				status_boxe = xLTD[i][4];
				ExpectedResulte = xLTD[i][5];
				
//				WebElement lang=driver.findElement(By.xpath(prop.getProperty("usermanagement")));
//				Select sef= new Select(lang);
//				String text=driver.findElement(By.xpath(prop.getProperty("user"))).getText();
//				sef.selectByValue(text);
//				Thread.sleep(2000);

				driver.findElement(By.xpath(prop.getProperty("username_box"))).clear();
				driver.findElement(By.xpath(prop.getProperty("username_box"))).sendKeys(username_box);
				driver.findElement(By.xpath(prop.getProperty("employeename_box"))).clear();
				driver.findElement(By.xpath(prop.getProperty("employeename_box"))).sendKeys(employeename_boxe);
				WebElement userrole = driver.findElement(By.xpath(prop.getProperty("userrole_box")));
				Select se = new Select(userrole);
				se.selectByVisibleText(userrole_boxe);
				driver.findElement(By.xpath(prop.getProperty("Search"))).click();
				Thread.sleep(2000);
				if (!driver.findElements(By.xpath(prop.getProperty("final_txt"))).isEmpty()) {
					ActualResulte = driver.findElement(By.xpath(prop.getProperty("final_txt"))).getText();
				} else {
					ActualResulte = driver.findElement(By.xpath(prop.getProperty("final_txt2"))).getText();
				}
				System.out.println(" Actual User is ---->" + ActualResulte);
				xLTD[i][6] = ActualResulte;

				if (ActualResulte.equals(ExpectedResulte)) {
					System.out.println("Test Case Passed");
					xLTD[i][7] = "Test Case Passed";
				} else {
					System.out.println("Test Case Failed");
					xLTD[i][7] = "Test Case Failed";
				}
				Assert.assertEquals(ActualResulte, ExpectedResulte);

			}
		}
		driver.quit();
	}

	@AfterMethod
	public void tearDown() throws Exception {

		writeXL(xLResultPath, "reports", xLTD);
	}

	

//Teach Java to R/W from MS Excel
// Method to read XL
	public String[][] readXL(String fPath, String fSheet) throws Exception {
		// Inputs : XL Path and XL Sheet name
		// Output : cellValue

		String[][] xData;
//Step   1 create Constructor of FIle Class
		File myxl = new File(fPath);
//Step 2 create Constructor of FileInputStream instrcut read data from xls                  
		FileInputStream myStream = new FileInputStream(myxl);
		// Step 3 create Constructor of HSSFworkbook
		HSSFWorkbook myWB = new HSSFWorkbook(myStream);
		// Step 4 get sheet name in run time
		HSSFSheet mySheet = myWB.getSheet(fSheet);
		xRows = mySheet.getLastRowNum() + 1;
		xCols = mySheet.getRow(0).getLastCellNum();
		System.out.println("Total Rows in Excel are " + xRows);
		System.out.println("Total Cols in Excel are " + xCols);
		xData = new String[xRows][xCols];
		for (int i = 0; i < xRows; i++) { /// outer for loop for rows
			HSSFRow row = mySheet.getRow(i);
			for (int j = 0; j < xCols; j++) { // inner for loop for columns
				HSSFCell cell = row.getCell(j);
				String value = "-";
				if (cell != null) {
					value = cellToString(cell);
				}
				xData[i][j] = value;
				System.out.print(value);
				System.out.print("----");
			}
			System.out.println("");
		}
		myxl = null; // Memory gets released
		return xData;
	}

//Change cell type
	public static String cellToString(HSSFCell cell) {
		// This function will convert an object of type excel cell to a string value
		int type = cell.getCellType();
		Object result;
		switch (type) {
		case HSSFCell.CELL_TYPE_NUMERIC: // 0
			result = cell.getNumericCellValue();
			break;
		case HSSFCell.CELL_TYPE_STRING: // 1
			result = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 2
			throw new RuntimeException("We can't evaluate formulas in Java");
		case HSSFCell.CELL_TYPE_BLANK: // 3
			result = "%";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // 4
			result = cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 5
			throw new RuntimeException("This cell has an error");
		default:
			throw new RuntimeException("We don't support this cell type: " + type);
		}
		return result.toString();
	}

// Method to write into an XL
	public void writeXL(String fPath, String fSheet, String[][] xData) throws Exception {
		// input parameter -- result path, sheet name, array Name
		// output value -- nill
		// Step 1 create constrcutor of File class to store result sheet path
		File outFile = new File(fPath);
		// step 2 create constructor of HSSFWorkbook class to add workbook into meemory
		HSSFWorkbook wb = new HSSFWorkbook();
		// step 3 create new sheet in existing workbook
		HSSFSheet osheet = wb.createSheet(fSheet);
		int xR_TS = xData.length;// row count
		int xC_TS = xData[0].length;// column count
		for (int myrow = 0; myrow < xR_TS; myrow++) {
			HSSFRow row = osheet.createRow(myrow);// create new rows in workbook
			for (int mycol = 0; mycol < xC_TS; mycol++) {
				HSSFCell cell = row.createCell(mycol);// create cell in workbook
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(xData[myrow][mycol]);// set value
			}
			FileOutputStream fOut = new FileOutputStream(outFile);// Write data
			wb.write(fOut);// write into excel file
			fOut.flush();
			fOut.close();
		}
	}

}