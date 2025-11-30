package org.SwagLabs;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginAuto {

    WebDriver driver;
    WebDriverWait wait;

    By usernameField = By.id("user-name");
    By passwordField = By.id("password");
    By loginBtn = By.id("login-button");
    By errorMsg = By.cssSelector("h3[data-test='error']");
    By errorCloseBtn = By.className("error-button");
    By inventoryPageHeader = By.className("title");

    @BeforeClass
    public void setUp() {
        System.out.println("Swag Labs Login Test Class Started");
//        driver = new ChromeDriver();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options); // بدل driver
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }

    private void login(String username, String password) {
        driver.get("https://www.saucedemo.com/"); // اعادة تحميل الصفحة لكل login
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginBtn).click();
    }

    private String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg));
        return driver.findElement(errorMsg).getText();
    }

    // ===================== Tests =====================

    @Test(priority = 1, description = "Valid login - standard_user")
    public void validLoginTest() throws InterruptedException {
        login("standard_user", "secret_sauce");
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryPageHeader));
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
        Assert.assertEquals(driver.findElement(inventoryPageHeader).getText(), "Products");
        System.out.println("Valid login successful");
        Thread.sleep(5000);
    }

    @Test(priority = 2, description = "Invalid username")
    public void invalidUsernameTest() throws InterruptedException {
        login("wrong_user", "secret_sauce");
        Assert.assertTrue(getErrorMessage().contains("do not match any user"));
        System.out.println("Error appeared for invalid username");
        Thread.sleep(5000);
    }

    @Test(priority = 3, description = "Invalid password")
    public void invalidPasswordTest() throws InterruptedException {
        login("standard_user", "wrong_password");
        Assert.assertTrue(getErrorMessage().contains("do not match any user"));
        System.out.println("Error appeared for invalid password");
        Thread.sleep(5000);
    }

    @Test(priority = 4, description = "Empty username")
    public void emptyUsernameTest() throws InterruptedException {
        login("", "secret_sauce");
        Assert.assertTrue(getErrorMessage().contains("Username is required"));
        System.out.println("Error appeared for empty username");
        Thread.sleep(5000);
    }

    @Test(priority = 5, description = "Empty password")
    public void emptyPasswordTest() throws InterruptedException {
        login("standard_user", "");
        Assert.assertTrue(getErrorMessage().contains("Password is required"));
        System.out.println("Error appeared for empty password");
        Thread.sleep(5000);
    }

    @Test(priority = 6, description = "Locked out user login attempt")
    public void lockedOutUserTest() throws InterruptedException {
        login("locked_out_user", "secret_sauce");
        Assert.assertTrue(getErrorMessage().contains("locked out"));
        System.out.println("Locked out user error verified");
        Thread.sleep(5000);
    }

    @Test(priority = 7, description = "Performance glitch user login")
    public void performanceGlitchUserTest() throws InterruptedException {
        login("performance_glitch_user", "secret_sauce");
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryPageHeader));
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
        System.out.println("Performance glitch user login successful");
        Thread.sleep(5000);
    }

    @Test(priority = 8, description = "Problem user login")
    public void problemUserLoginTest() throws InterruptedException {
        login("problem_user", "secret_sauce");
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryPageHeader));
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
        System.out.println("Problem user login successful (with UI issues)");
        Thread.sleep(5000);
    }

    @Test(priority = 9, description = "Error message should disappear after closing")
    public void errorMessageCloseTest() throws InterruptedException {
        login("wrong_user", "wrong_pass");
        Assert.assertTrue(driver.findElement(errorMsg).isDisplayed());
        driver.findElement(errorCloseBtn).click();
        Assert.assertTrue(driver.findElements(errorMsg).isEmpty());
        System.out.println("Error message successfully dismissed");
        Thread.sleep(5000);
    }

    @Test(priority = 10, description = "Check username & password placeholders")
    public void placeholderTest() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");
        Assert.assertEquals(driver.findElement(usernameField).getAttribute("placeholder"), "Username");
        Assert.assertEquals(driver.findElement(passwordField).getAttribute("placeholder"), "Password");
        System.out.println("Placeholders verified successfully");
        Thread.sleep(5000);
    }

    @Test(priority = 11, description = "Verify Login button is enabled")
    public void loginButtonEnabledTest() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");
        Assert.assertTrue(driver.findElement(loginBtn).isEnabled());
        System.out.println("Login button is enabled by default");
        Thread.sleep(5000);
    }

    @Test(priority = 12, description = "Verify redirect after login")
    public void verifyRedirectTest() throws InterruptedException {
        login("standard_user", "secret_sauce");
        wait.until(ExpectedConditions.urlContains("inventory"));
        Assert.assertEquals(driver.findElement(inventoryPageHeader).getText(), "Products");
        System.out.println("User redirected successfully after login");
        Thread.sleep(5000);
    }
}