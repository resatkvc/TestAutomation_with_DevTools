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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * WebDriver configuration class for UI test automation
 * Uses WebDriverManager for automatic driver management
 * Supports Chrome, Firefox, and Edge browsers
 */
public class WebDriverConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(WebDriverConfig.class);
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    /**
     * Initialize WebDriver using WebDriverManager
     * @param browserType Type of browser to use (chrome, firefox, edge)
     * @return Configured WebDriver instance
     */
    public WebDriver initializeDriver(String browserType) {
        try {
            driver = createLocalDriver(browserType);
            
            // Configure WebDriver
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            // Initialize WebDriverWait
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            logger.info("WebDriver initialized successfully for browser: {}", browserType);
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
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--allow-running-insecure-content");
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
     * Quit WebDriver and cleanup resources
     */
    public void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
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
} 