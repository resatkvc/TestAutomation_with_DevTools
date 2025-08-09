# Chrome DevTools Protocol Test Automation

Bu proje, **Selenium WebDriver** ve **Chrome DevTools Protocol (CDP)** kullanarak kapsamlı e-ticaret test otomasyonu yapar. **Allure Report** ile detaylı raporlama ve CDP'nin tüm domain'leri ile comprehensive monitoring özelliklerine sahiptir.

**Referans**: [Chrome DevTools Protocol Documentation](https://chromedevtools.github.io/devtools-protocol/)

## 🚀 Özellikler

- **Selenium WebDriver 4.18.1**: Modern web otomasyonu
- **Chrome DevTools Protocol (CDP)**: 7 domain ile comprehensive monitoring
  - **Network**: HTTP request/response monitoring
  - **Performance**: Performance metrics collection
  - **Console**: Console log capture
  - **Runtime**: JavaScript runtime monitoring
  - **Security**: SSL/TLS security monitoring  
  - **Page**: Page lifecycle events
  - **DOM**: DOM change monitoring
- **Allure Reports**: Güzel ve detaylı test raporları
- **Page Object Model**: Sürdürülebilir test yapısı
- **Random Test Data**: Faker ile rastgele test verisi
- **JUnit 5**: Modern test framework
- **Comprehensive Logging**: Detaylı test logları

## 📊 Test Akışı

1. **Kullanıcı Kaydı**: Rastgele kullanıcı bilgileri ile hesap oluşturma
2. **Ürün Ekleme**: Rastgele ürünleri sepete ekleme
3. **Sepet Kontrolü**: Sepetteki ürünleri doğrulama
4. **Ödeme İşlemi**: Tam satın alma akışı

## 🛠️ Kurulum

### Gereksinimler

- Java 21
- Maven 3.6+
- Chrome Browser

### Projeyi Çalıştırma

```bash
# Dependencies'leri yükle
mvn clean compile

# Testleri çalıştır
mvn test

# Allure raporu oluştur
mvn allure:report

# Allure raporunu aç
mvn allure:serve
```

## 🏗️ Proje Yapısı

```
src/test/java/proje/com/saucedemo/
├── AutomationExerciseCompleteTest.java    # Ana test sınıfı (CDP + Allure entegreli)
├── DevToolsTest.java                      # CDP test sınıfı
├── config/
│   └── WebDriverConfig.java               # WebDriver konfigürasyonu
├── pages/
│   ├── HomePage.java                       # Ana sayfa
│   ├── SignupLoginPage.java                # Kayıt/Giriş sayfası
│   ├── ProductsPage.java                   # Ürünler sayfası
│   ├── CartPage.java                       # Sepet sayfası
│   ├── CheckoutPage.java                   # Ödeme sayfası
│   └── PaymentPage.java                    # Ödeme işlemi
├── utils/
│   ├── ChromeDevToolsManager.java          # Comprehensive CDP Manager (7 domain)
│   └── TestDataGenerator.java             # Test verisi üretimi
└── verification/
    └── VerificationHelper.java            # Doğrulama yardımcısı

src/test/resources/
├── allure.properties                       # Allure konfigürasyonu
└── logback-test.xml                        # Logging konfigürasyonu
```

## 🔧 Chrome DevTools Protocol Implementation

### 📋 CDP Dokümantasyon Analizi

Bu proje [Chrome DevTools Protocol](https://chromedevtools.github.io/devtools-protocol/) resmi dokümantasyonuna göre geliştirilmiştir.

### 🎯 ChromeDevToolsManager.java - Merkezi CDP Yöneticisi

- ✅ **7 CDP Domain** desteği
- ✅ **Comprehensive monitoring** tüm alanlar için
- ✅ **Real-time data collection**
- ✅ **Allure integration** otomatik attachment

### 📊 Desteklenen CDP Domains

#### 1. 🌐 Network Domain
```java
// HTTP request/response monitoring
devTools.send(Network.enable());
devTools.addListener(Network.requestWillBeSent(), ...);
devTools.addListener(Network.responseReceived(), ...);
```

#### 2. ⚡ Performance Domain
```java
// Performance metrics ve timing
devTools.send(Performance.enable());
devTools.send(Performance.getMetrics());
```

#### 3. 📝 Console Domain
```java
// Console log capture
devTools.send(Console.enable());
devTools.addListener(Console.messageAdded(), ...);
```

#### 4. 🔧 Runtime Domain
```java
// JavaScript runtime monitoring
devTools.send(Runtime.enable());
devTools.addListener(Runtime.exceptionThrown(), ...);
devTools.addListener(Runtime.consoleAPICalled(), ...);
```

#### 5. 🔒 Security Domain
```java
// SSL/TLS security monitoring
devTools.send(Security.enable());
devTools.addListener(Security.securityStateChanged(), ...);
```

#### 6. 📄 Page Domain
```java
// Page lifecycle events
devTools.send(Page.enable());
devTools.addListener(Page.loadEventFired(), ...);
devTools.addListener(Page.domContentEventFired(), ...);
```

#### 7. 🏗️ DOM Domain
```java
// DOM change monitoring
devTools.send(DOM.enable());
```

## 🎯 CDP Kullanım Örnekleri

### Temel Kullanım
```java
// CDP Manager başlatma
ChromeDevToolsManager cdpManager = new ChromeDevToolsManager(driver);

// Tüm monitoring'i etkinleştir
cdpManager.enableAllMonitoring();

// Specific domain'leri etkinleştir
cdpManager.enableNetworkMonitoring();
cdpManager.enableConsoleMonitoring();
cdpManager.enableRuntimeMonitoring();
```

### Metrics Alma
```java
int networkRequests = cdpManager.getNetworkRequestCount();
int consoleLogs = cdpManager.getConsoleLogCount();
int jsErrors = cdpManager.getJavaScriptErrorCount();
boolean isInitialized = cdpManager.isInitialized();
```

### Allure Entegrasyonu
```java
// Otomatik attachment
cdpManager.attachToAllureReport();

// Manuel attachment
Allure.addAttachment("CDP Summary", cdpManager.getDevToolsSummary());
```

## 📈 Allure Report Özellikleri

### Test Detayları
- **Epic**: E-Commerce Automation
- **Features**: User Registration, Product Selection, Shopping Cart, Checkout Process
- **Severity Levels**: Critical, Normal
- **Steps**: Her test adımı detayları

### Otomatik CDP Attachments
- **DevTools Summary** - Genel CDP durumu
- **Network Requests** - Tüm HTTP istekleri
- **Console Logs** - Console mesajları
- **JavaScript Errors** - JS hataları
- **Security Events** - Güvenlik olayları
- **Performance Metrics** - Performans metrikleri

### Real-time Monitoring Çıktısı
```
🌐 Network Request #1: GET https://www.automationexercise.com (document)
✅ Network Response: Status: 200 (OK)
📝 Console [info]: Page loaded successfully
🔒 Security State Changed: secure
📄 Page Load Event fired
⚡ Performance metrics collected
```

## 🔍 CDP Protokol Uyumluluğu

### Resmi CDP Standardlarına Uygun
- ✅ **JSON message transport** interface
- ✅ **WebSocket connection** üzerinden iletişim
- ✅ **Domain-based command structure**
- ✅ **Event-driven architecture**
- ✅ **Error handling** ve exception management

### Chrome DevTools Protokol Versiyonları
- ✅ **v122** (Latest stable)
- ✅ **v120** (Backward compatibility)
- ✅ **Selenium 4.18.1** uyumlu

## 📝 Test Yazma

### Yeni Test Ekleme
```java
@Test
@Story("Your Story")
@Severity(SeverityLevel.NORMAL)
@Description("Test açıklaması")
@DisplayName("Test Adı")
void yourTestMethod() {
    Allure.step("Test adımı", () -> {
        // Test kodu
        
        // CDP bilgilerini ekle
        cdpManager.attachToAllureReport();
    });
}
```

### Test Sınıfı Güncellemeleri

#### AutomationExerciseCompleteTest.java
```java
// Comprehensive CDP monitoring
cdpManager.enableAllMonitoring();
cdpManager.attachToAllureReport();
```

#### DevToolsTest.java
```java
// CDP testing
cdpManager.enableAllMonitoring();

// Results
logger.info("Network Requests: {}", cdpManager.getNetworkRequestCount());
logger.info("Console Logs: {}", cdpManager.getConsoleLogCount());
logger.info("JS Errors: {}", cdpManager.getJavaScriptErrorCount());
```

## 🎯 Konsol Çıktısı Örneği

```
🔧 Initializing Chrome DevTools Protocol Manager...
✅ Chrome DevTools Protocol session created successfully
🚀 All CDP monitoring domains enabled
🌐 Network Request #1: GET https://www.automationexercise.com (document)
✅ Network Response: https://www.automationexercise.com - Status: 200 (OK)
🌐 Network Request #2: GET /static/css/bootstrap.min.css (stylesheet)
📝 Console [info]: Bootstrap CSS loaded
🔒 Security State Changed
📄 Page Load Event fired
⚡ Performance monitoring enabled
🏗️ DOM monitoring enabled

=== CHROME DEVTOOLS PROTOCOL RESULTS ===
CDP Manager Initialized: true
Network Requests: 15
Console Logs: 3
JavaScript Errors: 0
```

## 🎯 Örnek Kullanım

1. **Testleri çalıştır:**
   ```bash
   mvn test
   ```

2. **Allure raporu oluştur:**
   ```bash
   mvn allure:serve
   ```

3. **CDP loglarını incele:**
   - Console'da gerçek zamanlı CDP monitoring
   - `target/logs/test-automation.log` dosyasından detaylı logları incele

4. **Allure raporunda analiz yap:**
   - Test başarı oranları
   - CDP network aktivitesi
   - Her test adımının detayları
   - Hata durumlarında debug bilgileri

## 🔧 Konfigürasyon

### WebDriver
- Chrome otomatik başlatılır
- CDP otomatik etkinleştirilir
- Headless mod devre dışı (görsel takip için)

### Logging
- Console: INFO level
- File: DEBUG level (`target/logs/test-automation.log`)
- CDP: DEBUG level

### Allure
- Results: `target/allure-results`
- Automatic attachments: CDP verileri, hata detayları

## 📊 Proje Durumu

### ✅ Temizlenmiş Proje Yapısı
- **11 Java sınıfı** (gereksizler silindi)
- **2 konfigürasyon dosyası**
- **1 dokümantasyon dosyası**
- **Sıfır gereksiz kod**
- **Sıfır duplicate functionality**

### 🎯 CDP Özellikleri
- **7 CDP Domain** tam desteği
- **Network, Performance, Console, Runtime, Security, Page, DOM**
- **Comprehensive monitoring**
- **Real-time logging**
- **Allure integration**

### 📋 Test Yapısı
- **1 Ana test sınıfı** (AutomationExerciseCompleteTest)
- **1 CDP test sınıfı** (DevToolsTest)
- **6 Page Object sınıfı**
- **1 CDP Manager sınıfı**
- **1 Test data generator**
- **1 Verification helper**

### ❌ Temizlenen Gereksiz Dosyalar
1. **DEVTOOLS_STATUS.md** - Eski CDP durumu
2. **NetworkTracer.java** - ChromeDevToolsManager ile duplicate functionality
3. **target/** klasörü - Eski build artifact'ları

## ✅ Sonuç

✅ **Tamamen CDP dokümantasyonu standardlarına uygun**  
✅ **7 ana CDP domain desteği**  
✅ **Real-time monitoring ve logging**  
✅ **Comprehensive Allure integration**  
✅ **Production-ready kod kalitesi**  
✅ **Enterprise-level monitoring**  
✅ **Proje tamamen temizlenmiş ve optimize edilmiş**  

Artık projeniz **Chrome DevTools Protocol'ün tüm özelliklerini** kullanabilir ve **enterprise-level comprehensive monitoring** yapabilir!

---

**Test Site**: https://www.automationexercise.com  
**Framework**: Selenium WebDriver + Chrome DevTools Protocol + Allure  
**Language**: Java 21  
**Build Tool**: Maven  
**CDP Referans**: [https://chromedevtools.github.io/devtools-protocol/](https://chromedevtools.github.io/devtools-protocol/)