package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.utils.ZipkinTracer;
import proje.com.saucedemo.utils.TestDataGenerator;

import java.time.Duration;

/**
 * Checkout page object for SauceDemo
 */
public class CheckoutPage {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Page elements
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Wait for checkout page to load
     */
    public void waitForPageLoad() {
        try {
            ZipkinTracer.startSpan("wait-for-checkout-page-load");
            
            wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
            
            logger.info("Checkout page loaded successfully");
            ZipkinTracer.addTag("checkout_page_loaded", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to load checkout page: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Checkout page load failed", e);
        }
    }
    
    /**
     * Enter first name
     * @param firstName First name to enter
     */
    public void enterFirstName(String firstName) {
        try {
            ZipkinTracer.startSpan("enter-first-name");
            ZipkinTracer.addTag("first_name", firstName);
            
            WebElement firstNameElement = wait.until(ExpectedConditions.elementToBeClickable(firstNameField));
            firstNameElement.clear();
            firstNameElement.sendKeys(firstName);
            
            logger.info("First name entered: {}", firstName);
            ZipkinTracer.addTag("first_name_entered", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to enter first name: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to enter first name", e);
        }
    }
    
    /**
     * Enter last name
     * @param lastName Last name to enter
     */
    public void enterLastName(String lastName) {
        try {
            ZipkinTracer.startSpan("enter-last-name");
            ZipkinTracer.addTag("last_name", lastName);
            
            WebElement lastNameElement = wait.until(ExpectedConditions.elementToBeClickable(lastNameField));
            lastNameElement.clear();
            lastNameElement.sendKeys(lastName);
            
            logger.info("Last name entered: {}", lastName);
            ZipkinTracer.addTag("last_name_entered", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to enter last name: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to enter last name", e);
        }
    }
    
    /**
     * Enter postal code
     * @param postalCode Postal code to enter
     */
    public void enterPostalCode(String postalCode) {
        try {
            ZipkinTracer.startSpan("enter-postal-code");
            ZipkinTracer.addTag("postal_code", postalCode);
            
            WebElement postalCodeElement = wait.until(ExpectedConditions.elementToBeClickable(postalCodeField));
            postalCodeElement.clear();
            postalCodeElement.sendKeys(postalCode);
            
            logger.info("Postal code entered: {}", postalCode);
            ZipkinTracer.addTag("postal_code_entered", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to enter postal code: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to enter postal code", e);
        }
    }
    
    /**
     * Click continue button
     */
    public void clickContinueButton() {
        try {
            ZipkinTracer.startSpan("click-continue-button");
            
            WebElement continueElement = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
            continueElement.click();
            
            // Wait for overview page to load
            wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
            
            logger.info("Continue button clicked, navigated to overview page");
            ZipkinTracer.addTag("continue_button_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click continue button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click continue button", e);
        }
    }
    
    /**
     * Click cancel button
     */
    public void clickCancelButton() {
        try {
            ZipkinTracer.startSpan("click-cancel-button");
            
            WebElement cancelElement = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
            cancelElement.click();
            
            // Wait for inventory page to load
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            
            logger.info("Cancel button clicked, navigated to inventory page");
            ZipkinTracer.addTag("cancel_button_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click cancel button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click cancel button", e);
        }
    }
    
    /**
     * Fill checkout information with random data
     */
    public void fillCheckoutInformationWithRandomData() {
        try {
            ZipkinTracer.startSpan("fill-checkout-information-random");
            
            TestDataGenerator.CheckoutInfo checkoutInfo = TestDataGenerator.generateCheckoutInfo();
            
            enterFirstName(checkoutInfo.getFirstName());
            enterLastName(checkoutInfo.getLastName());
            enterPostalCode(checkoutInfo.getPostalCode());
            
            logger.info("Checkout information filled with random data: {}", checkoutInfo);
            ZipkinTracer.addTag("checkout_info_filled", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to fill checkout information: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to fill checkout information", e);
        }
    }
    
    /**
     * Fill checkout information with provided data
     * @param firstName First name
     * @param lastName Last name
     * @param postalCode Postal code
     */
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        try {
            ZipkinTracer.startSpan("fill-checkout-information");
            ZipkinTracer.addTag("first_name", firstName);
            ZipkinTracer.addTag("last_name", lastName);
            ZipkinTracer.addTag("postal_code", postalCode);
            
            enterFirstName(firstName);
            enterLastName(lastName);
            enterPostalCode(postalCode);
            
            logger.info("Checkout information filled: {} {} {}", firstName, lastName, postalCode);
            ZipkinTracer.addTag("checkout_info_filled", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to fill checkout information: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to fill checkout information", e);
        }
    }
    
    /**
     * Complete checkout process with random data
     */
    public void completeCheckoutWithRandomData() {
        try {
            ZipkinTracer.startSpan("complete-checkout-random");
            
            fillCheckoutInformationWithRandomData();
            clickContinueButton();
            
            logger.info("Checkout completed with random data");
            ZipkinTracer.addTag("checkout_completed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to complete checkout: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to complete checkout", e);
        }
    }
    
    /**
     * Check if error message is displayed
     * @return True if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-checkout-error-message");
            
            boolean errorDisplayed = driver.findElements(errorMessage).size() > 0;
            
            if (errorDisplayed) {
                String errorText = driver.findElement(errorMessage).getText();
                logger.warn("Checkout error message displayed: {}", errorText);
                ZipkinTracer.addTag("error_message", errorText);
            }
            
            ZipkinTracer.addTag("error_displayed", String.valueOf(errorDisplayed));
            ZipkinTracer.finishSpan();
            
            return errorDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check error message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Get error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        try {
            ZipkinTracer.startSpan("get-checkout-error-message");
            
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String errorText = errorElement.getText();
            
            logger.info("Checkout error message retrieved: {}", errorText);
            ZipkinTracer.addTag("error_message_text", errorText);
            ZipkinTracer.finishSpan();
            
            return errorText;
            
        } catch (Exception e) {
            logger.error("Failed to get error message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Check if checkout page is displayed
     * @return True if checkout page is displayed
     */
    public boolean isCheckoutPageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-checkout-page-displayed");
            
            boolean checkoutPageDisplayed = driver.getCurrentUrl().contains("/checkout-step-one.html") && 
                    driver.findElements(firstNameField).size() > 0 &&
                    driver.findElements(lastNameField).size() > 0 &&
                    driver.findElements(postalCodeField).size() > 0;
            
            logger.info("Checkout page displayed: {}", checkoutPageDisplayed);
            ZipkinTracer.addTag("checkout_page_displayed", String.valueOf(checkoutPageDisplayed));
            ZipkinTracer.finishSpan();
            
            return checkoutPageDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check checkout page display: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
} 