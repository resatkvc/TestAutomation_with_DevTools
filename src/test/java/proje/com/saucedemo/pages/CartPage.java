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
 * Cart page object for SauceDemo
 */
public class CartPage {
    
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Page elements
    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By removeButtons = By.cssSelector("button[id^='remove']");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By cartQuantity = By.className("cart_quantity");
    
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Wait for cart page to load
     */
    public void waitForPageLoad() {
        try {
            ZipkinTracer.startSpan("wait-for-cart-page-load");
            
            wait.until(ExpectedConditions.urlContains("/cart.html"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
            
            logger.info("Cart page loaded successfully");
            ZipkinTracer.addTag("cart_page_loaded", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to load cart page: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Cart page load failed", e);
        }
    }
    
    /**
     * Get all cart item names
     * @return List of cart item names
     */
    public List<String> getCartItemNames() {
        try {
            ZipkinTracer.startSpan("get-cart-item-names");
            
            List<WebElement> itemElements = driver.findElements(cartItemNames);
            List<String> itemNames = new ArrayList<>();
            
            for (WebElement element : itemElements) {
                itemNames.add(element.getText());
            }
            
            logger.info("Retrieved {} cart item names", itemNames.size());
            ZipkinTracer.addTag("cart_items_count", String.valueOf(itemNames.size()));
            ZipkinTracer.finishSpan();
            
            return itemNames;
            
        } catch (Exception e) {
            logger.error("Failed to get cart item names: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get cart item count
     * @return Number of items in cart
     */
    public int getCartItemCount() {
        try {
            ZipkinTracer.startSpan("get-cart-item-count");
            
            List<WebElement> cartItemsList = driver.findElements(cartItems);
            int count = cartItemsList.size();
            
            logger.info("Cart item count: {}", count);
            ZipkinTracer.addTag("cart_item_count", String.valueOf(count));
            ZipkinTracer.finishSpan();
            
            return count;
            
        } catch (Exception e) {
            logger.error("Failed to get cart item count: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return 0;
        }
    }
    
    /**
     * Remove item from cart by name
     * @param itemName Name of the item to remove
     * @return True if item was removed successfully
     */
    public boolean removeItemFromCart(String itemName) {
        try {
            ZipkinTracer.startSpan("remove-item-from-cart");
            ZipkinTracer.addTag("item_name", itemName);
            
            // Find item by name
            WebElement itemElement = driver.findElement(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + itemName + "']/ancestor::div[contains(@class, 'cart_item')]"));
            
            // Find remove button within this item
            WebElement removeButton = itemElement.findElement(By.cssSelector("button[id^='remove']"));
            
            // Click remove button
            removeButton.click();
            
            // Wait for item to be removed
            wait.until(ExpectedConditions.invisibilityOf(itemElement));
            
            logger.info("Item removed from cart: {}", itemName);
            ZipkinTracer.addTag("item_removed", "true");
            ZipkinTracer.finishSpan();
            
            return true;
            
        } catch (Exception e) {
            logger.error("Failed to remove item from cart: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Click checkout button
     */
    public void clickCheckoutButton() {
        try {
            ZipkinTracer.startSpan("click-checkout-button");
            
            WebElement checkoutElement = wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
            checkoutElement.click();
            
            // Wait for checkout page to load
            wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
            
            logger.info("Checkout button clicked, navigated to checkout page");
            ZipkinTracer.addTag("checkout_button_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click checkout button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click checkout button", e);
        }
    }
    
    /**
     * Click continue shopping button
     */
    public void clickContinueShoppingButton() {
        try {
            ZipkinTracer.startSpan("click-continue-shopping-button");
            
            WebElement continueElement = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
            continueElement.click();
            
            // Wait for inventory page to load
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            
            logger.info("Continue shopping button clicked, navigated to inventory page");
            ZipkinTracer.addTag("continue_shopping_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click continue shopping button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click continue shopping button", e);
        }
    }
    
    /**
     * Check if cart is empty
     * @return True if cart is empty
     */
    public boolean isCartEmpty() {
        try {
            ZipkinTracer.startSpan("check-cart-empty");
            
            List<WebElement> cartItemsList = driver.findElements(cartItems);
            boolean isEmpty = cartItemsList.isEmpty();
            
            logger.info("Cart is empty: {}", isEmpty);
            ZipkinTracer.addTag("cart_empty", String.valueOf(isEmpty));
            ZipkinTracer.finishSpan();
            
            return isEmpty;
            
        } catch (Exception e) {
            logger.error("Failed to check if cart is empty: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return true;
        }
    }
    
    /**
     * Check if item is in cart
     * @param itemName Name of the item to check
     * @return True if item is in cart
     */
    public boolean isItemInCart(String itemName) {
        try {
            ZipkinTracer.startSpan("check-item-in-cart");
            ZipkinTracer.addTag("item_name", itemName);
            
            List<WebElement> itemElements = driver.findElements(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + itemName + "']"));
            
            boolean inCart = !itemElements.isEmpty();
            
            logger.info("Item '{}' in cart: {}", itemName, inCart);
            ZipkinTracer.addTag("item_in_cart", String.valueOf(inCart));
            ZipkinTracer.finishSpan();
            
            return inCart;
            
        } catch (Exception e) {
            logger.error("Failed to check if item is in cart: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Get item price by name
     * @param itemName Name of the item
     * @return Item price
     */
    public String getItemPrice(String itemName) {
        try {
            ZipkinTracer.startSpan("get-item-price");
            ZipkinTracer.addTag("item_name", itemName);
            
            WebElement itemElement = driver.findElement(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + itemName + "']/ancestor::div[contains(@class, 'cart_item')]"));
            
            WebElement priceElement = itemElement.findElement(By.className("inventory_item_price"));
            String price = priceElement.getText();
            
            logger.info("Item '{}' price: {}", itemName, price);
            ZipkinTracer.addTag("item_price", price);
            ZipkinTracer.finishSpan();
            
            return price;
            
        } catch (Exception e) {
            logger.error("Failed to get item price: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Get item quantity by name
     * @param itemName Name of the item
     * @return Item quantity
     */
    public String getItemQuantity(String itemName) {
        try {
            ZipkinTracer.startSpan("get-item-quantity");
            ZipkinTracer.addTag("item_name", itemName);
            
            WebElement itemElement = driver.findElement(
                    By.xpath("//div[contains(@class, 'inventory_item_name') and text()='" + itemName + "']/ancestor::div[contains(@class, 'cart_item')]"));
            
            WebElement quantityElement = itemElement.findElement(By.className("cart_quantity"));
            String quantity = quantityElement.getText();
            
            logger.info("Item '{}' quantity: {}", itemName, quantity);
            ZipkinTracer.addTag("item_quantity", quantity);
            ZipkinTracer.finishSpan();
            
            return quantity;
            
        } catch (Exception e) {
            logger.error("Failed to get item quantity: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
} 