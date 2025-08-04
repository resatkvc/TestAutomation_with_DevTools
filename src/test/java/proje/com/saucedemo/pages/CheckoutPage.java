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
 * Page Object for AutomationExercise Checkout Page
 */
public class CheckoutPage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By checkoutTitle = By.cssSelector(".breadcrumbs h2");
    private final By addressDetailsTitle = By.cssSelector(".address_details h3");
    private final By reviewOrderTitle = By.cssSelector(".review-order h3");
    private final By commentField = By.cssSelector("textarea[name='message']");
    private final By placeOrderButton = By.cssSelector("a[href='/payment']");
    private final By nameField = By.id("name");
    private final By emailField = By.id("email");
    private final By addressField = By.id("address");
    private final By cityField = By.id("city");
    private final By stateField = By.id("state");
    private final By zipcodeField = By.id("zipcode");
    private final By mobileNumberField = By.id("mobile_number");
    private final By countryField = By.id("country");
    private final By deliveryAddress = By.cssSelector("#address_delivery");
    private final By billingAddress = By.cssSelector("#address_invoice");
    private final By orderSummary = By.cssSelector(".table-responsive");
    private final By totalAmount = By.cssSelector(".cart_total_price p");
    private final By productNames = By.cssSelector(".cart_description h4 a");
    private final By productPrices = By.cssSelector(".cart_price p");
    private final By productQuantities = By.cssSelector(".cart_quantity button");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to checkout page
     */
    public void navigateToCheckout() {
        logger.info("Navigating to checkout page");
        driver.get("https://www.automationexercise.com/checkout");
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutTitle));
        logger.info("Successfully navigated to checkout page");
    }

    /**
     * Fill delivery address information
     */
    public void fillDeliveryAddress(String name, String email, String address, String city, 
                                   String state, String zipcode, String mobileNumber, String country) {
        logger.info("Filling delivery address information");
        
        // Fill name
        WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
        nameElement.clear();
        nameElement.sendKeys(name);
        
        // Fill email
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailElement.clear();
        emailElement.sendKeys(email);
        
        // Fill address
        WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(addressField));
        addressElement.clear();
        addressElement.sendKeys(address);
        
        // Fill city
        WebElement cityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cityField));
        cityElement.clear();
        cityElement.sendKeys(city);
        
        // Fill state
        WebElement stateElement = wait.until(ExpectedConditions.visibilityOfElementLocated(stateField));
        stateElement.clear();
        stateElement.sendKeys(state);
        
        // Fill zipcode
        WebElement zipcodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(zipcodeField));
        zipcodeElement.clear();
        zipcodeElement.sendKeys(zipcode);
        
        // Fill mobile number
        WebElement mobileElement = wait.until(ExpectedConditions.visibilityOfElementLocated(mobileNumberField));
        mobileElement.clear();
        mobileElement.sendKeys(mobileNumber);
        
        // Fill country
        WebElement countryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(countryField));
        countryElement.clear();
        countryElement.sendKeys(country);
        
        logger.info("Delivery address information filled successfully");
    }

    /**
     * Add comment to order
     */
    public void addComment(String comment) {
        logger.info("Adding comment to order: {}", comment);
        WebElement commentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(commentField));
        commentElement.clear();
        commentElement.sendKeys(comment);
        logger.info("Comment added successfully");
    }

    /**
     * Click on Place Order button
     */
    public void clickPlaceOrder() {
        logger.info("Clicking on Place Order button");
        WebElement placeOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
        placeOrderBtn.click();
        logger.info("Successfully clicked on Place Order button");
    }

    /**
     * Get delivery address details
     */
    public String getDeliveryAddress() {
        logger.info("Getting delivery address details");
        try {
            WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryAddress));
            return addressElement.getText();
        } catch (Exception e) {
            logger.warn("Could not get delivery address");
            return "";
        }
    }

    /**
     * Get billing address details
     */
    public String getBillingAddress() {
        logger.info("Getting billing address details");
        try {
            WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(billingAddress));
            return addressElement.getText();
        } catch (Exception e) {
            logger.warn("Could not get billing address");
            return "";
        }
    }

    /**
     * Get order summary
     */
    public String getOrderSummary() {
        logger.info("Getting order summary");
        try {
            WebElement summaryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderSummary));
            return summaryElement.getText();
        } catch (Exception e) {
            logger.warn("Could not get order summary");
            return "";
        }
    }

    /**
     * Get total amount
     */
    public String getTotalAmount() {
        logger.info("Getting total amount");
        try {
            List<WebElement> totalElements = driver.findElements(totalAmount);
            if (!totalElements.isEmpty()) {
                return totalElements.get(totalElements.size() - 1).getText();
            }
        } catch (Exception e) {
            logger.warn("Could not get total amount");
        }
        return "0";
    }

    /**
     * Get product names in checkout
     */
    public List<String> getProductNames() {
        logger.info("Getting product names in checkout");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get product prices in checkout
     */
    public List<String> getProductPrices() {
        logger.info("Getting product prices in checkout");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get product quantities in checkout
     */
    public List<String> getProductQuantities() {
        logger.info("Getting product quantities in checkout");
        List<WebElement> elements = driver.findElements(productQuantities);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Check if page is loaded
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutTitle));
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
     * Verify checkout contains specific product
     */
    public boolean containsProduct(String productName) {
        logger.info("Checking if checkout contains product: {}", productName);
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
        List<WebElement> quantityElements = driver.findElements(productQuantities);
        
        for (int i = 0; i < productElements.size(); i++) {
            if (productElements.get(i).getText().contains(productName)) {
                return quantityElements.get(i).getText();
            }
        }
        return "0";
    }

    /**
     * Get product price by product name
     */
    public String getProductPriceByName(String productName) {
        logger.info("Getting price for product: {}", productName);
        List<WebElement> productElements = driver.findElements(productNames);
        List<WebElement> priceElements = driver.findElements(productPrices);
        
        for (int i = 0; i < productElements.size(); i++) {
            if (productElements.get(i).getText().contains(productName)) {
                return priceElements.get(i).getText();
            }
        }
        return "0";
    }
} 