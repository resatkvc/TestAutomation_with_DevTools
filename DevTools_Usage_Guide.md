# Chrome DevTools Protocol (CDP) Kullanım Kılavuzu

## 📋 Genel Bakış

Bu kılavuz, TestAutomation_with_DevTools projesinde Chrome DevTools Protocol (CDP) entegrasyonunun nasıl kullanılacağını açıklar.

## 🎯 DevTools Nedir?

Chrome DevTools Protocol (CDP), Chrome tarayıcısının iç işleyişini programatik olarak kontrol etmemizi sağlayan bir protokoldür. Bu sayede:

- Network isteklerini gerçek zamanlı izleyebiliriz
- JavaScript console mesajlarını yakalayabiliriz
- Sayfa performansını ölçebiliriz
- Belirli URL'leri bloklayabiliriz
- Request/response'ları modifiye edebiliriz

## 🚀 Hızlı Başlangıç

### 1. Temel Kurulum

```java
// WebDriver'ı başlat
WebDriverConfig config = new WebDriverConfig();
WebDriver driver = config.initializeDriver("chrome");

// DevTools monitoring'i etkinleştir
config.enableDevToolsMonitoring();

// Test senaryosu
config.navigateTo("https://www.example.com");

// Temizlik
config.quitDriver();
```

### 2. DevTools Kullanılabilirliğini Kontrol Etme

```java
if (config.isDevToolsAvailable()) {
    System.out.println("DevTools kullanılabilir!");
} else {
    System.out.println("DevTools kullanılamıyor (Firefox gibi)");
}
```

## 🌐 Network Monitoring

### Network İsteklerini İzleme

```java
// Network monitoring'i etkinleştir
config.enableNetworkMonitoring();

// Test URL'ine git
config.navigateTo("https://www.google.com");

// Network istatistiklerini al
DevToolsHelper.NetworkStats stats = config.getNetworkStats();
System.out.println("Toplam İstek: " + stats.getTotalRequests());
System.out.println("Toplam Yanıt: " + stats.getTotalResponses());
System.out.println("Bekleyen İstek: " + stats.getPendingRequests());
```

### URL Bloklama

```java
// CSS ve resim dosyalarını blokla
List<String> urlsToBlock = Arrays.asList(
    "*.css",
    "*.png", 
    "*.jpg",
    "*.jpeg",
    "*.gif"
);

config.blockUrls(urlsToBlock);

// Test sayfasına git (bloklanan kaynaklar olmadan)
config.navigateTo("https://www.example.com");
```

## 📝 Console Logging

### JavaScript Console Mesajlarını Yakalama

```java
// Console logging'i etkinleştir
config.enableConsoleLogging();

// JavaScript console mesajları oluştur
((JavascriptExecutor) driver).executeScript("console.log('Info mesajı');");
((JavascriptExecutor) driver).executeScript("console.warn('Uyarı mesajı');");
((JavascriptExecutor) driver).executeScript("console.error('Hata mesajı');");
```

Console mesajları otomatik olarak loglanır ve şu formatta görünür:
```
[CDP][Console][INFO] console: Info mesajı
[CDP][Console][WARNING] console: Uyarı mesajı
[CDP][Console][ERROR] console: Hata mesajı
```

## ⚡ Performance Monitoring

### Performans İzleme

```java
// Performance monitoring zaten enableAllMonitoring() ile etkin
// Test URL'ine git ve performans verilerini topla
config.navigateTo("https://www.github.com");

// Kısa bir bekleme süresi (performans verilerinin toplanması için)
Thread.sleep(3000);
```

## 📄 Page Monitoring

### Sayfa Olaylarını Dinleme

```java
// Page monitoring zaten enableAllMonitoring() ile etkin
// Farklı sayfalara git ve page event'lerini test et
String[] testUrls = {
    "https://www.wikipedia.org",
    "https://www.stackoverflow.com"
};

for (String url : testUrls) {
    config.navigateTo(url);
    Thread.sleep(2000); // Page event'lerinin yakalanması için
}
```

Page event'leri otomatik olarak loglanır:
```
[CDP][Page] Load event fired
[CDP][Page] DOM content loaded
```

## 🔧 Gelişmiş Kullanım

### DevToolsHelper'ı Doğrudan Kullanma

```java
DevToolsHelper devToolsHelper = config.getDevToolsHelper();

if (devToolsHelper != null && devToolsHelper.isEnabled()) {
    // Sadece network monitoring
    devToolsHelper.enableNetworkMonitoring();
    
    // Sadece console logging
    devToolsHelper.enableConsoleLogging();
    
    // Fetch interception (request/response modifikasyonu)
    devToolsHelper.enableFetchInterception();
    
    // Network istatistikleri
    DevToolsHelper.NetworkStats stats = devToolsHelper.getNetworkStats();
    System.out.println("Network Stats: " + stats);
}
```

### Test Senaryolarında Kullanım

```java
@Test
public void testWithNetworkMonitoring() {
    // Setup
    WebDriverConfig config = new WebDriverConfig();
    WebDriver driver = config.initializeDriver("chrome");
    config.enableNetworkMonitoring();
    
    try {
        // Test senaryosu
        config.navigateTo("https://www.example.com");
        
        // Network istatistiklerini kontrol et
        DevToolsHelper.NetworkStats stats = config.getNetworkStats();
        Assert.assertTrue(stats.getTotalRequests() > 0, "Network istekleri yakalanmalı");
        
    } finally {
        // Temizlik
        config.quitDriver();
    }
}
```

