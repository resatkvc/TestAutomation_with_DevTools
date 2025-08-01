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
 * Checkout overview page object for SauceDemo
 */
public class CheckoutOverviewPage {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckoutOverviewPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Page elements
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    
    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Wait for checkout overview page to load
     */
    public void waitForPageLoad() {
        try {
            ZipkinTracer.startSpan("wait-for-checkout-overview-page-load");
            
            wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(finishButton));
            
            logger.info("Checkout overview page loaded successfully");
            ZipkinTracer.addTag("overview_page_loaded", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to load checkout overview page: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Checkout overview page load failed", e);
        }
    }
    
    /**
     * Get all item names in overview
     * @return List of item names
     */
    public List<String> getItemNames() {
        try {
            ZipkinTracer.startSpan("get-overview-item-names");
            
            List<WebElement> itemElements = driver.findElements(itemNames);
            List<String> itemNamesList = new ArrayList<>();
            
            for (WebElement element : itemElements) {
                itemNamesList.add(element.getText());
            }
            
            logger.info("Retrieved {} item names from overview", itemNamesList.size());
            ZipkinTracer.addTag("overview_items_count", String.valueOf(itemNamesList.size()));
            ZipkinTracer.finishSpan();
            
            return itemNamesList;
            
        } catch (Exception e) {
            logger.error("Failed to get item names from overview: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get all item prices in overview
     * @return List of item prices
     */
    public List<String> getItemPrices() {
        try {
            ZipkinTracer.startSpan("get-overview-item-prices");
            
            List<WebElement> priceElements = driver.findElements(itemPrices);
            List<String> itemPricesList = new ArrayList<>();
            
            for (WebElement element : priceElements) {
                itemPricesList.add(element.getText());
            }
            
            logger.info("Retrieved {} item prices from overview", itemPricesList.size());
            ZipkinTracer.addTag("overview_prices_count", String.valueOf(itemPricesList.size()));
            ZipkinTracer.finishSpan();
            
            return itemPricesList;
            
        } catch (Exception e) {
            logger.error("Failed to get item prices from overview: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get subtotal amount
     * @return Subtotal amount
     */
    public String getSubtotal() {
        try {
            ZipkinTracer.startSpan("get-subtotal");
            
            WebElement subtotalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalLabel));
            String subtotalText = subtotalElement.getText();
            
            // Extract amount from "Item total: $XX.XX" format
            String subtotal = subtotalText.replace("Item total: $", "");
            
            logger.info("Subtotal: {}", subtotal);
            ZipkinTracer.addTag("subtotal", subtotal);
            ZipkinTracer.finishSpan();
            
            return subtotal;
            
        } catch (Exception e) {
            logger.error("Failed to get subtotal: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Get tax amount
     * @return Tax amount
     */
    public String getTax() {
        try {
            ZipkinTracer.startSpan("get-tax");
            
            WebElement taxElement = wait.until(ExpectedConditions.visibilityOfElementLocated(taxLabel));
            String taxText = taxElement.getText();
            
            // Extract amount from "Tax: $XX.XX" format
            String tax = taxText.replace("Tax: $", "");
            
            logger.info("Tax: {}", tax);
            ZipkinTracer.addTag("tax", tax);
            ZipkinTracer.finishSpan();
            
            return tax;
            
        } catch (Exception e) {
            logger.error("Failed to get tax: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Get total amount
     * @return Total amount
     */
    public String getTotal() {
        try {
            ZipkinTracer.startSpan("get-total");
            
            WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(totalLabel));
            String totalText = totalElement.getText();
            
            // Extract amount from "Total: $XX.XX" format
            String total = totalText.replace("Total: $", "");
            
            logger.info("Total: {}", total);
            ZipkinTracer.addTag("total", total);
            ZipkinTracer.finishSpan();
            
            return total;
            
        } catch (Exception e) {
            logger.error("Failed to get total: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Click finish button
     */
    public void clickFinishButton() {
        try {
            ZipkinTracer.startSpan("click-finish-button");
            
            WebElement finishElement = wait.until(ExpectedConditions.elementToBeClickable(finishButton));
            finishElement.click();
            
            // Wait for completion page to load
            wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
            
            logger.info("Finish button clicked, navigated to completion page");
            ZipkinTracer.addTag("finish_button_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click finish button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click finish button", e);
        }
    }
    
    /**
     * Click cancel button
     */
    public void clickCancelButton() {
        try {
            ZipkinTracer.startSpan("click-overview-cancel-button");
            
            WebElement cancelElement = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
            cancelElement.click();
            
            // Wait for inventory page to load
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            
            logger.info("Cancel button clicked, navigated to inventory page");
            ZipkinTracer.addTag("overview_cancel_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click cancel button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click cancel button", e);
        }
    }
    
    /**
     * Complete the order
     */
    public void completeOrder() {
        try {
            ZipkinTracer.startSpan("complete-order");
            
            clickFinishButton();
            
            logger.info("Order completed successfully");
            ZipkinTracer.addTag("order_completed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to complete order: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to complete order", e);
        }
    }
    
    /**
     * Check if overview page is displayed
     * @return True if overview page is displayed
     */
    public boolean isOverviewPageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-overview-page-displayed");
            
            boolean overviewPageDisplayed = driver.getCurrentUrl().contains("/checkout-step-two.html") && 
                    driver.findElements(finishButton).size() > 0;
            
            logger.info("Overview page displayed: {}", overviewPageDisplayed);
            ZipkinTracer.addTag("overview_page_displayed", String.valueOf(overviewPageDisplayed));
            ZipkinTracer.finishSpan();
            
            return overviewPageDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check overview page display: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Get item count in overview
     * @return Number of items in overview
     */
    public int getItemCount() {
        try {
            ZipkinTracer.startSpan("get-overview-item-count");
            
            List<WebElement> cartItemsList = driver.findElements(cartItems);
            int count = cartItemsList.size();
            
            logger.info("Overview item count: {}", count);
            ZipkinTracer.addTag("overview_item_count", String.valueOf(count));
            ZipkinTracer.finishSpan();
            
            return count;
            
        } catch (Exception e) {
            logger.error("Failed to get overview item count: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return 0;
        }
    }
} 