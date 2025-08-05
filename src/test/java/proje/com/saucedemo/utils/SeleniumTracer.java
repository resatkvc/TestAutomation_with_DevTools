package proje.com.saucedemo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Selenium WebDriver tracer for automation testing
 * Provides detailed logging and timing for Selenium operations
 * Tracks element interactions, page navigation, and test steps
 */
public class SeleniumTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(SeleniumTracer.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    public SeleniumTracer(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Navigate to URL with detailed logging
     */
    public void navigateToUrl(String url, String description) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("🚀 Navigating to: {} - {}", url, description);
            driver.get(url);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("✅ Navigation completed in {}ms: {}", duration, url);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("❌ Navigation failed in {}ms: {} - Error: {}", duration, url, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Click element with detailed logging
     */
    public void clickElement(WebElement element, String description) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("🖱️ Clicking element: {}", description);
            element.click();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("✅ Click completed in {}ms: {}", duration, description);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("❌ Click failed in {}ms: {} - Error: {}", duration, description, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Send keys to element with detailed logging
     */
    public void sendKeysToElement(WebElement element, String keys, String description) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("⌨️ Sending keys to element: {} - Keys: {}", description, keys);
            element.clear();
            element.sendKeys(keys);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("✅ SendKeys completed in {}ms: {}", duration, description);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("❌ SendKeys failed in {}ms: {} - Error: {}", duration, description, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Find element with detailed logging
     */
    public WebElement findElement(By locator, String description) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("🔍 Finding element: {}", description);
            WebElement element = driver.findElement(locator);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("✅ Element found in {}ms: {}", duration, description);
            return element;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("❌ Element not found in {}ms: {} - Error: {}", duration, description, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for element with detailed logging
     */
    public WebElement waitForElement(By locator, String description) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("⏳ Waiting for element: {}", description);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            long duration = System.currentTimeMillis() - startTime;
            logger.info("✅ Element ready in {}ms: {}", duration, description);
            return element;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("❌ Element not ready in {}ms: {} - Error: {}", duration, description, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for page load with detailed logging
     */
    public void waitForPageLoad(String pageName) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("⏳ Waiting for page load: {}", pageName);
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            long duration = System.currentTimeMillis() - startTime;
            logger.info("✅ Page loaded in {}ms: {}", duration, pageName);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("❌ Page load failed in {}ms: {} - Error: {}", duration, pageName, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Track test step with detailed logging
     */
    public void trackTestStep(String stepName, String description, boolean success, long duration) {
        if (success) {
            logger.info("✅ Test Step: {} - {} ({}ms)", stepName, description, duration);
        } else {
            logger.error("❌ Test Step: {} - {} ({}ms)", stepName, description, duration);
        }
    }
    
    /**
     * Track element interaction with detailed logging
     */
    public void trackElementInteraction(String action, String description, long duration) {
        logger.info("🎯 Element Interaction: {} - {} ({}ms)", action, description, duration);
    }
    
    /**
     * Track page navigation with detailed logging
     */
    public void trackPageNavigation(String pageName, String url, long duration) {
        logger.info("🌐 Page Navigation: {} - {} ({}ms)", pageName, url, duration);
    }
} 