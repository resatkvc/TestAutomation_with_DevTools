# AutomationExercise UI Test Automation with Zipkin Integration

Bu proje, `https://www.automationexercise.com/` sitesi için geliştirilmiş kapsamlı UI test otomasyonu projesidir. Proje, end-to-end test senaryolarını destekler ve **Zipkin distributed tracing** entegrasyonu ile test süreçlerini detaylı olarak izler.

## 🚀 Proje Özellikleri

### ✅ **Test Otomasyonu**
- **End-to-End Test Senaryoları**: Kullanıcı kaydı, ürün ekleme, sepet işlemleri, ödeme
- **Page Object Model (POM)**: Sürdürülebilir ve yeniden kullanılabilir kod yapısı
- **Random Test Data**: JavaFaker ile gerçekçi test verileri
- **Cross-Browser Testing**: Chrome, Firefox, Edge desteği
- **WebDriverManager**: Otomatik driver yönetimi

### 🔍 **Zipkin Distributed Tracing**
- **Comprehensive Tracing**: Tüm test adımlarının detaylı izlenmesi
- **Performance Monitoring**: Sayfa yükleme süreleri, element etkileşimleri
- **Error Tracking**: Test hatalarının detaylı analizi
- **Service Categorization**: Test adımlarının kategorize edilmesi
- **Real-time Monitoring**: Zipkin UI üzerinden canlı izleme

### 📊 **Monitoring & Analytics**
- **Network Monitoring**: Selenium Network API ile ağ trafiği izleme
- **Selenium Tracing**: WebDriver işlemlerinin detaylı izlenmesi
- **Logging**: SLF4J ve Logback ile kapsamlı loglama
- **Reporting**: Test sonuçlarının detaylı raporlanması

## 🛠️ Gereksinimler

- **Java 21** veya üzeri
- **Maven 3.8+**
- **Docker & Docker Compose** (Zipkin için)
- **Chrome/Firefox/Edge** browser
- **Git**

## 🚀 Kurulum ve Çalıştırma

### 1. **Projeyi Klonlayın**
```bash
git clone <repository-url>
cd Zipkin-Integrated_UI_Test_Automation
```

### 2. **Zipkin'i Başlatın**
```bash
# Zipkin'i Docker ile başlatın
docker-compose up -d

# Servislerin durumunu kontrol edin
docker-compose ps
```

### 3. **Projeyi Derleyin**
```bash
mvn clean compile
```

### 4. **Testleri Çalıştırın**
```bash
# Tüm testleri çalıştırın
mvn test

# Belirli test sınıfını çalıştırın
mvn test -Dtest=AutomationExerciseCompleteTest

# Farklı browser ile test
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### 5. **Zipkin UI'ya Erişin**
```
http://localhost:9411
```

## 📁 Proje Yapısı

```
src/test/java/proje/com/saucedemo/
├── AutomationExerciseCompleteTest.java    # Ana test sınıfı
├── config/
│   └── WebDriverConfig.java              # WebDriver konfigürasyonu
├── pages/                                # Page Object Model
│   ├── HomePage.java                     # Ana sayfa
│   ├── SignupLoginPage.java              # Kayıt/Giriş sayfası
│   ├── ProductsPage.java                 # Ürünler sayfası
│   ├── CartPage.java                     # Sepet sayfası
│   ├── CheckoutPage.java                 # Ödeme sayfası
│   └── PaymentPage.java                  # Ödeme işlemi
├── utils/                                # Yardımcı sınıflar
│   ├── TestDataGenerator.java            # Test verisi üretici
│   ├── ZipkinTracer.java                 # Zipkin entegrasyonu
│   ├── NetworkTracer.java                # Ağ izleme
│   └── SeleniumTracer.java              # Selenium izleme
└── verification/
    └── VerificationHelper.java           # Doğrulama yardımcısı
```

## 🧪 Test Senaryoları

### **End-to-End Test Flow**
1. **Kullanıcı Kaydı**: Random kullanıcı oluşturma
2. **Ürün Ekleme**: Random ürünleri sepete ekleme
3. **Sepet Doğrulama**: Sepetteki ürünleri kontrol etme
4. **Ödeme İşlemi**: Checkout ve ödeme tamamlama
5. **Sipariş Doğrulama**: Sipariş tamamlanmasını kontrol etme

### **Zipkin Tracing Kategorileri**
- **test.suite.setup**: Test suite başlatma
- **test.create.account**: Kullanıcı kaydı
- **test.add.products**: Ürün ekleme
- **test.verify.cart**: Sepet doğrulama
- **test.proceed.checkout**: Checkout işlemi
- **test.fill.checkout**: Ödeme bilgileri
- **test.complete.payment**: Ödeme tamamlama
- **test.verify.completion**: Sipariş doğrulama

## ⚙️ Konfigürasyon

### **Docker Compose Services**
```yaml
services:
  zipkin:                    # Distributed tracing server
    image: openzipkin/zipkin:latest
    ports: ["9411:9411"]
