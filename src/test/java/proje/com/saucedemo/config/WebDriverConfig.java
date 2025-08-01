package proje.com.saucedemo.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;

/**
 * WebDriver configuration class for SauceDemo test automation
 * Includes Zipkin integration setup
 */
public class WebDriverConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(WebDriverConfig.class);
    private static final String SELENIUM_GRID_URL = "http://localhost:4444/wd/hub";
    private static final String ZIPKIN_URL = "http://localhost:9411";
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    /**
     * Initialize WebDriver based on configuration
     * @param browserType Type of browser to use
     * @param useGrid Whether to use Selenium Grid
     * @return Configured WebDriver instance
     */
    public WebDriver initializeDriver(String browserType, boolean useGrid) {
        try {
            if (useGrid) {
                driver = createRemoteDriver(browserType);
            } else {
                driver = createLocalDriver(browserType);
            }
            
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
     * Create local WebDriver instance
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
                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                return new FirefoxDriver(firefoxOptions);
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                return new EdgeDriver(edgeOptions);
                
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
    }
    
    /**
     * Create remote WebDriver instance for Selenium Grid
     */
    private WebDriver createRemoteDriver(String browserType) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        switch (browserType.toLowerCase()) {
            case "chrome":
                capabilities.setBrowserName("chrome");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                capabilities.merge(chromeOptions);
                break;
                
            case "firefox":
                capabilities.setBrowserName("firefox");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                capabilities.merge(firefoxOptions);
                break;
                
            case "edge":
                capabilities.setBrowserName("edge");
                EdgeOptions edgeOptions = new EdgeOptions();
                capabilities.merge(edgeOptions);
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
        
        return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), capabilities);
    }
    
    /**
     * Get WebDriverWait instance
     */
    public WebDriverWait getWait() {
        return wait;
    }
    
    /**
     * Get current WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Quit WebDriver and cleanup
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
     * Navigate to URL with Zipkin tracing
     */
    public void navigateTo(String url) {
        try {
            logger.info("Navigating to URL: {}", url);
            driver.get(url);
            logger.info("Successfully navigated to: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}", e.getMessage());
            throw new RuntimeException("Navigation failed", e);
        }
    }
} 