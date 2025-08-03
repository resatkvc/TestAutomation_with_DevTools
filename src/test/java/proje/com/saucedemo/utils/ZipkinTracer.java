package proje.com.saucedemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Professional Zipkin Tracer utility for distributed tracing in test automation
 * Sends real spans to Zipkin server for monitoring and debugging
 */
public class ZipkinTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(ZipkinTracer.class);
    private static final String ZIPKIN_ENDPOINT = "http://localhost:9411/api/v2/spans";
    private static final String SERVICE_NAME = "saucedemo-test-automation";
    
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private static String currentTraceId;
    private static String currentSpanId;
    private static String currentOperationName;
    private static Instant spanStartTime;
    private static Map<String, String> currentTags = new ConcurrentHashMap<>();
    private static final Map<String, SpanInfo> activeSpans = new ConcurrentHashMap<>();
    
    /**
     * Initialize Zipkin tracing
     */
    public static void initializeTracing() {
        try {
            currentTraceId = generateTraceId();
            logger.info("Zipkin tracing initialized successfully with trace ID: {}", currentTraceId);
            
            // Test connection to Zipkin
            testZipkinConnection();
            
        } catch (Exception e) {
            logger.error("Failed to initialize Zipkin tracing: {}", e.getMessage());
        }
    }
    
    /**
     * Test connection to Zipkin server
     */
    private static void testZipkinConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:9411/health"))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                logger.info("Zipkin server is accessible and healthy");
            } else {
                logger.warn("Zipkin server responded with status: {}", response.statusCode());
            }
            
        } catch (Exception e) {
            logger.warn("Could not connect to Zipkin server: {}", e.getMessage());
        }
    }
    
    /**
     * Start a new span for test operation
     * @param operationName Name of the operation
     * @return Span info object
     */
    public static SpanInfo startSpan(String operationName) {
        try {
            currentSpanId = generateSpanId();
            currentOperationName = operationName;
            spanStartTime = Instant.now();
            currentTags.clear();
            
            SpanInfo spanInfo = new SpanInfo(currentSpanId, operationName, currentTraceId);
            activeSpans.put(currentSpanId, spanInfo);
            
            logger.info("Started span: {} with ID: {}", operationName, currentSpanId);
            
            return spanInfo;
            
        } catch (Exception e) {
            logger.error("Failed to start span: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Finish current span and send to Zipkin
     */
    public static void finishSpan() {
        try {
            if (currentSpanId != null && spanStartTime != null) {
                long duration = Instant.now().toEpochMilli() - spanStartTime.toEpochMilli();
                
                // Create Zipkin span format
                Map<String, Object> span = createZipkinSpan(currentSpanId, currentOperationName, duration);
                
                // Send to Zipkin
                sendSpanToZipkin(span);
                
                logger.info("Finished span: {} (Duration: {}ms)", currentOperationName, duration);
                logger.info("Span tags: {}", currentTags);
                
                // Clean up
                activeSpans.remove(currentSpanId);
                currentSpanId = null;
                currentOperationName = null;
                spanStartTime = null;
                currentTags.clear();
            }
        } catch (Exception e) {
            logger.error("Failed to finish span: {}", e.getMessage());
        }
    }
    
    /**
     * Create Zipkin span format
     */
    private static Map<String, Object> createZipkinSpan(String spanId, String operationName, long duration) {
        Map<String, Object> span = new HashMap<>();
        span.put("traceId", currentTraceId);
        span.put("id", spanId);
        span.put("name", operationName);
        span.put("timestamp", spanStartTime.toEpochMilli() * 1000); // Microseconds
        span.put("duration", duration * 1000); // Microseconds
        
        // Add local endpoint
        Map<String, Object> localEndpoint = new HashMap<>();
        localEndpoint.put("serviceName", SERVICE_NAME);
        span.put("localEndpoint", localEndpoint);
        
        // Add tags
        if (!currentTags.isEmpty()) {
            span.put("tags", new HashMap<>(currentTags));
        }
        
        return span;
    }
    
    /**
     * Send span to Zipkin server
     */
    private static void sendSpanToZipkin(Map<String, Object> span) {
        try {
            String spanJson = objectMapper.writeValueAsString(Collections.singletonList(span));
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ZIPKIN_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(spanJson))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 202) {
                logger.debug("Span sent to Zipkin successfully");
            } else {
                logger.warn("Failed to send span to Zipkin. Status: {}", response.statusCode());
            }
            
        } catch (Exception e) {
            logger.error("Failed to send span to Zipkin: {}", e.getMessage());
        }
    }
    
    /**
     * Add tag to current span
     * @param key Tag key
     * @param value Tag value
     */
    public static void addTag(String key, String value) {
        try {
            if (currentSpanId != null) {
                currentTags.put(key, value);
                logger.debug("Added tag: {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("Failed to add tag: {}", e.getMessage());
        }
    }
    
    /**
     * Add error to current span
     * @param error Error to add
     */
    public static void addError(Throwable error) {
        try {
            if (currentSpanId != null) {
                currentTags.put("error", error.getMessage());
                currentTags.put("error_type", error.getClass().getSimpleName());
                logger.error("Added error to span: {}", error.getMessage());
            }
        } catch (Exception e) {
            logger.error("Failed to add error to span: {}", e.getMessage());
        }
    }
    
    /**
     * Create child span
     * @param operationName Name of the child operation
     * @return Child span info
     */
    public static SpanInfo startChildSpan(String operationName) {
        try {
            String childSpanId = generateSpanId();
            String parentSpanId = currentSpanId;
            
            SpanInfo childSpan = new SpanInfo(childSpanId, operationName, currentTraceId, parentSpanId);
            activeSpans.put(childSpanId, childSpan);
            
            logger.info("Started child span: {} with ID: {} (parent: {})", operationName, childSpanId, parentSpanId);
            return childSpan;
            
        } catch (Exception e) {
            logger.error("Failed to start child span: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Get current span info
     * @return Current span info
     */
    public static SpanInfo getCurrentSpan() {
        if (currentSpanId != null) {
            return activeSpans.get(currentSpanId);
        }
        return null;
    }
    
    /**
     * Close tracing resources
     */
    public static void closeTracing() {
        try {
            // Finish any remaining spans
            for (SpanInfo span : activeSpans.values()) {
                logger.warn("Force finishing span: {}", span.getOperationName());
            }
            activeSpans.clear();
            
            logger.info("Zipkin tracing closed successfully");
            currentTraceId = null;
            currentSpanId = null;
            currentOperationName = null;
            spanStartTime = null;
            currentTags.clear();
            
        } catch (Exception e) {
            logger.error("Failed to close tracing: {}", e.getMessage());
        }
    }
    
    /**
     * Check if tracing is initialized
     * @return True if tracing is initialized
     */
    public static boolean isInitialized() {
        return currentTraceId != null;
    }
    
    /**
     * Get current trace ID
     * @return Current trace ID
     */
    public static String getCurrentTraceId() {
        return currentTraceId;
    }
    
    /**
     * Generate trace ID (16 characters hex)
     */
    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    /**
     * Generate span ID (16 characters hex)
     */
    private static String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    /**
     * Span info class
     */
    public static class SpanInfo {
        private final String spanId;
        private final String operationName;
        private final String traceId;
        private final String parentSpanId;
        
        public SpanInfo(String spanId, String operationName, String traceId) {
            this(spanId, operationName, traceId, null);
        }
        
        public SpanInfo(String spanId, String operationName, String traceId, String parentSpanId) {
            this.spanId = spanId;
            this.operationName = operationName;
            this.traceId = traceId;
            this.parentSpanId = parentSpanId;
        }
        
        public String getSpanId() {
            return spanId;
        }
        
        public String getOperationName() {
            return operationName;
        }
        
        public String getTraceId() {
            return traceId;
        }
        
        public String getParentSpanId() {
            return parentSpanId;
        }
        
        @Override
        public String toString() {
            return "SpanInfo{" +
                    "spanId='" + spanId + '\'' +
                    ", operationName='" + operationName + '\'' +
                    ", traceId='" + traceId + '\'' +
                    ", parentSpanId='" + parentSpanId + '\'' +
                    '}';
        }
    }
} 