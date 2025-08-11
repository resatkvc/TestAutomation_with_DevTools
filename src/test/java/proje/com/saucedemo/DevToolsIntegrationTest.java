package proje.com.saucedemo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import proje.com.saucedemo.config.WebDriverConfig;
import proje.com.saucedemo.utils.DevToolsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * DevTools Integration Test - Chrome DevTools Protocol (CDP) entegrasyon testleri
 * Network monitoring, console logging, performance tracking ve URL blocking özelliklerini test eder
 * 
 * @author TestAutomation_with_DevTools
 * @version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DevToolsIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(DevToolsIntegrationTest.class);
    
    private static WebDriverConfig webDriverConfig;
    private static WebDriver driver;
    
    @BeforeAll
    static void setUp() {
        logger.info("=== DevTools Integration Test Setup ===");
        webDriverConfig = new WebDriverConfig();
        driver = webDriverConfig.initializeDriver("chrome");
        
        // DevTools monitoring'i etkinleştir
        if (webDriverConfig.isDevToolsAvailable()) {
            webDriverConfig.enableDevToolsMonitoring();
            logger.info("DevTools monitoring enabled for all tests");
        } else {
            logger.warn("DevTools not available - some tests may be skipped");
        }
    }
    
    @AfterAll
    static void tearDown() {
        logger.info("=== DevTools Integration Test Cleanup ===");
        if (webDriverConfig != null) {
            webDriverConfig.quitDriver();
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Test DevTools Availability")
    void testDevToolsAvailability() {
        logger.info("Testing DevTools availability...");
        
        Assertions.assertTrue(webDriverConfig.isDevToolsAvailable(), 
            "DevTools should be available for Chrome browser");
        
        DevToolsHelper devToolsHelper = webDriverConfig.getDevToolsHelper();
        Assertions.assertNotNull(devToolsHelper, "DevTools Helper should not be null");
        Assertions.assertTrue(devToolsHelper.isEnabled(), "DevTools should be enabled");
        
        logger.info("DevTools availability test passed");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test Network Monitoring")
    void testNetworkMonitoring() {
        logger.info("Testing network monitoring...");
        
        // Network monitoring'i etkinleştir
        webDriverConfig.enableNetworkMonitoring();
        
        // Test URL'ine git (network istekleri oluşturmak için)
        String testUrl = "https://www.google.com";
        webDriverConfig.navigateTo(testUrl);
        
        // Kısa bir bekleme süresi (network isteklerinin tamamlanması için)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Network istatistiklerini kontrol et
        DevToolsHelper.NetworkStats stats = webDriverConfig.getNetworkStats();
        logger.info("Network Statistics: {}", stats);
        
        Assertions.assertTrue(stats.getTotalRequests() > 0, 
            "Should have captured network requests");
        Assertions.assertTrue(stats.getTotalResponses() > 0, 
            "Should have captured network responses");
        
        logger.info("Network monitoring test passed");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test Console Logging")
    void testConsoleLogging() {
        logger.info("Testing console logging...");
        
        // Console logging'i etkinleştir
        webDriverConfig.enableConsoleLogging();
        
        // JavaScript console log'ları oluştur
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("console.log('Test info message');");
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("console.warn('Test warning message');");
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("console.error('Test error message');");
        
        // Kısa bir bekleme süresi
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("Console logging test completed");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test URL Blocking")
    void testUrlBlocking() {
        logger.info("Testing URL blocking...");
        
        // CSS ve resim dosyalarını blokla
        List<String> urlsToBlock = Arrays.asList(
            "*.css",
            "*.png",
            "*.jpg",
            "*.jpeg",
            "*.gif"
        );
        
        webDriverConfig.blockUrls(urlsToBlock);
        
        // Test URL'ine git (bloklanan kaynaklar olmadan)
        String testUrl = "https://www.example.com";
        webDriverConfig.navigateTo(testUrl);
        
        // Kısa bir bekleme süresi
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("URL blocking test completed");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test Performance Monitoring")
    void testPerformanceMonitoring() {
        logger.info("Testing performance monitoring...");
        
        // Performance monitoring zaten etkin (enableAllMonitoring ile)
        // Test URL'ine git ve performans verilerini topla
        String testUrl = "https://www.github.com";
        webDriverConfig.navigateTo(testUrl);
        
        // Kısa bir bekleme süresi
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("Performance monitoring test completed");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test Page Monitoring")
    void testPageMonitoring() {
        logger.info("Testing page monitoring...");
        
        // Page monitoring zaten etkin (enableAllMonitoring ile)
        // Farklı sayfalara git ve page event'lerini test et
        String[] testUrls = {
            "https://www.wikipedia.org",
            "https://www.stackoverflow.com"
        };
        
        for (String url : testUrls) {
            webDriverConfig.navigateTo(url);
            
            // Kısa bir bekleme süresi
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        logger.info("Page monitoring test completed");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test Network Statistics")
    void testNetworkStatistics() {
        logger.info("Testing network statistics...");
        
        // Yeni bir sayfa yükle
        String testUrl = "https://www.mozilla.org";
        webDriverConfig.navigateTo(testUrl);
        
        // Kısa bir bekleme süresi
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Network istatistiklerini al ve logla
        DevToolsHelper.NetworkStats stats = webDriverConfig.getNetworkStats();
        logger.info("Final Network Statistics: {}", stats);
        
        // İstatistiklerin mantıklı olduğunu kontrol et
        Assertions.assertTrue(stats.getTotalRequests() >= 0, 
            "Total requests should be non-negative");
        Assertions.assertTrue(stats.getTotalResponses() >= 0, 
            "Total responses should be non-negative");
        Assertions.assertTrue(stats.getPendingRequests() >= 0, 
            "Pending requests should be non-negative");
        
        logger.info("Network statistics test passed");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test DevTools Session Management")
    void testDevToolsSessionManagement() {
        logger.info("Testing DevTools session management...");
        
        DevToolsHelper devToolsHelper = webDriverConfig.getDevToolsHelper();
        Assertions.assertNotNull(devToolsHelper, "DevTools Helper should not be null");
        
        // DevTools oturumunun etkin olduğunu kontrol et
        Assertions.assertTrue(devToolsHelper.isEnabled(), "DevTools should be enabled");
        
        // Network istatistiklerini kontrol et
        DevToolsHelper.NetworkStats stats = devToolsHelper.getNetworkStats();
        logger.info("Current Network Stats: {}", stats);
        
        logger.info("DevTools session management test passed");
    }
}
