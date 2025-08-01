package proje.com.saucedemo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simplified Zipkin Tracer utility for distributed tracing in test automation
 * This is a simplified version that logs trace information without complex dependencies
 */
public class ZipkinTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(ZipkinTracer.class);
    private static final String ZIPKIN_ENDPOINT = "http://localhost:9411/api/v2/spans";
    
    private static String currentTraceId;
    private static String currentSpanId;
    private static String currentOperationName;
    private static Instant spanStartTime;
    private static Map<String, String> currentTags = new HashMap<>();
    
    /**
     * Initialize Zipkin tracing
     */
    public static void initializeTracing() {
        try {
            currentTraceId = UUID.randomUUID().toString();
            logger.info("Zipkin tracing initialized successfully with trace ID: {}", currentTraceId);
            
        } catch (Exception e) {
            logger.error("Failed to initialize Zipkin tracing: {}", e.getMessage());
        }
    }
    
    /**
     * Start a new span for test operation
     * @param operationName Name of the operation
     * @return Span info object
     */
    public static SpanInfo startSpan(String operationName) {
        try {
            currentSpanId = UUID.randomUUID().toString();
            currentOperationName = operationName;
            spanStartTime = Instant.now();
            currentTags.clear();
            
            logger.info("Started span: {} with ID: {}", operationName, currentSpanId);
            
            return new SpanInfo(currentSpanId, operationName);
            
        } catch (Exception e) {
            logger.error("Failed to start span: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Finish current span
     */
    public static void finishSpan() {
        try {
            if (currentSpanId != null && spanStartTime != null) {
                long duration = Instant.now().toEpochMilli() - spanStartTime.toEpochMilli();
                
                logger.info("Finished span: {} (Duration: {}ms)", currentOperationName, duration);
                logger.info("Span tags: {}", currentTags);
                
                // Reset current span
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
            String childSpanId = UUID.randomUUID().toString();
            logger.info("Started child span: {} with ID: {}", operationName, childSpanId);
            return new SpanInfo(childSpanId, operationName);
            
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
            return new SpanInfo(currentSpanId, currentOperationName);
        }
        return null;
    }
    
    /**
     * Close tracing resources
     */
    public static void closeTracing() {
        try {
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
     * Span info class
     */
    public static class SpanInfo {
        private final String spanId;
        private final String operationName;
        
        public SpanInfo(String spanId, String operationName) {
            this.spanId = spanId;
            this.operationName = operationName;
        }
        
        public String getSpanId() {
            return spanId;
        }
        
        public String getOperationName() {
            return operationName;
        }
        
        @Override
        public String toString() {
            return "SpanInfo{" +
                    "spanId='" + spanId + '\'' +
                    ", operationName='" + operationName + '\'' +
                    '}';
        }
    }
} 