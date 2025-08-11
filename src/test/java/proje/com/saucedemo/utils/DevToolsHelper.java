package proje.com.saucedemo.utils;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v138.network.Network;
import org.openqa.selenium.devtools.v138.log.Log;
import org.openqa.selenium.devtools.v138.fetch.Fetch;
import org.openqa.selenium.devtools.v138.performance.Performance;
import org.openqa.selenium.devtools.v138.page.Page;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DevTools Helper - Chrome DevTools Protocol (CDP) entegrasyonu için yardımcı sınıf
 * Network monitoring, console logging, performance tracking ve request interception özellikleri
 * 
 * @author TestAutomation_with_DevTools
 * @version 1.0
 */
public class DevToolsHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(DevToolsHelper.class);
    
    private DevTools devTools;
    private WebDriver driver;
    private boolean isEnabled = false;
    
    // Network monitoring için sayaçlar
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicInteger responseCount = new AtomicInteger(0);
    private final ConcurrentHashMap<String, Long> requestTimings = new ConcurrentHashMap<>();
    
    /**
     * DevTools Helper constructor
     * @param driver WebDriver instance (ChromeDriver/EdgeDriver)
     */
    public DevToolsHelper(WebDriver driver) {
        this.driver = driver;
        initializeDevTools();
    }
    
    /**
     * DevTools oturumunu başlat
     */
    private void initializeDevTools() {
        try {
            if (driver instanceof HasDevTools) {
                this.devTools = ((HasDevTools) driver).getDevTools();
                this.devTools.createSession();
                this.isEnabled = true;
                logger.info("DevTools session created successfully");
            } else {
                logger.warn("Driver does not support DevTools (HasDevTools interface)");
                this.isEnabled = false;
            }
        } catch (Exception e) {
            logger.error("Failed to initialize DevTools: {}", e.getMessage(), e);
            this.isEnabled = false;
        }
    }
    
    /**
     * Network monitoring'i etkinleştir (tüm istekler)
     */
    public void enableNetworkMonitoring() {
        enableSelectiveNetworkMonitoring(null);
    }
    
    /**
     * Seçici network monitoring'i etkinleştir (sadece belirli URL'ler)
     * @param targetUrls İzlenecek URL'ler (null ise tümü izlenir)
     */
    public void enableSelectiveNetworkMonitoring(List<String> targetUrls) {
        if (!isEnabled || devTools == null) {
            logger.warn("DevTools not available for network monitoring");
            return;
        }
        
        try {
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
            
            // Request gönderimini dinle
            devTools.addListener(Network.requestWillBeSent(), request -> {
                String requestId = request.getRequestId().toString();
                String url = request.getRequest().getUrl();
                String method = request.getRequest().getMethod();
                
                // Sadece hedef URL'leri logla
                boolean shouldLog = targetUrls == null || targetUrls.isEmpty() || 
                                  targetUrls.stream().anyMatch(url::contains);
                
                requestTimings.put(requestId, System.currentTimeMillis());
                requestCount.incrementAndGet();
                
                if (shouldLog) {
                    logger.info("[CDP][Network] {} {} -> {}", method, requestId, url);
                }
            });
            
            // Response'ları dinle
            devTools.addListener(Network.responseReceived(), response -> {
                String requestId = response.getRequestId().toString();
                String url = response.getResponse().getUrl();
                int status = response.getResponse().getStatus();
                Long startTime = requestTimings.get(requestId);
                
                // Sadece hedef URL'leri logla
                boolean shouldLog = targetUrls == null || targetUrls.isEmpty() || 
                                  targetUrls.stream().anyMatch(url::contains);
                
                responseCount.incrementAndGet();
                
                if (shouldLog) {
                    if (startTime != null) {
                        long duration = System.currentTimeMillis() - startTime;
                        logger.info("[CDP][Network] Response {} {} -> {} ({}ms)", 
                                  status, requestId, url, duration);
                        requestTimings.remove(requestId);
                    } else {
                        logger.info("[CDP][Network] Response {} {} -> {}", 
                                  status, requestId, url);
                    }
                }
            });
            
            // Network hatalarını dinle
            devTools.addListener(Network.loadingFailed(), failure -> {
                String requestId = failure.getRequestId().toString();
                String url = failure.getRequestId().toString();
                String errorText = failure.getErrorText();
                
                // Sadece hedef URL'leri logla
                boolean shouldLog = targetUrls == null || targetUrls.isEmpty() || 
                                  targetUrls.stream().anyMatch(url::contains);
                
                if (shouldLog) {
                    logger.error("[CDP][Network] Failed {} -> {}: {}", requestId, url, errorText);
                }
            });
            
            if (targetUrls != null && !targetUrls.isEmpty()) {
                logger.info("Selective network monitoring enabled for: {}", targetUrls);
            } else {
                logger.info("Network monitoring enabled successfully (all requests)");
            }
            
        } catch (Exception e) {
            logger.error("Failed to enable network monitoring: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Console logging'i etkinleştir
     */
    public void enableConsoleLogging() {
        if (!isEnabled || devTools == null) {
            logger.warn("DevTools not available for console logging");
            return;
        }
        
        try {
            devTools.send(Log.enable());
            
            devTools.addListener(Log.entryAdded(), entry -> {
                String level = entry.getLevel().toString();
                String text = entry.getText();
                String source = entry.getSource().toString();
                
                switch (level) {
                    case "ERROR":
                        logger.error("[CDP][Console][{}] {}", source, text);
                        break;
                    case "WARNING":
                        logger.warn("[CDP][Console][{}] {}", source, text);
                        break;
                    case "INFO":
                        logger.info("[CDP][Console][{}] {}", source, text);
                        break;
                    default:
                        logger.debug("[CDP][Console][{}] {}: {}", level, source, text);
                }
            });
            
            logger.info("Console logging enabled successfully");
            
        } catch (Exception e) {
            logger.error("Failed to enable console logging: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Performance monitoring'i etkinleştir
     */
    public void enablePerformanceMonitoring() {
        if (!isEnabled || devTools == null) {
            logger.warn("DevTools not available for performance monitoring");
            return;
        }
        
        try {
            devTools.send(Performance.enable(Optional.empty()));
            
            logger.info("Performance monitoring enabled successfully");
            
        } catch (Exception e) {
            logger.error("Failed to enable performance monitoring: {}", e.getMessage(), e);
        }
    }
    
    /**
     * URL'leri blokla
     * @param urlsToBlock Bloklanacak URL pattern'leri
     */
    public void blockUrls(List<String> urlsToBlock) {
        if (!isEnabled || devTools == null) {
            logger.warn("DevTools not available for URL blocking");
            return;
        }
        
        try {
            devTools.send(Network.setBlockedURLs(urlsToBlock));
            logger.info("Blocked URLs via CDP: {}", urlsToBlock);
            
        } catch (Exception e) {
            logger.error("Failed to block URLs: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Fetch interception'i etkinleştir (request/response modifikasyonu için)
     */
    public void enableFetchInterception() {
        if (!isEnabled || devTools == null) {
            logger.warn("DevTools not available for fetch interception");
            return;
        }
        
        try {
            devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));
            
            devTools.addListener(Fetch.requestPaused(), event -> {
                String requestId = event.getRequestId().toString();
                String url = event.getRequest().getUrl();
                
                logger.info("[CDP][Fetch] Intercepted request {} -> {}", requestId, url);
                
                // Otomatik olarak devam ettir (modifikasyon yapmadan)
                devTools.send(Fetch.continueRequest(
                    event.getRequestId(), 
                    Optional.empty(), 
                    Optional.empty(), 
                    Optional.empty(), 
                    Optional.empty(), 
                    Optional.empty()
                ));
            });
            
            logger.info("Fetch interception enabled (auto-continue)");
            
        } catch (Exception e) {
            logger.error("Failed to enable fetch interception: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Sayfa yükleme olaylarını dinle
     */
    public void enablePageMonitoring() {
        if (!isEnabled || devTools == null) {
            logger.warn("DevTools not available for page monitoring");
            return;
        }
        
        try {
            devTools.send(Page.enable(Optional.empty()));
            
            devTools.addListener(Page.loadEventFired(), event -> {
                logger.info("[CDP][Page] Load event fired");
            });
            
            devTools.addListener(Page.domContentEventFired(), event -> {
                logger.info("[CDP][Page] DOM content loaded");
            });
            
            logger.info("Page monitoring enabled successfully");
            
        } catch (Exception e) {
            logger.error("Failed to enable page monitoring: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Tüm monitoring özelliklerini etkinleştir
     */
    public void enableAllMonitoring() {
        enableNetworkMonitoring();
        enableConsoleLogging();
        enablePerformanceMonitoring();
        enablePageMonitoring();
        logger.info("All DevTools monitoring features enabled");
    }
    
    /**
     * Network istatistiklerini al
     */
    public NetworkStats getNetworkStats() {
        return new NetworkStats(
            requestCount.get(),
            responseCount.get(),
            requestTimings.size()
        );
    }
    
    /**
     * DevTools oturumunu kapat
     */
    public void close() {
        if (devTools != null) {
            try {
                devTools.clearListeners();
                devTools.close();
                logger.info("DevTools session closed successfully");
            } catch (Exception e) {
                logger.warn("Error closing DevTools: {}", e.getMessage());
            }
        }
    }
    
    /**
     * DevTools etkin mi?
     */
    public boolean isEnabled() {
        return isEnabled;
    }
    
    /**
     * Network istatistikleri için inner class
     */
    public static class NetworkStats {
        private final int totalRequests;
        private final int totalResponses;
        private final int pendingRequests;
        
        public NetworkStats(int totalRequests, int totalResponses, int pendingRequests) {
            this.totalRequests = totalRequests;
            this.totalResponses = totalResponses;
            this.pendingRequests = pendingRequests;
        }
        
        public int getTotalRequests() { return totalRequests; }
        public int getTotalResponses() { return totalResponses; }
        public int getPendingRequests() { return pendingRequests; }
        
        @Override
        public String toString() {
            return String.format("NetworkStats{requests=%d, responses=%d, pending=%d}", 
                               totalRequests, totalResponses, pendingRequests);
        }
    }
}