```

### **WebDriverManager Konfigürasyonu**
```java
// Otomatik driver yönetimi
WebDriverManager.chromedriver().setup();
WebDriverManager.firefoxdriver().setup();
WebDriverManager.edgedriver().setup();

// Browser seçimi
driver = webDriverConfig.initializeDriver("chrome");
driver = webDriverConfig.initializeDriver("firefox");
driver = webDriverConfig.initializeDriver("edge");
```

### **Zipkin Tracing Konfigürasyonu**
```java
// Service name for Zipkin categorization
private static final String SERVICE_NAME = "automation-exercise-test";

// Tracing methods
zipkinTracer.startSpan("operation.name", "description");
zipkinTracer.trackPageNavigation("page.name", "url", duration);
zipkinTracer.trackElementInteraction("element.name", "action", duration);
zipkinTracer.trackTestStep("step.name", "description", success, duration);
zipkinTracer.endSpan("operation.name", success);
```

## 📊 Raporlama

### **Zipkin UI Özellikleri**
- **Trace Search**: Test adımlarını arama
- **Service Dependencies**: Servis bağımlılıkları
- **Performance Metrics**: Süre analizleri
- **Error Tracking**: Hata izleme
- **Real-time Monitoring**: Canlı izleme

### **Test Raporları**
- **JUnit Reports**: Test sonuçları
- **Log Files**: Detaylı loglar
- **Zipkin Traces**: Distributed tracing
- **Network Logs**: Ağ trafiği

## 🔧 Geliştirme

### **Yeni Test Senaryosu Ekleme**
1. **Page Object Oluştur**: `pages/` klasörüne yeni sayfa sınıfı ekle
2. **Test Metodu Yaz**: `AutomationExerciseCompleteTest.java`'ya test metodu ekle
3. **Zipkin Tracing**: Test adımlarını Zipkin ile izle
4. **Verification**: Doğrulama metodları ekle

### **Zipkin Tracing Ekleme**
```java
// Test başlangıcında
zipkinTracer.startSpan("test.operation", "Test operation description");

// Test adımlarında
zipkinTracer.trackElementInteraction("element", "action", duration);
zipkinTracer.trackPageNavigation("page", "url", loadTime);

// Test sonunda
zipkinTracer.trackTestStep("step", "description", success, duration);
zipkinTracer.endSpan("test.operation", success);
```

## 🐛 Hata Ayıklama

### **Zipkin UI'da Hata Analizi**
1. **Trace ID'yi Bul**: Loglardan trace ID'yi al
2. **Zipkin UI'da Ara**: `http://localhost:9411` adresinde trace ID ile ara
3. **Span Detaylarını İncele**: Hata olan span'i bul ve detaylarını incele
4. **Performance Analizi**: Yavaş olan adımları tespit et

### **Log Analizi**
```bash
# Test loglarını görüntüle
tail -f target/test.log

# Zipkin trace ID'lerini filtrele
grep "Trace ID" target/test.log
```

## ⚡ Performans

### **Optimizasyon Önerileri**
- **WebDriverManager**: Otomatik driver yönetimi
- **Resource Management**: WebDriver kaynaklarını düzgün kapat
- **Network Monitoring**: Ağ trafiğini optimize et
- **Zipkin Sampling**: Production'da sampling oranını ayarla

### **Performance Metrics**
- **Page Load Times**: Sayfa yükleme süreleri
- **Element Interaction Times**: Element etkileşim süreleri
- **Test Execution Times**: Test çalıştırma süreleri
- **Network Latency**: Ağ gecikme süreleri

## 🤝 Katkıda Bulunma

1. **Fork** yapın
2. **Feature branch** oluşturun (`git checkout -b feature/amazing-feature`)
3. **Commit** yapın (`git commit -m 'Add amazing feature'`)
4. **Push** yapın (`git push origin feature/amazing-feature`)
5. **Pull Request** oluşturun

## 📄 Lisans

Bu proje eğitim ve test otomasyonu amaçlıdır. MIT lisansı altında lisanslanmıştır.

## 📞 İletişim

- **Proje**: [GitHub Repository](https://github.com/your-username/Zipkin-Integrated_UI_Test_Automation)
- **Zipkin UI**: http://localhost:9411

---

**Not**: Bu proje eğitim ve test otomasyonu amaçlıdır. Gerçek e-ticaret sitelerinde kullanmadan önce gerekli izinleri alın. 