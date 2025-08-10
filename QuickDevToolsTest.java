import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import proje.com.saucedemo.utils.ChromeDevToolsManager;

/**
 * Quick test to verify DevTools functionality
 * Run this to check if DevTools is working properly
 */
public class QuickDevToolsTest {
    
    public static void main(String[] args) {
        System.out.println("=== Quick DevTools Test ===");
        
        WebDriver driver = null;
        ChromeDevToolsManager cdpManager = null;
        
        try {
            // Setup Chrome with DevTools-compatible options
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--enable-logging");
            options.addArguments("--v=1");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-plugins");
            options.addArguments("--disable-images");
            options.addArguments("--disable-javascript-harmony-shipping");
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-backgrounding-occluded-windows");
            options.addArguments("--disable-renderer-backgrounding");
            options.addArguments("--disable-features=TranslateUI");
            options.addArguments("--disable-ipc-flooding-protection");
            
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("detach", true);
            
            System.out.println("Starting Chrome driver...");
            driver = new ChromeDriver(options);
            
            System.out.println("Initializing DevTools...");
            cdpManager = new ChromeDevToolsManager(driver);
            
            // Check DevTools status
            System.out.println("DevTools Status:");
            System.out.println(cdpManager.getDevToolsStatus());
            
            if (!cdpManager.isInitialized()) {
                System.out.println("❌ DevTools failed to initialize!");
                System.out.println("This usually means:");
                System.out.println("1. Chrome version is not compatible with DevTools v122");
                System.out.println("2. DevTools dependency is missing from pom.xml");
                System.out.println("3. Chrome options are not compatible with DevTools");
                return;
            }
            
            System.out.println("✅ DevTools initialized successfully!");
            
            // Enable monitoring
            System.out.println("Enabling DevTools monitoring...");
            cdpManager.enableAllMonitoring();
            
            // Navigate to a test page
            System.out.println("Navigating to Google...");
            driver.get("https://www.google.com");
            Thread.sleep(3000);
            
            // Check results
            int networkRequests = cdpManager.getNetworkRequestCount();
            int consoleLogs = cdpManager.getConsoleLogCount();
            
            System.out.println("=== RESULTS ===");
            System.out.println("Network Requests: " + networkRequests);
            System.out.println("Console Logs: " + consoleLogs);
            System.out.println("DevTools Initialized: " + cdpManager.isInitialized());
            
            if (networkRequests > 0) {
                System.out.println("✅ DevTools is working perfectly!");
            } else {
                System.out.println("⚠️ DevTools initialized but no network requests captured");
                System.out.println("This might be normal for simple pages");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (cdpManager != null) {
                    cdpManager.cleanup();
                }
                if (driver != null) {
                    driver.quit();
                }
            } catch (Exception e) {
                System.out.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}
