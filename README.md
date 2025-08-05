# UI Test Automation with DevTools Integration

Bu proje, Selenium WebDriver kullanarak web UI otomasyonu yapan ve DevTools API ile network trafiğini izleyen bir test otomasyonu projesidir.

## Özellikler

- **Selenium WebDriver**: Modern web otomasyonu için
- **DevTools Integration**: Network trafiğini gerçek zamanlı izleme
- **Page Object Model**: Sürdürülebilir test yapısı
- **Comprehensive Logging**: Detaylı test logları
- **Random Test Data**: Faker kütüphanesi ile rastgele test verisi
- **JUnit 5**: Modern test framework

## Proje Yapısı

```
src/test/java/proje/com/saucedemo/
├── config/
│   └── WebDriverConfig.java          # WebDriver konfigürasyonu
├── pages/
│   ├── HomePage.java                 # Ana sayfa
│   ├── SignupLoginPage.java          # Kayıt/Giriş sayfası
│   ├── ProductsPage.java             # Ürünler sayfası
│   ├── CartPage.java                 # Sepet sayfası
│   ├── CheckoutPage.java             # Ödeme sayfası
│   └── PaymentPage.java              # Ödeme işlemi
├── utils/
│   ├── NetworkTracer.java            # DevTools network izleme
│   ├── SeleniumTracer.java           # Selenium işlem izleme
│   └── TestDataGenerator.java        # Test verisi üretimi
├── verification/
│   └── VerificationHelper.java       # Doğrulama yardımcısı
└── AutomationExerciseCompleteTest.java # Ana test sınıfı
```

## Kurulum

### Gereksinimler

- Java 21
- Maven 3.6+
- Chrome Browser

### Çalıştırma

```bash
# Projeyi derle
mvn clean compile

# Testleri çalıştır
mvn test

# Belirli bir test sınıfını çalıştır
mvn test -Dtest=AutomationExerciseCompleteTest
```

## DevTools Network Monitoring

Proje, Chrome DevTools API kullanarak network trafiğini gerçek zamanlı olarak izler:

- HTTP isteklerini ve yanıtlarını yakalar
- Network hatalarını tespit eder
- Detaylı logging sağlar
- Performance analizi yapar

## Test Senaryoları

1. **Hesap Oluşturma**: Yeni kullanıcı kaydı
2. **Ürün Ekleme**: Sepete rastgele ürün ekleme
3. **Sepet Doğrulama**: Sepetteki ürünleri kontrol etme
4. **Ödeme İşlemi**: Checkout ve ödeme tamamlama
5. **Sipariş Doğrulama**: Sipariş tamamlanmasını kontrol etme

## Logging

Proje kapsamlı logging sağlar:

- Console ve dosya logları
- Network trafiği logları
- Selenium işlem logları
- Test adım logları

Loglar `logs/` klasöründe saklanır.

## Konfigürasyon

### WebDriver Konfigürasyonu

`src/test/java/proje/com/saucedemo/config/WebDriverConfig.java` dosyasında:

- Browser türü seçimi
- Timeout ayarları
- Driver path konfigürasyonu

### Logging Konfigürasyonu

`src/test/resources/logback-test.xml` dosyasında:

- Log seviyeleri
- Appender konfigürasyonu
- Format ayarları

## Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır. 