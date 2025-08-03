package proje.com.saucedemo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Level;

/**
 * Network traffic tracer for Selenium WebDriver
 * Captures and traces all network requests made by the browser
 */
public class NetworkTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(NetworkTracer.class);
    private final WebDriver driver;
    
    public NetworkTracer(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Enable network logging for Chrome
     */
    public void enableNetworkLogging() {
        try {
            ZipkinTracer.startSpan("enable-network-logging");
            ZipkinTracer.addTag("action", "enable_network_logging");
            
            // Enable performance logging
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            
            logger.info("Network logging enabled");
            ZipkinTracer.addTag("network_logging_enabled", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to enable network logging: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
        }
    }
    
    /**
     * Capture all network requests
     */
    public void captureNetworkRequests(String operation) {
        try {
            ZipkinTracer.startSpan("capture-network-requests");
            ZipkinTracer.addTag("action", "capture_network");
            ZipkinTracer.addTag("operation", operation);
            
            // Get performance logs
            List<LogEntry> performanceLogs = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
            
            for (LogEntry entry : performanceLogs) {
                String message = entry.getMessage();
                
                // Filter for network requests
                if (message.contains("Network.requestWillBeSent") || 
                    message.contains("Network.responseReceived") ||
                    message.contains("Network.loadingFinished")) {
                    
                    logger.info("Network request: {}", message);
                    ZipkinTracer.addTag("network_request", message);
                    
                    // Extract URL and method
                    if (message.contains("url")) {
                        String url = extractUrlFromLog(message);
                        String method = extractMethodFromLog(message);
                        
                        ZipkinTracer.addTag("request_url", url);
                        ZipkinTracer.addTag("request_method", method);
                        ZipkinTracer.addTag("request_timestamp", String.valueOf(entry.getTimestamp()));
                    }
                }
            }
            
            ZipkinTracer.addTag("requests_captured", String.valueOf(performanceLogs.size()));
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to capture network requests: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
        }
    }
    
    /**
     * Monitor specific network activity
     */
    public void monitorNetworkActivity(String description) {
        try {
            ZipkinTracer.startSpan("monitor-network-activity");
            ZipkinTracer.addTag("action", "monitor_network");
            ZipkinTracer.addTag("description", description);
            
            // Execute JavaScript to monitor network
            String script = """
                let requests = [];
                let originalFetch = window.fetch;
                let originalXHR = window.XMLHttpRequest;
                
                // Monitor fetch requests
                window.fetch = function(...args) {
                    let url = args[0];
                    let options = args[1] || {};
                    let method = options.method || 'GET';
                    
                    requests.push({
                        type: 'fetch',
                        url: url,
                        method: method,
                        timestamp: Date.now()
                    });
                    
                    console.log('Network Request:', {type: 'fetch', url, method});
                    return originalFetch.apply(this, args);
                };
                
                // Monitor XHR requests
                let originalOpen = XMLHttpRequest.prototype.open;
                XMLHttpRequest.prototype.open = function(method, url) {
                    requests.push({
                        type: 'xhr',
                        url: url,
                        method: method,
                        timestamp: Date.now()
                    });
                    
                    console.log('Network Request:', {type: 'xhr', url, method});
                    return originalOpen.apply(this, arguments);
                };
                
                // Return requests array
                return requests;
                """;
            
            Object result = ((JavascriptExecutor) driver).executeScript(script);
            
            ZipkinTracer.addTag("network_monitoring_enabled", "true");
            ZipkinTracer.addTag("monitoring_description", description);
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to monitor network activity: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
        }
    }
    
    /**
     * Get captured network requests
     */
    public List<String> getCapturedRequests() {
        try {
            ZipkinTracer.startSpan("get-captured-requests");
            ZipkinTracer.addTag("action", "get_requests");
            
            String script = "return window.requests || [];";
            @SuppressWarnings("unchecked")
            List<String> requests = (List<String>) ((JavascriptExecutor) driver).executeScript(script);
            
            logger.info("Captured {} network requests", requests.size());
            ZipkinTracer.addTag("requests_count", String.valueOf(requests.size()));
            ZipkinTracer.finishSpan();
            
            return requests;
            
        } catch (Exception e) {
            logger.error("Failed to get captured requests: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return List.of();
        }
    }
    
    /**
     * Clear captured requests
     */
    public void clearCapturedRequests() {
        try {
            ZipkinTracer.startSpan("clear-captured-requests");
            ZipkinTracer.addTag("action", "clear_requests");
            
            String script = "window.requests = [];";
            ((JavascriptExecutor) driver).executeScript(script);
            
            logger.info("Cleared captured network requests");
            ZipkinTracer.addTag("requests_cleared", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to clear captured requests: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
        }
    }
    
    /**
     * Extract URL from log message
     */
    private String extractUrlFromLog(String logMessage) {
        try {
            if (logMessage.contains("\"url\":")) {
                int start = logMessage.indexOf("\"url\":") + 7;
                int end = logMessage.indexOf("\"", start);
                return logMessage.substring(start, end);
            }
        } catch (Exception e) {
            logger.warn("Failed to extract URL from log: {}", e.getMessage());
        }
        return "unknown";
    }
    
    /**
     * Extract method from log message
     */
    private String extractMethodFromLog(String logMessage) {
        try {
            if (logMessage.contains("\"method\":")) {
                int start = logMessage.indexOf("\"method\":") + 10;
                int end = logMessage.indexOf("\"", start);
                return logMessage.substring(start, end);
            }
        } catch (Exception e) {
            logger.warn("Failed to extract method from log: {}", e.getMessage());
        }
        return "GET";
    }
    
    /**
     * Wait for network requests to complete
     */
    public void waitForNetworkIdle(int timeoutSeconds) {
        try {
            ZipkinTracer.startSpan("wait-for-network-idle");
            ZipkinTracer.addTag("action", "wait_network_idle");
            ZipkinTracer.addTag("timeout_seconds", String.valueOf(timeoutSeconds));
            
            String script = """
                return new Promise((resolve) => {
                    let lastRequestTime = Date.now();
                    let checkInterval = setInterval(() => {
                        if (Date.now() - lastRequestTime > 1000) {
                            clearInterval(checkInterval);
                            resolve(true);
                        }
                    }, 100);
                    
                    // Override fetch to track requests
                    let originalFetch = window.fetch;
                    window.fetch = function(...args) {
                        lastRequestTime = Date.now();
                        return originalFetch.apply(this, args);
                    };
                    
                    // Override XHR to track requests
                    let originalOpen = XMLHttpRequest.prototype.open;
                    XMLHttpRequest.prototype.open = function() {
                        lastRequestTime = Date.now();
                        return originalOpen.apply(this, arguments);
                    };
                });
                """;
            
            ((JavascriptExecutor) driver).executeAsyncScript(script);
            
            logger.info("Network is idle");
            ZipkinTracer.addTag("network_idle", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to wait for network idle: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
        }
    }
} 