package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Page Object for AutomationExercise Payment Page
 */
public class PaymentPage {
    private static final Logger logger = LoggerFactory.getLogger(PaymentPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By paymentTitle = By.cssSelector(".breadcrumbs h2");
    private final By cardNameField = By.id("name_on_card");
    private final By cardNumberField = By.id("card_number");
    private final By cvcField = By.id("cvc");
    private final By expiryMonthField = By.id("expiry_month");
    private final By expiryYearField = By.id("expiry_year");
    private final By payAndConfirmOrderButton = By.id("submit");
    private final By orderPlacedMessage = By.cssSelector(".alert-success");
    private final By orderDetails = By.cssSelector(".table-responsive");
    private final By downloadInvoiceButton = By.cssSelector("a[href='/download_invoice/0']");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");
    private final By orderConfirmationMessage = By.cssSelector(".alert-success p");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to payment page
     */
    public void navigateToPayment() {
        logger.info("Navigating to payment page");
        driver.get("https://automationexercise.com/payment");
        wait.until(ExpectedConditions.visibilityOfElementLocated(paymentTitle));
        logger.info("Successfully navigated to payment page");
    }

    /**
     * Fill card information
     */
    public void fillCardInformation(String cardName, String cardNumber, String cvc, 
                                   String expiryMonth, String expiryYear) {
        logger.info("Filling card information");
        
        // Fill card name
        WebElement cardNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNameField));
        cardNameElement.clear();
        cardNameElement.sendKeys(cardName);
        
        // Fill card number
        WebElement cardNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberField));
        cardNumberElement.clear();
        cardNumberElement.sendKeys(cardNumber);
        
        // Fill CVC
        WebElement cvcElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cvcField));
        cvcElement.clear();
        cvcElement.sendKeys(cvc);
        
        // Fill expiry month
        WebElement expiryMonthElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryMonthField));
        expiryMonthElement.clear();
        expiryMonthElement.sendKeys(expiryMonth);
        
        // Fill expiry year
        WebElement expiryYearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryYearField));
        expiryYearElement.clear();
        expiryYearElement.sendKeys(expiryYear);
        
        logger.info("Card information filled successfully");
    }

    /**
     * Click on Pay and Confirm Order button
     */
    public void clickPayAndConfirmOrder() {
        logger.info("Clicking on Pay and Confirm Order button");
        WebElement payButton = wait.until(ExpectedConditions.elementToBeClickable(payAndConfirmOrderButton));
        payButton.click();
        logger.info("Successfully clicked on Pay and Confirm Order button");
    }

    /**
     * Check if order was placed successfully
     */
    public boolean isOrderPlaced() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(orderPlacedMessage));
            logger.info("Order placed successfully");
            return true;
        } catch (Exception e) {
            logger.warn("Order placement failed");
            return false;
        }
    }

    /**
     * Get order confirmation message
     */
    public String getOrderConfirmationMessage() {
        logger.info("Getting order confirmation message");
        try {
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderConfirmationMessage));
            return messageElement.getText();
        } catch (Exception e) {
            logger.warn("Could not get order confirmation message");
            return "";
        }
    }

    /**
     * Get order details
     */
    public String getOrderDetails() {
        logger.info("Getting order details");
        try {
            WebElement detailsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderDetails));
            return detailsElement.getText();
        } catch (Exception e) {
            logger.warn("Could not get order details");
            return "";
        }
    }

    /**
     * Click on Download Invoice button
     */
    public void clickDownloadInvoice() {
        logger.info("Clicking on Download Invoice button");
        try {
            WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(downloadInvoiceButton));
            downloadButton.click();
            logger.info("Successfully clicked on Download Invoice button");
        } catch (Exception e) {
            logger.warn("Download Invoice button not found");
        }
    }

    /**
     * Click on Continue button
     */
    public void clickContinue() {
        logger.info("Clicking on Continue button");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();
        logger.info("Successfully clicked on Continue button");
    }

    /**
     * Complete payment process with random card data
     */
    public void completePaymentWithRandomData() {
        logger.info("Completing payment with random card data");
        
        // Generate random card data
        String cardName = "Test User " + System.currentTimeMillis();
        String cardNumber = "4111111111111111"; // Test card number
        String cvc = String.valueOf(100 + (int)(Math.random() * 900)); // Random 3-digit CVC
        String expiryMonth = String.valueOf(1 + (int)(Math.random() * 12)); // Random month 1-12
        String expiryYear = String.valueOf(2025 + (int)(Math.random() * 10)); // Random year 2025-2034
        
        fillCardInformation(cardName, cardNumber, cvc, expiryMonth, expiryYear);
        clickPayAndConfirmOrder();
        
        logger.info("Payment completed with random data");
    }

    /**
     * Check if page is loaded
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(paymentTitle));
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
     * Verify payment form is displayed
     */
    public boolean isPaymentFormDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cardNameField));
            wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberField));
            wait.until(ExpectedConditions.visibilityOfElementLocated(cvcField));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get card name field value
     */
    public String getCardNameValue() {
        try {
            WebElement cardNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNameField));
            return cardNameElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get card number field value
     */
    public String getCardNumberValue() {
        try {
            WebElement cardNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberField));
            return cardNumberElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get CVC field value
     */
    public String getCvcValue() {
        try {
            WebElement cvcElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cvcField));
            return cvcElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get expiry month field value
     */
    public String getExpiryMonthValue() {
        try {
            WebElement expiryMonthElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryMonthField));
            return expiryMonthElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get expiry year field value
     */
    public String getExpiryYearValue() {
        try {
            WebElement expiryYearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryYearField));
            return expiryYearElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }
} 