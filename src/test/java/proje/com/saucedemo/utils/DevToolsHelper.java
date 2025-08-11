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
                
                // Sadece API çağrıları ve form gönderimlerini logla
                boolean shouldLog = isRelevantRequest(url, method, targetUrls);
                
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
                
                // Sadece API çağrıları ve form gönderimlerini logla
                boolean shouldLog = isRelevantRequest(url, "GET", targetUrls);
                
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
                
                // Sadece API çağrıları ve form gönderimlerini logla
                boolean shouldLog = isRelevantRequest(url, "GET", targetUrls);
                
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
     * İsteğin loglanmaya değer olup olmadığını kontrol et
     * Sadece API çağrıları, form gönderimleri ve önemli sayfa yüklemelerini kabul et
     */
    private boolean isRelevantRequest(String url, String method, List<String> targetUrls) {
        // Statik kaynakları filtrele
        if (isStaticResource(url)) {
            return false;
        }
        
        // Sadece belirli HTTP metodlarını kabul et
        if (!isRelevantMethod(method)) {
            return false;
        }
        
        // URL filtreleme kontrolü
        if (targetUrls != null && !targetUrls.isEmpty()) {
            return targetUrls.stream().anyMatch(url::contains);
        }
        
        return true;
    }
    
    /**
     * Statik kaynak olup olmadığını kontrol et
     */
    private boolean isStaticResource(String url) {
        String lowerUrl = url.toLowerCase();
        
        // CSS dosyaları
        if (lowerUrl.contains(".css") || lowerUrl.contains("css/")) {
            return true;
        }
        
        // JavaScript dosyaları
        if (lowerUrl.contains(".js") || lowerUrl.contains("js/")) {
            return true;
        }
        
        // Resim dosyaları
        if (lowerUrl.contains(".jpg") || lowerUrl.contains(".jpeg") || 
            lowerUrl.contains(".png") || lowerUrl.contains(".gif") || 
            lowerUrl.contains(".svg") || lowerUrl.contains(".ico") ||
            lowerUrl.contains(".webp") || lowerUrl.contains("images/")) {
            return true;
        }
        
        // Font dosyaları
        if (lowerUrl.contains(".woff") || lowerUrl.contains(".woff2") || 
            lowerUrl.contains(".ttf") || lowerUrl.contains(".eot") ||
            lowerUrl.contains("fonts/")) {
            return true;
        }
        
        // Diğer statik kaynaklar
        if (lowerUrl.contains(".pdf") || lowerUrl.contains(".xml") || 
            lowerUrl.contains(".txt") || lowerUrl.contains("static/")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * İlgili HTTP metodları olup olmadığını kontrol et
     */
    private boolean isRelevantMethod(String method) {
        if (method == null) return false;
        
        String upperMethod = method.toUpperCase();
        return upperMethod.equals("POST") || 
               upperMethod.equals("PUT") || 
               upperMethod.equals("DELETE") || 
               upperMethod.equals("PATCH") ||
               // GET metodları sadece API endpoint'leri için
               (upperMethod.equals("GET") && isApiEndpoint(method));
    }
    
    /**
     * API endpoint olup olmadığını kontrol et
     */
    private boolean isApiEndpoint(String url) {
        if (url == null) return false;
        
        String lowerUrl = url.toLowerCase();
        
        // API endpoint pattern'leri
        return lowerUrl.contains("/api/") ||
               lowerUrl.contains("/rest/") ||
               lowerUrl.contains("/ajax/") ||
               lowerUrl.contains("/signup") ||
               lowerUrl.contains("/login") ||
               lowerUrl.contains("/logout") ||
               lowerUrl.contains("/cart") ||
               lowerUrl.contains("/checkout") ||
               lowerUrl.contains("/payment") ||
               lowerUrl.contains("/order") ||
               lowerUrl.contains("/product") ||
               lowerUrl.contains("/search") ||
               lowerUrl.contains("/filter") ||
               lowerUrl.contains("/add") ||
               lowerUrl.contains("/remove") ||
               lowerUrl.contains("/update");
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
     * Test adımları için özel monitoring - sadece kullanıcı etkileşimlerini izle
     */
    public void enableTestStepMonitoring() {
        if (!isEnabled || devTools == null) {
            logger.warn("DevTools not available for test step monitoring");
            return;
        }
        
        try {
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
            
            // Request gönderimini dinle
            devTools.addListener(Network.requestWillBeSent(), request -> {
                String requestId = request.getRequestId().toString();
                String url = request.getRequest().getUrl();
                String method = request.getRequest().getMethod();
                
                // Sadece test adımlarıyla ilgili istekleri logla
                if (isTestStepRequest(url, method)) {
                    requestTimings.put(requestId, System.currentTimeMillis());
                    requestCount.incrementAndGet();
                    
                    // Test adımı türünü belirle
                    String testStepType = getTestStepType(url, method);
                    logger.info("[TEST-STEP][Network] {} {} -> {} | {}", method, requestId, url, testStepType);
                }
            });
            
            // Response'ları dinle
            devTools.addListener(Network.responseReceived(), response -> {
                String requestId = response.getRequestId().toString();
                String url = response.getResponse().getUrl();
                int status = response.getResponse().getStatus();
                Long startTime = requestTimings.get(requestId);
                
                // Sadece test adımlarıyla ilgili response'ları logla
                if (isTestStepRequest(url, "GET")) {
                    responseCount.incrementAndGet();
                    String testStepType = getTestStepType(url, "GET");
                    
                    if (startTime != null) {
                        long duration = System.currentTimeMillis() - startTime;
                        String statusIcon = status >= 200 && status < 300 ? "✅" : status >= 400 ? "❌" : "⚠️";
                        logger.info("[TEST-STEP][Network] Response {} {} {} -> {} ({}ms) | {}", 
                                  statusIcon, status, requestId, url, duration, testStepType);
                        requestTimings.remove(requestId);
                    } else {
                        String statusIcon = status >= 200 && status < 300 ? "✅" : status >= 400 ? "❌" : "⚠️";
                        logger.info("[TEST-STEP][Network] Response {} {} {} -> {} | {}", 
                                  statusIcon, status, requestId, url, testStepType);
                    }
                }
            });
            
            // Network hatalarını dinle
            devTools.addListener(Network.loadingFailed(), failure -> {
                String requestId = failure.getRequestId().toString();
                String url = failure.getRequestId().toString();
                String errorText = failure.getErrorText();
                
                // Sadece test adımlarıyla ilgili hataları logla
                if (isTestStepRequest(url, "GET")) {
                    logger.error("[TEST-STEP][Network] Failed {} -> {}: {}", requestId, url, errorText);
                }
            });
            
            logger.info("Test step monitoring enabled - only user interactions and form submissions will be logged");
            
        } catch (Exception e) {
            logger.error("Failed to enable test step monitoring: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Test adımı isteği olup olmadığını kontrol et
     */
    private boolean isTestStepRequest(String url, String method) {
        if (url == null) return false;
        
        String lowerUrl = url.toLowerCase();
        
        // Statik kaynakları filtrele
        if (isStaticResource(url)) {
            return false;
        }
        
        // Google Analytics/Ads çağrılarını filtrele
        if (lowerUrl.contains("csi.gstatic.com") || 
            lowerUrl.contains("google-analytics.com") ||
            lowerUrl.contains("googletagmanager.com") ||
            lowerUrl.contains("doubleclick.net") ||
            lowerUrl.contains("googleads")) {
            return false;
        }
        
        // Tüm HTTP metodlarını kabul et (GET, POST, PUT, DELETE, PATCH)
        if (method == null) {
            return false;
        }
        
        // Ana test sitesi URL'lerini kabul et
        if (lowerUrl.contains("automationexercise.com")) {
            return true;
        }
        
        // Test adımlarıyla ilgili endpoint'ler
        return lowerUrl.contains("/signup") ||
               lowerUrl.contains("/login") ||
               lowerUrl.contains("/logout") ||
               lowerUrl.contains("/cart") ||
               lowerUrl.contains("/checkout") ||
               lowerUrl.contains("/payment") ||
               lowerUrl.contains("/order") ||
               lowerUrl.contains("/add_to_cart") ||
               lowerUrl.contains("/remove_from_cart") ||
               lowerUrl.contains("/update_cart") ||
               lowerUrl.contains("/subscribe") ||
               lowerUrl.contains("/contact") ||
               lowerUrl.contains("/newsletter") ||
               lowerUrl.contains("/search") ||
               lowerUrl.contains("/filter") ||
               lowerUrl.contains("/product_detail") ||
               lowerUrl.contains("/products") ||
               lowerUrl.contains("/view_cart") ||
               lowerUrl.contains("/view_product") ||
               lowerUrl.contains("/category") ||
               lowerUrl.contains("/brand") ||
               // Form gönderimleri
               lowerUrl.contains("form") ||
               lowerUrl.contains("submit") ||
               lowerUrl.contains("action") ||
               // Sayfa navigasyonları
               lowerUrl.contains("home") ||
               lowerUrl.contains("about") ||
               lowerUrl.contains("contact");
    }
    
    /**
     * Test adımı türünü belirle
     */
    private String getTestStepType(String url, String method) {
        if (url == null) return "Unknown";
        
        String lowerUrl = url.toLowerCase();
        String upperMethod = method != null ? method.toUpperCase() : "";
        
        // Sayfa navigasyonları
        if (upperMethod.equals("GET")) {
            if (lowerUrl.contains("/signup") || lowerUrl.contains("/login")) {
                return "🔐 AUTH_PAGE";
            } else if (lowerUrl.contains("/products")) {
                return "🛍️ PRODUCTS_PAGE";
            } else if (lowerUrl.contains("/cart") || lowerUrl.contains("view_cart")) {
                return "🛒 CART_PAGE";
            } else if (lowerUrl.contains("/checkout")) {
                return "💳 CHECKOUT_PAGE";
            } else if (lowerUrl.contains("/payment")) {
                return "💸 PAYMENT_PAGE";
            } else if (lowerUrl.contains("/product_detail")) {
                return "📦 PRODUCT_DETAIL";
            } else if (lowerUrl.contains("/home")) {
                return "🏠 HOME_PAGE";
            } else if (lowerUrl.contains("/contact")) {
                return "📞 CONTACT_PAGE";
            } else if (lowerUrl.contains("/about")) {
                return "ℹ️ ABOUT_PAGE";
            } else if (lowerUrl.contains("/category")) {
                return "📂 CATEGORY_PAGE";
            } else if (lowerUrl.contains("/brand")) {
                return "🏷️ BRAND_PAGE";
            }
        }
        
        // Form gönderimleri
        if (upperMethod.equals("POST")) {
            if (lowerUrl.contains("/signup")) {
                return "✅ SIGNUP_SUBMIT";
            } else if (lowerUrl.contains("/login")) {
                return "✅ LOGIN_SUBMIT";
            } else if (lowerUrl.contains("/add_to_cart")) {
                return "✅ ADD_TO_CART";
            } else if (lowerUrl.contains("/remove_from_cart")) {
                return "✅ REMOVE_FROM_CART";
            } else if (lowerUrl.contains("/update_cart")) {
                return "✅ UPDATE_CART";
            } else if (lowerUrl.contains("/checkout")) {
                return "✅ CHECKOUT_SUBMIT";
            } else if (lowerUrl.contains("/payment")) {
                return "✅ PAYMENT_SUBMIT";
            } else if (lowerUrl.contains("/order")) {
                return "✅ ORDER_SUBMIT";
            } else if (lowerUrl.contains("/subscribe")) {
                return "✅ SUBSCRIBE";
            } else if (lowerUrl.contains("/contact")) {
                return "✅ CONTACT_SUBMIT";
            } else if (lowerUrl.contains("/newsletter")) {
                return "✅ NEWSLETTER_SUBSCRIBE";
            } else if (lowerUrl.contains("/search")) {
                return "🔍 SEARCH_SUBMIT";
            }
        }
        
        // Diğer metodlar
        if (upperMethod.equals("PUT")) {
            return "🔄 UPDATE_ACTION";
        } else if (upperMethod.equals("DELETE")) {
            return "🗑️ DELETE_ACTION";
        } else if (upperMethod.equals("PATCH")) {
            return "🔧 PATCH_ACTION";
        }
        
        return "�� PAGE_LOAD";
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
