package org.SwagLabs;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;
import static java.lang.Thread.sleep;

public class ck_problem_user {
    WebDriver web;
    WebDriverWait wait;
    @BeforeClass
    public void setup() {
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

    }

    @Test
    public void ck_happy_pass() throws InterruptedException {
        WebElement id =web.findElement(By.id("user-name"));
        id.sendKeys("problem_user");
        WebElement search =web.findElement(By.id("password"));
        search.sendKeys("secret_sauce");
        WebElement Button =web.findElement(By.id("login-button"));
        Button.click();
        WebElement product =web.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        product.click();
        Thread.sleep(3000);
        WebElement cartlogo =web.findElement(By.className("shopping_cart_link"));
        cartlogo.click();
        WebElement checkout_button =web.findElement(By.id("checkout"));
        checkout_button.click();
        web.findElement(By.id("first-name")).sendKeys("Mohamed");
        web.findElement(By.id("last-name")).sendKeys("Yousry");
        web.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(3000);
        web.findElement(By.id("continue")).click();
        Thread.sleep(3000);
        web.findElement(By.id("finish")).click();
        Thread.sleep(1000);
        String successMsg = web.findElement(By.className("complete-header")).getText();
        Assert.assertTrue(successMsg.contains("Thank you for your order!"));

    }
    @AfterClass
    public void tearDown() {
        web.quit();
        System.out.println("Browser closed");
    }

}