## 🐛 Sorun Giderme

### Yaygın Sorunlar ve Çözümleri

#### 1. DevTools Başlatılamıyor

**Semptom:** `DevTools not available` uyarısı

**Çözüm:**
- Chrome versiyonunuzu kontrol edin: `chrome --version`
- POM.xml'de doğru `selenium-devtools-vXXX` paketini kullandığınızdan emin olun
- Chrome'u tamamen kapatıp yeniden açın

#### 2. Network İstekleri Yakalanmıyor

**Semptom:** Network istatistikleri 0 gösteriyor

**Çözüm:**
- Chrome options'da `--remote-allow-origins=*` olduğundan emin olun
- DevTools session'ının başarıyla oluşturulduğunu kontrol edin
- Sayfa yükleme süresini artırın

#### 3. Console Mesajları Görünmüyor

**Semptom:** JavaScript console mesajları loglanmıyor

**Çözüm:**
- Console logging'in etkin olduğunu kontrol edin
- JavaScript kodunun çalıştığından emin olun
- Log seviyelerini kontrol edin

### Log Seviyeleri

DevTools loglarını görmek için logback konfigürasyonunu kontrol edin:

```xml
<logger name="proje.com.saucedemo.utils.DevToolsHelper" level="INFO"/>
<logger name="proje.com.saucedemo.config.WebDriverConfig" level="INFO"/>
```

## 📊 Performans İpuçları

### 1. Memory Management

```java
// DevTools session'larını düzgün kapatın
try {
    // Test senaryosu
} finally {
    if (config.getDevToolsHelper() != null) {
        config.getDevToolsHelper().close();
    }
    config.quitDriver();
}
```

### 2. Selective Monitoring

```java
// Sadece ihtiyacınız olan monitoring'i etkinleştirin
config.enableNetworkMonitoring(); // Sadece network
// config.enableConsoleLogging(); // Sadece console
// config.enablePerformanceMonitoring(); // Sadece performance
```

### 3. URL Blocking Optimizasyonu

```java
// Test senaryosuna göre optimize edin
List<String> urlsToBlock = Arrays.asList("*.css"); // Sadece CSS blokla
config.blockUrls(urlsToBlock);
```

## 🔍 Debugging

### DevTools Session Durumunu Kontrol Etme

```java
DevToolsHelper devToolsHelper = config.getDevToolsHelper();
if (devToolsHelper != null) {
    System.out.println("DevTools Enabled: " + devToolsHelper.isEnabled());
    
    // Network istatistiklerini kontrol et
    DevToolsHelper.NetworkStats stats = devToolsHelper.getNetworkStats();
    System.out.println("Current Stats: " + stats);
}
```

### Detaylı Logging

```java
// Logback konfigürasyonunda debug seviyesini etkinleştirin
<logger name="proje.com.saucedemo.utils.DevToolsHelper" level="DEBUG"/>
```

## 📚 Örnek Test Senaryoları

### 1. API İsteklerini İzleme

```java
@Test
public void testApiRequests() {
    WebDriverConfig config = new WebDriverConfig();
    WebDriver driver = config.initializeDriver("chrome");
    config.enableNetworkMonitoring();
    
    try {
        // API çağrıları yapan sayfaya git
        config.navigateTo("https://jsonplaceholder.typicode.com/");
        
        // Network istatistiklerini kontrol et
        DevToolsHelper.NetworkStats stats = config.getNetworkStats();
        Assert.assertTrue(stats.getTotalRequests() > 0);
        
    } finally {
        config.quitDriver();
    }
}
```

### 2. JavaScript Hatalarını Yakalama

```java
@Test
public void testJavaScriptErrors() {
    WebDriverConfig config = new WebDriverConfig();
    WebDriver driver = config.initializeDriver("chrome");
    config.enableConsoleLogging();
    
    try {
        // JavaScript hataları oluşturan sayfaya git
        config.navigateTo("https://www.example.com");
        
        // JavaScript hataları oluştur
        ((JavascriptExecutor) driver).executeScript("console.error('Test error');");
        
        // Kısa bir bekleme süresi
        Thread.sleep(1000);
        
    } finally {
        config.quitDriver();
    }
}
```

### 3. Performans Testi

```java
@Test
public void testPagePerformance() {
    WebDriverConfig config = new WebDriverConfig();
    WebDriver driver = config.initializeDriver("chrome");
    config.enableDevToolsMonitoring(); // Tüm monitoring'i etkinleştir
    
    try {
        // Performans testi yapılacak sayfaya git
        config.navigateTo("https://www.github.com");
        
        // Sayfa yükleme süresini ölç
        long startTime = System.currentTimeMillis();
        // Sayfa yükleme işlemleri
        long endTime = System.currentTimeMillis();
        
        System.out.println("Page Load Time: " + (endTime - startTime) + "ms");
        
    } finally {
        config.quitDriver();
    }
}
```

## 🔗 Faydalı Kaynaklar

- [Chrome DevTools Protocol](https://chromedevtools.github.io/devtools-protocol/)
- [Selenium DevTools Documentation](https://www.selenium.dev/documentation/webdriver/bidirectional/chrome_devtools/)
- [CDP Domains Reference](https://chromedevtools.github.io/devtools-protocol/tot/)

---

**Not:** Bu kılavuz, TestAutomation_with_DevTools projesinin DevTools entegrasyonunu kullanmak için hazırlanmıştır. Chrome 139 versiyonu için `selenium-devtools-v138` paketi kullanılmaktadır.
