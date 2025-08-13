package proje.com.saucedemo.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import proje.com.saucedemo.utils.DevToolsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * TestAutomation_with_DevTools - DevTools entegrasyonlu WebDriver konfigürasyon sınıfı
 * Otomatik driver yönetimi için WebDriverManager kullanır
 * Chrome DevTools Protocol (CDP) ile Chrome, Firefox ve Edge tarayıcılarını destekler
 */
public class WebDriverConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(WebDriverConfig.class);
    
    private WebDriver driver;
    private WebDriverWait wait;
    private DevToolsHelper devToolsHelper;
    
    /**
     * DevTools entegrasyonu ile WebDriverManager kullanarak WebDriver'ı başlatır
     * @param browserType Kullanılacak tarayıcı türü (chrome, firefox, edge)
     * @return Konfigüre edilmiş WebDriver örneği
     */
    public WebDriver initializeDriver(String browserType) {
        try {
            driver = createLocalDriver(browserType);
            
            // WebDriver'ı konfigüre et
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));
            
            // Daha uzun timeout ile WebDriverWait'i başlat
            wait = new WebDriverWait(driver, Duration.ofSeconds(45));
            
            // Desteklenen tarayıcılar için DevTools Helper'ı başlat
            if (driver instanceof org.openqa.selenium.chrome.ChromeDriver || 
                driver instanceof org.openqa.selenium.edge.EdgeDriver) {
                devToolsHelper = new DevToolsHelper(driver);
                logger.info("DevTools Helper tarayıcı için başlatıldı: {}", browserType);
            } else {
                logger.info("DevTools bu tarayıcı için desteklenmiyor: {}", browserType);
            }
            
            logger.info("TestAutomation_with_DevTools WebDriver initialized successfully for browser: {}", browserType);
            return driver;
            
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver: {}", e.getMessage());
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }
    
    /**
     * WebDriverManager kullanarak yerel WebDriver örneği oluşturur
     */
    private WebDriver createLocalDriver(String browserType) {
        switch (browserType.toLowerCase()) {
            case "chrome":
                // Otomatik Chrome versiyon tespiti kullan
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                // Temel Chrome seçenekleri
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--remote-allow-origins=*");
                
                // DevTools için ek Chrome seçenekleri
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--allow-running-insecure-content");
                chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
                
                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--no-sandbox");
                firefoxOptions.addArguments("--disable-dev-shm-usage");
                return new FirefoxDriver(firefoxOptions);
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--no-sandbox");
                edgeOptions.addArguments("--disable-dev-shm-usage");
                return new EdgeDriver(edgeOptions);
                
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
    }
    
    /**
     * WebDriverWait örneğini alır
     */
    public WebDriverWait getWait() {
        return wait;
    }
    
    /**
     * WebDriver örneğini alır
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * DevTools Helper örneğini alır
     */
    public DevToolsHelper getDevToolsHelper() {
        return devToolsHelper;
    }
    
    /**
     * Tüm DevTools izleme özelliklerini etkinleştirir
     */
    public void enableDevToolsMonitoring() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableAllMonitoring();
            logger.info("All DevTools monitoring features enabled");
        } else {
            logger.warn("DevTools not available for monitoring");
        }
    }
    
    /**
     * Sadece network izlemeyi etkinleştirir
     */
    public void enableNetworkMonitoring() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableNetworkMonitoring();
            logger.info("Network monitoring enabled");
        } else {
            logger.warn("DevTools not available for network monitoring");
        }
    }
    
    /**
     * Seçici network izlemeyi etkinleştirir (sadece belirli URL'ler)
     */
    public void enableSelectiveNetworkMonitoring(List<String> targetUrls) {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableSelectiveNetworkMonitoring(targetUrls);
            logger.info("Selective network monitoring enabled for: {}", targetUrls);
        } else {
            logger.warn("DevTools not available for selective network monitoring");
        }
    }
    
    /**
     * Test adımı izlemeyi etkinleştirir (sadece kullanıcı etkileşimleri ve form gönderimleri)
     */
    public void enableTestStepMonitoring() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableTestStepMonitoring();
            logger.info("Test step monitoring enabled - only user interactions will be logged");
        } else {
            logger.warn("DevTools not available for test step monitoring");
        }
    }
    
    /**
     * Sadece console loglamayı etkinleştirir
     */
    public void enableConsoleLogging() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.enableConsoleLogging();
            logger.info("Console logging enabled");
        } else {
            logger.warn("DevTools not available for console logging");
        }
    }
    
    /**
     * Belirli URL'leri engeller
     */
    public void blockUrls(java.util.List<String> urlsToBlock) {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            devToolsHelper.blockUrls(urlsToBlock);
        } else {
            logger.warn("DevTools not available for URL blocking");
        }
    }
    
    /**
     * Network istatistiklerini alır
     */
    public DevToolsHelper.NetworkStats getNetworkStats() {
        if (devToolsHelper != null && devToolsHelper.isEnabled()) {
            return devToolsHelper.getNetworkStats();
        }
        return new DevToolsHelper.NetworkStats(0, 0, 0);
    }
    
    /**
     * WebDriver'ı kapatır ve kaynakları temizler
     */
    public void quitDriver() {
        if (devToolsHelper != null) {
            try {
                devToolsHelper.close();
                logger.info("DevTools session closed");
            } catch (Exception e) {
                logger.warn("Error closing DevTools: {}", e.getMessage());
            }
        }
        
        if (driver != null) {
            try {
                driver.quit();
                logger.info("TestAutomation_with_DevTools WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: {}", e.getMessage());
            }
        }
    }
    
    /**
     * URL'ye git
     */
    public void navigateTo(String url) {
        if (driver != null) {
            driver.get(url);
            logger.info("Navigated to: {}", url);
        }
    }
    
    /**
     * Mevcut URL'yi alır
     */
    public String getCurrentUrl() {
        if (driver != null) {
            return driver.getCurrentUrl();
        }
        return "";
    }
    
    /**
     * Sayfa başlığını alır
     */
    public String getPageTitle() {
        if (driver != null) {
            return driver.getTitle();
        }
        return "";
    }
    
    /**
     * DevTools'un kullanılabilir olup olmadığını kontrol eder
     */
    public boolean isDevToolsAvailable() {
        return devToolsHelper != null && devToolsHelper.isEnabled();
    }
} 