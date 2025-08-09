// Simple test to verify DevTools fixes
// This file can be used to test the ChromeDevToolsManager fixes

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import proje.com.saucedemo.utils.ChromeDevToolsManager;

public class TestDevToolsFix {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Chrome DevTools Fixes ===");
        
        WebDriver driver = null;
        ChromeDevToolsManager cdpManager = null;
        
        try {
            // Setup Chrome with DevTools-compatible options
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--enable-logging");
            options.addArguments("--v=1");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            
            driver = new ChromeDriver(options);
            System.out.println("✅ Chrome driver created successfully");
            
            // Initialize DevTools Manager
            cdpManager = new ChromeDevToolsManager(driver);
            System.out.println("✅ DevTools Manager created");
            
            // Check initialization status
            System.out.println("DevTools Status:\n" + cdpManager.getDevToolsStatus());
            
            if (cdpManager.isInitialized()) {
                System.out.println("✅ DevTools initialized successfully");
                
                // Enable monitoring
                cdpManager.enableAllMonitoring();
                System.out.println("✅ All monitoring enabled");
                
                // Navigate to a test page
                driver.get("https://www.google.com");
                System.out.println("✅ Navigated to Google");
                
                // Wait for network activity
                Thread.sleep(2000);
                
                // Check results
                System.out.println("Network Requests: " + cdpManager.getNetworkRequestCount());
                System.out.println("Console Logs: " + cdpManager.getConsoleLogCount());
                System.out.println("JS Errors: " + cdpManager.getJavaScriptErrorCount());
                
                System.out.println("✅ All DevTools fixes working correctly!");
                
            } else {
                System.out.println("❌ DevTools failed to initialize");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            if (cdpManager != null) {
                cdpManager.cleanup();
            }
            if (driver != null) {
                driver.quit();
            }
            System.out.println("✅ Cleanup completed");
        }
    }
}
