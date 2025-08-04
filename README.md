# AutomationExercise UI Test Automation with Zipkin Integration

Bu proje, AutomationExercise web sitesi için kapsamlı UI test otomasyonu sağlar ve Zipkin ile dağıtık izleme (distributed tracing) entegrasyonu içerir.

## 🎯 Proje Özellikleri

- **End-to-End Test Flow**: Kullanıcı kaydı, ürün ekleme, sepet işlemleri, ödeme süreci
- **DevTools Network Monitoring**: Chrome DevTools API ile gerçek zamanlı HTTP istek izleme
- **Zipkin Integration**: Dağıtık izleme ve performans analizi
- **Page Object Model**: Temiz ve sürdürülebilir kod yapısı
- **Random Test Data**: JavaFaker ile gerçekçi test verileri
- **Comprehensive Logging**: SLF4J ve Logback ile detaylı loglama

## 🚀 Gereksinimler

- Java 11+
- Maven 3.6+
- Chrome Browser
- Docker (Zipkin için)

## 📦 Kurulum ve Çalıştırma

### 1. Projeyi Klonlayın
```bash
git clone <repository-url>
cd Zipkin-Integrated_UI_Test_Automation
```

### 2. Zipkin'i Başlatın
```bash
docker-compose up -d
```

### 3. Testleri Çalıştırın
```bash
# Tüm testleri çalıştır
mvn test

# Belirli bir test sınıfını çalıştır
mvn test -Dtest=AutomationExerciseCompleteTest

# Belirli bir test metodunu çalıştır
mvn test -Dtest=AutomationExerciseCompleteTest#testCreateAccount
```

### 4. Zipkin UI'da Sonuçları Görüntüleyin
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
│   ├── HomePage.java
│   ├── SignupLoginPage.java
│   ├── ProductsPage.java
│   ├── CartPage.java
│   ├── CheckoutPage.java
│   └── PaymentPage.java
├── utils/                                # Yardımcı sınıflar
│   ├── ZipkinTracer.java                # Zipkin entegrasyonu
│   ├── NetworkTracer.java               # DevTools network monitoring
│   ├── SeleniumTracer.java              # Selenium izleme
│   └── TestDataGenerator.java           # Test veri üreteci
└── verification/
    └── VerificationHelper.java           # Doğrulama yardımcıları
```

## 🧪 Test Senaryoları

### 1. Kullanıcı Kaydı
- Ana sayfaya git
- Signup/Login linkine tıkla
- Yeni kullanıcı bilgilerini gir
- Hesap oluştur

### 2. Ürün Ekleme
- Ürünler sayfasına git
- Rastgele ürünleri sepete ekle
- Alışverişe devam et

### 3. Sepet İşlemleri
- Sepete git
- Ürünleri doğrula
- Checkout'a geç

### 4. Ödeme Süreci
- Teslimat bilgilerini gir
- Ödeme bilgilerini gir
- Siparişi tamamla

## 🔍 Zipkin Integration

### DevTools Network Monitoring
- Chrome DevTools API kullanarak gerçek zamanlı HTTP istek izleme
- Her HTTP method için ayrı service name:
  - `automation-exercise-get`
  - `automation-exercise-post`
  - `automation-exercise-put`
  - `automation-exercise-delete`

### Zipkin'de Görünen Bilgiler
- **Test Adımları**: `automation-exercise-test`
- **HTTP İstekleri**: Method-specific service name'ler
- **Performans Metrikleri**: Response time, status codes
- **Hata Takibi**: Failed requests ve exceptions

### Zipkin UI'da Görüntüleme
1. Service dropdown'da farklı service name'ler
2. Her HTTP isteği için ayrı trace
3. Request/response detayları
4. Timeline görünümü

## ⚙️ Konfigürasyon

### WebDriver
- Chrome browser kullanımı
- WebDriverManager ile otomatik driver yönetimi
- DevTools API desteği

### Zipkin
- Docker container ile çalıştırma
- HTTP endpoint: `http://localhost:9411`
- Memory storage (geliştirme için)

### Logging
- SLF4J + Logback
- Console ve file output
- Trace ID correlation

## 📊 Raporlama

### Zipkin UI
- Service dependencies
- Request flow visualization
- Performance metrics
- Error tracking

### Console Logs
- Test adımları
- HTTP istekleri
- Trace ID'ler
- Hata mesajları

## 🛠️ Geliştirme

### Yeni Test Ekleme
1. Page Object oluştur
2. Test metodunu ekle
3. Zipkin tracing ekle
4. Verification helper kullan

### DevTools Monitoring
- Sadece Chrome browser destekler
- Otomatik HTTP method detection
- Real-time network monitoring

## 🐛 Hata Ayıklama

### Yaygın Sorunlar
1. **DevTools not available**: Chrome driver kullanıldığından emin olun
2. **Zipkin connection failed**: Docker container'ın çalıştığından emin olun
3. **Test failures**: Site erişilebilirliğini kontrol edin

### Debug Logs
```bash
# Debug modunda çalıştır
mvn test -Dtest=AutomationExerciseCompleteTest -Dlogging.level.proje.com.saucedemo=DEBUG
```

## 📈 Performans

### Optimizasyonlar
- DevTools API ile gerçek zamanlı monitoring
- Minimal overhead ile tracing
- Efficient memory usage
- Fast test execution

### Monitoring
- Network request timing
- Page load performance
- Element interaction delays
- Overall test duration

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun
3. Değişikliklerinizi commit edin
4. Pull request gönderin

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 📞 İletişim

Proje ile ilgili sorularınız için issue açabilirsiniz.

---

**Not**: Bu proje sadece eğitim ve geliştirme amaçlıdır. Production ortamında kullanmadan önce güvenlik ve performans testleri yapılmalıdır. 