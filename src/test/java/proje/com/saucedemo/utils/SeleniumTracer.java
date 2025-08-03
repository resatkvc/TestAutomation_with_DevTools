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
    
    public SeleniumTracer(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Navigate to URL with tracing
     */
    public void navigateTo(String url) {
        try {
            ZipkinTracer.startSpan("selenium-navigate");
            ZipkinTracer.addTag("action", "navigate");
            ZipkinTracer.addTag("url", url);
            ZipkinTracer.addTag("current_url", driver.getCurrentUrl());
            
            logger.info("Navigating to: {}", url);
            driver.get(url);
            
            ZipkinTracer.addTag("new_url", driver.getCurrentUrl());
            ZipkinTracer.addTag("page_title", driver.getTitle());
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Click element with detailed tracing
     */
    public void click(WebElement element, String description) {
        try {
            ZipkinTracer.startSpan("selenium-click");
            ZipkinTracer.addTag("action", "click");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("element_tag", element.getTagName());
            ZipkinTracer.addTag("element_text", element.getText());
            ZipkinTracer.addTag("element_location", element.getLocation().toString());
            
            logger.info("Clicking element: {} - {}", description, element.getText());
            element.click();
            
            ZipkinTracer.addTag("click_successful", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Send keys to element with tracing
     */
    public void sendKeys(WebElement element, String keys, String description) {
        try {
            ZipkinTracer.startSpan("selenium-sendkeys");
            ZipkinTracer.addTag("action", "sendkeys");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("element_tag", element.getTagName());
            ZipkinTracer.addTag("keys_length", String.valueOf(keys.length()));
            ZipkinTracer.addTag("keys_masked", keys.replaceAll(".", "*"));
            
            logger.info("Sending keys to element: {} - {} characters", description, keys.length());
            element.clear();
            element.sendKeys(keys);
            
            ZipkinTracer.addTag("sendkeys_successful", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Find element with tracing
     */
    public WebElement findElement(By locator, String description) {
        try {
            ZipkinTracer.startSpan("selenium-find-element");
            ZipkinTracer.addTag("action", "find_element");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("locator_type", locator.toString());
            
            logger.info("Finding element: {} with locator: {}", description, locator);
            WebElement element = driver.findElement(locator);
            
            ZipkinTracer.addTag("element_found", "true");
            ZipkinTracer.addTag("element_tag", element.getTagName());
            ZipkinTracer.addTag("element_text", element.getText());
            ZipkinTracer.finishSpan();
            
            return element;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Wait for element with tracing
     */
    public WebElement waitForElement(By locator, String description) {
        try {
            ZipkinTracer.startSpan("selenium-wait-for-element");
            ZipkinTracer.addTag("action", "wait_for_element");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("locator_type", locator.toString());
            ZipkinTracer.addTag("timeout_seconds", "15");
            
            logger.info("Waiting for element: {} with locator: {}", description, locator);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            
            ZipkinTracer.addTag("element_found", "true");
            ZipkinTracer.addTag("element_tag", element.getTagName());
            ZipkinTracer.addTag("element_text", element.getText());
            ZipkinTracer.finishSpan();
            
            return element;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Wait for page load with tracing
     */
    public void waitForPageLoad(String pageName) {
        try {
            ZipkinTracer.startSpan("selenium-wait-for-page-load");
            ZipkinTracer.addTag("action", "wait_for_page_load");
            ZipkinTracer.addTag("page_name", pageName);
            ZipkinTracer.addTag("current_url", driver.getCurrentUrl());
            
            logger.info("Waiting for page load: {}", pageName);
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            ZipkinTracer.addTag("page_loaded", "true");
            ZipkinTracer.addTag("final_url", driver.getCurrentUrl());
            ZipkinTracer.addTag("page_title", driver.getTitle());
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Get element text with tracing
     */
    public String getElementText(WebElement element, String description) {
        try {
            ZipkinTracer.startSpan("selenium-get-text");
            ZipkinTracer.addTag("action", "get_text");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("element_tag", element.getTagName());
            
            logger.info("Getting text from element: {}", description);
            String text = element.getText();
            
            ZipkinTracer.addTag("text_length", String.valueOf(text.length()));
            ZipkinTracer.addTag("text_preview", text.length() > 50 ? text.substring(0, 50) + "..." : text);
            ZipkinTracer.finishSpan();
            
            return text;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Verify element is displayed with tracing
     */
    public boolean isElementDisplayed(WebElement element, String description) {
        try {
            ZipkinTracer.startSpan("selenium-is-displayed");
            ZipkinTracer.addTag("action", "is_displayed");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("element_tag", element.getTagName());
            
            logger.info("Checking if element is displayed: {}", description);
            boolean isDisplayed = element.isDisplayed();
            
            ZipkinTracer.addTag("is_displayed", String.valueOf(isDisplayed));
            ZipkinTracer.finishSpan();
            
            return isDisplayed;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Get current URL with tracing
     */
    public String getCurrentUrl() {
        try {
            ZipkinTracer.startSpan("selenium-get-current-url");
            ZipkinTracer.addTag("action", "get_current_url");
            
            String url = driver.getCurrentUrl();
            
            ZipkinTracer.addTag("current_url", url);
            ZipkinTracer.finishSpan();
            
            return url;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Get page title with tracing
     */
    public String getPageTitle() {
        try {
            ZipkinTracer.startSpan("selenium-get-page-title");
            ZipkinTracer.addTag("action", "get_page_title");
            
            String title = driver.getTitle();
            
            ZipkinTracer.addTag("page_title", title);
            ZipkinTracer.finishSpan();
            
            return title;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Execute JavaScript with tracing
     */
    public Object executeScript(String script, String description) {
        try {
            ZipkinTracer.startSpan("selenium-execute-script");
            ZipkinTracer.addTag("action", "execute_script");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("script_length", String.valueOf(script.length()));
            
            logger.info("Executing JavaScript: {}", description);
            Object result = ((JavascriptExecutor) driver).executeScript(script);
            
            ZipkinTracer.addTag("script_successful", "true");
            ZipkinTracer.addTag("result_type", result != null ? result.getClass().getSimpleName() : "null");
            ZipkinTracer.finishSpan();
            
            return result;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
    
    /**
     * Take screenshot with tracing
     */
    public byte[] takeScreenshot(String description) {
        try {
            ZipkinTracer.startSpan("selenium-take-screenshot");
            ZipkinTracer.addTag("action", "take_screenshot");
            ZipkinTracer.addTag("description", description);
            ZipkinTracer.addTag("current_url", driver.getCurrentUrl());
            
            logger.info("Taking screenshot: {}", description);
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            
            ZipkinTracer.addTag("screenshot_size_bytes", String.valueOf(screenshot.length));
            ZipkinTracer.finishSpan();
            
            return screenshot;
            
        } catch (Exception e) {
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw e;
        }
    }
} 