# TestAutomation_with_DevTools

Modern web test otomasyonu projesi - Selenium WebDriver ve Chrome DevTools Protocol (CDP) entegrasyonu ile güçlendirilmiş.

## 🚀 Özellikler

- **Selenium WebDriver 4.34.0** - En güncel Selenium sürümü
- **Chrome DevTools Protocol (CDP)** - Network monitoring, console logging, performance tracking
- **WebDriverManager** - Otomatik driver yönetimi
- **JUnit 5** - Modern test framework
- **Logback** - Gelişmiş logging
- **Faker** - Test verisi üretimi

## 📋 Gereksinimler

- Java 21+
- Maven 3.6+
- Chrome 139+ (DevTools için)
- Windows 10/11

## 🛠️ Kurulum

1. **Projeyi klonlayın:**
```bash
git clone <repository-url>
cd TestAutomation_with_DevTools
```

2. **Bağımlılıkları yükleyin:**
```bash
mvn clean install
```

3. **Chrome versiyonunuzu kontrol edin:**
```bash
chrome --version
```

## 🔧 DevTools Entegrasyonu

### Chrome DevTools Protocol (CDP) Özellikleri

Bu proje, Chrome 139 versiyonunuz için `selenium-devtools-v138` paketini kullanır. DevTools şu özellikleri sağlar:

#### 🌐 Network Monitoring
- HTTP isteklerini ve yanıtlarını gerçek zamanlı izleme
- Request/response timing analizi
- Network hatalarını yakalama
- İstatistik toplama

#### 📝 Console Logging
- JavaScript console mesajlarını yakalama
- Error, warning, info seviyelerini ayrıştırma
- Kaynak bilgisi ile birlikte loglama

#### ⚡ Performance Monitoring
- Sayfa yükleme performansını izleme
- Performance metrics toplama

#### 🚫 URL Blocking
- Belirli URL pattern'lerini bloklama
- CSS, resim, script dosyalarını engelleme
- Test senaryoları için kaynak kontrolü

#### 📄 Page Monitoring
- Sayfa yükleme olaylarını dinleme
- DOM content loaded event'lerini yakalama

## 🧪 Test Çalıştırma

### Tüm Testleri Çalıştırma
```bash
mvn test
```

### DevTools Entegrasyon Testlerini Çalıştırma
```bash
mvn test -Dtest=DevToolsIntegrationTest
```

### Belirli Bir Test Metodunu Çalıştırma
```bash
mvn test -Dtest=DevToolsIntegrationTest#testNetworkMonitoring
```

## 📊 Kullanım Örnekleri

### Temel DevTools Kullanımı

```java
// WebDriver'ı başlat
WebDriverConfig config = new WebDriverConfig();
WebDriver driver = config.initializeDriver("chrome");

// DevTools monitoring'i etkinleştir
config.enableDevToolsMonitoring();

// Test senaryosu
config.navigateTo("https://www.example.com");

// Network istatistiklerini al
DevToolsHelper.NetworkStats stats = config.getNetworkStats();
System.out.println("Network Stats: " + stats);

// Temizlik
config.quitDriver();
```

### Network Monitoring

```java
// Sadece network monitoring'i etkinleştir
config.enableNetworkMonitoring();

// URL'leri blokla
List<String> urlsToBlock = Arrays.asList("*.css", "*.png");
config.blockUrls(urlsToBlock);
```

### Console Logging

```java
// Console logging'i etkinleştir
config.enableConsoleLogging();

// JavaScript console mesajları otomatik olarak yakalanır
driver.executeScript("console.log('Test message');");
```

## 📁 Proje Yapısı

