package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class UntitledTestCase {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testUntitledTestCase() throws Exception {
    driver.get("http://localhost:8080/api/swagger-ui/index.html");
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div")).click();
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div/button/span")).click();
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div[2]/div/div/div/div[2]/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).click();
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).clear();
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).sendKeys("{\n  \"username\": \"arshia\",\n  \"password\": \"Atal\",\n  \"email\": \"arshiaataei81@gmail.com\",\n  \"role\": \"client\",\n  \"address\": {\n    \"country\": \"Iran\",\n    \"city\": \"Tehran\"\n  }\n}");
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div[2]/div/div/div[3]/div[2]")).click();
    driver.findElement(By.xpath("//div[@id='operations-authentication-controller-signup']/div[2]/div/div[2]/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div[2]/div/div/div/div[2]/button")).click();
    driver.findElement(By.xpath("//input[@value='']")).click();
    driver.findElement(By.xpath("//input[@value='1']")).clear();
    driver.findElement(By.xpath("//input[@value='1']")).sendKeys("1");
    driver.findElement(By.xpath("//input[@value='1']")).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]")).click();
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td[2]/div/textarea")).click();
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td[2]/div/textarea")).clear();
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td[2]/div/textarea")).sendKeys("null");
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td")).click();
    driver.findElement(By.xpath("//div[@id='operations-restaurant-controller-getRestaurants']/div[2]/div/div[2]/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div/button/span[2]/a/span")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div/div/div[2]/button")).click();
    driver.findElement(By.xpath("//input[@value='']")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]/input")).clear();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]/input")).sendKeys("1");
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]/input")).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]")).click();
    driver.findElement(By.xpath("//input[@value='']")).click();
    driver.findElement(By.xpath("//input[@value='2']")).clear();
    driver.findElement(By.xpath("//input[@value='2']")).sendKeys("2");
    driver.findElement(By.xpath("//input[@value='2']")).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td[2]")).click();
    driver.findElement(By.xpath("//input[@value='2025-10-26']")).clear();
    driver.findElement(By.xpath("//input[@value='2025-10-26']")).sendKeys("2025-10-26");
    driver.findElement(By.xpath("//input[@value='2025-10-26']")).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div/div[2]/div/table/tbody/tr[2]/td[2]")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getAvailableTimes']/div[2]/div/div[2]/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div/button/span[2]/a/span")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div/div[2]/button")).click();
    driver.findElement(By.xpath("//input[@value='']")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]/input")).clear();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]/input")).sendKeys("1");
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]/input")).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).clear();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[3]/div[2]/div/div/div/textarea")).sendKeys("{\n  \"people\": \"2\",\n  \"datetime\": \"2025-10-26 13:00\"\n}");
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div/div[3]/div[2]")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-addReservation']/div[2]/div/div[2]/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getCustomerReservations']/div/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getCustomerReservations']/div[2]/div/div/div/div[2]/button")).click();
    driver.findElement(By.xpath("//input[@value='']")).click();
    driver.findElement(By.xpath("//input[@value='4']")).clear();
    driver.findElement(By.xpath("//input[@value='4']")).sendKeys("4");
    driver.findElement(By.xpath("//input[@value='4']")).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getCustomerReservations']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-getCustomerReservations']/div[2]/div/div[2]/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-cancelReservation']/div/button")).click();
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-cancelReservation']/div[2]/div/div/div/div[2]/button")).click();
    driver.findElement(By.xpath("//input[@value='']")).click();
    driver.findElement(By.xpath("//input[@value='0']")).clear();
    driver.findElement(By.xpath("//input[@value='0']")).sendKeys("0");
    driver.findElement(By.xpath("//input[@value='0']")).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath("//div[@id='operations-reservation-controller-cancelReservation']/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
