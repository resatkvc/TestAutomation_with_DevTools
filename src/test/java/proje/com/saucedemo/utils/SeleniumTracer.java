package proje.com.saucedemo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Selenium WebDriver wrapper that traces all interactions
 * Provides detailed tracing for clicks, inputs, navigation, and other WebDriver operations
 */
public class SeleniumTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(SeleniumTracer.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final ZipkinTracer zipkinTracer;
    
    public SeleniumTracer(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.zipkinTracer = new ZipkinTracer();
    }
    
    /**
     * Navigate to URL with tracing
     */
    public void navigateTo(String url) {
        try {
            zipkinTracer.startSpan("selenium-navigate", "Navigate to URL: " + url);
            
            logger.info("Navigating to: {}", url);
            driver.get(url);
            
            zipkinTracer.trackPageNavigation("Navigation", url, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-navigate", true);
            
        } catch (Exception e) {
            logger.error("Navigation failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("Navigation", "Failed to navigate to: " + url, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-navigate", false);
            throw e;
        }
    }
    
    /**
     * Click element with detailed tracing
     */
    public void click(WebElement element, String description) {
        try {
            zipkinTracer.startSpan("selenium-click", "Click element: " + description);
            
            logger.info("Clicking element: {} - {}", description, element.getText());
            element.click();
            
            zipkinTracer.trackElementInteraction("Click", description, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-click", true);
            
        } catch (Exception e) {
            logger.error("Click failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("Click", "Failed to click element: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-click", false);
            throw e;
        }
    }
    
    /**
     * Send keys to element with tracing
     */
    public void sendKeys(WebElement element, String keys, String description) {
        try {
            zipkinTracer.startSpan("selenium-sendkeys", "Send keys to element: " + description);
            
            logger.info("Sending keys to element: {} - {} characters", description, keys.length());
            element.clear();
            element.sendKeys(keys);
            
            zipkinTracer.trackElementInteraction("SendKeys", description, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-sendkeys", true);
            
        } catch (Exception e) {
            logger.error("SendKeys failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("SendKeys", "Failed to send keys to element: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-sendkeys", false);
            throw e;
        }
    }
    
    /**
     * Find element with tracing
     */
    public WebElement findElement(By locator, String description) {
        try {
            zipkinTracer.startSpan("selenium-find-element", "Find element: " + description);
            
            logger.info("Finding element: {} with locator: {}", description, locator);
            WebElement element = driver.findElement(locator);
            
            zipkinTracer.trackElementInteraction("FindElement", description, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-find-element", true);
            
            return element;
            
        } catch (Exception e) {
            logger.error("FindElement failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("FindElement", "Failed to find element: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-find-element", false);
            throw e;
        }
    }
    
    /**
     * Wait for element with tracing
     */
    public WebElement waitForElement(By locator, String description) {
        try {
            zipkinTracer.startSpan("selenium-wait-for-element", "Wait for element: " + description);
            
            logger.info("Waiting for element: {} with locator: {}", description, locator);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            
            zipkinTracer.trackElementInteraction("WaitForElement", description, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-wait-for-element", true);
            
            return element;
            
        } catch (Exception e) {
            logger.error("WaitForElement failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("WaitForElement", "Failed to wait for element: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-wait-for-element", false);
            throw e;
        }
    }
    
    /**
     * Wait for page load with tracing
     */
    public void waitForPageLoad(String pageName) {
        try {
            zipkinTracer.startSpan("selenium-wait-for-page-load", "Wait for page load: " + pageName);
            
            logger.info("Waiting for page load: {}", pageName);
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            zipkinTracer.trackPageNavigation("PageLoad", pageName, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-wait-for-page-load", true);
            
        } catch (Exception e) {
            logger.error("WaitForPageLoad failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("WaitForPageLoad", "Failed to wait for page load: " + pageName, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-wait-for-page-load", false);
            throw e;
        }
    }
    
    /**
     * Get element text with tracing
     */
    public String getElementText(WebElement element, String description) {
        try {
            zipkinTracer.startSpan("selenium-get-text", "Get text from element: " + description);
            
            logger.info("Getting text from element: {}", description);
            String text = element.getText();
            
            zipkinTracer.trackElementInteraction("GetText", description, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-get-text", true);
            
            return text;
            
        } catch (Exception e) {
            logger.error("GetText failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("GetText", "Failed to get text from element: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-get-text", false);
            throw e;
        }
    }
    
    /**
     * Verify element is displayed with tracing
     */
    public boolean isElementDisplayed(WebElement element, String description) {
        try {
            zipkinTracer.startSpan("selenium-is-displayed", "Check if element is displayed: " + description);
            
            logger.info("Checking if element is displayed: {}", description);
            boolean isDisplayed = element.isDisplayed();
            
            zipkinTracer.trackElementInteraction("IsDisplayed", description, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-is-displayed", true);
            
            return isDisplayed;
            
        } catch (Exception e) {
            logger.error("IsDisplayed failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("IsDisplayed", "Failed to check if element is displayed: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-is-displayed", false);
            throw e;
        }
    }
    
    /**
     * Get current URL with tracing
     */
    public String getCurrentUrl() {
        try {
            zipkinTracer.startSpan("selenium-get-current-url", "Get current URL");
            
            String url = driver.getCurrentUrl();
            
            zipkinTracer.trackPageNavigation("GetCurrentUrl", url, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-get-current-url", true);
            
            return url;
            
        } catch (Exception e) {
            logger.error("GetCurrentUrl failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("GetCurrentUrl", "Failed to get current URL", false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-get-current-url", false);
            throw e;
        }
    }
    
    /**
     * Get page title with tracing
     */
    public String getPageTitle() {
        try {
            zipkinTracer.startSpan("selenium-get-page-title", "Get page title");
            
            String title = driver.getTitle();
            
            zipkinTracer.trackPageNavigation("GetPageTitle", title, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-get-page-title", true);
            
            return title;
            
        } catch (Exception e) {
            logger.error("GetPageTitle failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("GetPageTitle", "Failed to get page title", false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-get-page-title", false);
            throw e;
        }
    }
    
    /**
     * Execute JavaScript with tracing
     */
    public Object executeScript(String script, String description) {
        try {
            zipkinTracer.startSpan("selenium-execute-script", "Execute JavaScript: " + description);
            
            logger.info("Executing JavaScript: {}", description);
            Object result = ((JavascriptExecutor) driver).executeScript(script);
            
            zipkinTracer.trackElementInteraction("ExecuteScript", description, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-execute-script", true);
            
            return result;
            
        } catch (Exception e) {
            logger.error("ExecuteScript failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("ExecuteScript", "Failed to execute JavaScript: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-execute-script", false);
            throw e;
        }
    }
    
    /**
     * Take screenshot with tracing
     */
    public byte[] takeScreenshot(String description) {
        try {
            zipkinTracer.startSpan("selenium-take-screenshot", "Take screenshot: " + description);
            
            logger.info("Taking screenshot: {}", description);
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            
            zipkinTracer.trackTestStep("TakeScreenshot", "Screenshot taken: " + description, true, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-take-screenshot", true);
            
            return screenshot;
            
        } catch (Exception e) {
            logger.error("TakeScreenshot failed: {}", e.getMessage());
            zipkinTracer.trackTestStep("TakeScreenshot", "Failed to take screenshot: " + description, false, System.currentTimeMillis());
            zipkinTracer.endSpan("selenium-take-screenshot", false);
            throw e;
        }
    }
} 