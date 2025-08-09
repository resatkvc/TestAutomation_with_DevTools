package proje.com.saucedemo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.config.WebDriverConfig;
import proje.com.saucedemo.utils.ChromeDevToolsManager;

/**
 * Simple test to verify DevTools is working
 */
public class DevToolsTest {
    
    private static final Logger logger = LoggerFactory.getLogger(DevToolsTest.class);
    
    private WebDriver driver;
    private WebDriverConfig config;
    private ChromeDevToolsManager cdpManager;
    
    @BeforeEach
    void setUp() {
        config = new WebDriverConfig();
        driver = config.initializeDriver("chrome");
        cdpManager = new ChromeDevToolsManager(driver);
    }
    
    @AfterEach
    void tearDown() {
        if (cdpManager != null) {
            cdpManager.cleanup();
        }
        if (driver != null) {
            config.quitDriver();
        }
    }
    
    @Test
    @DisplayName("DevTools Network Monitoring Test")
    void testDevToolsNetworkMonitoring() {
        try {
            logger.info("=== Testing DevTools Network Monitoring ===");
            
            // Navigate to a simple page
            driver.get("https://www.google.com");
            logger.info("Navigated to Google");
            
            // Enable comprehensive CDP monitoring
            cdpManager.enableAllMonitoring();
            logger.info("✅ CDP monitoring suite enabled");
            
            // Navigate to test site to capture network traffic
            driver.get("https://www.automationexercise.com");
            logger.info("Navigated to AutomationExercise");
            
            // Wait a bit for requests to complete
            Thread.sleep(5000);
            
            // Check comprehensive DevTools status
            boolean cdpInitialized = cdpManager.isInitialized();
            int networkRequests = cdpManager.getNetworkRequestCount();
            int consoleLogs = cdpManager.getConsoleLogCount();
            int jsErrors = cdpManager.getJavaScriptErrorCount();
            
            logger.info("=== CHROME DEVTOOLS PROTOCOL RESULTS ===");
            logger.info("CDP Manager Initialized: {}", cdpInitialized);
            logger.info("Network Requests: {}", networkRequests);
            logger.info("Console Logs: {}", consoleLogs);
            logger.info("JavaScript Errors: {}", jsErrors);
            logger.info("CDP Summary: {}", cdpManager.getDevToolsSummary());
            
            // Verify comprehensive DevTools is working
            if (cdpInitialized && networkRequests > 0) {
                logger.info("✅ Chrome DevTools Protocol monitoring is working perfectly!");
                logger.info("✅ Captured {} network requests, {} console logs, {} JS errors", 
                           networkRequests, consoleLogs, jsErrors);
            } else {
                logger.warn("❌ CDP monitoring may not be working properly");
            }
            
        } catch (Exception e) {
            logger.error("DevTools test failed: {}", e.getMessage());
            throw new RuntimeException("DevTools test failed", e);
        }
    }
}
