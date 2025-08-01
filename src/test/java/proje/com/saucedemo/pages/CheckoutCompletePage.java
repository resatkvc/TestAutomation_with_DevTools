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
import java.util.List;

/**
 * Checkout complete page object for SauceDemo
 */
public class CheckoutCompletePage {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckoutCompletePage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Page elements
    private final By completionMessage = By.xpath("//h2[contains(text(), 'Thank you for your order!')]");
    private final By dispatchMessage = By.xpath("//div[contains(text(), 'Your order has been dispatched')]");
    private final By backHomeButton = By.id("back-to-products");
    private final By ponyImage = By.className("pony_express");
    
    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Wait for checkout complete page to load
     */
    public void waitForPageLoad() {
        try {
            ZipkinTracer.startSpan("wait-for-checkout-complete-page-load");
            
            wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(completionMessage));
            
            logger.info("Checkout complete page loaded successfully");
            ZipkinTracer.addTag("complete_page_loaded", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to load checkout complete page: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Checkout complete page load failed", e);
        }
    }
    
    /**
     * Check if completion message is displayed
     * @return True if completion message is displayed
     */
    public boolean isCompletionMessageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-completion-message");
            
            WebElement completionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(completionMessage));
            boolean messageDisplayed = completionElement.isDisplayed();
            
            if (messageDisplayed) {
                String messageText = completionElement.getText();
                logger.info("Completion message displayed: {}", messageText);
                ZipkinTracer.addTag("completion_message", messageText);
            }
            
            ZipkinTracer.addTag("completion_message_displayed", String.valueOf(messageDisplayed));
            ZipkinTracer.finishSpan();
            
            return messageDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check completion message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Check if dispatch message is displayed
     * @return True if dispatch message is displayed
     */
    public boolean isDispatchMessageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-dispatch-message");
            
            WebElement dispatchElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dispatchMessage));
            boolean messageDisplayed = dispatchElement.isDisplayed();
            
            if (messageDisplayed) {
                String messageText = dispatchElement.getText();
                logger.info("Dispatch message displayed: {}", messageText);
                ZipkinTracer.addTag("dispatch_message", messageText);
            }
            
            ZipkinTracer.addTag("dispatch_message_displayed", String.valueOf(messageDisplayed));
            ZipkinTracer.finishSpan();
            
            return messageDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check dispatch message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Get completion message text
     * @return Completion message text
     */
    public String getCompletionMessage() {
        try {
            ZipkinTracer.startSpan("get-completion-message");
            
            WebElement completionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(completionMessage));
            String messageText = completionElement.getText();
            
            logger.info("Completion message: {}", messageText);
            ZipkinTracer.addTag("completion_message_text", messageText);
            ZipkinTracer.finishSpan();
            
            return messageText;
            
        } catch (Exception e) {
            logger.error("Failed to get completion message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Get dispatch message text
     * @return Dispatch message text
     */
    public String getDispatchMessage() {
        try {
            ZipkinTracer.startSpan("get-dispatch-message");
            
            WebElement dispatchElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dispatchMessage));
            String messageText = dispatchElement.getText();
            
            logger.info("Dispatch message: {}", messageText);
            ZipkinTracer.addTag("dispatch_message_text", messageText);
            ZipkinTracer.finishSpan();
            
            return messageText;
            
        } catch (Exception e) {
            logger.error("Failed to get dispatch message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Click back home button
     */
    public void clickBackHomeButton() {
        try {
            ZipkinTracer.startSpan("click-back-home-button");
            
            WebElement backHomeElement = wait.until(ExpectedConditions.elementToBeClickable(backHomeButton));
            backHomeElement.click();
            
            // Wait for inventory page to load
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            
            logger.info("Back home button clicked, navigated to inventory page");
            ZipkinTracer.addTag("back_home_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click back home button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click back home button", e);
        }
    }
    
    /**
     * Check if pony image is displayed
     * @return True if pony image is displayed
     */
    public boolean isPonyImageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-pony-image");
            
            List<WebElement> ponyElements = driver.findElements(ponyImage);
            boolean imageDisplayed = !ponyElements.isEmpty() && ponyElements.get(0).isDisplayed();
            
            logger.info("Pony image displayed: {}", imageDisplayed);
            ZipkinTracer.addTag("pony_image_displayed", String.valueOf(imageDisplayed));
            ZipkinTracer.finishSpan();
            
            return imageDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check pony image: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Verify order completion
     * @return True if order completion is verified
     */
    public boolean verifyOrderCompletion() {
        try {
            ZipkinTracer.startSpan("verify-order-completion");
            
            boolean completionMessageDisplayed = isCompletionMessageDisplayed();
            boolean dispatchMessageDisplayed = isDispatchMessageDisplayed();
            boolean ponyImageDisplayed = isPonyImageDisplayed();
            
            boolean orderCompleted = completionMessageDisplayed && dispatchMessageDisplayed && ponyImageDisplayed;
            
            logger.info("Order completion verification: completion={}, dispatch={}, pony={}, overall={}", 
                    completionMessageDisplayed, dispatchMessageDisplayed, ponyImageDisplayed, orderCompleted);
            
            ZipkinTracer.addTag("order_completion_verified", String.valueOf(orderCompleted));
            ZipkinTracer.addTag("completion_message_ok", String.valueOf(completionMessageDisplayed));
            ZipkinTracer.addTag("dispatch_message_ok", String.valueOf(dispatchMessageDisplayed));
            ZipkinTracer.addTag("pony_image_ok", String.valueOf(ponyImageDisplayed));
            ZipkinTracer.finishSpan();
            
            return orderCompleted;
            
        } catch (Exception e) {
            logger.error("Failed to verify order completion: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Complete the checkout process by going back home
     */
    public void completeCheckoutProcess() {
        try {
            ZipkinTracer.startSpan("complete-checkout-process");
            
            // Verify order completion
            boolean orderCompleted = verifyOrderCompletion();
            
            if (orderCompleted) {
                // Click back home button
                clickBackHomeButton();
                logger.info("Checkout process completed successfully");
                ZipkinTracer.addTag("checkout_process_completed", "true");
            } else {
                logger.error("Order completion verification failed");
                ZipkinTracer.addTag("checkout_process_completed", "false");
            }
            
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to complete checkout process: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to complete checkout process", e);
        }
    }
    
    /**
     * Check if complete page is displayed
     * @return True if complete page is displayed
     */
    public boolean isCompletePageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-complete-page-displayed");
            
            boolean completePageDisplayed = driver.getCurrentUrl().contains("/checkout-complete.html") && 
                    driver.findElements(completionMessage).size() > 0 &&
                    driver.findElements(backHomeButton).size() > 0;
            
            logger.info("Complete page displayed: {}", completePageDisplayed);
            ZipkinTracer.addTag("complete_page_displayed", String.valueOf(completePageDisplayed));
            ZipkinTracer.finishSpan();
            
            return completePageDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check complete page display: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
} 