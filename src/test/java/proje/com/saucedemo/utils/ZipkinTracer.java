package proje.com.saucedemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Zipkin Tracer for AutomationExercise Test Automation
 * Optimized for distributed tracing with proper categorization
 */
public class ZipkinTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(ZipkinTracer.class);
    private static final String ZIPKIN_BASE_URL = "http://localhost:9411";
    private static final String DEFAULT_SERVICE_NAME = "automation-exercise-test";
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String traceId;
    private final String spanId;
    private final Instant startTime;
    private final String serviceName;
    
    public ZipkinTracer() {
        this(DEFAULT_SERVICE_NAME);
    }
    
    public ZipkinTracer(String serviceName) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.traceId = generateTraceId();
        this.spanId = generateSpanId();
        this.startTime = Instant.now();
        this.serviceName = serviceName;
        
        // Set MDC for logging correlation
        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);
        MDC.put("serviceName", this.serviceName);
        
        logger.info("ZipkinTracer initialized with traceId: {}, spanId: {}, serviceName: {}", traceId, spanId, this.serviceName);
    }
    
    /**
     * Start a new trace span
     */
    public void startSpan(String operationName, String description) {
        try {
            ZipkinSpan span = ZipkinSpan.builder()
                    .traceId(traceId)
                    .id(spanId)
                    .name(operationName)
                    .timestamp(startTime.toEpochMilli() * 1000) // Convert to microseconds
                    .duration(0)
                    .localEndpoint(ZipkinEndpoint.builder()
                            .serviceName(serviceName)
                            .ipv4("127.0.0.1")
                            .port(8080)
                            .build())
                    .tags(java.util.Map.of(
                            "description", description,
                            "operation", operationName,
                            "test.type", "ui.automation",
                            "target.site", "automationexercise.com"
                    ))
                    .build();
            
            sendSpan(span);
            logger.info("Started span: {} - {}", operationName, description);
            
        } catch (Exception e) {
            logger.error("Failed to start span: {}", e.getMessage());
        }
    }
    
    /**
     * End current span
     */
    public void endSpan(String operationName, boolean success) {
        try {
            Instant endTime = Instant.now();
            long duration = (endTime.toEpochMilli() - startTime.toEpochMilli()) * 1000; // Convert to microseconds
            
            ZipkinSpan span = ZipkinSpan.builder()
                    .traceId(traceId)
                    .id(spanId)
                    .name(operationName)
                    .timestamp(startTime.toEpochMilli() * 1000)
                    .duration(duration)
                    .localEndpoint(ZipkinEndpoint.builder()
                            .serviceName(serviceName)
                            .ipv4("127.0.0.1")
                            .port(8080)
                            .build())
                    .tags(java.util.Map.of(
                            "status", success ? "success" : "failed",
                            "duration.ms", String.valueOf(duration / 1000),
                            "test.result", success ? "PASS" : "FAIL"
                    ))
                    .build();
            
            sendSpan(span);
            logger.info("Ended span: {} - Duration: {}ms, Success: {}", operationName, duration / 1000, success);
            
        } catch (Exception e) {
            logger.error("Failed to end span: {}", e.getMessage());
        }
    }
    
    /**
     * Add child span for specific test steps
     */
    public void addChildSpan(String parentSpanId, String operationName, String description, long duration) {
        try {
            String childSpanId = generateSpanId();
            
            ZipkinSpan span = ZipkinSpan.builder()
                    .traceId(traceId)
                    .id(childSpanId)
                    .parentId(parentSpanId)
                    .name(operationName)
                    .timestamp(Instant.now().toEpochMilli() * 1000)
                    .duration(duration * 1000) // Convert to microseconds
                    .localEndpoint(ZipkinEndpoint.builder()
                            .serviceName(serviceName)
                            .ipv4("127.0.0.1")
                            .port(8080)
                            .build())
                    .tags(java.util.Map.of(
                            "description", description,
                            "step.type", "test.step",
                            "target.element", operationName
                    ))
                    .build();
            
            sendSpan(span);
            logger.info("Added child span: {} - {}", operationName, description);
            
        } catch (Exception e) {
            logger.error("Failed to add child span: {}", e.getMessage());
        }
    }
    
    /**
     * Track page navigation
     */
    public void trackPageNavigation(String pageName, String url, long loadTime) {
        try {
            String spanId = generateSpanId();
            
            ZipkinSpan span = ZipkinSpan.builder()
                    .traceId(traceId)
                    .id(spanId)
                    .name("page.navigation")
                    .timestamp(Instant.now().toEpochMilli() * 1000)
                    .duration(loadTime * 1000)
                    .localEndpoint(ZipkinEndpoint.builder()
                            .serviceName(serviceName)
                            .ipv4("127.0.0.1")
                            .port(8080)
                            .build())
                    .tags(java.util.Map.of(
                            "page.name", pageName,
                            "page.url", url,
                            "load.time.ms", String.valueOf(loadTime),
                            "operation.type", "navigation"
                    ))
                    .build();
            
            sendSpan(span);
            logger.info("Tracked page navigation: {} - {} ({}ms)", pageName, url, loadTime);
            
        } catch (Exception e) {
            logger.error("Failed to track page navigation: {}", e.getMessage());
        }
    }
    
    /**
     * Track element interaction
     */
    public void trackElementInteraction(String elementName, String action, long duration) {
        try {
            String spanId = generateSpanId();
            
            ZipkinSpan span = ZipkinSpan.builder()
                    .traceId(traceId)
                    .id(spanId)
                    .name("element.interaction")
                    .timestamp(Instant.now().toEpochMilli() * 1000)
                    .duration(duration * 1000)
                    .localEndpoint(ZipkinEndpoint.builder()
                            .serviceName(serviceName)
                            .ipv4("127.0.0.1")
                            .port(8080)
                            .build())
                    .tags(java.util.Map.of(
                            "element.name", elementName,
                            "action.type", action,
                            "duration.ms", String.valueOf(duration),
                            "operation.type", "interaction"
                    ))
                    .build();
            
            sendSpan(span);
            logger.info("Tracked element interaction: {} - {} ({}ms)", elementName, action, duration);
            
        } catch (Exception e) {
            logger.error("Failed to track element interaction: {}", e.getMessage());
        }
    }
    
    /**
     * Track test step completion
     */
    public void trackTestStep(String stepName, String description, boolean success, long duration) {
        try {
            String spanId = generateSpanId();
            
            ZipkinSpan span = ZipkinSpan.builder()
                    .traceId(traceId)
                    .id(spanId)
                    .name("test.step")
                    .timestamp(Instant.now().toEpochMilli() * 1000)
                    .duration(duration * 1000)
                    .localEndpoint(ZipkinEndpoint.builder()
                            .serviceName(serviceName)
                            .ipv4("127.0.0.1")
                            .port(8080)
                            .build())
                    .tags(java.util.Map.of(
                            "step.name", stepName,
                            "step.description", description,
                            "step.status", success ? "PASS" : "FAIL",
                            "duration.ms", String.valueOf(duration),
                            "operation.type", "test.step"
                    ))
                    .build();
            
            sendSpan(span);
            logger.info("Tracked test step: {} - {} ({}ms, {})", stepName, description, duration, success ? "PASS" : "FAIL");
            
        } catch (Exception e) {
            logger.error("Failed to track test step: {}", e.getMessage());
        }
    }
    
    /**
     * Send span to Zipkin
     */
    private void sendSpan(ZipkinSpan span) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(java.util.List.of(span));
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ZIPKIN_BASE_URL + "/api/v2/spans"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 202) {
                logger.debug("Span sent successfully to Zipkin");
            } else {
                logger.warn("Failed to send span to Zipkin. Status: {}", response.statusCode());
            }
            
        } catch (IOException | InterruptedException e) {
            logger.error("Error sending span to Zipkin: {}", e.getMessage());
        }
    }
    
    /**
     * Generate unique trace ID
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    /**
     * Generate unique span ID
     */
    private String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    /**
     * Get current trace ID
     */
    public String getTraceId() {
        return traceId;
    }
    
    /**
     * Get current span ID
     */
    public String getSpanId() {
        return spanId;
    }
    
    /**
     * Cleanup MDC
     */
    public void cleanup() {
        MDC.clear();
        logger.info("ZipkinTracer cleanup completed");
    }
    
    // Zipkin Span Data Classes
    public static class ZipkinSpan {
        private String traceId;
        private String id;
        private String parentId;
        private String name;
        private long timestamp;
        private long duration;
        private ZipkinEndpoint localEndpoint;
        private java.util.Map<String, String> tags;
        
        // Builder pattern
        public static ZipkinSpanBuilder builder() {
            return new ZipkinSpanBuilder();
        }
        
        // Getters and setters
        public String getTraceId() { return traceId; }
        public void setTraceId(String traceId) { this.traceId = traceId; }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getParentId() { return parentId; }
        public void setParentId(String parentId) { this.parentId = parentId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        
        public ZipkinEndpoint getLocalEndpoint() { return localEndpoint; }
        public void setLocalEndpoint(ZipkinEndpoint localEndpoint) { this.localEndpoint = localEndpoint; }
        
        public java.util.Map<String, String> getTags() { return tags; }
        public void setTags(java.util.Map<String, String> tags) { this.tags = tags; }
        
        public static class ZipkinSpanBuilder {
            private ZipkinSpan span = new ZipkinSpan();
            
            public ZipkinSpanBuilder traceId(String traceId) {
                span.traceId = traceId;
                return this;
            }
            
            public ZipkinSpanBuilder id(String id) {
                span.id = id;
                return this;
            }
            
            public ZipkinSpanBuilder parentId(String parentId) {
                span.parentId = parentId;
                return this;
            }
            
            public ZipkinSpanBuilder name(String name) {
                span.name = name;
                return this;
            }
            
            public ZipkinSpanBuilder timestamp(long timestamp) {
                span.timestamp = timestamp;
                return this;
            }
            
            public ZipkinSpanBuilder duration(long duration) {
                span.duration = duration;
                return this;
            }
            
            public ZipkinSpanBuilder localEndpoint(ZipkinEndpoint localEndpoint) {
                span.localEndpoint = localEndpoint;
                return this;
            }
            
            public ZipkinSpanBuilder tags(java.util.Map<String, String> tags) {
                span.tags = tags;
                return this;
            }
            
            public ZipkinSpan build() {
                return span;
            }
        }
    }
    
    public static class ZipkinEndpoint {
        private String serviceName;
        private String ipv4;
        private int port;
        
        // Builder pattern
        public static ZipkinEndpointBuilder builder() {
            return new ZipkinEndpointBuilder();
        }
        
        // Getters and setters
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        
        public String getIpv4() { return ipv4; }
        public void setIpv4(String ipv4) { this.ipv4 = ipv4; }
        
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        
        public static class ZipkinEndpointBuilder {
            private ZipkinEndpoint endpoint = new ZipkinEndpoint();
            
            public ZipkinEndpointBuilder serviceName(String serviceName) {
                endpoint.serviceName = serviceName;
                return this;
            }
            
            public ZipkinEndpointBuilder ipv4(String ipv4) {
                endpoint.ipv4 = ipv4;
                return this;
            }
            
            public ZipkinEndpointBuilder port(int port) {
                endpoint.port = port;
                return this;
            }
            
            public ZipkinEndpoint build() {
                return endpoint;
            }
        }
    }
} 