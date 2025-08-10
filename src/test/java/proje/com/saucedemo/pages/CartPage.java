package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for AutomationExercise Cart Page
 */
public class CartPage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators - Updated based on GitHub project
    private final By cartTitle = By.cssSelector("#cart_info_table");
    private final By cartItems = By.cssSelector("#cart_info_table tbody tr");
    private final By productNames = By.cssSelector(".cart_description h4 a");
    private final By productPrices = By.cssSelector(".cart_price p");
    private final By productQuantities = By.cssSelector(".cart_quantity button");
    private final By totalPrices = By.cssSelector(".cart_total_price p");
    private final By deleteButtons = By.cssSelector(".cart_quantity_delete");
    private final By proceedToCheckoutButton = By.cssSelector(".btn.check_out");
    private final By continueShoppingButton = By.cssSelector(".btn.btn-default");
    private final By emptyCartMessage = By.cssSelector(".empty_cart");
    private final By cartEmptyMessage = By.cssSelector(".empty_cart p");
    private final By quantityInputs = By.cssSelector(".cart_quantity input");
    private final By updateCartButton = By.cssSelector(".btn.btn-default.btn-xs");
    private final By cartTotal = By.cssSelector(".cart_total_price p");
    private final By cartPageTitle = By.cssSelector(".breadcrumbs h2");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to cart page
     */
    public void navigateToCart() {
        logger.info("Navigating to cart page");
        driver.get("https://automationexercise.com/view_cart");
        
        // Wait for cart page to load - try multiple locators based on GitHub project
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTitle));
        } catch (Exception e) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitle));
            } catch (Exception e2) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".breadcrumbs")));
            }
        }
        
        logger.info("Successfully navigated to cart page");
    }

    /**
     * Get all cart items
     */
    public List<WebElement> getCartItems() {
        logger.info("Getting all cart items");
        return driver.findElements(cartItems);
    }

    /**
     * Get product names in cart
     */
    public List<String> getProductNames() {
        logger.info("Getting product names in cart");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get product prices in cart
     */
    public List<String> getProductPrices() {
        logger.info("Getting product prices in cart");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get product quantities in cart
     */
    public List<String> getProductQuantities() {
        logger.info("Getting product quantities in cart");
        List<WebElement> elements = driver.findElements(productQuantities);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get total prices in cart
     */
    public List<String> getTotalPrices() {
        logger.info("Getting total prices in cart");
        List<WebElement> elements = driver.findElements(totalPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Delete product from cart by index
     */
    public void deleteProductByIndex(int index) {
        logger.info("Deleting product from cart by index: {}", index);
        List<WebElement> deleteButtons = driver.findElements(this.deleteButtons);
        
        if (index >= 0 && index < deleteButtons.size()) {
            WebElement deleteButton = deleteButtons.get(index);
            deleteButton.click();
            logger.info("Product deleted from cart");
        } else {
            logger.warn("Invalid index: {}", index);
        }
    }

    /**
     * Delete all products from cart
     */
    public void deleteAllProducts() {
        logger.info("Deleting all products from cart");
        List<WebElement> deleteButtons = driver.findElements(this.deleteButtons);
        
        for (WebElement deleteButton : deleteButtons) {
            deleteButton.click();
            try {
                Thread.sleep(1000); // Wait for deletion to complete
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info("All products deleted from cart");
    }

    /**
     * Update product quantity by index
     */
    public void updateProductQuantity(int index, String newQuantity) {
        logger.info("Updating product quantity by index: {} to: {}", index, newQuantity);
        List<WebElement> quantityInputs = driver.findElements(this.quantityInputs);
        
        if (index >= 0 && index < quantityInputs.size()) {
            WebElement quantityInput = quantityInputs.get(index);
            quantityInput.clear();
            quantityInput.sendKeys(newQuantity);
            
            // Click update button if available
            try {
                WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(updateCartButton));
                updateButton.click();
                logger.info("Product quantity updated");
            } catch (Exception e) {
                logger.warn("Update button not found");
            }
        } else {
            logger.warn("Invalid index: {}", index);
        }
    }

    /**
     * Click on Proceed to Checkout button
     */
    public void clickProceedToCheckout() {
        logger.info("Clicking on Proceed to Checkout button");
        WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutButton));
        proceedButton.click();
        logger.info("Successfully clicked on Proceed to Checkout button");
    }

    /**
     * Click on Continue Shopping button
     */
    public void clickContinueShopping() {
        logger.info("Clicking on Continue Shopping button");
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
        continueButton.click();
        logger.info("Successfully clicked on Continue Shopping button");
    }

    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get cart empty message
     */
    public String getCartEmptyMessage() {
        try {
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cartEmptyMessage));
            return messageElement.getText();
        } catch (Exception e) {
            return "Cart is not empty";
        }
    }

    /**
     * Get cart items count
     */
    public int getCartItemsCount() {
        logger.info("Getting cart items count");
        List<WebElement> items = getCartItems();
        return items.size();
    }

    /**
     * Get total cart value
     */
    public String getTotalCartValue() {
        logger.info("Getting total cart value");
        try {
            List<WebElement> totalElements = driver.findElements(cartTotal);
            if (!totalElements.isEmpty()) {
                return totalElements.get(totalElements.size() - 1).getText();
            }
        } catch (Exception e) {
            logger.warn("Could not get total cart value");
        }
        return "0";
    }

    /**
     * Check if page is loaded
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Verify cart contains specific product
     */
    public boolean containsProduct(String productName) {
        logger.info("Checking if cart contains product: {}", productName);
        List<String> productNames = getProductNames();
        return productNames.stream()
                .anyMatch(name -> name.contains(productName));
    }

    /**
     * Get product quantity by product name
     */
    public String getProductQuantityByName(String productName) {
        logger.info("Getting quantity for product: {}", productName);
        List<WebElement> productElements = driver.findElements(productNames);
        List<WebElement> quantityElements = driver.findElements(quantityInputs);
        
        for (int i = 0; i < productElements.size(); i++) {
            if (productElements.get(i).getText().contains(productName)) {
                return quantityElements.get(i).getAttribute("value");
            }
        }
        return "0";
    }
} 