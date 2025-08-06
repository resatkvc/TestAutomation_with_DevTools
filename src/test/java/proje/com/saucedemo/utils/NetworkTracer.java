package proje.com.saucedemo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v122.network.Network;
import proje.com.saucedemo.utils.MetricsExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * TestAutomation_with_DevTools - Network traffic tracer for Selenium WebDriver
 * Captures and traces all network requests made by the browser using DevTools API
 * Provides detailed logging of HTTP requests and responses
 */
public class NetworkTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(NetworkTracer.class);
    private final WebDriver driver;
    private DevTools devTools;
    private boolean devToolsEnabled = false;
    private int requestCount = 0;
    
    public NetworkTracer(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Enable network logging using DevTools API
     * Captures all HTTP requests and responses for detailed analysis
     */
    public void enableNetworkLogging() {
        try {
            logger.info("Enabling DevTools network monitoring for TestAutomation_with_DevTools...");
            
            // Check if DevTools is available (Chrome only)
            if (driver instanceof HasDevTools) {
                try {
                    logger.info("Chrome driver detected, initializing DevTools...");
                    
                    // Get DevTools instance
                    devTools = ((HasDevTools) driver).getDevTools();
                    
                    // Create DevTools session
                    devTools.createSession();
                    logger.info("DevTools session created successfully");
                    
                    // Wait a bit for session to be ready
                    Thread.sleep(1000);
                    
                    // Enable network monitoring
                    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                    logger.info("Network monitoring enabled");
                    
                    // Wait a bit for network monitoring to be ready
                    Thread.sleep(500);
                    
                    // Listen for network requests
                    devTools.addListener(Network.requestWillBeSent(), request -> {
                        try {
                            String method = request.getRequest().getMethod();
                            String url = request.getRequest().getUrl();
                            String resourceType = getResourceType(url);
                            requestCount++;
                            
                            logger.info("🌐 DevTools HTTP Request #{}: {} {} ({})", requestCount, method, url, resourceType);
                            
                            // Record metrics for HTTP request
                            String urlDomain = extractDomain(url);
                            MetricsExporter.recordHttpRequest(method, 0, urlDomain, resourceType, 0.0);
                            
                            // Log additional request details if available
                            if (request.getRequest().getHeaders() != null) {
                                logger.debug("Request headers: {}", request.getRequest().getHeaders());
                            }
                            
                        } catch (Exception e) {
                            logger.error("❌ Failed to process HTTP request: {}", e.getMessage());
                        }
                    });
                    
                    // Listen for network responses
                    devTools.addListener(Network.responseReceived(), response -> {
                        try {
                            String url = response.getResponse().getUrl();
                            int status = response.getResponse().getStatus();
                            String statusText = response.getResponse().getStatusText();
                            String resourceType = getResourceType(url);
                            
                            String statusIcon = (status >= 200 && status < 300) ? "✅" : "❌";
                            logger.info("{} DevTools HTTP Response: {} - Status: {} ({}) - Type: {}", 
                                      statusIcon, url, status, statusText, resourceType);
                            
                            // Record response metrics with actual HTTP method
                            String urlDomain = extractDomain(url);
                            // Don't record response separately - it's already recorded in request
                            // Just log the response for debugging
                            logger.debug("📊 DevTools Response: {} - Status: {} - Domain: {}", url, status, urlDomain);
                            
                            // Log response headers if available
                            if (response.getResponse().getHeaders() != null) {
                                logger.debug("Response headers: {}", response.getResponse().getHeaders());
                            }
                            
                        } catch (Exception e) {
                            logger.error("Failed to process HTTP response: {}", e.getMessage());
                        }
                    });
                    
                    // Listen for loading failed events
                    devTools.addListener(Network.loadingFailed(), failed -> {
                        try {
                            String url = failed.getRequestId().toString();
                            String errorText = failed.getErrorText();
                            
                            logger.error("❌ DevTools Network Error: {} - Error: {}", url, errorText);
                            
                            // Record network error metrics
                            String urlDomain = extractDomain(url);
                            MetricsExporter.recordNetworkError(errorText, urlDomain);
                            
                        } catch (Exception e) {
                            logger.error("Failed to process loading failed event: {}", e.getMessage());
                        }
                    });
                    
                    devToolsEnabled = true;
                    logger.info("✅ DevTools network monitoring enabled successfully for TestAutomation_with_DevTools");
                    
                } catch (Exception e) {
                    logger.error("❌ DevTools failed to initialize: {}", e.getMessage());
                    logger.error("Stack trace:", e);
                    devToolsEnabled = false;
                }
            } else {
                logger.warn("❌ DevTools not available (not Chrome driver)");
                logger.info("Driver type: {}", driver.getClass().getSimpleName());
                devToolsEnabled = false;
            }
            
        } catch (Exception e) {
            logger.error("❌ Failed to enable network logging: {}", e.getMessage());
        }
    }
    
    /**
     * Get request count for debugging
     */
    public int getRequestCount() {
        return requestCount;
    }
    
    /**
     * Check if DevTools is enabled
     */
    public boolean isDevToolsEnabled() {
        return devToolsEnabled;
    }
    
    /**
     * Extract domain from URL
     */
    private String extractDomain(String url) {
        try {
            if (url != null && !url.isEmpty()) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    String domain = url.replaceFirst("^https?://", "");
                    return domain.split("/")[0];
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to extract domain from URL: {}", url);
        }
        return "unknown";
    }
    
    /**
     * Get resource type from URL
     */
    private String getResourceType(String url) {
        try {
            if (url != null && !url.isEmpty()) {
                String lowerUrl = url.toLowerCase();
                if (lowerUrl.contains(".js")) return "script";
                if (lowerUrl.contains(".css")) return "stylesheet";
                if (lowerUrl.contains(".png") || lowerUrl.contains(".jpg") || lowerUrl.contains(".jpeg") || 
                    lowerUrl.contains(".gif") || lowerUrl.contains(".svg") || lowerUrl.contains(".ico")) return "image";
                if (lowerUrl.contains(".woff") || lowerUrl.contains(".ttf") || lowerUrl.contains(".eot")) return "font";
                if (lowerUrl.contains(".mp4") || lowerUrl.contains(".webm") || lowerUrl.contains(".ogg")) return "media";
                if (lowerUrl.contains(".xml") || lowerUrl.contains(".json")) return "data";
            }
        } catch (Exception e) {
            logger.debug("Failed to extract resource type from URL: {}", url);
        }
        return "document";
    }
    
    /**
     * Cleanup DevTools session
     */
    public void cleanup() {
        if (devTools != null) {
            try {
                devTools.close();
                logger.info("DevTools session closed for TestAutomation_with_DevTools");
            } catch (Exception e) {
                logger.error("Failed to close DevTools session: {}", e.getMessage());
            }
        }
    }
} 