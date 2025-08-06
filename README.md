# TestAutomation_with_DevTools

Bu proje, Selenium WebDriver kullanarak web UI otomasyonu yapan ve DevTools API ile network trafiğini izleyen, aynı zamanda Grafana + Prometheus + PushGateway + Loki ile kapsamlı monitoring yapan bir test otomasyonu projesidir.

## 🚀 Özellikler

- **Selenium WebDriver 4.18.1**: Modern web otomasyonu için
- **DevTools Integration**: Network trafiğini gerçek zamanlı izleme
- **Prometheus Metrics**: Test performans metrikleri
- **Grafana Dashboards**: Görsel monitoring dashboard'ları
- **Loki Log Aggregation**: Merkezi log toplama ve analiz
- **PushGateway**: Test metriklerini push etme
- **Page Object Model**: Sürdürülebilir test yapısı
- **Comprehensive Logging**: Detaylı test logları
- **Random Test Data**: Faker kütüphanesi ile rastgele test verisi
- **JUnit 5**: Modern test framework

## 📊 Monitoring Stack

### Grafana Dashboard
- **URL**: http://localhost:3000
- **Kullanıcı**: admin
- **Şifre**: admin123

### Prometheus
- **URL**: http://localhost:9090

### PushGateway
- **URL**: http://localhost:9091

### Loki (Log Aggregation)
- **URL**: http://localhost:3100

### cAdvisor (Container Metrics)
- **URL**: http://localhost:8080

### Node Exporter (System Metrics)
- **URL**: http://localhost:9100

## 🏗️ Proje Yapısı

```
TestAutomation_with_DevTools/
├── src/test/java/proje/com/saucedemo/
│   ├── config/
│   │   └── WebDriverConfig.java          # WebDriver konfigürasyonu
│   ├── pages/
│   │   ├── HomePage.java                 # Ana sayfa
│   │   ├── SignupLoginPage.java          # Kayıt/Giriş sayfası
│   │   ├── ProductsPage.java             # Ürünler sayfası
│   │   ├── CartPage.java                 # Sepet sayfası
│   │   ├── CheckoutPage.java             # Ödeme sayfası
│   │   └── PaymentPage.java              # Ödeme işlemi
│   ├── utils/
│   │   ├── NetworkTracer.java            # DevTools network izleme
│   │   ├── SeleniumTracer.java           # Selenium işlem izleme
│   │   ├── TestDataGenerator.java        # Test verisi üretimi
│   │   └── MetricsExporter.java          # Prometheus metrics export
│   ├── verification/
│   │   └── VerificationHelper.java       # Doğrulama yardımcısı
│   └── AutomationExerciseCompleteTest.java # Ana test sınıfı
├── monitoring/
│   ├── prometheus/
│   │   └── prometheus.yml                # Prometheus konfigürasyonu
│   ├── grafana/
│   │   ├── provisioning/
│   │   │   ├── datasources/
│   │   │   │   └── datasources.yml       # Grafana datasource
│   │   │   └── dashboards/
│   │   └── dashboards/
│   ├── loki/
│   │   └── loki-config.yml               # Loki konfigürasyonu
│   └── promtail/
│       └── promtail-config.yml           # Promtail konfigürasyonu
├── logs/
│   └── test-automation.log               # Test logları
├── docker-compose.yml                    # Monitoring stack
└── pom.xml                              # Maven dependencies
```

## 🛠️ Kurulum

### Gereksinimler

- Java 21
- Maven 3.6+
- Docker & Docker Compose
- Chrome Browser

### Adım 1: Monitoring Stack'i Başlatın

```bash
# Monitoring stack'i başlat
docker-compose up -d

# Servislerin durumunu kontrol et
docker-compose ps
```

### Adım 2: Projeyi Derleyin

```bash
# Dependencies'leri yükle
mvn clean compile

# Testleri çalıştır
mvn test
```

### Adım 3: Monitoring Dashboard'larını İnceleyin

1. **Grafana**: http://localhost:3000 (admin/admin123)
2. **Prometheus**: http://localhost:9090
3. **PushGateway**: http://localhost:9091
4. **Loki**: http://localhost:3100

## 📈 Metrikler ve Dashboard'lar

### Test Metrikleri

- **Test Success Rate**: Test başarı oranı (%)
- **Test Execution Count**: Test çalıştırma sayısı
- **Test Duration**: Test süreleri (saniye)
- **Active Tests**: Aktif test sayısı
- **Failed Tests**: Başarısız test sayısı

### Network Metrikleri

- **HTTP Requests**: HTTP istek sayısı
- **HTTP Response Times**: HTTP yanıt süreleri
- **Network Errors**: Network hata sayısı

### Performance Metrikleri

- **Page Load Times**: Sayfa yükleme süreleri
- **Browser Memory Usage**: Tarayıcı bellek kullanımı
- **Element Wait Times**: Element bekleme süreleri

### System Metrikleri

- **Container CPU Usage**: Container CPU kullanımı
- **Container Memory Usage**: Container bellek kullanımı
- **Node Metrics**: Sistem kaynak kullanımı

## 🔧 Konfigürasyon

### Prometheus Konfigürasyonu

