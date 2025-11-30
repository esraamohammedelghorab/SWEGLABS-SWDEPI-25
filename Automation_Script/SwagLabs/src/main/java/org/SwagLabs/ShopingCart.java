package org.SwagLabs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

import static java.lang.Thread.sleep;

public class ShopingCart {
    WebDriver driver;
    WebDriverWait wait;



    @BeforeSuite
    public void beforeSuite() {
        System.out.println("=== Test Suite: Sauce Demo E-commerce Test ===");
        System.setProperty("webdriver.chrome.driver","D:\\Bassem DEPI\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
    }

    @BeforeClass
    public void setupClass() {
        System.out.println("Before Class...");
    }

//    @BeforeMethod
//    public void setup() {
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        driver.manage().window().maximize();
//        System.out.println("Browser launched and maximized");
//    }
@BeforeMethod
public void setup() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--incognito");
    options.addArguments("--start-maximized");
    options.addArguments("--disable-notifications");
    options.addArguments("--disable-extensions");
    driver = new ChromeDriver(options);
    wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Increased timeout for better stability
    System.out.println("Browser launched in incognito mode and maximized");
}

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }

    @AfterClass
    public void cleanUpClass() {
        System.out.println("Test class execution completed");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("=== Test Suite Execution Finished ===");
    }

    private void waitForElement(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitForClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private String getElementText(By locator) {
        return driver.findElement(locator).getText();
    }

    // Test Method
    @Test(priority = 1, enabled = true, description = "Complete shopping flow: Login, Add items to cart, and verify cart contents")
    public void testShoppingCartFlow() throws InterruptedException {
        System.out.println("\nStarting test: Shopping Cart Flow Verification");

        System.out.println("Step 1: Navigating to Sauce Demo website");
        driver.get("https://www.saucedemo.com/");
        waitForElement(By.className("login_container"));
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"), "Should be on Sauce Demo login page");
        System.out.println("✓ Successfully navigated to Sauce Demo");

        // Step 2: Enter username "standard_user"
        System.out.println("Step 2: Entering username");
        WebElement usernameField = driver.findElement(By.id("user-name"));
        usernameField.clear();
        usernameField.sendKeys("standard_user");
        System.out.println("✓ Username entered: standard_user");

        // Step 3: Enter password "secret_sauce"
        System.out.println("Step 3: Entering password");
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("secret_sauce");
        System.out.println("✓ Password entered");

        // Step 4: Click on "Login" button
        System.out.println("Step 4: Clicking login button");
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        // Verify login success
        waitForElement(By.className("inventory_list"));
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Should be redirected to inventory page after login");
        System.out.println("✓ Successfully logged in and redirected to inventory page");

        // Step 5: Browse to inventory page (already there after login)
        System.out.println("Step 5: Browsing inventory page");
        String pageTitle = getElementText(By.className("title"));
        Assert.assertEquals(pageTitle, "Products", "Should be on Products page");
        System.out.println("✓ On inventory page: " + pageTitle);

        // Step 6: Add 'Sauce Labs Onesie' and "Sauce Labs Backpack" to cart
        System.out.println("Step 6: Adding items to cart");

        // Add -to-cart-sauce-labs-bike-light
        WebElement onesieAddButton = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
        onesieAddButton.click();
        sleep(3000);
        WebElement backpackAddButton = driver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt"));
        backpackAddButton.click();
        sleep(3000);

        // Step 7: Click on cart icon
        System.out.println("Step 7: Clicking cart icon");
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();

        // Verify we're on cart page
        waitForElement(By.className("cart_list"));
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"), "Should be on cart page");
        String cartTitle = getElementText(By.className("title"));
        Assert.assertEquals(cartTitle, "Your Cart", "Should be on Your Cart page");
        System.out.println("✓ Successfully navigated to cart page");

        // Step 8: Verify both items appear in the cart with correct details
        System.out.println("Step 8: Verifying cart items and details");

        // Get all cart items
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 2, "Should have 2 items in cart");
        System.out.println("✓ Cart contains " + cartItems.size() + " items");

        sleep(3000);

        //System.out.println("🎉 All validations passed! Test completed successfully.");

        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        sleep(3000);
        driver.findElement(By.name("firstName")).sendKeys("Bassem");
        driver.findElement(By.name("lastName")).sendKeys("Kamal");
        driver.findElement(By.name("postalCode")).sendKeys("010");
        sleep(3000);

        driver.findElement(By.id("continue")).click();
        sleep(3000);
        driver.findElement(By.id("finish")).click();
        sleep(3000);
        driver.findElement(By.id("back-to-products")).click();


        sleep(3000);
    }

    @Test(priority = 2, enabled = true, description = "Additional test: Verify empty cart functionality and cart icon not clickable")
    public void testEmptyCart() throws InterruptedException {
        System.out.println("\nStarting test: Empty Cart Verification");
        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");

        sleep(3000);
        driver.findElement(By.id("login-button")).click();

        // Wait for inventory page to load
        waitForElement(By.className("inventory_list"));

        // Store current URL before clicking cart
        String currentUrlBeforeClick = driver.getCurrentUrl();
        System.out.println("Current URL before cart click: " + currentUrlBeforeClick);

        // Try to click cart icon
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(3000);

        String currentUrlAfterClick = driver.getCurrentUrl();
        System.out.println("Current URL after cart click: " + currentUrlAfterClick);

        // Verify URL didn't change (cart click should not navigate)
        Assert.assertEquals(currentUrlAfterClick, currentUrlBeforeClick,
                "Cart icon should not be clickable/navigable when empty");

        System.out.println("✓ Cart icon correctly not clickable when empty");

        // Continue with existing empty cart verification
        driver.get("https://www.saucedemo.com/cart.html");
        waitForElement(By.className("cart_list"));
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 0, "Cart should be empty for new user");
        System.out.println("✓ Empty cart verified successfully");
    }

    @Test(priority = 3, enabled = true, description = "Test item removal from cart")
    public void testRemoveItemFromCart() throws InterruptedException {
        System.out.println("\nStarting test: Remove Item From Cart");
        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        sleep(3000);
        // Add item to cart
        waitForElement(By.id("add-to-cart-sauce-labs-onesie"));
        driver.findElement(By.id("add-to-cart-sauce-labs-onesie")).click();

        // Go to cart
        driver.findElement(By.className("shopping_cart_link")).click();
        sleep(3000);
        // Remove item from cart
        waitForElement(By.id("remove-sauce-labs-onesie"));
        driver.findElement(By.id("remove-sauce-labs-onesie")).click();

        // Verify item is removed
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 0, "Cart should be empty after removal");
        System.out.println("✓ Item successfully removed from cart");
    }

    @Test(priority = 4, enabled = true, description = "Verify items added to cart are displayed correctly")
    public void testAddItemToCart() throws InterruptedException {
        System.out.println("\nStarting test: Remove Item From Cart");
        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        sleep(3000);
        // Add item to cart
        waitForElement(By.id("add-to-cart-sauce-labs-onesie"));
        driver.findElement(By.id("add-to-cart-sauce-labs-onesie")).click();

        // Go to cart
        driver.findElement(By.className("shopping_cart_link")).click();
        sleep(3000);
        // Verify item is removed
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 1, "Should have 1 items in cart");
        System.out.println("✓ Item successfully added to cart");
    }

    @Test(priority = 5,enabled = true, description = "Verify cart item quantity can be updated correctly through item management")
    public void testCartItemQuantityUpdate() throws InterruptedException {
        System.out.println("\n=== Starting Test: Cart Item Quantity Update Verification ===");

        // Step 1: Navigate and login
        System.out.println("Step 1: Navigating to Sauce Demo and logging in");
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        sleep(3000);
        // Verify login success
        waitForElement(By.className("inventory_list"));
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Should be on inventory page");
        System.out.println("✓ Successfully logged in");

        // Step 2: Test initial cart state
        System.out.println("\nStep 2: Verifying initial cart state");

        // Check cart is initially empty (no badge visible)
        List<WebElement> initialCartBadges = driver.findElements(By.className("shopping_cart_badge"));
        Assert.assertTrue(initialCartBadges.isEmpty(), "Cart should be empty initially");
        System.out.println("✓ Cart is initially empty");

        // Step 3: Add first item and verify quantity
        System.out.println("\nStep 3: Adding first item to cart");

        // Add Sauce Labs Backpack
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        sleep(3000);
        // Verify cart badge shows 1
        String cartBadgeAfterFirstItem = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartBadgeAfterFirstItem, "1", "Cart should show 1 item after adding first product");
        System.out.println("✓ Cart badge updated to: " + cartBadgeAfterFirstItem);

        // Step 4: Add second item and verify quantity increases
        System.out.println("\nStep 4: Adding second item to cart");

        // Add Sauce Labs Bike Light
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        sleep(3000);
        // Verify cart badge shows 2
        String cartBadgeAfterSecondItem = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartBadgeAfterSecondItem, "2", "Cart should show 2 items after adding second product");
        System.out.println("✓ Cart badge updated to: " + cartBadgeAfterSecondItem);

        // Step 5: Navigate to cart and verify quantities
        System.out.println("\nStep 5: Verifying cart page quantities");

        driver.findElement(By.className("shopping_cart_link")).click();
        sleep(3000);
        waitForElement(By.className("cart_list"));
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"), "Should be on cart page");
        System.out.println("✓ Navigated to cart page");

        // Verify we have 2 items in cart
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 2, "Should have 2 items in cart");
        System.out.println("✓ Cart contains " + cartItems.size() + " items");

        // Step 6: Return to inventory and remove one item
        System.out.println("\nStep 6: Removing one item from inventory");

        driver.findElement(By.id("continue-shopping")).click();
        sleep(2000);
        waitForElement(By.className("inventory_list"));
        System.out.println("✓ Returned to inventory page");

        // Remove Sauce Labs Backpack
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        sleep(3000);
        System.out.println("✓ Removed Sauce Labs Backpack from cart");

        // Verify cart badge decreases to 1
        String cartBadgeAfterRemoval = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartBadgeAfterRemoval, "1", "Cart should show 1 item after removal");
        System.out.println("✓ Cart badge updated to: " + cartBadgeAfterRemoval);

        // Step 7: Go back to cart and verify updated quantity
        System.out.println("\nStep 7: Verifying updated cart state");

        driver.findElement(By.className("shopping_cart_link")).click();
        sleep(3000);
        waitForElement(By.className("cart_list"));

        // Verify only 1 item remains
        List<WebElement> updatedCartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(updatedCartItems.size(), 1, "Should have 1 item in cart after removal");

        // Verify the remaining item is correct and shows quantity 1
        WebElement remainingItem = updatedCartItems.get(0);
        String remainingItemName = remainingItem.findElement(By.className("inventory_item_name")).getText();
        String remainingItemQuantity = remainingItem.findElement(By.className("cart_quantity")).getText();

        Assert.assertEquals(remainingItemName, "Sauce Labs Bike Light", "Remaining item should be Bike Light");
        Assert.assertEquals(remainingItemQuantity, "1", "Remaining item should show quantity 1");
        System.out.println("✓ Only " + remainingItemName + " remains with quantity: " + remainingItemQuantity);

        // Step 8: Remove last item and verify empty cart
        System.out.println("\nStep 8: Testing empty cart scenario");

        driver.findElement(By.id("continue-shopping")).click();
        sleep(3000);
        waitForElement(By.className("inventory_list"));

        // Remove the last item
        driver.findElement(By.id("remove-sauce-labs-bike-light")).click();
        sleep(3000);
        System.out.println("✓ Removed Sauce Labs Bike Light from cart");

        // Verify cart badge disappears (empty cart)
        List<WebElement> finalCartBadges = driver.findElements(By.className("shopping_cart_badge"));
        Assert.assertTrue(finalCartBadges.isEmpty(), "Cart badge should not be visible when cart is empty");
        System.out.println("✓ Cart badge correctly hidden when cart is empty");

        // Step 9: Final verification - go to cart and confirm it's empty
        System.out.println("\nStep 9: Final empty cart verification");

        driver.findElement(By.className("shopping_cart_link")).click();
        sleep(3000);
        waitForElement(By.className("cart_list"));

        List<WebElement> finalCartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(finalCartItems.size(), 0, "Cart should be empty");
        System.out.println("✓ Cart is completely empty - 0 items");

        System.out.println("\n TEST PASSED: Cart item quantity management verified successfully!");
        System.out.println(" Item addition increases cart count correctly");
        System.out.println(" Item removal decreases cart count correctly");
        System.out.println(" Cart badge accurately reflects item count");
        System.out.println(" Empty cart state handled properly");
        System.out.println(" All items show fixed quantity 1 in cart (as designed)");
    }

    @Test(priority = 6, enabled = true, description = "Verify cart is empty after removing all items")
    public void testRemoveAllItemsFromCart() throws InterruptedException {
        System.out.println("\nStarting test: Remove Item From Cart");
        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        sleep(3000);
        // Add item to cart
        waitForElement(By.id("add-to-cart-sauce-labs-onesie"));
        driver.findElement(By.id("add-to-cart-sauce-labs-onesie")).click();

        waitForElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt"));
        driver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt"));
        // Go to cart
        driver.findElement(By.className("shopping_cart_link")).click();
        sleep(3000);
        // Remove item from cart
        waitForElement(By.id("remove-sauce-labs-onesie"));
        driver.findElement(By.id("remove-sauce-labs-onesie")).click();

        waitForElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt"));
        driver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt")).click();

        // Verify item is removed
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 0, "Cart should be empty after removal");
        System.out.println("✓ Items successfully removed from cart");
    }

    @Test(priority = 7, enabled = true, description = "Cart is not accessible without login - user redirected to login ")
    public void Verify_non_logged() throws InterruptedException {

        driver.get("https://www.saucedemo.com/cart.html");
        waitForElement(By.className("login_container"));

        Assert.assertFalse(driver.getCurrentUrl().contains("cart"), "User should be redirected to login page, not remain on cart page");

        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com") &&!driver.getCurrentUrl().contains("cart"),
                "Should be redirected to login page");

        System.out.println("✓ Cart is not accessible without login - user redirected to login");
    }

}