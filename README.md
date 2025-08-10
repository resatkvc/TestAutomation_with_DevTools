# TestAutomation

Selenium WebDriver kullanarak test otomasyonu projesi.

## 🚀 Özellikler

- **Selenium WebDriver** desteği
- **Page Object Model** tasarım deseni
- **Test Data Generation** - Otomatik test verisi üretimi
- **Comprehensive Testing** - Kapsamlı test senaryoları
- **Retry Mechanism** - Hata durumunda yeniden deneme
- **Detailed Logging** - Detaylı log kayıtları

## 📋 Gereksinimler

- Java 21+
- Maven 3.6+
- Chrome Browser
- Selenium WebDriver 4.18.1+

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

3. **Chrome versiyonunu kontrol edin:**
```bash
chrome --version
```

## 🧪 Testleri Çalıştırma

### Tüm testleri çalıştırma:
```bash
mvn test
```

### Belirli test sınıfını çalıştırma:
```bash
mvn test -Dtest=DevToolsTest
mvn test -Dtest=NetworkMonitoringTest
mvn test -Dtest=AutomationExerciseCompleteTest
```



## 📁 Proje Yapısı

```
src/test/java/proje/com/saucedemo/
├── config/
│   └── WebDriverConfig.java          # WebDriver konfigürasyonu
├── utils/
│   └── TestDataGenerator.java        # Test verisi üretici
├── pages/                            # Page Object Model
│   ├── HomePage.java
│   ├── ProductsPage.java
│   ├── CartPage.java
│   ├── CheckoutPage.java
│   ├── PaymentPage.java
│   └── SignupLoginPage.java
├── verification/
│   └── VerificationHelper.java       # Doğrulama yardımcıları
├── DevToolsTest.java                 # DevTools test sınıfı
├── NetworkMonitoringTest.java        # Network izleme testi
└── AutomationExerciseCompleteTest.java # Tam otomasyon testi
```

## 🔧 DevTools Özellikleri

### Network Monitoring
- HTTP isteklerini ve yanıtlarını izleme
- Başarısız istekleri yakalama
- İstek türlerini belirleme (script, stylesheet, image, vb.)

### Console Monitoring
- JavaScript console loglarını yakalama
- Log seviyelerini (info, warning, error) izleme
- Kaynak bilgilerini toplama

### Performance Monitoring
- Sayfa yükleme performansını izleme
- DOM Content Loaded olaylarını yakalama
- Performans metriklerini toplama

### Runtime Monitoring
- JavaScript hatalarını yakalama
- Console API çağrılarını izleme
- Stack trace bilgilerini toplama

## 📊 Test Sonuçları

Testler çalıştırıldığında aşağıdaki bilgiler loglanır:

- **Network Requests**: Yakalanan HTTP istek sayısı
- **Console Logs**: JavaScript console log sayısı
- **JavaScript Errors**: JavaScript hata sayısı
- **Security Events**: Güvenlik olay sayısı
- **Performance Metrics**: Performans metrikleri

## 🐛 Sorun Giderme

### DevTools Başlatılamıyorsa:
1. Chrome versiyonunun 138+ olduğundan emin olun
2. ChromeDriver'ın güncel olduğunu kontrol edin
3. Chrome'u yeniden başlatın

### Network İstekleri Yakalanmıyorsa:
1. DevTools'un başarıyla başlatıldığını kontrol edin
2. Sayfa yükleme süresini artırın
3. Network monitoring'in etkin olduğunu doğrulayın

## 📝 Loglar

Proje SLF4J ve Logback kullanarak detaylı loglama yapar. Loglar `target/test-classes/logback-test.xml` dosyasında yapılandırılır.

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.