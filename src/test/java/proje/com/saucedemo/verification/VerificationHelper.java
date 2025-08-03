package proje.com.saucedemo.verification;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.utils.ZipkinTracer;

import java.time.Duration;
import java.util.List;

/**
 * Verification helper class for SauceDemo test automation
 * Contains all verification methods in one place
 */
public class VerificationHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(VerificationHelper.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    public VerificationHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Verify login was successful
     * @return True if login successful
     */
    public boolean verifyLoginSuccessful() {
        try {
            ZipkinTracer.startSpan("verify-login-successful");
            ZipkinTracer.addTag("verification", "login");
            
            // Check if we're on the inventory page (successful login)
            boolean isOnInventoryPage = driver.getCurrentUrl().contains("/inventory.html");
            boolean hasShoppingCart = driver.findElements(By.className("shopping_cart_link")).size() > 0;
            
            boolean loginSuccessful = isOnInventoryPage && hasShoppingCart;
            
            logger.info("Login verification result: {}", loginSuccessful);
            ZipkinTracer.addTag("login_successful", String.valueOf(loginSuccessful));
            ZipkinTracer.finishSpan();
            
            return loginSuccessful;
            
        } catch (Exception e) {
            logger.error("Login verification failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify products are added to cart
     * @param expectedProducts List of expected product names
     * @return True if all products are in cart
     */
    public boolean verifyProductsInCart(List<String> expectedProducts) {
        try {
            ZipkinTracer.startSpan("verify-products-in-cart");
            ZipkinTracer.addTag("expected_products_count", String.valueOf(expectedProducts.size()));
            
            // Navigate to cart
            WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link")));
            cartLink.click();
            
            // Wait for cart page to load
            wait.until(ExpectedConditions.urlContains("/cart.html"));
            
            // Get all cart items
            List<WebElement> cartItems = driver.findElements(By.className("inventory_item_name"));
            
            // Verify each expected product is in cart
            boolean allProductsFound = true;
            for (String expectedProduct : expectedProducts) {
                boolean productFound = cartItems.stream()
                        .anyMatch(item -> item.getText().equals(expectedProduct));
                
                if (!productFound) {
                    logger.warn("Product not found in cart: {}", expectedProduct);
                    allProductsFound = false;
                } else {
                    logger.info("Product found in cart: {}", expectedProduct);
                }
            }
            
            logger.info("Cart verification result: {} (Expected: {}, Found: {})", 
                    allProductsFound, expectedProducts.size(), cartItems.size());
            
            ZipkinTracer.addTag("cart_verification_successful", String.valueOf(allProductsFound));
            ZipkinTracer.addTag("actual_products_count", String.valueOf(cartItems.size()));
            ZipkinTracer.finishSpan();
            
            return allProductsFound;
            
        } catch (Exception e) {
            logger.error("Cart verification failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify checkout information page is displayed
     * @return True if checkout page is displayed
     */
    public boolean verifyCheckoutPageDisplayed() {
        try {
            ZipkinTracer.startSpan("verify-checkout-page");
            
            boolean isOnCheckoutPage = driver.getCurrentUrl().contains("/checkout-step-one.html");
            boolean hasFirstNameField = driver.findElements(By.id("first-name")).size() > 0;
            boolean hasLastNameField = driver.findElements(By.id("last-name")).size() > 0;
            boolean hasPostalCodeField = driver.findElements(By.id("postal-code")).size() > 0;
            boolean hasContinueButton = driver.findElements(By.id("continue")).size() > 0;
            
            boolean checkoutPageDisplayed = isOnCheckoutPage && hasFirstNameField && hasLastNameField && 
                    hasPostalCodeField && hasContinueButton;
            
            logger.info("Checkout page verification result: {}", checkoutPageDisplayed);
            ZipkinTracer.addTag("checkout_page_displayed", String.valueOf(checkoutPageDisplayed));
            ZipkinTracer.finishSpan();
            
            return checkoutPageDisplayed;
            
        } catch (Exception e) {
            logger.error("Checkout page verification failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify checkout overview page is displayed
     * @return True if overview page is displayed
     */
    public boolean verifyCheckoutOverviewDisplayed() {
        try {
            ZipkinTracer.startSpan("verify-checkout-overview");
            
            boolean isOnOverviewPage = driver.getCurrentUrl().contains("/checkout-step-two.html");
            boolean hasFinishButton = driver.findElements(By.id("finish")).size() > 0;
            
            boolean overviewDisplayed = isOnOverviewPage && hasFinishButton;
            
            logger.info("Checkout overview verification result: {}", overviewDisplayed);
            ZipkinTracer.addTag("overview_page_displayed", String.valueOf(overviewDisplayed));
            ZipkinTracer.finishSpan();
            
            return overviewDisplayed;
            
        } catch (Exception e) {
            logger.error("Checkout overview verification failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify order completion message
     * @return True if order completion message is displayed
     */
    public boolean verifyOrderCompletionMessage() {
        try {
            ZipkinTracer.startSpan("verify-order-completion");
            
            // Wait for completion page to load
            wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
            
            // Check for completion message
            WebElement completionMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(text(), 'Thank you for your order!')]")));
            
            // Check for dispatch message
            WebElement dispatchMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(text(), 'Your order has been dispatched')]")));
            
            boolean messageDisplayed = completionMessage.isDisplayed() && dispatchMessage.isDisplayed();
            
            logger.info("Order completion verification result: {}", messageDisplayed);
            ZipkinTracer.addTag("order_completion_verified", String.valueOf(messageDisplayed));
            ZipkinTracer.finishSpan();
            
            return messageDisplayed;
            
        } catch (Exception e) {
            logger.error("Order completion verification failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify back to home functionality
     * @return True if successfully returned to home
     */
    public boolean verifyBackToHome() {
        try {
            ZipkinTracer.startSpan("verify-back-to-home");
            
            // Verify we're on inventory page
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            
            // Check for inventory page elements
            boolean hasInventoryItems = driver.findElements(By.className("inventory_item")).size() > 0;
            boolean hasShoppingCart = driver.findElements(By.className("shopping_cart_link")).size() > 0;
            boolean hasMenuButton = driver.findElements(By.id("react-burger-menu-btn")).size() > 0;
            
            boolean backToHomeSuccessful = driver.getCurrentUrl().contains("/inventory.html") && 
                    hasInventoryItems && hasShoppingCart && hasMenuButton;
            
            logger.info("Back to home verification result: {}", backToHomeSuccessful);
            ZipkinTracer.addTag("back_to_home_successful", String.valueOf(backToHomeSuccessful));
            ZipkinTracer.finishSpan();
            
            return backToHomeSuccessful;
            
        } catch (Exception e) {
            logger.error("Back to home verification failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify cart badge count
     * @param expectedCount Expected number of items in cart
     * @return True if cart badge count matches expected
     */
    public boolean verifyCartBadgeCount(int expectedCount) {
        try {
            ZipkinTracer.startSpan("verify-cart-badge-count");
            ZipkinTracer.addTag("expected_count", String.valueOf(expectedCount));
            
            List<WebElement> cartBadges = driver.findElements(By.className("shopping_cart_badge"));
            
            if (cartBadges.isEmpty()) {
                boolean countMatches = expectedCount == 0;
                logger.info("No cart badge found, expected count: {}, actual: 0", expectedCount);
                ZipkinTracer.addTag("cart_badge_count", "0");
                ZipkinTracer.finishSpan();
                return countMatches;
            }
            
            String badgeText = cartBadges.get(0).getText();
            int actualCount = Integer.parseInt(badgeText);
            
            boolean countMatches = actualCount == expectedCount;
            
            logger.info("Cart badge count verification: expected={}, actual={}, matches={}", 
                    expectedCount, actualCount, countMatches);
            
            ZipkinTracer.addTag("cart_badge_count", String.valueOf(actualCount));
            ZipkinTracer.addTag("count_matches", String.valueOf(countMatches));
            ZipkinTracer.finishSpan();
            
            return countMatches;
            
        } catch (Exception e) {
            logger.error("Cart badge count verification failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify product price is displayed
     * @param productName Name of the product
     * @return True if product price is displayed
     */
    public boolean verifyProductPriceDisplayed(String productName) {
        try {
            ZipkinTracer.startSpan("verify-product-price");
            ZipkinTracer.addTag("product_name", productName);
            
            // Find product by name and check if price is displayed
            WebElement productElement = driver.findElement(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']/ancestor::div[contains(@class, 'inventory_item')]"));
            
            WebElement priceElement = productElement.findElement(By.className("inventory_item_price"));
            boolean priceDisplayed = priceElement.isDisplayed() && !priceElement.getText().isEmpty();
            
            logger.info("Product price verification for '{}': {}", productName, priceDisplayed);
            ZipkinTracer.addTag("price_displayed", String.valueOf(priceDisplayed));
            ZipkinTracer.finishSpan();
            
            return priceDisplayed;
            
        } catch (Exception e) {
            logger.error("Product price verification failed for '{}': {}", productName, e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
} 