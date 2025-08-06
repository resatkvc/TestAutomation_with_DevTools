package proje.com.saucedemo.utils;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.PushGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Selenium DevTools Network Metrics Exporter
 * Collects network data from DevTools and sends to Prometheus PushGateway
 * For visualization in Grafana
 */
public class MetricsExporter {
    
    private static final Logger logger = LoggerFactory.getLogger(MetricsExporter.class);
    
    // Prometheus Registry
    private static final CollectorRegistry registry = new CollectorRegistry();
    
    // PushGateway Configuration
    private static final String PUSHGATEWAY_URL = "http://localhost:9091";
    private static final String JOB_NAME = "selenium-devtools";
    
    // Network Metrics
    private static final Counter httpRequestCounter = Counter.build()
            .name("devtools_http_requests_total")
            .help("Total HTTP requests captured by DevTools")
            .labelNames("method", "status_code", "domain", "resource_type")
            .register(registry);
    
    private static final Histogram httpRequestDurationHistogram = Histogram.build()
            .name("devtools_http_request_duration_seconds")
            .help("HTTP request duration from DevTools")
            .labelNames("method", "domain", "resource_type")
            .buckets(0.1, 0.5, 1, 2, 5, 10, 30)
            .register(registry);
    
    private static final Counter networkErrorCounter = Counter.build()
            .name("devtools_network_errors_total")
            .help("Network errors captured by DevTools")
            .labelNames("error_type", "domain")
            .register(registry);
    
    private static final Counter resourceLoadCounter = Counter.build()
            .name("devtools_resource_loads_total")
            .help("Resource loads (images, scripts, stylesheets)")
            .labelNames("resource_type", "domain")
            .register(registry);
    
    // PushGateway Instance
    private static PushGateway pushGateway;
    
    static {
        try {
            pushGateway = new PushGateway(PUSHGATEWAY_URL);
            logger.info("✅ DevTools MetricsExporter initialized with PushGateway: {}", PUSHGATEWAY_URL);
        } catch (Exception e) {
            logger.error("❌ Failed to initialize PushGateway: {}", e.getMessage());
            pushGateway = null;
        }
    }
    
    /**
     * Record HTTP request from DevTools
     */
    public static void recordHttpRequest(String method, int statusCode, String domain, String resourceType, double durationSeconds) {
        try {
            httpRequestCounter.labels(method, String.valueOf(statusCode), domain, resourceType).inc();
            httpRequestDurationHistogram.labels(method, domain, resourceType).observe(durationSeconds);
            
            logger.debug("📊 DevTools HTTP: {} {} ({}) - Status: {} - Duration: {}s", 
                       method, domain, resourceType, statusCode, durationSeconds);
        } catch (Exception e) {
            logger.error("Failed to record HTTP request: {}", e.getMessage());
        }
    }
    
    /**
     * Record network error from DevTools
     */
    public static void recordNetworkError(String errorType, String domain) {
        try {
            networkErrorCounter.labels(errorType, domain).inc();
            
            logger.debug("📊 DevTools Network Error: {} - Domain: {}", errorType, domain);
        } catch (Exception e) {
            logger.error("Failed to record network error: {}", e.getMessage());
        }
    }
    
    /**
     * Record resource load from DevTools
     */
    public static void recordResourceLoad(String resourceType, String domain) {
        try {
            resourceLoadCounter.labels(resourceType, domain).inc();
            
            logger.debug("📊 DevTools Resource Load: {} - Domain: {}", resourceType, domain);
        } catch (Exception e) {
            logger.error("Failed to record resource load: {}", e.getMessage());
        }
    }
    
    /**
     * Push metrics to PushGateway
     */
    public static void pushMetrics() {
        if (pushGateway == null) {
            logger.warn("PushGateway not initialized, skipping metrics push");
            return;
        }
        
        try {
            pushGateway.pushAdd(registry, JOB_NAME);
            logger.debug("📊 DevTools metrics pushed to PushGateway successfully");
        } catch (IOException e) {
            logger.error("Failed to push metrics to PushGateway: {}", e.getMessage());
        }
    }
    
    /**
     * Push metrics with custom labels
     */
    public static void pushMetricsWithLabels(String testName, String browser) {
        if (pushGateway == null) {
            logger.warn("PushGateway not initialized, skipping metrics push");
            return;
        }
        
        try {
            // Create labels map
            java.util.Map<String, String> labels = new java.util.HashMap<>();
            labels.put("test_name", testName);
            labels.put("browser", browser);
            
            pushGateway.pushAdd(registry, JOB_NAME, labels);
            logger.debug("📊 DevTools metrics pushed with labels: test_name={}, browser={}", testName, browser);
        } catch (IOException e) {
            logger.error("Failed to push metrics to PushGateway: {}", e.getMessage());
        }
    }
    
    /**
     * Cleanup resources
     */
    public static void cleanup() {
        try {
            if (pushGateway != null) {
                pushGateway.delete(JOB_NAME);
                logger.info("📊 DevTools metrics cleanup completed");
            }
        } catch (IOException e) {
            logger.error("Failed to cleanup metrics: {}", e.getMessage());
        }
    }
} 