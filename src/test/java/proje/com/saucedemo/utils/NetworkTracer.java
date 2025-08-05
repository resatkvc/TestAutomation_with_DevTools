package proje.com.saucedemo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v120.network.Network;
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
                            requestCount++;
                            
                            logger.info("🌐 HTTP Request #{}: {} {}", requestCount, method, url);
                            
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
                            
                            String statusIcon = (status >= 200 && status < 300) ? "✅" : "❌";
                            logger.info("{} HTTP Response: {} - Status: {} ({})", statusIcon, url, status, statusText);
                            
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
                            
                            logger.error("❌ Network request failed: {} - Error: {}", url, errorText);
                            
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