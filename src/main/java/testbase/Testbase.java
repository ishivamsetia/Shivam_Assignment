package testbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Testbase {
	public static WebDriver driver;
	public static File file;
	public static FileInputStream fis;
	public static Properties prop;
	public static String username_box,userrole_boxe,employeename_boxe,status_boxe,ExpectedResulte,ActualResulte;
	
public Testbase() throws IOException {
	file=new File(System.getProperty("user.dir")+"\\src\\main\\java\\com\\shivam\\properties\\config.properties");
	fis=new FileInputStream(file);
	prop=new Properties();
	prop.load(fis);
	
}	

public static void launchbrowser() {
	if(prop.getProperty("browser").equals("chrome")) {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}
//	if(browser.equals(prop.getProperty("firefox"))) {
//		 WebDriverManager.firefoxdriver().setup();
//		    driver = new FirefoxDriver();
//	}
	else {
		 WebDriverManager.firefoxdriver().setup();
		    driver = new FirefoxDriver();

	}
//	WebDriverManager.chromedriver().setup();
//	driver = new ChromeDriver();
	driver.get(prop.getProperty("url"));
	driver.manage().window().maximize();
//	driver.manage().deleteAllCookies();
	
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
}

public static void login() {
	try {
		driver.findElement(By.xpath(prop.getProperty("username"))).clear();
		driver.findElement(By.xpath(prop.getProperty("username"))).sendKeys("Admin");
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("password"))).clear();
		driver.findElement(By.xpath(prop.getProperty("password"))).sendKeys("admin123");
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("login"))).click();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
	
}
