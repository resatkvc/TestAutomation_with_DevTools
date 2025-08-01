package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.utils.ZipkinTracer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory page object for SauceDemo
 */
public class InventoryPage {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Page elements
    private final By productItems = By.className("inventory_item");
    private final By productNames = By.className("inventory_item_name");
    private final By addToCartButtons = By.cssSelector("button[id^='add-to-cart']");
    private final By removeButtons = By.cssSelector("button[id^='remove']");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By sortDropdown = By.className("product_sort_container");
    
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Wait for inventory page to load
     */
    public void waitForPageLoad() {
        try {
            ZipkinTracer.startSpan("wait-for-inventory-page-load");
            
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(productItems));
            
            logger.info("Inventory page loaded successfully");
            ZipkinTracer.addTag("inventory_page_loaded", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to load inventory page: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Inventory page load failed", e);
        }
    }
    
    /**
     * Get all product names
     * @return List of product names
     */
    public List<String> getAllProductNames() {
        try {
            ZipkinTracer.startSpan("get-all-product-names");
            
            List<WebElement> productElements = driver.findElements(productNames);
            List<String> productNamesList = new ArrayList<>();
            
            for (WebElement element : productElements) {
                productNamesList.add(element.getText());
            }
            
            logger.info("Retrieved {} product names", productNamesList.size());
            ZipkinTracer.addTag("products_count", String.valueOf(productNamesList.size()));
            ZipkinTracer.finishSpan();
            
            return productNamesList;
            
        } catch (Exception e) {
            logger.error("Failed to get product names: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return new ArrayList<>();
        }
    }
    
    /**
     * Add product to cart by name
     * @param productName Name of the product to add
     * @return True if product was added successfully
     */
    public boolean addProductToCart(String productName) {
        try {
            ZipkinTracer.startSpan("add-product-to-cart");
            ZipkinTracer.addTag("product_name", productName);
            
            // Find product by name
            WebElement productElement = driver.findElement(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']/ancestor::div[contains(@class, 'inventory_item')]"));
            
            // Find add to cart button within this product
            WebElement addButton = productElement.findElement(By.cssSelector("button[id^='add-to-cart']"));
            
            // Click add to cart button
            addButton.click();
            
            // Wait for button to change to "Remove"
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']/ancestor::div[contains(@class, 'inventory_item')]//button[contains(@id, 'remove')]")));
            
            logger.info("Product added to cart: {}", productName);
            ZipkinTracer.addTag("product_added", "true");
            ZipkinTracer.finishSpan();
            
            return true;
            
        } catch (Exception e) {
            logger.error("Failed to add product to cart: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Add multiple products to cart
     * @param productNames List of product names to add
     * @return Number of products successfully added
     */
    public int addMultipleProductsToCart(List<String> productNames) {
        try {
            ZipkinTracer.startSpan("add-multiple-products-to-cart");
            ZipkinTracer.addTag("products_count", String.valueOf(productNames.size()));
            
            int addedCount = 0;
            for (String productName : productNames) {
                if (addProductToCart(productName)) {
                    addedCount++;
                }
            }
            
            logger.info("Added {} out of {} products to cart", addedCount, productNames.size());
            ZipkinTracer.addTag("products_added_count", String.valueOf(addedCount));
            ZipkinTracer.finishSpan();
            
            return addedCount;
            
        } catch (Exception e) {
            logger.error("Failed to add multiple products: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return 0;
        }
    }
    
    /**
     * Get cart badge count
     * @return Number of items in cart
     */
    public int getCartBadgeCount() {
        try {
            ZipkinTracer.startSpan("get-cart-badge-count");
            
            List<WebElement> badges = driver.findElements(cartBadge);
            
            if (badges.isEmpty()) {
                logger.info("No cart badge found, cart is empty");
                ZipkinTracer.addTag("cart_badge_count", "0");
                ZipkinTracer.finishSpan();
                return 0;
            }
            
            String badgeText = badges.get(0).getText();
            int count = Integer.parseInt(badgeText);
            
            logger.info("Cart badge count: {}", count);
            ZipkinTracer.addTag("cart_badge_count", String.valueOf(count));
            ZipkinTracer.finishSpan();
            
            return count;
            
        } catch (Exception e) {
            logger.error("Failed to get cart badge count: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return 0;
        }
    }
    
    /**
     * Click cart link to navigate to cart
     */
    public void clickCartLink() {
        try {
            ZipkinTracer.startSpan("click-cart-link");
            
            WebElement cartElement = wait.until(ExpectedConditions.elementToBeClickable(cartLink));
            cartElement.click();
            
            // Wait for cart page to load
            wait.until(ExpectedConditions.urlContains("/cart.html"));
            
            logger.info("Cart link clicked, navigated to cart page");
            ZipkinTracer.addTag("cart_link_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click cart link: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click cart link", e);
        }
    }
    
    /**
     * Remove product from cart by name
     * @param productName Name of the product to remove
     * @return True if product was removed successfully
     */
    public boolean removeProductFromCart(String productName) {
        try {
            ZipkinTracer.startSpan("remove-product-from-cart");
            ZipkinTracer.addTag("product_name", productName);
            
            // Find product by name
            WebElement productElement = driver.findElement(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']/ancestor::div[contains(@class, 'inventory_item')]"));
            
            // Find remove button within this product
            WebElement removeButton = productElement.findElement(By.cssSelector("button[id^='remove']"));
            
            // Click remove button
            removeButton.click();
            
            // Wait for button to change back to "Add to cart"
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']/ancestor::div[contains(@class, 'inventory_item')]//button[contains(@id, 'add-to-cart')]")));
            
            logger.info("Product removed from cart: {}", productName);
            ZipkinTracer.addTag("product_removed", "true");
            ZipkinTracer.finishSpan();
            
            return true;
            
        } catch (Exception e) {
            logger.error("Failed to remove product from cart: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Check if product is in cart
     * @param productName Name of the product to check
     * @return True if product is in cart
     */
    public boolean isProductInCart(String productName) {
        try {
            ZipkinTracer.startSpan("check-product-in-cart");
            ZipkinTracer.addTag("product_name", productName);
            
            List<WebElement> removeButtons = driver.findElements(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']/ancestor::div[contains(@class, 'inventory_item')]//button[contains(@id, 'remove')]"));
            
            boolean inCart = !removeButtons.isEmpty();
            
            logger.info("Product '{}' in cart: {}", productName, inCart);
            ZipkinTracer.addTag("product_in_cart", String.valueOf(inCart));
            ZipkinTracer.finishSpan();
            
            return inCart;
            
        } catch (Exception e) {
            logger.error("Failed to check if product is in cart: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Get product price by name
     * @param productName Name of the product
     * @return Product price
     */
    public String getProductPrice(String productName) {
        try {
            ZipkinTracer.startSpan("get-product-price");
            ZipkinTracer.addTag("product_name", productName);
            
            WebElement productElement = driver.findElement(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + productName + "']/ancestor::div[contains(@class, 'inventory_item')]"));
            
            WebElement priceElement = productElement.findElement(By.className("inventory_item_price"));
            String price = priceElement.getText();
            
            logger.info("Product '{}' price: {}", productName, price);
            ZipkinTracer.addTag("product_price", price);
            ZipkinTracer.finishSpan();
            
            return price;
            
        } catch (Exception e) {
            logger.error("Failed to get product price: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
} 