`monitoring/prometheus/prometheus.yml` dosyasında:
- Scrape interval: 15s
- Test automation metrics: PushGateway (localhost:9091)
- Node Exporter: node-exporter:9100
- cAdvisor: cadvisor:8080

### Grafana Konfigürasyonu

- Datasource: Prometheus (http://prometheus:9090)
- Datasource: Loki (http://loki:3100)
- Dashboard: Test Automation Dashboard

### Loki Konfigürasyonu

- Log retention: 744h (31 gün)
- Storage: Filesystem
- Index: BoltDB

## 🧪 Test Çalıştırma

### Manuel Çalıştırma

```bash
# 1. Monitoring stack'i başlat
docker-compose up -d

# 2. Testleri çalıştır
mvn test

# 3. Grafana'ya eriş
# http://localhost:3000 (admin/admin123)
```

### IDE'de Çalıştırma

1. IntelliJ IDEA veya Eclipse açın
2. `AutomationExerciseCompleteTest.java` dosyasını açın
3. Test metodlarını çalıştırın
4. Grafana'da gerçek zamanlı metrikleri izleyin

## 📊 Dashboard Kullanımı

### Grafana Dashboard'ında Görebileceğiniz Metrikler:

1. **Test Success Rate**: Yüzde olarak başarılı test oranı
2. **Test Execution Count**: Belirli zaman diliminde çalışan test sayısı
3. **Test Duration**: Test süreleri histogramı
4. **Page Load Times**: Sayfa yükleme süreleri
5. **Browser Memory Usage**: Tarayıcı bellek kullanımı (MB)
6. **HTTP Requests**: HTTP istek sayısı ve yanıt süreleri
7. **Container CPU/Memory**: Sistem kaynak kullanımı
8. **Log Analysis**: Loki ile log analizi

## 🔍 Log Analizi

### Loki ile Log Sorgulama

```logql
# Test loglarını filtrele
{job="test-automation"}

# Hata loglarını filtrele
{job="test-automation"} |= "ERROR"

# Belirli test metodunu filtrele
{job="test-automation"} |= "testCreateAccount"

# Network loglarını filtrele
{job="test-automation"} |= "HTTP Request"
```

## 🐛 Sorun Giderme

### PushGateway Bağlantı Sorunu

```bash
# PushGateway'ın çalışıp çalışmadığını kontrol edin
docker-compose ps pushgateway

# PushGateway loglarını kontrol edin
docker-compose logs pushgateway
```

### Docker Servisleri Başlamıyor

```bash
# Docker servislerini kontrol edin
docker-compose ps

# Logları kontrol edin
docker-compose logs prometheus
docker-compose logs grafana
```

### Grafana'da Metrikler Görünmüyor

1. Prometheus'ta targets'ları kontrol edin: http://localhost:9090/targets
2. PushGateway target'ının UP olduğundan emin olun
3. 2-5 dakika bekleyin (ilk scrape için)

## 📝 Test Yazma

### Yeni Test Ekleme

```java
@Test
public void testExample() {
    String testName = "testExample";
    String browser = "chrome";
    
    try {
        MetricsExporter.recordTestExecution(testName, browser);
        
        // Test logic here
        // ...
        
        MetricsExporter.recordTestSuccess(testName, browser, duration);
        
    } catch (Exception e) {
        MetricsExporter.recordTestFailure(testName, browser, "assertion_error", duration);
        throw e;
    } finally {
        MetricsExporter.pushMetrics();
    }
}
```

### Sayfa Yükleme Süresi Kaydetme

```java
MetricsExporter.recordPageLoadTime("pageName", "chrome", 2.5);
```

### Browser Memory Kaydetme

```java
Runtime runtime = Runtime.getRuntime();
long memoryUsage = runtime.totalMemory() - runtime.freeMemory();
MetricsExporter.recordBrowserMemoryUsage("chrome", memoryUsage / 1024 / 1024);
```

## 🎯 Örnek Kullanım Senaryosu

1. **Monitoring stack'i başlatın:**  
   ```bash
   docker-compose up -d
   ```

2. **IDE'de testleri çalıştırın:**  
   - IntelliJ IDEA veya Eclipse açın  
   - `AutomationExerciseCompleteTest.java` çalıştırın

3. **Grafana'da izleyin:**  
   - http://localhost:3000 (admin/admin123)  
   - Testlerin gerçek zamanlı çalışmasını görün  
   - Başarı oranını takip edin  
   - Test sürelerini analiz edin

4. **Performans Analizi:**  
   - Hangi testlerin yavaş olduğunu tespit edin  
   - Başarısızlık nedenlerini analiz edin  
   - Sayfa yükleme performansını değerlendirin

## 🔄 Sürekli Monitoring

PushGateway test suite tamamlandıktan sonra da çalışmaya devam eder. Bu sayede:

- Sürekli monitoring yapabilirsiniz
- Geçmiş metrikleri karşılaştırabilirsiniz
- Trend analizi yapabilirsiniz

## 📞 Destek

Sorun yaşarsanız:

1. Docker servislerinin çalıştığını kontrol edin
2. PushGateway'ın çalıştığını kontrol edin
3. Test loglarını inceleyin
4. Prometheus targets'larını kontrol edin

---

**Not**: Bu proje Windows ortamında test edilmiştir. Linux/Mac için script'leri uyarlamanız gerekebilir. 