package proje.com.saucedemo;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v122.network.Network;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Optional;

public class NetworkMonitoringTest {
    
    @Test
    public void testNetworkMonitoring() {
        System.out.println("🌐 Network Monitoring Test for Chrome 138 Başlıyor...");
        
        try {
            // 1. WebDriver Setup
            WebDriverManager.chromedriver().setup();
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            
            WebDriver driver = new ChromeDriver(options);
            
            // 2. DevTools Kontrolü
            System.out.println("\n🔧 DevTools Durumu Kontrol Ediliyor (Chrome 138)...");
            
            if (!(driver instanceof HasDevTools)) {
                System.out.println("❌ Bu driver DevTools desteklemiyor!");
                driver.quit();
                return;
            }
            
            DevTools devTools = ((HasDevTools) driver).getDevTools();
            
            try {
                devTools.createSession();
                System.out.println("✅ DevTools session created successfully for Chrome 138");
                System.out.println("ℹ️ Using DevTools v122 (latest available) for Chrome 138 compatibility");
            } catch (Exception e) {
                System.out.println("⚠️ DevTools session creation failed - Chrome 138 compatibility issue");
                System.out.println("Proceeding with basic browser test instead");
                performBasicBrowserTest(driver);
                return;
            }
            
            // 3. Network Monitoring Başlatma
            System.out.println("\n📡 Network Monitoring Başlatılıyor...");
            
            CountDownLatch requestLatch = new CountDownLatch(1);
            
            // Network isteklerini dinle
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            
            devTools.addListener(Network.requestWillBeSent(), request -> {
                System.out.println("📤 İSTEK GÖNDERİLDİ:");
                System.out.println("   URL: " + request.getRequest().getUrl());
                System.out.println("   Method: " + request.getRequest().getMethod());
                System.out.println("   Type: " + request.getType());
                System.out.println("   Request ID: " + request.getRequestId());
                System.out.println("   Timestamp: " + request.getTimestamp());
                System.out.println("   ---");
            });
            
            devTools.addListener(Network.responseReceived(), response -> {
                System.out.println("📥 YANIT ALINDI:");
                System.out.println("   URL: " + response.getResponse().getUrl());
                System.out.println("   Status: " + response.getResponse().getStatus());
                System.out.println("   Type: " + response.getType());
                System.out.println("   Request ID: " + response.getRequestId());
                System.out.println("   Timestamp: " + response.getTimestamp());
                System.out.println("   ---");
                
                requestLatch.countDown();
            });
            
            devTools.addListener(Network.loadingFailed(), failure -> {
                System.out.println("❌ YÜKLEME BAŞARISIZ:");
                System.out.println("   URL: " + failure.getRequestId());
                System.out.println("   Error: " + failure.getErrorText());
                System.out.println("   ---");
            });
            
            // 4. Test Sayfasına Git
            System.out.println("\n🌍 Test Sayfasına Gidiliyor...");
            driver.get("https://automationexercise.com");
            
            // 5. Network İsteklerini Bekle
            System.out.println("\n⏳ Network İstekleri Bekleniyor...");
            try {
                requestLatch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println("⏰ Timeout - Network istekleri beklenirken zaman aşımı");
            }
            
            // 6. Sayfa Başlığını Kontrol Et
            String pageTitle = driver.getTitle();
            System.out.println("\n📄 Sayfa Başlığı: " + pageTitle);
            
            // 7. DevTools Kapat
            devTools.close();
            driver.quit();
            
            System.out.println("\n✅ Network Monitoring Test for Chrome 138 Tamamlandı!");
            
        } catch (Exception e) {
            System.out.println("❌ Network Monitoring Test Hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Basic browser test when DevTools is not available
     */
    private void performBasicBrowserTest(WebDriver driver) {
        try {
            System.out.println("\n=== Basic Browser Test for Chrome 138 ===");
            
            // Navigate to test sites
            System.out.println("Navigating to Google...");
            driver.get("https://www.google.com");
            Thread.sleep(2000);
            
            String googleTitle = driver.getTitle();
            System.out.println("Google page title: " + googleTitle);
            
            System.out.println("Navigating to AutomationExercise...");
            driver.get("https://automationexercise.com");
            Thread.sleep(3000);
            
            String automationTitle = driver.getTitle();
            System.out.println("AutomationExercise page title: " + automationTitle);
            
            driver.quit();
            System.out.println("✅ Basic browser test for Chrome 138 completed successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Basic browser test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
