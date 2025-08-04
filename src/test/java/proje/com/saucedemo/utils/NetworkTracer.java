package proje.com.saucedemo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Network traffic tracer for Selenium WebDriver
 * Captures and traces all network requests made by the browser
 * Uses DevTools API for real-time HTTP method detection
 */
public class NetworkTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(NetworkTracer.class);
    private final WebDriver driver;
    private final ZipkinTracer zipkinTracer;
    private DevTools devTools;
    private boolean devToolsEnabled = false;
    
    public NetworkTracer(WebDriver driver) {
        this.driver = driver;
        this.zipkinTracer = new ZipkinTracer();
    }
    
    /**
     * Enable network logging for Chrome with DevTools
     */
    public void enableNetworkLogging() {
        try {
            zipkinTracer.startSpan("enable-network-logging", "Enable network logging with DevTools");
            
            // Always enable JavaScript monitoring as primary method
            enableJavaScriptMonitoring();
            
            // Try DevTools as secondary method
            if (driver instanceof HasDevTools) {
                try {
                    devTools = ((HasDevTools) driver).getDevTools();
                    devTools.createSession();
                    
                    // Enable network monitoring
                    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                    
                    // Listen for network requests
                    devTools.addListener(Network.requestWillBeSent(), request -> {
                        try {
                            String method = request.getRequest().getMethod();
                            String url = request.getRequest().getUrl();
                            
                            logger.info("HTTP Request (DevTools): {} {}", method, url);
                            
                            // Create method-specific service name for Zipkin
                            String serviceName = "automation-exercise-" + method.toLowerCase();
                            ZipkinTracer methodTracer = new ZipkinTracer(serviceName);
                            
                            // Track the HTTP request in Zipkin
                            methodTracer.startSpan("http-request", method + " " + url);
                            methodTracer.trackElementInteraction("HTTP Request", method + " " + url, System.currentTimeMillis());
                            methodTracer.endSpan("http-request", true);
                            methodTracer.cleanup();
                            
                            logger.info("Sent HTTP request to Zipkin: {} {} with service: {}", method, url, serviceName);
                        } catch (Exception e) {
                            logger.error("Failed to process HTTP request: {}", e.getMessage());
                        }
                    });
                    
                    // Listen for network responses
                    devTools.addListener(Network.responseReceived(), response -> {
                        try {
                            String url = response.getResponse().getUrl();
                            int status = response.getResponse().getStatus();
                            
                            logger.info("HTTP Response (DevTools): {} - Status: {}", url, status);
                            
                            // Track response in Zipkin
                            zipkinTracer.trackTestStep("HTTP Response", "Response received for: " + url + " (Status: " + status + ")", status >= 200 && status < 300, System.currentTimeMillis());
                        } catch (Exception e) {
                            logger.error("Failed to process HTTP response: {}", e.getMessage());
                        }
                    });
                    
                    devToolsEnabled = true;
                    logger.info("DevTools network monitoring enabled successfully");
                    
                } catch (Exception e) {
                    logger.warn("DevTools failed to initialize: {}, using JavaScript monitoring only", e.getMessage());
                    devToolsEnabled = false;
                }
            } else {
                logger.info("DevTools not available (not Chrome driver), using JavaScript monitoring only");
                devToolsEnabled = false;
            }
            
            zipkinTracer.endSpan("enable-network-logging", true);
            
        } catch (Exception e) {
            logger.error("Failed to enable network logging: {}", e.getMessage());
            zipkinTracer.endSpan("enable-network-logging", false);
        }
    }
    
    /**
     * Enable JavaScript-based network monitoring as primary method
     */
    private void enableJavaScriptMonitoring() {
        try {
            logger.info("Enabling JavaScript-based network monitoring");
            
            String script = """
                // Store original functions
                window._originalFetch = window.fetch;
                window._originalXHR = window.XMLHttpRequest;
                window._networkRequests = [];
                
                // Override fetch
                window.fetch = function(...args) {
                    const url = args[0];
                    const options = args[1] || {};
                    const method = options.method || 'GET';
                    
                    const request = {
                        type: 'fetch',
                        url: url,
                        method: method,
                        timestamp: Date.now()
                    };
                    
                    window._networkRequests.push(request);
                    console.log('Network Request (JS):', request);
                    
                    return window._originalFetch.apply(this, args);
                };
                
                // Override XMLHttpRequest
                const OriginalXHR = window.XMLHttpRequest;
                window.XMLHttpRequest = function() {
                    const xhr = new OriginalXHR();
                    const originalOpen = xhr.open;
                    
                    xhr.open = function(method, url) {
                        const request = {
                            type: 'xhr',
                            url: url,
                            method: method,
                            timestamp: Date.now()
                        };
                        
                        window._networkRequests.push(request);
                        console.log('Network Request (JS):', request);
                        
                        return originalOpen.apply(this, arguments);
                        };
                    
                    return xhr;
                };
                
                // Monitor page loads and navigation
                const originalPushState = history.pushState;
                const originalReplaceState = history.replaceState;
                
                history.pushState = function(...args) {
                    const request = {
                        type: 'navigation',
                        url: args[2] || window.location.href,
                        method: 'GET',
                        timestamp: Date.now()
                    };
                    
                    window._networkRequests.push(request);
                    console.log('Navigation Request (JS):', request);
                    
                    return originalPushState.apply(this, args);
                };
                
                history.replaceState = function(...args) {
                    const request = {
                        type: 'navigation',
                        url: args[2] || window.location.href,
                        method: 'GET',
                        timestamp: Date.now()
                    };
                    
                    window._networkRequests.push(request);
                    console.log('Navigation Request (JS):', request);
                    
                    return originalReplaceState.apply(this, args);
                };
                
                // Monitor form submissions
                document.addEventListener('submit', function(event) {
                    const form = event.target;
                    const method = form.method.toUpperCase();
                    const url = form.action || window.location.href;
                    
                    const request = {
                        type: 'form',
                        url: url,
                        method: method,
                        timestamp: Date.now()
                    };
                    
                    window._networkRequests.push(request);
                    console.log('Form Request (JS):', request);
                });
                
                // Monitor link clicks
                document.addEventListener('click', function(event) {
                    if (event.target.tagName === 'A') {
                        const link = event.target;
                        const url = link.href;
                        
                        if (url && !url.startsWith('javascript:')) {
                            const request = {
                                type: 'link',
                                url: url,
                                method: 'GET',
                                timestamp: Date.now()
                            };
                            
                            window._networkRequests.push(request);
                            console.log('Link Request (JS):', request);
                        }
                    }
                });
                
                console.log('JavaScript network monitoring enabled');
                return true;
                """;
            
            Object result = ((JavascriptExecutor) driver).executeScript(script);
            logger.info("JavaScript network monitoring enabled successfully");
            
        } catch (Exception e) {
            logger.error("Failed to enable JavaScript monitoring: {}", e.getMessage());
        }
    }
    
    /**
     * Capture all network requests
     */
    public void captureNetworkRequests(String operation) {
        try {
            zipkinTracer.startSpan("capture-network-requests", "Capture network requests for operation: " + operation);
            
            // Always use JavaScript monitoring as it's more reliable
            captureNetworkRequestsWithJavaScript(operation);
            
            zipkinTracer.endSpan("capture-network-requests", true);
            
        } catch (Exception e) {
            logger.error("Failed to capture network requests: {}", e.getMessage());
            zipkinTracer.trackTestStep("Network Capture", "Failed to capture network requests", false, System.currentTimeMillis());
            zipkinTracer.endSpan("capture-network-requests", false);
        }
    }
    
    /**
     * Monitor specific network activity
     */
    public void monitorNetworkActivity(String description) {
        try {
            zipkinTracer.startSpan("monitor-network-activity", "Monitor network activity: " + description);
            
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
            
            zipkinTracer.trackTestStep("Network Monitoring", "Network monitoring enabled for: " + description, true, System.currentTimeMillis());
            zipkinTracer.endSpan("monitor-network-activity", true);
            
        } catch (Exception e) {
            logger.error("Failed to monitor network activity: {}", e.getMessage());
            zipkinTracer.trackTestStep("Network Monitoring", "Failed to monitor network activity", false, System.currentTimeMillis());
            zipkinTracer.endSpan("monitor-network-activity", false);
        }
    }
    
    /**
     * Capture network requests using JavaScript with method-specific service names
     */
    private void captureNetworkRequestsWithJavaScript(String operation) {
        try {
            String script = """
                // Get all captured requests
                const requests = window._networkRequests || [];
                console.log('Captured requests:', requests);
                return requests;
                """;
            
            @SuppressWarnings("unchecked")
            List<Object> requests = (List<Object>) ((JavascriptExecutor) driver).executeScript(script);
            
            logger.info("Captured {} network requests via JavaScript", requests.size());
            
            // Process each request with method-specific service name
            for (Object request : requests) {
                if (request instanceof java.util.Map) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> requestMap = (java.util.Map<String, Object>) request;
                    
                    String method = (String) requestMap.get("method");
                    String url = (String) requestMap.get("url");
                    String type = (String) requestMap.get("type");
                    
                    if (method != null && url != null) {
                        // Create method-specific service name
                        String serviceName = "automation-exercise-" + method.toLowerCase();
                        
                        // Create new ZipkinTracer with method-specific service name
                        ZipkinTracer methodTracer = new ZipkinTracer(serviceName);
                        methodTracer.trackElementInteraction("Network Request", method + " " + url + " (" + type + ")", System.currentTimeMillis());
                        methodTracer.cleanup();
                        
                        logger.info("Sent to Zipkin: {} request to {} with service name: {} (type: {})", method, url, serviceName, type);
                    }
                }
            }
            
            // Also track the current page navigation as a GET request
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl != null && !currentUrl.isEmpty()) {
                String serviceName = "automation-exercise-get";
                ZipkinTracer methodTracer = new ZipkinTracer(serviceName);
                methodTracer.trackElementInteraction("Page Navigation", "GET " + currentUrl + " (navigation)", System.currentTimeMillis());
                methodTracer.cleanup();
                
                logger.info("Sent page navigation to Zipkin: GET {} with service name: {}", currentUrl, serviceName);
            }
            
            // Clear captured requests for next operation
            ((JavascriptExecutor) driver).executeScript("window._networkRequests = [];");
            
        } catch (Exception e) {
            logger.error("Failed to capture network requests with JavaScript: {}", e.getMessage());
        }
    }
    
    /**
     * Get captured network requests
     */
    public List<String> getCapturedRequests() {
        try {
            zipkinTracer.startSpan("get-captured-requests", "Get captured network requests");
            
            String script = "return window._networkRequests || [];";
            @SuppressWarnings("unchecked")
            List<String> requests = (List<String>) ((JavascriptExecutor) driver).executeScript(script);
            
            logger.info("Captured {} network requests", requests.size());
            zipkinTracer.trackTestStep("Get Requests", "Retrieved " + requests.size() + " captured requests", true, System.currentTimeMillis());
            zipkinTracer.endSpan("get-captured-requests", true);
            
            return requests;
            
        } catch (Exception e) {
            logger.error("Failed to get captured requests: {}", e.getMessage());
            zipkinTracer.trackTestStep("Get Requests", "Failed to get captured requests", false, System.currentTimeMillis());
            zipkinTracer.endSpan("get-captured-requests", false);
            return List.of();
        }
    }
    
    /**
     * Clear captured requests
     */
    public void clearCapturedRequests() {
        try {
            zipkinTracer.startSpan("clear-captured-requests", "Clear captured network requests");
            
            String script = "window._networkRequests = [];";
            ((JavascriptExecutor) driver).executeScript(script);
            
            logger.info("Cleared captured network requests");
            zipkinTracer.trackTestStep("Clear Requests", "Successfully cleared captured requests", true, System.currentTimeMillis());
            zipkinTracer.endSpan("clear-captured-requests", true);
            
        } catch (Exception e) {
            logger.error("Failed to clear captured requests: {}", e.getMessage());
            zipkinTracer.trackTestStep("Clear Requests", "Failed to clear captured requests", false, System.currentTimeMillis());
            zipkinTracer.endSpan("clear-captured-requests", false);
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
            zipkinTracer.startSpan("wait-for-network-idle", "Wait for network to become idle");
            
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
            zipkinTracer.trackTestStep("Network Idle", "Network became idle after waiting", true, System.currentTimeMillis());
            zipkinTracer.endSpan("wait-for-network-idle", true);
            
        } catch (Exception e) {
            logger.error("Failed to wait for network idle: {}", e.getMessage());
            zipkinTracer.trackTestStep("Network Idle", "Failed to wait for network idle", false, System.currentTimeMillis());
            zipkinTracer.endSpan("wait-for-network-idle", false);
        }
    }
    
    /**
     * Cleanup DevTools session
     */
    public void cleanup() {
        if (devTools != null) {
            try {
                devTools.close();
                logger.info("DevTools session closed");
            } catch (Exception e) {
                logger.error("Failed to close DevTools session: {}", e.getMessage());
            }
        }
    }
} 