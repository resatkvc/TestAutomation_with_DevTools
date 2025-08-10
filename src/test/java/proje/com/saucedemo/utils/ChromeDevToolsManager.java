package proje.com.saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
// Chrome DevTools imports - using v122 (latest available for Chrome 138)
import org.openqa.selenium.devtools.v122.console.Console;
import org.openqa.selenium.devtools.v122.network.Network;
import org.openqa.selenium.devtools.v122.performance.Performance;
import org.openqa.selenium.devtools.v122.runtime.Runtime;
import org.openqa.selenium.devtools.v122.security.Security;
import org.openqa.selenium.devtools.v122.page.Page;
import org.openqa.selenium.devtools.v122.dom.DOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Chrome DevTools Protocol Manager for Chrome 138
 * Uses DevTools v122 (latest available) for Chrome 138 compatibility
 */
public class ChromeDevToolsManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ChromeDevToolsManager.class);
    
    private final WebDriver driver;
    private DevTools devTools;
    private boolean isInitialized = false;
    
    // Monitoring flags
    private boolean networkMonitoringEnabled = false;
    private boolean performanceMonitoringEnabled = false;
    private boolean consoleMonitoringEnabled = false;
    private boolean runtimeMonitoringEnabled = false;
    private boolean securityMonitoringEnabled = false;
    private boolean pageMonitoringEnabled = false;
    private boolean domMonitoringEnabled = false;
    
    // Metrics storage
    private final AtomicInteger networkRequestCount = new AtomicInteger(0);
    private final AtomicInteger consoleLogCount = new AtomicInteger(0);
    private final AtomicInteger jsErrorCount = new AtomicInteger(0);
    private final AtomicInteger securityEventCount = new AtomicInteger(0);
    
    private final Map<String, Object> performanceMetrics = new ConcurrentHashMap<>();
    private final List<String> consoleLogs = Collections.synchronizedList(new ArrayList<>());
    private final List<String> jsErrors = Collections.synchronizedList(new ArrayList<>());
    private final List<String> networkRequests = Collections.synchronizedList(new ArrayList<>());
    private final List<String> securityEvents = Collections.synchronizedList(new ArrayList<>());
    
    public ChromeDevToolsManager(WebDriver driver) {
        this.driver = driver;
        initializeDevTools();
    }
    
    /**
     * Initialize DevTools session for Chrome 138
     */
    private void initializeDevTools() {
        try {
            if (driver instanceof HasDevTools) {
                logger.info("🔧 Initializing Chrome DevTools Protocol Manager...");
                
                // Get Chrome version for logging
                String chromeVersion = getChromeVersion();
                logger.info("Chrome version detected: {}", chromeVersion);
                
                // Check Chrome version compatibility
                if (chromeVersion != null && isChrome138OrHigher(chromeVersion)) {
                    logger.info("✅ Chrome 138+ detected - using DevTools v122 (latest available)");
                    logger.info("ℹ️ Note: DevTools v138 not yet available, using v122 for compatibility");
                }
                
                devTools = ((HasDevTools) driver).getDevTools();
                devTools.createSession();
                
                isInitialized = true;
                logger.info("✅ Chrome DevTools Protocol session created successfully");
                
            } else {
                logger.warn("❌ DevTools not available - not a Chrome driver");
                logger.warn("Driver type: {}", driver.getClass().getSimpleName());
                isInitialized = false;
            }
        } catch (Exception e) {
            logger.error("❌ Failed to initialize DevTools: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            isInitialized = false;
        }
    }
    
    /**
     * Get Chrome version from WebDriver
     */
    private String getChromeVersion() {
        try {
            if (driver instanceof org.openqa.selenium.HasCapabilities) {
                org.openqa.selenium.Capabilities caps = ((org.openqa.selenium.HasCapabilities) driver).getCapabilities();
                return (String) caps.getCapability("browserVersion");
            }
            return null;
        } catch (Exception e) {
            logger.warn("Could not detect Chrome version: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if Chrome version is 138 or higher
     */
    private boolean isChrome138OrHigher(String chromeVersion) {
        try {
            if (chromeVersion == null || chromeVersion.isEmpty()) {
                return false;
            }
            
            String[] versionParts = chromeVersion.split("\\.");
            if (versionParts.length > 0) {
                int majorVersion = Integer.parseInt(versionParts[0]);
                return majorVersion >= 138;
            }
            return false;
        } catch (Exception e) {
            logger.warn("Could not parse Chrome version: {}", chromeVersion);
            return false;
        }
    }
    
    /**
     * Enable comprehensive monitoring for all supported domains
     */
    public void enableAllMonitoring() {
        if (!isInitialized) {
            logger.warn("⚠️ DevTools not initialized, cannot enable monitoring");
            return;
        }
        
        try {
            enableNetworkMonitoring();
            enablePerformanceMonitoring();
            enableConsoleMonitoring();
            enableRuntimeMonitoring();
            enableSecurityMonitoring();
            enablePageMonitoring();
            enableDOMMonitoring();
            
            logger.info("🚀 All CDP monitoring domains enabled");
        } catch (Exception e) {
            logger.error("Failed to enable all monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Enable Network domain monitoring
     */
    public void enableNetworkMonitoring() {
        if (!isInitialized) return;
        
        try {
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            
            // Listen for network requests
            devTools.addListener(Network.requestWillBeSent(), request -> {
                try {
                    String method = request.getRequest().getMethod();
                    String url = request.getRequest().getUrl();
                    String resourceType = determineResourceType(url);
                    
                    networkRequestCount.incrementAndGet();
                    String requestInfo = String.format("%s %s (%s)", method, url, resourceType);
                    networkRequests.add(requestInfo);
                    
                    logger.info("🌐 Network Request #{}: {}", networkRequestCount.get(), requestInfo);
                    
                } catch (Exception e) {
                    logger.error("Failed to process network request: {}", e.getMessage());
                }
            });
            
            // Listen for network responses
            devTools.addListener(Network.responseReceived(), response -> {
                try {
                    String url = response.getResponse().getUrl();
                    int status = response.getResponse().getStatus();
                    String statusText = response.getResponse().getStatusText();
                    
                    String statusIcon = (status >= 200 && status < 300) ? "✅" : "❌";
                    logger.info("{} Network Response: {} - Status: {} ({})", statusIcon, url, status, statusText);
                    
                } catch (Exception e) {
                    logger.error("Failed to process network response: {}", e.getMessage());
                }
            });
            
            // Listen for network failures
            devTools.addListener(Network.loadingFailed(), failed -> {
                String errorText = failed.getErrorText();
                logger.error("❌ Network Loading Failed: {}", errorText);
            });
            
            networkMonitoringEnabled = true;
            logger.info("✅ Network monitoring enabled");
            
        } catch (Exception e) {
            logger.error("Failed to enable network monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Enable Performance domain monitoring
     */
    public void enablePerformanceMonitoring() {
        if (!isInitialized) return;
        
        try {
            devTools.send(Performance.enable(Optional.empty()));
            devTools.send(Performance.getMetrics());
            
            performanceMonitoringEnabled = true;
            logger.info("✅ Performance monitoring enabled");
            
        } catch (Exception e) {
            logger.error("Failed to enable performance monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Enable Console domain monitoring
     */
    public void enableConsoleMonitoring() {
        if (!isInitialized) return;
        
        try {
            devTools.send(Console.enable());
            
            // Listen for console messages
            devTools.addListener(Console.messageAdded(), message -> {
                try {
                    String level = message.getLevel().toString();
                    String text = message.getText();
                    String source = message.getSource().toString();
                    
                    consoleLogCount.incrementAndGet();
                    String logEntry = String.format("[%s] %s (Source: %s)", level, text, source);
                    consoleLogs.add(logEntry);
                    
                    logger.info("📝 Console [{}]: {}", level, text);
                    
                } catch (Exception e) {
                    logger.error("Failed to process console message: {}", e.getMessage());
                }
            });
            
            consoleMonitoringEnabled = true;
            logger.info("✅ Console monitoring enabled");
            
        } catch (Exception e) {
            logger.error("Failed to enable console monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Enable Runtime domain monitoring
     */
    public void enableRuntimeMonitoring() {
        if (!isInitialized) return;
        
        try {
            devTools.send(Runtime.enable());
            
            // Listen for JavaScript exceptions
            devTools.addListener(Runtime.exceptionThrown(), exception -> {
                try {
                    String message = exception.getExceptionDetails().getText();
                    String stack = exception.getExceptionDetails().getStackTrace().toString();
                    
                    jsErrorCount.incrementAndGet();
                    String errorInfo = String.format("JS Error: %s\nStack: %s", message, stack);
                    jsErrors.add(errorInfo);
                    
                    logger.error("💥 JavaScript Exception: {}", message);
                    
                } catch (Exception e) {
                    logger.error("Failed to process JS exception: {}", e.getMessage());
                }
            });
            
            // Listen for console API calls
            devTools.addListener(Runtime.consoleAPICalled(), console -> {
                try {
                    String type = console.getType().toString();
                    String message = console.getArgs().toString();
                    
                    logger.info("🖥️  Console API [{}]: {}", type, message);
                    
                } catch (Exception e) {
                    logger.error("Failed to process console API call: {}", e.getMessage());
                }
            });
            
            runtimeMonitoringEnabled = true;
            logger.info("✅ Runtime monitoring enabled");
            
        } catch (Exception e) {
            logger.error("Failed to enable runtime monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Enable Security domain monitoring
     */
    public void enableSecurityMonitoring() {
        if (!isInitialized) return;
        
        try {
            devTools.send(Security.enable());
            
            // Listen for security state changes
            devTools.addListener(Security.securityStateChanged(), securityState -> {
                try {
                    securityEventCount.incrementAndGet();
                    String securityInfo = "Security State Changed";
                    securityEvents.add(securityInfo);
                    
                    logger.info("🔒 Security State Changed");
                    
                } catch (Exception e) {
                    logger.error("Failed to process security state change: {}", e.getMessage());
                }
            });
            
            securityMonitoringEnabled = true;
            logger.info("✅ Security monitoring enabled");
            
        } catch (Exception e) {
            logger.error("Failed to enable security monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Enable Page domain monitoring
     */
    public void enablePageMonitoring() {
        if (!isInitialized) return;
        
        try {
            devTools.send(Page.enable());
            
            // Listen for page load events
            devTools.addListener(Page.loadEventFired(), loadEvent -> {
                logger.info("📄 Page Load Event fired");
                performanceMetrics.put("pageLoadEvent", "fired");
            });
            
            // Listen for DOM content loaded
            devTools.addListener(Page.domContentEventFired(), domEvent -> {
                logger.info("🏗️  DOM Content Loaded event fired");
                performanceMetrics.put("domContentLoadedEvent", "fired");
            });
            
            pageMonitoringEnabled = true;
            logger.info("✅ Page monitoring enabled");
            
        } catch (Exception e) {
            logger.error("Failed to enable page monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Enable DOM domain monitoring
     */
    public void enableDOMMonitoring() {
        if (!isInitialized) return;
        
        try {
            devTools.send(DOM.enable(Optional.empty()));
            
            domMonitoringEnabled = true;
            logger.info("✅ DOM monitoring enabled");
            
        } catch (Exception e) {
            logger.error("Failed to enable DOM monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Get comprehensive DevTools summary
     */
    public String getDevToolsSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Chrome DevTools Protocol Summary ===\n");
        summary.append(String.format("DevTools Initialized: %s\n", isInitialized));
        summary.append(String.format("Network Monitoring: %s (Requests: %d)\n", networkMonitoringEnabled, networkRequestCount.get()));
        summary.append(String.format("Performance Monitoring: %s\n", performanceMonitoringEnabled));
        summary.append(String.format("Console Monitoring: %s (Logs: %d)\n", consoleMonitoringEnabled, consoleLogCount.get()));
        summary.append(String.format("Runtime Monitoring: %s (JS Errors: %d)\n", runtimeMonitoringEnabled, jsErrorCount.get()));
        summary.append(String.format("Security Monitoring: %s (Events: %d)\n", securityMonitoringEnabled, securityEventCount.get()));
        summary.append(String.format("Page Monitoring: %s\n", pageMonitoringEnabled));
        summary.append(String.format("DOM Monitoring: %s\n", domMonitoringEnabled));
        
        return summary.toString();
    }
    
    /**
     * Log all DevTools data
     */
    public void attachToAllureReport() {
        logger.info("DevTools Summary: {}", getDevToolsSummary());
        
        if (!networkRequests.isEmpty()) {
            logger.info("Network Requests: {}", String.join("\n", networkRequests));
        }
        
        if (!consoleLogs.isEmpty()) {
            logger.info("Console Logs: {}", String.join("\n", consoleLogs));
        }
        
        if (!jsErrors.isEmpty()) {
            logger.info("JavaScript Errors: {}", String.join("\n", jsErrors));
        }
        
        if (!securityEvents.isEmpty()) {
            logger.info("Security Events: {}", String.join("\n", securityEvents));
        }
        
        if (!performanceMetrics.isEmpty()) {
            StringBuilder perfData = new StringBuilder();
            performanceMetrics.forEach((key, value) -> 
                perfData.append(String.format("%s: %s\n", key, value)));
            logger.info("Performance Metrics: {}", perfData.toString());
        }
    }
    
    /**
     * Determine resource type from URL
     */
    private String determineResourceType(String url) {
        if (url == null) return "unknown";
        
        String lowerUrl = url.toLowerCase();
        if (lowerUrl.contains(".js")) return "script";
        if (lowerUrl.contains(".css")) return "stylesheet";
        if (lowerUrl.contains(".png") || lowerUrl.contains(".jpg") || lowerUrl.contains(".jpeg") || 
            lowerUrl.contains(".gif") || lowerUrl.contains(".svg") || lowerUrl.contains(".ico")) return "image";
        if (lowerUrl.contains(".woff") || lowerUrl.contains(".ttf") || lowerUrl.contains(".eot")) return "font";
        if (lowerUrl.contains(".mp4") || lowerUrl.contains(".webm") || lowerUrl.contains(".ogg")) return "media";
        if (lowerUrl.contains(".xml") || lowerUrl.contains(".json")) return "data";
        
        return "document";
    }
    
    /**
     * Get network request count
     */
    public int getNetworkRequestCount() {
        return networkRequestCount.get();
    }
    
    /**
     * Get console log count
     */
    public int getConsoleLogCount() {
        return consoleLogCount.get();
    }
    
    /**
     * Get JavaScript error count
     */
    public int getJavaScriptErrorCount() {
        return jsErrorCount.get();
    }
    
    /**
     * Check if DevTools is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }
    
    /**
     * Get detailed DevTools status for debugging
     */
    public String getDevToolsStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== DevTools Status ===\n");
        status.append(String.format("Initialized: %s\n", isInitialized));
        status.append(String.format("Driver Type: %s\n", driver.getClass().getSimpleName()));
        status.append(String.format("HasDevTools Interface: %s\n", driver instanceof HasDevTools));
        status.append(String.format("DevTools Instance: %s\n", devTools != null ? "Available" : "Null"));
        
        if (isInitialized) {
            status.append("Monitoring Status:\n");
            status.append(String.format("  Network: %s\n", networkMonitoringEnabled));
            status.append(String.format("  Performance: %s\n", performanceMonitoringEnabled));
            status.append(String.format("  Console: %s\n", consoleMonitoringEnabled));
            status.append(String.format("  Runtime: %s\n", runtimeMonitoringEnabled));
            status.append(String.format("  Security: %s\n", securityMonitoringEnabled));
            status.append(String.format("  Page: %s\n", pageMonitoringEnabled));
            status.append(String.format("  DOM: %s\n", domMonitoringEnabled));
        }
        
        return status.toString();
    }
    
    /**
     * Cleanup DevTools session
     */
    public void cleanup() {
        if (devTools != null) {
            try {
                devTools.close();
                logger.info("🔧 DevTools session closed");
            } catch (Exception e) {
                logger.error("Failed to close DevTools session: {}", e.getMessage());
            }
        }
    }
}
