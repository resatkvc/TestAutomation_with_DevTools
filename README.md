# 🚀 TestAutomation_with_DevTools

**Modern Selenium Test Automation with Chrome DevTools Protocol Integration**

Bu proje, Selenium WebDriver ve Chrome DevTools Protocol (CDP) kullanarak gelişmiş web test otomasyonu sağlar. E-ticaret akışlarını test ederken gerçek zamanlı network monitoring, console logging ve performance tracking özellikleri sunar.

## 📋 İçindekiler

- [🎯 Proje Amacı](#-proje-amacı)
- [✨ Özellikler](#-özellikler)
- [🛠️ Teknoloji Stack](#️-teknoloji-stack)
- [📦 Gereksinimler](#-gereksinimler)
- [⚙️ Kurulum](#️-kurulum)
- [🚀 Kullanım](#-kullanım)
- [📊 HTML Raporlama](#-html-raporlama)
- [🔧 Konfigürasyon](#-konfigürasyon)
- [📁 Proje Yapısı](#-proje-yapısı)
- [🧪 Test Senaryoları](#-test-senaryoları)
- [📈 DevTools Entegrasyonu](#-devtools-entegrasyonu)
- [🔍 Loglama Sistemi](#-loglama-sistemi)
- [📝 Örnekler](#-örnekler)
- [❓ SSS](#-sss)
- [🤝 Katkıda Bulunma](#-katkıda-bulunma)
- [📄 Lisans](#-lisans)

## 🎯 Proje Amacı

Bu proje, modern web uygulamalarının test otomasyonunu geliştirmek için tasarlanmıştır. Temel hedefler:

- **🔍 Detaylı Network Monitoring**: HTTP isteklerini, response'ları ve performans metriklerini gerçek zamanlı izleme
- **📊 Kapsamlı Raporlama**: HTML tabanlı interaktif raporlar ile test sonuçlarını görselleştirme
- **⚡ Performance Tracking**: Sayfa yükleme süreleri ve memory kullanımını analiz etme
- **🛡️ Güvenilir Test Akışı**: E-ticaret senaryolarını end-to-end test etme

## ✨ Özellikler

### 🌐 **DevTools Entegrasyonu**
- **Network Monitoring**: HTTP isteklerini, response'ları ve süreleri izleme
- **Console Logging**: Browser console mesajlarını yakalama
- **Performance Tracking**: Sayfa yükleme metriklerini analiz etme
- **Request Interception**: İstekleri yakalama ve modifiye etme
- **URL Blocking**: Belirli URL'leri engelleme

### 📊 **HTML Raporlama**
- **Renkli ve İnteraktif**: Modern tasarım ile kullanıcı dostu arayüz
- **Arama ve Filtreleme**: Loglarda hızlı arama ve seviye bazlı filtreleme
- **Responsive Tasarım**: Mobil ve desktop uyumlu
- **Gerçek Zamanlı**: Test sırasında oluşturulan güncel raporlar

### 🧪 **Test Otomasyonu**
- **Page Object Model**: Sürdürülebilir test yapısı
- **Test Data Generation**: Otomatik test verisi oluşturma
- **Retry Mechanism**: Hata durumlarında otomatik yeniden deneme
- **Comprehensive Verification**: Kapsamlı doğrulama sistemi

## 🛠️ Teknoloji Stack

| Teknoloji | Versiyon | Amaç |
|-----------|----------|------|
| **Java** | 21+ | Ana programlama dili |
| **Maven** | 3.6+ | Build ve dependency management |
| **Selenium WebDriver** | 4.34.0 | Web otomasyon framework |
| **Chrome DevTools Protocol** | v138 | Browser monitoring |
| **JUnit 5** | 5.10.0 | Test framework |
| **WebDriverManager** | 5.5.3 | Driver management |
| **SLF4J + Logback** | 2.0.9 | Loglama sistemi |
| **FreeMarker** | 2.3.32 | HTML template engine |
| **JavaFaker** | 1.0.2 | Test verisi oluşturma |

## 📦 Gereksinimler

### **Sistem Gereksinimleri**
- **Java**: 21 veya üzeri
- **Maven**: 3.6 veya üzeri
- **Chrome Browser**: 139 veya üzeri
- **RAM**: Minimum 4GB (8GB önerilen)
- **Disk**: 2GB boş alan

### **Kritik Versiyon Uyumluluğu**
```
Java 21+ ←→ Maven 3.6+ ←→ Selenium 4.34.0 ←→ Chrome 139 ←→ selenium-devtools-v138
```

## ⚙️ Kurulum

### **1. Repository Klonlama**
```bash
git clone https://github.com/your-username/TestAutomation_with_DevTools.git
cd TestAutomation_with_DevTools
```

### **2. Bağımlılıkları Yükleme**
```bash
mvn clean install
```

### **3. WebDriver Kurulumu**
Proje WebDriverManager kullandığı için otomatik olarak driver'ları indirir.

### **4. Chrome Browser Kontrolü**
Chrome 139 veya üzeri versiyonunun yüklü olduğundan emin olun.

## 🚀 Kullanım

### **Tüm Testleri Çalıştırma**
```bash
mvn test
```

### **Belirli Test Sınıfını Çalıştırma**
```bash
mvn test -Dtest=AutomationExerciseCompleteTest
```

### **Belirli Test Metodunu Çalıştırma**
```bash
mvn test -Dtest=AutomationExerciseCompleteTest#testCreateAccount
```

### **Clean Build ile Çalıştırma**
```bash
mvn clean test
```

## 📊 HTML Raporlama

### **Rapor Türleri**

#### **1. `test-automation-report.html`**
- **Amaç**: Genel test akışını gösterir
- **İçerik**: Test adımları, doğrulamalar, hatalar
- **Kullanım**: Test sonuçlarını genel olarak analiz etmek için

#### **2. `devtools-network-report.html`**
- **Amaç**: Network trafiğini detaylı analiz eder
- **İçerik**: HTTP istekleri, response süreleri, URL'ler
- **Kullanım**: Performance sorunlarını tespit etmek için

#### **3. `devtools-console-report.html`**
- **Amaç**: Browser console loglarını gösterir
- **İçerik**: JavaScript hataları, uyarıları, console.log'lar
- **Kullanım**: Frontend hatalarını debug etmek için

#### **4. `devtools-performance-report.html`**
- **Amaç**: Performance metriklerini analiz eder
- **İçerik**: Sayfa yükleme süreleri, memory kullanımı
- **Kullanım**: Performance optimizasyonu için

### **Raporları Görüntüleme**
```bash
# Raporlar target/reports/ klasöründe oluşturulur
open target/reports/test-automation-report.html
open target/reports/devtools-network-report.html
```

### **Rapor Özellikleri**
- ✅ **Arama**: Loglarda hızlı arama
- ✅ **Filtreleme**: Seviye bazlı filtreleme (ERROR, WARN, INFO, DEBUG)
- ✅ **Responsive**: Mobil uyumlu tasarım
- ✅ **Renkli**: Log seviyelerine göre renk kodlaması
- ✅ **İnteraktif**: Hover efektleri ve animasyonlar

## 🔧 Konfigürasyon

### **Logback Konfigürasyonu**
```xml
<!-- src/test/resources/logback-test.xml -->
<configuration>
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Ayrı log dosyaları -->
    <appender name="MAIN_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>target/logs/test-automation.log</file>
        <!-- ... -->
    </appender>
    
    <appender name="DEVTOOLS_NETWORK_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>target/logs/devtools-network.log</file>
        <!-- ... -->
    </appender>
</configuration>
```

### **DevTools Konfigürasyonu**
```java
// DevTools özelliklerini etkinleştirme
if (webDriverConfig.isDevToolsAvailable()) {
    webDriverConfig.enableTestStepMonitoring();
    logger.info("Test step monitoring enabled");
}
```

## 📁 Proje Yapısı

```
TestAutomation_with_DevTools/
├── src/
│   └── test/
│       ├── java/proje/com/saucedemo/
│       │   ├── config/
│       │   │   └── WebDriverConfig.java          # WebDriver konfigürasyonu
│       │   ├── pages/
│       │   │   ├── HomePage.java                 # Ana sayfa
│       │   │   ├── SignupLoginPage.java          # Kayıt/Giriş sayfası
│       │   │   ├── ProductsPage.java             # Ürünler sayfası
│       │   │   ├── CartPage.java                 # Sepet sayfası
│       │   │   ├── CheckoutPage.java             # Ödeme sayfası
│       │   │   └── PaymentPage.java              # Ödeme işlemi
│       │   ├── utils/
│       │   │   ├── DevToolsHelper.java           # DevTools entegrasyonu
│       │   │   ├── HTMLReportGenerator.java      # HTML rapor oluşturucu
│       │   │   └── TestDataGenerator.java        # Test verisi oluşturucu
│       │   ├── verification/
│       │   │   └── VerificationHelper.java       # Doğrulama yardımcısı
│       │   └── AutomationExerciseCompleteTest.java # Ana test sınıfı
│       └── resources/
│           ├── templates/
│           │   ├── main-report.ftl               # Ana rapor template
│           │   ├── network-report.ftl            # Network rapor template
│           │   ├── console-report.ftl            # Console rapor template
│           │   └── performance-report.ftl        # Performance rapor template
│           └── logback-test.xml                  # Loglama konfigürasyonu
├── target/
│   ├── logs/                                     # Log dosyaları
│   │   ├── test-automation.log                   # Ana test logları
│   │   ├── devtools-network.log                  # Network logları
│   │   ├── devtools-console.log                  # Console logları
│   │   └── devtools-performance.log              # Performance logları
│   └── reports/                                  # HTML raporlar
│       ├── test-automation-report.html           # Ana test raporu
│       ├── devtools-network-report.html          # Network raporu
│       ├── devtools-console-report.html          # Console raporu
│       └── devtools-performance-report.html      # Performance raporu
├── pom.xml                                       # Maven konfigürasyonu
├── README.md                                     # Bu dosya
└── .gitignore                                    # Git ignore kuralları
```

## 🧪 Test Senaryoları

### **1. Hesap Oluşturma Testi**
```java
@Test
@Order(1)
@DisplayName("Step 1: Navigate to AutomationExercise and Create Account")
void testCreateAccount() {
    // Ana sayfaya git
    driver.get(BASE_URL);
    
    // Kayıt işlemini başlat
    homePage.clickSignupLogin();
    
    // Kullanıcı bilgilerini doldur
    signupLoginPage.startSignup(userName, userEmail);
    
    // Hesap bilgilerini doldur
    signupLoginPage.fillAccountInformation(...);
    
    // Hesabı oluştur
    signupLoginPage.createAccount();
    
    // Doğrula
    verificationHelper.verifyAccountCreated(accountCreated);
}
```

### **2. Ürün Ekleme Testi**
```java
@Test
@Order(2)
@DisplayName("Step 2: Add Random Products to Cart")
void testAddProductsToCart() {
    // Ürünler sayfasına git
    productsPage.navigateToProducts();
    
    // Rastgele ürün ekle
    productsPage.addRandomProductToCart();
    
    // Alışverişe devam et
    productsPage.clickContinueShopping();
}
```

### **3. Sepet Doğrulama Testi**
```java
@Test
@Order(3)
@DisplayName("Step 3: Navigate to Cart and Verify Products")
void testVerifyCartProducts() {
    // Sepete git
    cartPage.navigateToCart();
    
    // Ürünleri doğrula
    verificationHelper.verifyCartNotEmpty(cartPage.getCartItemsCount() > 0);
    
    // Ürün listesini al ve doğrula
    List<String> cartProductNames = cartPage.getProductNames();
    for (String productName : cartProductNames) {
        verificationHelper.verifyProductInCart(cartPage.containsProduct(productName), productName);
    }
}
```

### **4. Ödeme Tamamlama Testi**
```java
@Test
@Order(4)
@DisplayName("Step 4: Complete Checkout and Payment")
void testCompleteCheckoutAndPayment() {
    // Ödemeye geç
    cartPage.clickProceedToCheckout();
    
    // Teslimat adresini doldur
    checkoutPage.fillDeliveryAddress(...);
    
    // Siparişi ver
    checkoutPage.clickPlaceOrder();
    
    // Ödemeyi tamamla
    paymentPage.completePaymentWithRandomData();
    
    // Doğrula
    verificationHelper.verifyOrderPlaced(paymentPage.isOrderPlaced());
}
```

## 📈 DevTools Entegrasyonu

### **Network Monitoring**
```java
// Network isteklerini izleme
devTools.addListener(Network.requestWillBeSent(), request -> {
    String method = request.getRequest().getMethod();
    String url = request.getRequest().getUrl();
    networkLogger.info("[CDP][Network] {} {} -> {}", method, requestId, url);
});

// Response'ları izleme
devTools.addListener(Network.responseReceived(), response -> {
    int status = response.getResponse().getStatus();
    long duration = System.currentTimeMillis() - startTime;
    networkLogger.info("[CDP][Network] Response {} {} -> {} ({}ms)", 
                      status, requestId, url, duration);
});
```

### **Console Logging**
```java
// Console mesajlarını yakalama
devTools.addListener(Log.entryAdded(), entry -> {
    String level = entry.getLevel().toString();
    String text = entry.getText();
    consoleLogger.info("[CDP][Console][{}] {}", level, text);
});
```

### **Performance Tracking**
```java
// Performance metriklerini izleme
devTools.send(Performance.enable(Optional.empty()));
devTools.addListener(Performance.metrics(), metrics -> {
    performanceLogger.info("[CDP][Performance] Metrics: {}", metrics);
});
```

## 🔍 Loglama Sistemi

### **Log Seviyeleri**
- **ERROR**: Kritik hatalar (kırmızı)
- **WARN**: Uyarılar (turuncu)
- **INFO**: Bilgi mesajları (mavi)
- **DEBUG**: Debug mesajları (gri)

### **Log Dosyaları**
- **`test-automation.log`**: Genel test logları
- **`devtools-network.log`**: Network istekleri
- **`devtools-console.log`**: Console mesajları
- **`devtools-performance.log`**: Performance metrikleri

### **Log Formatı**
```
2025-08-13T01:27:41.928Z [CDP Connection] INFO p.c.s.utils.DevToolsHelper.Network - [TEST-STEP][Network] GET 7EFF0B3AE9D657BC469561D22A7ABEDE -> https://www.automationexercise.com/ | 🏠 HOME_PAGE
```

## 📝 Örnekler

### **DevTools Helper Kullanımı**
```java
// DevTools Helper'ı başlat
DevToolsHelper devToolsHelper = new DevToolsHelper(driver);

// Network monitoring'i etkinleştir
devToolsHelper.enableTestStepMonitoring();

// Console logging'i etkinleştir
devToolsHelper.enableConsoleLogging();

// Performance monitoring'i etkinleştir
devToolsHelper.enablePerformanceMonitoring();

// URL'leri engelle
devToolsHelper.blockUrls(Arrays.asList("google-analytics.com", "doubleclick.net"));
```

### **HTML Rapor Oluşturma**
```java
// HTML raporları oluştur
HTMLReportGenerator reportGenerator = new HTMLReportGenerator();
reportGenerator.generateAllReports();
```

### **Test Verisi Oluşturma**
```java
// Kullanıcı bilgileri oluştur
TestDataGenerator.UserInfo userInfo = TestDataGenerator.generateUserInfo();
String email = userInfo.getEmail();
String password = userInfo.getPassword();

// Hesap bilgileri oluştur
TestDataGenerator.AccountInfo accountInfo = TestDataGenerator.generateAccountInfo();
```

## ❓ SSS

### **Q: Chrome versiyonu uyumsuzluğu nasıl çözülür?**
**A:** Chrome 139 veya üzeri versiyonunu yükleyin. WebDriverManager otomatik olarak uygun driver'ı indirir.

### **Q: DevTools çalışmıyor, ne yapmalıyım?**
**A:** 
1. Chrome versiyonunu kontrol edin
2. Selenium versiyonunun 4.34.0 olduğundan emin olun
3. `selenium-devtools-v138` dependency'sinin eklendiğini kontrol edin

### **Q: HTML raporları boş geliyor, neden?**
**A:** 
1. Test çalıştırıldığından emin olun
2. Log dosyalarının oluştuğunu kontrol edin
3. FreeMarker template'lerinin doğru konumda olduğunu kontrol edin

### **Q: Network monitoring çok fazla log üretiyor, nasıl azaltabilirim?**
**A:** `DevToolsHelper.enableTestStepMonitoring()` kullanın. Bu sadece test adımlarıyla ilgili istekleri loglar.

### **Q: Test başarısız oluyor, nasıl debug edebilirim?**
**A:** 
1. HTML raporlarını inceleyin
2. Console loglarını kontrol edin
3. Network isteklerini analiz edin
4. Retry mechanism'ini kullanın

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

### **Katkıda Bulunma Kuralları**
- Kod standartlarına uyun
- Test coverage'ını koruyun
- README'yi güncelleyin
- Yeni özellikler için dokümantasyon ekleyin


## 🎉 Teşekkürler

Bu proje aşağıdaki açık kaynak projeleri kullanır:

- [Selenium WebDriver](https://selenium.dev/)
- [Chrome DevTools Protocol](https://chromedevtools.github.io/devtools-protocol/)
- [JUnit 5](https://junit.org/junit5/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
- [FreeMarker](https://freemarker.apache.org/)
- [JavaFaker](https://github.com/DiUS/java-faker)

---

**⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!**

**📧 İletişim:** [kavciresat@gmail.com](kavciresat@gmail.com)

**🌐 Website:** [https://www.linkedin.com/in/kavci/](https://www.linkedin.com/in/kavci/)
