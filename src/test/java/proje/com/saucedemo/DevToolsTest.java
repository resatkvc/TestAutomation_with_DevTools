package proje.com.saucedemo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.config.WebDriverConfig;
import proje.com.saucedemo.utils.ChromeDevToolsManager;

/**
 * Simple DevTools test to verify CDP functionality
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DevToolsTest {
    
    private static final Logger logger = LoggerFactory.getLogger(DevToolsTest.class);
    private static WebDriver driver;
    private static WebDriverConfig webDriverConfig;
    private static ChromeDevToolsManager cdpManager;
    
    @BeforeAll
    static void setUp() {
        try {
            logger.info("=== Setting up DevTools test ===");
            webDriverConfig = new WebDriverConfig();
            driver = webDriverConfig.initializeDriver("chrome");
            cdpManager = new ChromeDevToolsManager(driver);
            
            // Log detailed DevTools status
            logger.info("DevTools Status:\n{}", cdpManager.getDevToolsStatus());
            
            logger.info("DevTools test setup completed");
        } catch (Exception e) {
            logger.error("Test setup failed: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterAll
    static void tearDown() {
        try {
            logger.info("=== Cleaning up DevTools test ===");
            if (cdpManager != null) {
                logger.info("CDP Manager initialized: {}", cdpManager.isInitialized());
                logger.info("Network requests captured: {}", cdpManager.getNetworkRequestCount());
                logger.info("Console logs captured: {}", cdpManager.getConsoleLogCount());
                logger.info("JavaScript errors: {}", cdpManager.getJavaScriptErrorCount());
                cdpManager.cleanup();
            }
            if (driver != null) {
                webDriverConfig.quitDriver();
            }
            logger.info("DevTools test cleanup completed");
        } catch (Exception e) {
            logger.error("Test cleanup failed: {}", e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Test DevTools Network Monitoring")
    void testDevToolsNetworkMonitoring() {
        try {
            logger.info("=== Testing DevTools Network Monitoring ===");
            
            // Check DevTools status before enabling monitoring
            logger.info("Pre-monitoring DevTools Status:\n{}", cdpManager.getDevToolsStatus());
            
            if (!cdpManager.isInitialized()) {
                logger.error("❌ DevTools not initialized - cannot proceed with test");
                Assertions.fail("DevTools must be initialized to run this test");
                return;
            }
            
            // Enable comprehensive DevTools monitoring
            cdpManager.enableAllMonitoring();
            logger.info("✅ All CDP domains enabled");
            
            // Navigate to test sites
            driver.get("https://www.google.com");
            logger.info("Navigated to Google");
            
            driver.get("https://www.automationexercise.com");
            logger.info("Navigated to AutomationExercise");
            
            // Wait a bit for network activity
            Thread.sleep(3000);
            
            // Check DevTools data
            int networkRequests = cdpManager.getNetworkRequestCount();
            int consoleLogs = cdpManager.getConsoleLogCount();
            int jsErrors = cdpManager.getJavaScriptErrorCount();
            
            logger.info("=== DEVTOOLS RESULTS ===");
            logger.info("Network Requests: {}", networkRequests);
            logger.info("Console Logs: {}", consoleLogs);
            logger.info("JavaScript Errors: {}", jsErrors);
            logger.info("CDP Manager Initialized: {}", cdpManager.isInitialized());
            
            // Log final DevTools status
            logger.info("Final DevTools Status:\n{}", cdpManager.getDevToolsStatus());
            
            // Verify DevTools is working
            Assertions.assertTrue(cdpManager.isInitialized(), "CDP Manager should be initialized");
            Assertions.assertTrue(networkRequests > 0, "Should capture network requests");
            
            logger.info("✅ Chrome DevTools Protocol monitoring is working perfectly!");
            
        } catch (Exception e) {
            logger.error("DevTools test failed: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("DevTools test failed", e);
        }
    }
}
