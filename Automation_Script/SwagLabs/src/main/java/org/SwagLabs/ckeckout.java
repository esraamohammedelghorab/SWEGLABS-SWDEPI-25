
package org.SwagLabs;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.Assert;
import java.time.Duration;

import static java.lang.Thread.sleep;

public class ckeckout {
    WebDriver web;
    WebDriverWait wait;

    @BeforeClass
    public void setup() throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        web = new ChromeDriver(options);
        wait = new WebDriverWait(web, Duration.ofSeconds(5)); // Increased timeout for better stability
        System.out.println("Browser launched in incognito mode and maximized");
        web.navigate().to("https://www.saucedemo.com/");
        web.manage().window().maximize();

        WebElement id =web.findElement(By.id("user-name"));
        id.sendKeys("standard_user");
        WebElement search =web.findElement(By.id("password"));
        search.sendKeys("secret_sauce");
        WebElement Button =web.findElement(By.id("login-button"));
        Button.click();
        WebElement product =web.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        product.click();
        Thread.sleep(5000);
        WebElement cartlogo =web.findElement(By.className("shopping_cart_link"));
        cartlogo.click();
        WebElement checkout_button =web.findElement(By.id("checkout"));
        checkout_button.click();
    }

    @Test(priority = 1)
    public void ck_yourinformation_ValidData() throws InterruptedException {
        web.findElement(By.id("first-name")).sendKeys("Mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        web.findElement(By.id("finish")).click();
        Thread.sleep(1000);
        String successMsg = web.findElement(By.className("complete-header")).getText();
        Assert.assertTrue(successMsg.contains("Thank you for your order!"));

    }
    @Test(priority = 2)
    public void emptyFirstNameShowsError() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        Thread.sleep(5000);

        web.findElement(By.id("first-name")).sendKeys("");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        String errorMsg = web.findElement(By.className("error-message-container")).getText();
        Assert.assertEquals(errorMsg, "Error: First Name is required");
    }
    @Test(priority = 3)
    public void emptylastNameShowsError() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        Thread.sleep(5000);
        web.findElement(By.id("first-name")).sendKeys("mohamed");
        web.findElement(By.id("last-name")).sendKeys("");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        String errorMsg = web.findElement(By.className("error-message-container")).getText();
        Assert.assertEquals(errorMsg, "Error: Last Name is required");
    }
    @Test(priority = 4)
    public void emptyPostalCodeShowsError() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        Thread.sleep(5000);
        web.findElement(By.id("first-name")).sendKeys("mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        String errorMsg = web.findElement(By.className("error-message-container")).getText();
        Assert.assertEquals(errorMsg, "Error: Postal Code is required");
    }
    @Test(priority = 5)
    public void emptyِAllfieldsShowsError() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        Thread.sleep(5000);
        web.findElement(By.id("first-name")).sendKeys("");
        web.findElement(By.id("last-name")).sendKeys("");
        web.findElement(By.id("postal-code")).sendKeys("");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        String errorMsg = web.findElement(By.className("error-message-container")).getText();
        Assert.assertEquals(errorMsg, "Error: First Name is required");
    }
    @Test(priority = 6)
    public void ck_space_at_ِAllFields_ShowsError() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        Thread.sleep(5000);

        web.findElement(By.id("first-name")).sendKeys(" ");
        web.findElement(By.id("last-name")).sendKeys(" ");
        web.findElement(By.id("postal-code")).sendKeys(" ");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        String errorMsg = web.findElement(By.className("error-message-container")).getText();
        Assert.assertEquals(errorMsg, "Error: First Name is required");
    }

    @Test(priority = 7)
    public void ck_overview_Verify_item_name() throws InterruptedException {
        web.get("https://www.saucedemo.com/inventory.html");

        WebElement product =web.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        product.click();
        Thread.sleep(5000);
        WebElement cartlogo =web.findElement(By.className("shopping_cart_link"));
        cartlogo.click();
        WebElement checkout_button =web.findElement(By.id("checkout"));
        checkout_button.click();
        web.findElement(By.id("first-name")).sendKeys("Mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        String productName = web.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(productName, "Sauce Labs Backpack");
    }
    @Test(priority = 8)
    public void ck_overview_Verify_item_price() throws InterruptedException {
        web.get("https://www.saucedemo.com/inventory.html");
        Thread.sleep(5000);
        WebElement cartlogo =web.findElement(By.className("shopping_cart_link"));
        cartlogo.click();
        WebElement checkout_button =web.findElement(By.id("checkout"));
        checkout_button.click();
        Thread.sleep(5000);
        web.findElement(By.id("first-name")).sendKeys("Mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        String productvalue = web.findElement(By.className("inventory_item_price")).getText();
        Assert.assertEquals(productvalue, "$29.99");
    }
    @Test(priority = 9)
    public void ck_yourinformation_validite_CancelBtn_navigate() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        web.findElement(By.id("first-name")).sendKeys("Mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("cancel")).click();
        Thread.sleep(5000);
        Assert.assertEquals(web.getCurrentUrl(), "https://www.saucedemo.com/cart.html");

    }
    @Test(priority = 10)
    public void ck_Overview_validite_CancelBtn_navigate() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        web.findElement(By.id("first-name")).sendKeys("Mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        web.findElement(By.id("cancel")).click();
        Thread.sleep(5000);
        Assert.assertEquals(web.getCurrentUrl(), "https://www.saucedemo.com/inventory.html");

    }
    @Test(priority = 11)
    public void ck_Overview_validite_CartBtn_navigate() throws InterruptedException {
        web.get("https://www.saucedemo.com/checkout-step-one.html");
        web.findElement(By.id("first-name")).sendKeys("Mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(5000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(5000);
        web.findElement(By.className("shopping_cart_link"));
        Thread.sleep(5000);
        Assert.assertEquals(web.getCurrentUrl(), "https://www.saucedemo.com/cart.html");

    }

    @AfterClass
    public void tearDown() {
        web.quit();
        System.out.println("Browser closed");
    }


}