package proje.com.saucedemo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Network traffic tracer for Selenium WebDriver
 * Captures and traces all network requests made by the browser using DevTools API
 * Automatically sends HTTP methods to Zipkin with method-specific service names
 */
public class NetworkTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(NetworkTracer.class);
    private final WebDriver driver;
    private final ZipkinTracer zipkinTracer;
    private DevTools devTools;
    private boolean devToolsEnabled = false;
    private int requestCount = 0;
    
    public NetworkTracer(WebDriver driver) {
        this.driver = driver;
        this.zipkinTracer = new ZipkinTracer();
    }
    
    /**
     * Enable network logging using DevTools API
     * Automatically captures all HTTP requests and sends them to Zipkin
     */
    public void enableNetworkLogging() {
        try {
            zipkinTracer.startSpan("enable-network-logging", "Enable DevTools network monitoring");
            
            // Check if DevTools is available (Chrome only)
            if (driver instanceof HasDevTools) {
                try {
                    logger.info("Chrome driver detected, initializing DevTools...");
                    devTools = ((HasDevTools) driver).getDevTools();
                    devTools.createSession();
                    logger.info("DevTools session created successfully");
                    
                    // Enable network monitoring
                    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                    logger.info("Network monitoring enabled");
                    
                    // Listen for network requests
                    devTools.addListener(Network.requestWillBeSent(), request -> {
                        try {
                            String method = request.getRequest().getMethod();
                            String url = request.getRequest().getUrl();
                            requestCount++;
                            
                            logger.info("HTTP Request #{}: {} {}", requestCount, method, url);
                            
                            // Create method-specific service name for Zipkin
                            String serviceName = "automation-exercise-" + method.toLowerCase();
                            ZipkinTracer methodTracer = new ZipkinTracer(serviceName);
                            
                            // Track the HTTP request in Zipkin
                            methodTracer.startSpan("http-request", method + " " + url);
                            methodTracer.trackElementInteraction("HTTP Request", method + " " + url, System.currentTimeMillis());
                            methodTracer.endSpan("http-request", true);
                            methodTracer.cleanup();
                            
                            logger.info("✅ Sent to Zipkin: {} {} with service: {} (Total: {})", method, url, serviceName, requestCount);
                        } catch (Exception e) {
                            logger.error("❌ Failed to process HTTP request: {}", e.getMessage());
                        }
                    });
                    
                    // Listen for network responses
                    devTools.addListener(Network.responseReceived(), response -> {
                        try {
                            String url = response.getResponse().getUrl();
                            int status = response.getResponse().getStatus();
                            
                            logger.info("HTTP Response: {} - Status: {}", url, status);
                            
                            // Track response in Zipkin
                            zipkinTracer.trackTestStep("HTTP Response", "Response received for: " + url + " (Status: " + status + ")", status >= 200 && status < 300, System.currentTimeMillis());
                        } catch (Exception e) {
                            logger.error("Failed to process HTTP response: {}", e.getMessage());
                        }
                    });
                    
                    devToolsEnabled = true;
                    logger.info("✅ DevTools network monitoring enabled successfully");
                    
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
            
            zipkinTracer.endSpan("enable-network-logging", true);
            
        } catch (Exception e) {
            logger.error("❌ Failed to enable network logging: {}", e.getMessage());
            zipkinTracer.endSpan("enable-network-logging", false);
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
                logger.info("DevTools session closed");
            } catch (Exception e) {
                logger.error("Failed to close DevTools session: {}", e.getMessage());
            }
        }
    }
} 