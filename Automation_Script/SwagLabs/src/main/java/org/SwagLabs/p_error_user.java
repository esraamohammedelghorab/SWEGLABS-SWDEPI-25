package org.SwagLabs;

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
import java.util.stream.Collectors;

public class p_error_user {

    WebDriver web;


    @BeforeTest
    public void beforeTest() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        web = new ChromeDriver(options);
        web.navigate().to("https://www.saucedemo.com/");
        web.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(5));
        WebElement id = web.findElement(By.id("user-name"));
        id.sendKeys("problem_user");

        WebElement password = web.findElement(By.id("password"));
        password.sendKeys("secret_sauce");

        WebElement loginButton = web.findElement(By.id("login-button"));
        loginButton.click();

        Thread.sleep(5000);
    }

    @AfterTest
    public void afterTest() {
        if (web != null) {
            web.quit();
        }
    }


    @BeforeMethod(onlyForGroups = "addToCart")
    public void addItemToCart() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement addToCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
        addToCart.click();
    }

    @BeforeMethod(onlyForGroups = "remove")
    public void removeItemFromCart() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement remove = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("remove-sauce-labs-backpack")));
        remove.click();
    }

    @BeforeMethod(onlyForGroups = "Menue")
    public void openMenu() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement cartIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_link")));
        cartIcon.click();

        WebElement menuButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-burger-menu-btn")));
        menuButton.click();
    }

    @BeforeMethod(onlyForGroups = "sort")
    public void openSortDropdown() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement sortDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product_sort_container")));
        sortDropdown.click();
    }


    @Test(groups = "addToCart", priority = 1)
    public void addToCartTest() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));

        WebElement removeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("remove-sauce-labs-backpack")));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_link")));
        Assert.assertEquals(cartIcon.getText(), "1");
    }

    @Test(groups = "remove", priority = 2)
    public void removeFromCartTest() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));

        WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn_inventory")));
        Assert.assertTrue(addButton.isDisplayed());

        WebElement cartIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_link")));
        Assert.assertEquals(cartIcon.getText(), "");
    }

    @Test(priority = 3)
    public void productDetailsTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
        product.click();

        String currentURL = web.getCurrentUrl();
        Assert.assertTrue(currentURL.equals("https://www.saucedemo.com/inventory-item.html?id=4"));
        Thread.sleep(3000);
    }

    @Test(priority = 4)
    public void backToProductsTest() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-to-products")));
        backButton.click();

        String currentURL = web.getCurrentUrl();
        Assert.assertTrue(currentURL.equals("https://www.saucedemo.com/inventory.html"));
    }

    @Test(priority = 5)
    public void convertButtonToRemoveTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement menuButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-burger-menu-btn")));
        menuButton.click();

        WebElement resetButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reset_sidebar_link")));
        resetButton.click();

        WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn_inventory")));
        Assert.assertEquals(addButton.getText(), "Add to cart");
    }

    @Test(priority = 6)
    public void checkCartIconTest() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement cartIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_link")));
        Assert.assertEquals(cartIcon.getText(), "");
    }

    @Test(groups = "allItems", priority = 6)
    public void allItemsTest() {
        String currentURL = web.getCurrentUrl();
        Assert.assertTrue(currentURL.equals("https://www.saucedemo.com/inventory.html"));
    }

    @Test(priority = 7)
    public void sortZATest() throws InterruptedException {
        web.findElement(By.cssSelector("[value='za']")).click();
        List<String> names = web.findElements(By.className("inventory_item_name"))
                .stream().map(WebElement::getText).collect(Collectors.toList());
        Thread.sleep(4000);
        boolean isSorted = names.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList()).equals(names);
        Assert.assertTrue(isSorted, "Items are not sorted Z→A");
    }

    @Test(priority = 8)
    public void sortAZTest() {
        web.findElement(By.cssSelector("[value='az']")).click();
        List<String> names = web.findElements(By.className("inventory_item_name"))
                .stream().map(WebElement::getText).collect(Collectors.toList());

        boolean isSorted = names.stream().sorted().collect(Collectors.toList()).equals(names);
        Assert.assertTrue(isSorted, "Items are not sorted A→Z");
    }

    @Test(priority = 9)
    public void sortLowToHighTest() {
        web.findElement(By.cssSelector("[value='lohi']")).click();
        List<Double> prices = web.findElements(By.className("inventory_item_price"))
                .stream().map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());

        boolean isSorted = prices.stream().sorted().collect(Collectors.toList()).equals(prices);
        Assert.assertTrue(isSorted, "Items are not sorted Low→High");
    }

    @Test(priority = 10)
    public void sortHighToLowTest() {
        web.findElement(By.cssSelector("[value='hilo']")).click();
        List<Double> prices = web.findElements(By.className("inventory_item_price"))
                .stream().map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());

        boolean isSorted = prices.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList()).equals(prices);
        Assert.assertTrue(isSorted, "Items are not sorted High→Low");
    }

    @Test(groups = "Menue", priority = 11)
    public void logoutTest() {
        WebDriverWait wait = new WebDriverWait(web, Duration.ofSeconds(10));
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));
        logoutButton.click();
    }
}
