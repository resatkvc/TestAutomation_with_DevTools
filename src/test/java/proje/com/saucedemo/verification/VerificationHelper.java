package proje.com.saucedemo.verification;

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
 * AutomationExercise test otomasyonu için doğrulama yardımcı sınıfı
 * Tüm doğrulama metodlarını tek yerde içerir
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
     * Sayfanın yüklendiğini doğrular
     * @param pageName Loglama için sayfa adı
     * @param isLoaded Sayfanın yüklenip yüklenmediğini gösteren boolean
     */
    public void verifyPageLoaded(String pageName, boolean isLoaded) {
        if (isLoaded) {
            logger.info("{} loaded successfully", pageName);
        } else {
            logger.error("{} failed to load", pageName);
            throw new RuntimeException(pageName + " failed to load");
        }
    }
    
    /**
     * Hesabın başarıyla oluşturulduğunu doğrular
     * @param isCreated Hesabın oluşturulup oluşturulmadığını gösteren boolean
     */
    public void verifyAccountCreated(boolean isCreated) {
        if (isCreated) {
            logger.info("Account created successfully");
        } else {
            logger.error("Account creation failed");
            throw new RuntimeException("Account creation failed");
        }
    }
    
    /**
     * Sepetin boş olmadığını doğrular
     * @param isNotEmpty Sepetin boş olup olmadığını gösteren boolean
     */
    public void verifyCartNotEmpty(boolean isNotEmpty) {
        if (isNotEmpty) {
            logger.info("Cart is not empty");
        } else {
            logger.error("Cart is empty");
            throw new RuntimeException("Cart is empty");
        }
    }
    
    /**
     * Ürünün sepette olduğunu doğrular
     * @param isInCart Ürünün sepette olup olmadığını gösteren boolean
     * @param productName Ürün adı
     */
    public void verifyProductInCart(boolean isInCart, String productName) {
        if (isInCart) {
            logger.info("Product '{}' is in cart", productName);
        } else {
            logger.error("Product '{}' is not in cart", productName);
            throw new RuntimeException("Product '" + productName + "' is not in cart");
        }
    }
    
    /**
     * Siparişin başarıyla verildiğini doğrular
     * @param isPlaced Siparişin verilip verilmediğini gösteren boolean
     */
    public void verifyOrderPlaced(boolean isPlaced) {
        if (isPlaced) {
            logger.info("Order placed successfully");
        } else {
            logger.error("Order placement failed");
            throw new RuntimeException("Order placement failed");
        }
    }
    
    /**
     * Mevcut URL'yi doğrular
     * @param currentUrl Mevcut URL
     * @param expectedUrl Beklenen URL
     */
    public void verifyCurrentUrl(String currentUrl, String expectedUrl) {
        if (currentUrl.equals(expectedUrl)) {
            logger.info("URL verification successful: {}", currentUrl);
        } else {
            logger.error("URL verification failed. Expected: {}, Actual: {}", expectedUrl, currentUrl);
            throw new RuntimeException("URL verification failed");
        }
    }
    
    /**
     * Verify login was successful
     * @return True if login successful
     */
    public boolean verifyLoginSuccessful() {
        try {
            logger.info("Verifying login success");
            
            // Check if we're on the home page (successful login)
            boolean isOnHomePage = driver.getCurrentUrl().contains("/");
            boolean hasLogoutLink = driver.findElements(By.cssSelector("a[href='/logout']")).size() > 0;
            
            boolean loginSuccessful = isOnHomePage && hasLogoutLink;
            
            logger.info("Login verification result: {}", loginSuccessful);
            
            return loginSuccessful;
            
        } catch (Exception e) {
            logger.error("Login verification failed: {}", e.getMessage());
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
            logger.info("Verifying products in cart. Expected: {}", expectedProducts.size());
            
            // Navigate to cart
            WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/view_cart']")));
            cartLink.click();
            
            // Wait for cart page to load
            wait.until(ExpectedConditions.urlContains("/view_cart"));
            
            // Get all cart items
            List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_description h4 a"));
            
            // Verify each expected product is in cart
            boolean allProductsFound = true;
            for (String expectedProduct : expectedProducts) {
                boolean productFound = cartItems.stream()
                        .anyMatch(item -> item.getText().contains(expectedProduct));
                
                if (!productFound) {
                    logger.warn("Product not found in cart: {}", expectedProduct);
                    allProductsFound = false;
                } else {
                    logger.info("Product found in cart: {}", expectedProduct);
                }
            }
            
            logger.info("Cart verification result: {} (Expected: {}, Found: {})", 
                    allProductsFound, expectedProducts.size(), cartItems.size());
            
            return allProductsFound;
            
        } catch (Exception e) {
            logger.error("Cart verification failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify checkout page is displayed
     * @return True if checkout page is displayed
     */
    public boolean verifyCheckoutPageDisplayed() {
        try {
            logger.info("Verifying checkout page is displayed");
            
            boolean isOnCheckoutPage = driver.getCurrentUrl().contains("/checkout");
            boolean hasCheckoutForm = driver.findElements(By.id("name")).size() > 0;
            
            boolean checkoutPageDisplayed = isOnCheckoutPage && hasCheckoutForm;
            
            logger.info("Checkout page verification result: {}", checkoutPageDisplayed);
            
            return checkoutPageDisplayed;
            
        } catch (Exception e) {
            logger.error("Checkout page verification failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify payment page is displayed
     * @return True if payment page is displayed
     */
    public boolean verifyPaymentPageDisplayed() {
        try {
            logger.info("Verifying payment page is displayed");
            
            boolean isOnPaymentPage = driver.getCurrentUrl().contains("/payment");
            boolean hasPaymentForm = driver.findElements(By.id("name_on_card")).size() > 0;
            
            boolean paymentPageDisplayed = isOnPaymentPage && hasPaymentForm;
            
            logger.info("Payment page verification result: {}", paymentPageDisplayed);
            
            return paymentPageDisplayed;
            
        } catch (Exception e) {
            logger.error("Payment page verification failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify order completion message
     * @return True if order completion message is displayed
     */
    public boolean verifyOrderCompletionMessage() {
        try {
            logger.info("Verifying order completion message");
            
            boolean hasSuccessMessage = driver.findElements(By.cssSelector(".alert-success")).size() > 0;
            boolean hasOrderPlacedMessage = driver.findElements(By.cssSelector("h2[data-qa='account-created']")).size() > 0;
            
            boolean orderCompleted = hasSuccessMessage || hasOrderPlacedMessage;
            
            logger.info("Order completion verification result: {}", orderCompleted);
            
            return orderCompleted;
            
        } catch (Exception e) {
            logger.error("Order completion verification failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify back to home page
     * @return True if successfully returned to home page
     */
    public boolean verifyBackToHome() {
        try {
            logger.info("Verifying return to home page");
            
            boolean isOnHomePage = driver.getCurrentUrl().equals("https://www.automationexercise.com/");
            boolean hasHomePageElements = driver.findElements(By.cssSelector(".features_items")).size() > 0;
            
            boolean backToHome = isOnHomePage && hasHomePageElements;
            
            logger.info("Back to home verification result: {}", backToHome);
            
            return backToHome;
            
        } catch (Exception e) {
            logger.error("Back to home verification failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify cart badge count
     * @param expectedCount Expected count in cart badge
     * @return True if cart badge count matches expected
     */
    public boolean verifyCartBadgeCount(int expectedCount) {
        try {
            logger.info("Verifying cart badge count. Expected: {}", expectedCount);
            
            // Find cart badge element
            List<WebElement> cartBadges = driver.findElements(By.cssSelector(".badge"));
            
            if (cartBadges.isEmpty()) {
                logger.warn("No cart badge found");
                return expectedCount == 0;
            }
            
            String badgeText = cartBadges.get(0).getText();
            int actualCount = Integer.parseInt(badgeText);
            
            boolean countMatches = actualCount == expectedCount;
            
            logger.info("Cart badge count verification result: {} (Expected: {}, Actual: {})", 
                    countMatches, expectedCount, actualCount);
            
            return countMatches;
            
        } catch (Exception e) {
            logger.error("Cart badge count verification failed: {}", e.getMessage());
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
            logger.info("Verifying product price is displayed for: {}", productName);
            
            // Find product price elements
            List<WebElement> priceElements = driver.findElements(By.cssSelector(".cart_price p"));
            
            boolean priceDisplayed = !priceElements.isEmpty();
            
            if (priceDisplayed) {
                String price = priceElements.get(0).getText();
                logger.info("Product price found: {}", price);
            }
            
            logger.info("Product price verification result: {}", priceDisplayed);
            
            return priceDisplayed;
            
        } catch (Exception e) {
            logger.error("Product price verification failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify element is present
     * @param elementName Name of the element for logging
     * @param isPresent Boolean indicating if element is present
     */
    public void verifyElementPresent(String elementName, boolean isPresent) {
        if (isPresent) {
            logger.info("Element '{}' is present", elementName);
        } else {
            logger.error("Element '{}' is not present", elementName);
            throw new RuntimeException("Element '" + elementName + "' is not present");
        }
    }
    
    /**
     * Verify text matches
     * @param elementName Name of the element for logging
     * @param actualText Actual text
     * @param expectedText Expected text
     */
    public void verifyTextMatches(String elementName, String actualText, String expectedText) {
        if (actualText.equals(expectedText)) {
            logger.info("Text verification successful for '{}': {}", elementName, actualText);
        } else {
            logger.error("Text verification failed for '{}'. Expected: {}, Actual: {}", 
                    elementName, expectedText, actualText);
            throw new RuntimeException("Text verification failed for '" + elementName + "'");
        }
    }
} 