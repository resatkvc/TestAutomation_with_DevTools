package proje.com.saucedemo.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import proje.com.saucedemo.utils.DevToolsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * TestAutomation_with_DevTools - WebDriver configuration class with DevTools integration
 * Uses WebDriverManager for automatic driver management
 * Supports Chrome, Firefox, and Edge browsers with Chrome DevTools Protocol (CDP)
 * 
 * @author TestAutomation_with_DevTools
 * @version 2.0
 */
public class WebDriverConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(WebDriverConfig.class);
    
    private WebDriver driver;
    private WebDriverWait wait;
    private DevToolsHelper devToolsHelper;
    
    /**
     * Initialize WebDriver using WebDriverManager with DevTools integration
     * @param browserType Type of browser to use (chrome, firefox, edge)
     * @return Configured WebDriver instance
     */
    public WebDriver initializeDriver(String browserType) {
        try {
            driver = createLocalDriver(browserType);
            
            // Configure WebDriver
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));
            
            // Initialize WebDriverWait with longer timeout
            wait = new WebDriverWait(driver, Duration.ofSeconds(45));
            
            // Initialize DevTools Helper for supported browsers
            if (driver instanceof org.openqa.selenium.chrome.ChromeDriver || 
                driver instanceof org.openqa.selenium.edge.EdgeDriver) {
                devToolsHelper = new DevToolsHelper(driver);
                logger.info("DevTools Helper initialized for browser: {}", browserType);
            } else {
                logger.info("DevTools not supported for browser: {}", browserType);
            }
            
            logger.info("TestAutomation_with_DevTools WebDriver initialized successfully for browser: {}", browserType);
            return driver;
            
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver: {}", e.getMessage());
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }
    
    /**
     * Create local WebDriver instance using WebDriverManager
     */
    private WebDriver createLocalDriver(String browserType) {
        switch (browserType.toLowerCase()) {
            case "chrome":
                // Use automatic Chrome version detection
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                // Basic Chrome options
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--remote-allow-origins=*");
                
                // DevTools için ek Chrome options
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--allow-running-insecure-content");
                chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
                
                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--no-sandbox");
                firefoxOptions.addArguments("--disable-dev-shm-usage");
                return new FirefoxDriver(firefoxOptions);
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--no-sandbox");
                edgeOptions.addArguments("--disable-dev-shm-usage");
                return new EdgeDriver(edgeOptions);
                
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
    }
    
    /**
     * Get WebDriverWait instance
     */
    public WebDriverWait getWait() {
        return wait;
    }
    
    /**
     * Get WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Get DevTools Helper instance
     */
    public DevToolsHelper getDevToolsHelper() {
        return devToolsHelper;
    }
    
    /**
     * Enable all DevTools monitoring features
     */
    public void enableDevToolsMonitoring() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableAllMonitoring();
            logger.info("All DevTools monitoring features enabled");
        } else {
            logger.warn("DevTools not available for monitoring");
        }
    }
    
    /**
     * Enable only network monitoring
     */
    public void enableNetworkMonitoring() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableNetworkMonitoring();
            logger.info("Network monitoring enabled");
        } else {
            logger.warn("DevTools not available for network monitoring");
        }
    }
    
    /**
     * Enable only console logging
     */
    public void enableConsoleLogging() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableConsoleLogging();
            logger.info("Console logging enabled");
        } else {
            logger.warn("DevTools not available for console logging");
        }
    }
    
    /**
     * Block specific URLs
     */
    public void blockUrls(java.util.List<String> urlsToBlock) {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.blockUrls(urlsToBlock);
        } else {
            logger.warn("DevTools not available for URL blocking");
        }
    }
    
    /**
     * Get network statistics
     */
    public DevToolsHelper.NetworkStats getNetworkStats() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            return devToolsHelper.getNetworkStats();
        }
        return new DevToolsHelper.NetworkStats(0, 0, 0);
    }
    
    /**
     * Quit WebDriver and cleanup resources
     */
    public void quitDriver() {
        if (devToolsHelper != null) {
            try {
                devToolsHelper.close();
                logger.info("DevTools session closed");
            } catch (Exception e) {
                logger.warn("Error closing DevTools: {}", e.getMessage());
            }
        }
        
        if (driver != null) {
            try {
                driver.quit();
                logger.info("TestAutomation_with_DevTools WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Navigate to URL
     */
    public void navigateTo(String url) {
        if (driver != null) {
            driver.get(url);
            logger.info("Navigated to: {}", url);
        }
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        if (driver != null) {
            return driver.getCurrentUrl();
        }
        return "";
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        if (driver != null) {
            return driver.getTitle();
        }
        return "";
    }
    
    /**
     * Check if DevTools is available
     */
    public boolean isDevToolsAvailable() {
        return devToolsHelper != null && devToolsHelper.isEnabled();
    }
} 