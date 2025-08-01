# 🧪 SauceDemo UI Test Automation with Zipkin Integration

Bu proje, [SauceDemo](https://www.saucedemo.com/) sitesi için profesyonel bir UI test otomasyonu projesidir. Selenium WebDriver, JUnit 5, ve basitleştirilmiş Zipkin tracing entegrasyonu ile geliştirilmiştir.

## 🎯 Proje Özellikleri

- **Selenium WebDriver 4.18.1** - Modern web otomasyonu
- **JUnit 5** - Test framework
- **Page Object Model (POM)** - Sürdürülebilir test yapısı
- **Simplified Zipkin Integration** - Basitleştirilmiş distributed tracing
- **Docker Compose** - Kolay deployment
- **Random Test Data** - Faker ile dinamik test verileri
- **Comprehensive Logging** - Detaylı loglama
- **Professional Structure** - Modüler ve genişletilebilir yapı

## 📋 Test Senaryosu

Proje aşağıdaki end-to-end test senaryosunu gerçekleştirir:

1. **Login** - `standard_user` ile giriş yapma
2. **Add Products** - Random 2 ürün sepete ekleme
3. **Cart Verification** - Sepetteki ürünleri doğrulama
4. **Checkout** - Ödeme sürecine geçiş
5. **Fill Information** - Random checkout bilgileri doldurma
6. **Order Completion** - Siparişi tamamlama
7. **Verification** - Sipariş tamamlanma mesajını doğrulama
8. **Return Home** - Ana sayfaya dönme

## 🏗️ Proje Yapısı

```
src/
├── main/
│   └── java/
│       └── proje/
│           └── com/
│               └── Main.java
└── test/
    ├── java/
    │   └── proje/
    │       └── com/
    │           └── saucedemo/
    │               ├── config/
    │               │   └── WebDriverConfig.java
    │               ├── pages/
    │               │   ├── LoginPage.java
    │               │   ├── InventoryPage.java
    │               │   ├── CartPage.java
    │               │   ├── CheckoutPage.java
    │               │   ├── CheckoutOverviewPage.java
    │               │   └── CheckoutCompletePage.java
    │               ├── utils/
    │               │   ├── ZipkinTracer.java
    │               │   └── TestDataGenerator.java
    │               ├── verification/
    │               │   └── VerificationHelper.java
    │               └── SauceDemoCompleteTest.java
    └── resources/
        └── logback-test.xml
```

## 🚀 Kurulum ve Çalıştırma

### Gereksinimler

- Java 21
- Maven 3.8+
- Docker & Docker Compose
- Chrome/Firefox/Edge browser

### 1. Projeyi Klonlayın

```bash
git clone <repository-url>
cd Zipkin-Integrated_UI_Test_Automation
```

### 2. Docker Servislerini Başlatın

```bash
docker-compose up -d
```

Bu komut aşağıdaki servisleri başlatır:
- **Zipkin** (http://localhost:9411)
- **Selenium Grid Hub** (http://localhost:4444)
- **Chrome Node**
- **Firefox Node**
- **Edge Node**

### 3. Testleri Çalıştırın

#### Tüm Testleri Çalıştırma
```bash
mvn clean test
```

#### Belirli Test Sınıfını Çalıştırma
```bash
mvn test -Dtest=SauceDemoCompleteTest
```

#### Selenium Grid ile Çalıştırma
```bash
mvn test -Dtest=SauceDemoCompleteTest -Dbrowser=chrome -DuseGrid=true
```

## 🔧 Konfigürasyon

### WebDriver Konfigürasyonu

`WebDriverConfig.java` dosyasında aşağıdaki ayarları değiştirebilirsiniz:

```java
// Local Chrome kullanımı
driver = webDriverConfig.initializeDriver("chrome", false);

// Selenium Grid kullanımı
driver = webDriverConfig.initializeDriver("chrome", true);
```

### Zipkin Konfigürasyonu

`ZipkinTracer.java` dosyasında Zipkin endpoint'ini değiştirebilirsiniz:

```java
private static final String ZIPKIN_ENDPOINT = "http://localhost:9411/api/v2/spans";
```

## 📊 Monitoring ve Tracing

### Simplified Zipkin Integration

Bu proje basitleştirilmiş bir Zipkin entegrasyonu kullanır:
- **Trace ID**: Her test oturumu için benzersiz ID
- **Span ID**: Her test adımı için benzersiz ID
- **Duration Tracking**: Her span'in süresini ölçer
- **Tag System**: Test verilerini span'lere ekler
- **Error Tracking**: Hataları span'lere kaydeder

### Trace Bilgileri

Her test adımı için aşağıdaki trace'ler oluşturulur:
- `test-login`
- `test-add-products-to-cart`
- `test-verify-cart-products`
- `test-proceed-to-checkout`
- `test-fill-checkout-information`
- `test-complete-order`
- `test-verify-order-completion-and-return-home`

## 📝 Log Dosyaları

Test logları aşağıdaki konumlarda saklanır:
- **Console**: Gerçek zamanlı log çıktısı
- **File**: `logs/saucedemo-test.log`
- **Rolling**: Günlük log dosyaları

## 🧪 Test Verileri

### Random Test Data

`TestDataGenerator.java` sınıfı aşağıdaki random verileri üretir:
- **First Name**: Faker ile random isim
- **Last Name**: Faker ile random soyisim
- **Postal Code**: Faker ile random posta kodu
- **Products**: 6 farklı üründen random seçim

### SauceDemo Credentials

```java
public static final String STANDARD_USER = "standard_user";
public static final String STANDARD_PASSWORD = "secret_sauce";
```

## 🔍 Verification Helper

`VerificationHelper.java` sınıfı tüm doğrulama işlemlerini merkezi olarak yönetir:

- Login doğrulama
- Cart ürün doğrulama
- Checkout sayfa doğrulama
- Order completion doğrulama
- Back to home doğrulama

## 🐳 Docker Servisleri

### Zipkin
```yaml
zipkin:
  image: openzipkin/zipkin:latest
  ports:
    - "9411:9411"
```

### Selenium Grid
```yaml
selenium-hub:
  image: selenium/hub:4.18.1
  ports:
    - "4444:4444"
```

## 📈 Performans ve Monitoring

### Simplified Tracing Metrics
- **Trace Duration**: Her test adımının süresi
- **Span Count**: Toplam span sayısı
- **Error Rate**: Hata oranı
- **Tag Information**: Test verileri

### Test Metrics
- **Test Duration**: Toplam test süresi
- **Success Rate**: Başarı oranı
- **Product Count**: Eklenen ürün sayısı
- **Cart Verification**: Sepet doğrulama durumu

## 🛠️ Geliştirme

### Yeni Test Ekleme

1. `pages/` klasörüne yeni Page Object ekleyin
2. `verification/` klasörüne verification method'ları ekleyin
3. `utils/` klasörüne utility method'ları ekleyin
4. Test sınıfında yeni test method'u ekleyin

### Yeni Browser Desteği

`WebDriverConfig.java` dosyasında yeni browser desteği ekleyin:

```java
case "safari":
    WebDriverManager.safaridriver().setup();
    return new SafariDriver();
```

## 🐛 Troubleshooting

### Selenium Grid Bağlantı Sorunu
```bash
# Grid durumunu kontrol edin
curl http://localhost:4444/status
```

### Zipkin Bağlantı Sorunu
```bash
# Zipkin durumunu kontrol edin
curl http://localhost:9411/health
```

### Chrome Driver Sorunu
```bash
# WebDriverManager cache'ini temizleyin
mvn clean
rm -rf ~/.cache/selenium
```

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📞 İletişim

Proje hakkında sorularınız için:
- **Email**: [your-email@example.com]
- **GitHub**: [your-github-profile]

---

**Not**: Bu proje eğitim ve öğrenme amaçlı geliştirilmiştir. Production ortamında kullanmadan önce güvenlik ve performans testlerini yapmanız önerilir. 