```
TestAutomation_with_DevTools/
├── pom.xml                          # Maven konfigürasyonu
├── README.md                        # Proje dokümantasyonu
└── src/
    └── test/
        ├── java/
        │   └── proje/
        │       └── com/
        │           └── saucedemo/
        │               ├── config/
        │               │   └── WebDriverConfig.java      # WebDriver + DevTools konfigürasyonu
        │               ├── utils/
        │               │   ├── DevToolsHelper.java       # DevTools yardımcı sınıfı
        │               │   ├── TestDataGenerator.java    # Test verisi üretici
        │               │   └── VerificationHelper.java   # Doğrulama yardımcısı
        │               ├── pages/                        # Page Object Model
        │               │   ├── HomePage.java
        │               │   ├── ProductsPage.java
        │               │   └── ...
        │               ├── verification/
        │               │   └── VerificationHelper.java
        │               ├── AutomationExerciseCompleteTest.java
        │               └── DevToolsIntegrationTest.java  # DevTools test sınıfı
        └── resources/
            ├── allure.properties
            └── logback-test.xml
```

## 🔍 DevTools API Referansı

### DevToolsHelper Sınıfı

#### Ana Metodlar:
- `enableNetworkMonitoring()` - Network izleme
- `enableConsoleLogging()` - Console log yakalama
- `enablePerformanceMonitoring()` - Performans izleme
- `enablePageMonitoring()` - Sayfa olaylarını dinleme
- `blockUrls(List<String>)` - URL bloklama
- `getNetworkStats()` - Network istatistikleri
- `close()` - DevTools oturumunu kapat

#### NetworkStats Sınıfı:
- `getTotalRequests()` - Toplam istek sayısı
- `getTotalResponses()` - Toplam yanıt sayısı
- `getPendingRequests()` - Bekleyen istek sayısı

### WebDriverConfig Sınıfı

#### DevTools Metodları:
- `enableDevToolsMonitoring()` - Tüm monitoring özelliklerini etkinleştir
- `enableNetworkMonitoring()` - Sadece network monitoring
- `enableConsoleLogging()` - Sadece console logging
- `blockUrls(List<String>)` - URL bloklama
- `getNetworkStats()` - Network istatistikleri
- `isDevToolsAvailable()` - DevTools kullanılabilir mi?

## 🐛 Sorun Giderme

### DevTools Bağlantı Sorunları

1. **Chrome versiyonu uyumsuzluğu:**
   - Chrome versiyonunuzu kontrol edin: `chrome --version`
   - POM.xml'de doğru `selenium-devtools-vXXX` paketini kullandığınızdan emin olun

2. **DevTools session hatası:**
   - WebDriver'ı yeniden başlatın
   - Chrome'u tamamen kapatıp yeniden açın

3. **Network monitoring çalışmıyor:**
   - Chrome options'da `--remote-allow-origins=*` olduğundan emin olun
   - DevTools session'ının başarıyla oluşturulduğunu kontrol edin

### Log Seviyeleri

Logback konfigürasyonu `src/test/resources/logback-test.xml` dosyasında bulunur:

```xml
<!-- DevTools loglarını görmek için -->
<logger name="proje.com.saucedemo.utils.DevToolsHelper" level="INFO"/>
<logger name="proje.com.saucedemo.config.WebDriverConfig" level="INFO"/>
```

## 📈 Performans İpuçları

1. **Network monitoring'i sadece gerektiğinde etkinleştirin**
2. **Büyük test suite'lerde DevTools session'larını düzgün kapatın**
3. **URL blocking'i test senaryolarına göre optimize edin**
4. **Memory leak'leri önlemek için listener'ları temizleyin**

## 🔗 Faydalı Linkler

- [Chrome DevTools Protocol](https://chromedevtools.github.io/devtools-protocol/)
- [Selenium DevTools Documentation](https://www.selenium.dev/documentation/webdriver/bidirectional/chrome_devtools/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
- [JUnit 5](https://junit.org/junit5/)

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

---

**TestAutomation_with_DevTools** - Modern web test otomasyonu için güçlü DevTools entegrasyonu ile geliştirilmiştir.