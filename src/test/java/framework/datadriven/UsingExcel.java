package framework.datadriven;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class UsingExcel {
    private WebDriver driver;

    @BeforeClass
    public void setUp() throws Exception {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();

        // Maximize the browser's window
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Constants.URL);
        driver.findElement(By.xpath("//span[text()='Learn Now']")).click();
        // Tell the code about the location of Excel file
        ExcelUtility.setExcelFile(Constants.File_Path + Constants.File_Name, "LoginTests");
        // Click login button
        driver.findElement(By.xpath("//div[@id='navbar']//a[contains(text(),'Login')]")).click();
    }

    @DataProvider(name = "loginData")
    public Object[][] dataProvider() {
        Object[][] testData = ExcelUtility.getTestData("Invalid_Login");
        return testData;
    }

    @Test(dataProvider="loginData")
    public void testUsingExcel(String username, String password) throws Exception {

        Thread.sleep(5000);
        // Enter username
        WebElement useremail = driver.findElement(By.id("user_email"));
        useremail.clear();
        useremail.sendKeys(username);
        // Enter password
        WebElement pass = driver.findElement(By.id("user_password"));
        pass.clear();
        pass.sendKeys(password);
        // Click Login button
        driver.findElement(By.name("commit")).click();
        Thread.sleep(2000);

        // Find if error messages exist
        boolean result = driver.findElements(By.xpath("//form[@id='new_user']//div[3]")).size() != 0;
        Assert.assertTrue(result);
    }

    @AfterClass
    public void tearDown() throws Exception {
        //driver.quit();
    }
}
