package org.SwagLabs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class Integration {

    WebDriver driver;
    WebDriverWait wait;
    final String BASE_URL = "https://www.saucedemo.com/";

    @BeforeClass
    public void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void login(String username, String password) {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    @Test(priority = 1, description = "Scenario 1: End to End Checkout Flow Test")
    public void endToEndCheckoutTest() throws InterruptedException {
        login("standard_user", "secret_sauce");

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        driver.findElement(By.id("item_1_title_link")).click();
        driver.navigate().back();
        Thread.sleep(1000);

        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
        Assert.assertTrue(driver.findElements(By.className("cart_item")).size() >= 2);
        Thread.sleep(1000);

        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        driver.findElement(By.id("first-name")).sendKeys("Bassem");
        driver.findElement(By.id("last-name")).sendKeys("Kamal");
        driver.findElement(By.id("postal-code")).sendKeys("010");
        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        driver.findElement(By.id("finish")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-to-products")));
    }

    @Test(priority = 2, description = "Scenario 2: Add and Remove Item Sync Test")
    public void addRemoveItemSyncTest() throws InterruptedException {
        login("standard_user", "secret_sauce");

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        Thread.sleep(1000);

        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
        Thread.sleep(1000);
        Assert.assertEquals(driver.findElements(By.className("cart_item")).size(), 0);
    }

    @Test(priority = 3, description = "Scenario 3: Big Cart Stress Test")
    public void bigCartStressTest() throws InterruptedException {
        login("standard_user", "secret_sauce");

        String[] allItems = {
                "add-to-cart-sauce-labs-backpack",
                "add-to-cart-sauce-labs-bike-light",
                "add-to-cart-sauce-labs-bolt-t-shirt",
                "add-to-cart-sauce-labs-fleece-jacket",
                "add-to-cart-sauce-labs-onesie",
                "add-to-cart-sauce-labs-red-onesie"
        };
        Thread.sleep(1000);

        for(String id: allItems) driver.findElement(By.id(id)).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
        Thread.sleep(1000);
        Assert.assertEquals(driver.findElements(By.className("cart_item")).size(), allItems.length);
        Thread.sleep(1000);

        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        driver.findElement(By.id("first-name")).sendKeys("Bassem");
        driver.findElement(By.id("last-name")).sendKeys("Kamal");
        driver.findElement(By.id("postal-code")).sendKeys("010");
        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        driver.findElement(By.id("finish")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-to-products")));
    }

    @Test(priority = 4, description = "Scenario 4: Sidebar Navigation Test")
    public void sidebarNavigationTest() throws InterruptedException {
        login("standard_user", "secret_sauce");

        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("about_sidebar_link"))).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("saucelabs.com"));
        Thread.sleep(1000);
        driver.navigate().back();

        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))).click();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    }

    @Test(priority = 5, description = "Scenario 5: Price Calculation Test")
    public void priceCalculationTest() throws InterruptedException {
        login("standard_user", "secret_sauce");

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        Thread.sleep(1000);

        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));

        List<WebElement> prices = driver.findElements(By.className("inventory_item_price"));
        double sum = prices.stream()
                .mapToDouble(p -> Double.parseDouble(p.getText().replace("$",""))).sum();
        Thread.sleep(1000);

        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        driver.findElement(By.id("first-name")).sendKeys("Bassem");
        driver.findElement(By.id("last-name")).sendKeys("Kamal");
        driver.findElement(By.id("postal-code")).sendKeys("010");
        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        driver.findElement(By.id("finish")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-to-products")));
    }

    @Test(priority = 6, description = "Scenario 6: Checkout with Removed Items Negative Test")
    public void checkoutWithRemovedItemsTest() throws InterruptedException {
        login("standard_user", "secret_sauce");

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        Thread.sleep(1000);

        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        Thread.sleep(1000);

        driver.navigate().back();
        wait.until(ExpectedConditions.urlContains("cart.html"));
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        driver.findElement(By.id("first-name")).sendKeys("Bassem");
        driver.findElement(By.id("last-name")).sendKeys("Kamal");
        driver.findElement(By.id("postal-code")).sendKeys("010");
        driver.findElement(By.id("continue")).click();
        Thread.sleep(1000);

        WebElement errorMsg = driver.findElement(By.cssSelector("h3[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed());

        driver.get(BASE_URL + "cart.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
        int remainingItems = driver.findElements(By.className("cart_item")).size();
        Assert.assertEquals(remainingItems, 1);
    }

}
