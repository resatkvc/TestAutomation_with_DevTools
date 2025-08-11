# TestAutomation_with_DevTools

Modern web test otomasyonu projesi - Selenium WebDriver ve Chrome DevTools Protocol (CDP) entegrasyonu ile güçlendirilmiş e-ticaret test otomasyonu.

## 🎯 Proje Amacı

Bu proje, **AutomationExercise** web sitesi üzerinde tam bir e-ticaret akışını test eder ve **Chrome DevTools Protocol** kullanarak test adımlarının network trafiğini gerçek zamanlı olarak izler.

## 🚀 Özellikler

- **Selenium WebDriver 4.34.0** - En güncel Selenium sürümü
- **Chrome DevTools Protocol (CDP)** - Test adımlarının network trafiğini izleme
- **WebDriverManager** - Otomatik driver yönetimi
- **JUnit 5** - Modern test framework
- **Page Object Model** - Sürdürülebilir test yapısı
- **Test Data Generation** - Otomatik test verisi üretimi
- **Smart Network Filtering** - Sadece test adımlarıyla ilgili network trafiğini izleme

## 📋 Gereksinimler

### Kritik Versiyon Uyumluluğu ⚠️

**DevTools entegrasyonu için versiyonların eşleşmesi ÇOK ÖNEMLİ:**

- **Java 21+**
- **Maven 3.6+**
- **Chrome 139** (DevTools için)
- **Selenium 4.34.0** (Chrome 139 ile uyumlu)
- **selenium-devtools-v138** (Chrome 139 için)

### Versiyon Kontrolü

```bash
# Chrome versiyonunu kontrol et
chrome --version

# Java versiyonunu kontrol et
java -version

# Maven versiyonunu kontrol et
mvn -version
```

**⚠️ ÖNEMLİ:** Chrome versiyonunuz ile selenium-devtools paketinin versiyonu eşleşmelidir:
- Chrome 139 → selenium-devtools-v138
- Chrome 140 → selenium-devtools-v139 (gelecekte)

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

Bu proje, Chrome 139 versiyonunuz için `selenium-devtools-v138` paketini kullanır ve şu özellikleri sağlar:

#### 🌐 Test Step Network Monitoring
- **Sadece test adımlarıyla ilgili** network trafiğini izler
- CSS, JS, resim dosyaları gibi statik kaynakları filtreler
- Google Analytics çağrılarını filtreler
- GET, POST, PUT, DELETE metodlarını yakalar
- Response sürelerini ölçer

#### 📊 Akıllı Filtreleme Sistemi
- **Statik Kaynak Filtreleme**: CSS, JS, resim, font dosyaları
- **Analytics Filtreleme**: Google Analytics, Google Ads çağrıları
- **Test Adımı Kategorileri**: Sayfa navigasyonları, form gönderimleri
- **Görsel Durum İkonları**: ✅ Başarılı, ❌ Hata, ⚠️ Uyarı

#### 📝 Gelişmiş Logging
- Test adımı türlerini emoji ile gösterir
- Request ID'leri ile eşleştirme
- Response sürelerini milisaniye cinsinden
- HTTP durum kodlarını görsel olarak

## 🧪 Test Senaryosu

### E-Ticaret Test Akışı

Proje, **AutomationExercise** sitesi üzerinde tam bir e-ticaret akışını test eder:

1. **Hesap Oluşturma** - Yeni kullanıcı kaydı
2. **Ürün Ekleme** - Sepete rastgele ürünler ekleme
3. **Sepet Doğrulama** - Sepet içeriğini kontrol etme
4. **Ödeme Tamamlama** - Checkout ve ödeme işlemi

### Test Çalıştırma

```bash
# Tüm test akışını çalıştır
mvn test -Dtest=AutomationExerciseCompleteTest

# Sadece hesap oluşturma adımını çalıştır
mvn test -Dtest=AutomationExerciseCompleteTest#testCreateAccount

# Sadece ürün ekleme adımını çalıştır
mvn test -Dtest=AutomationExerciseCompleteTest#testAddProductsToCart
```

## 📊 Test Adımlarının Network İzlenmesi

### Nasıl Çalışır?

1. **DevTools Session Başlatma:**
```java
// WebDriver başlatılırken otomatik olarak DevTools session oluşturulur
WebDriverConfig config = new WebDriverConfig();
driver = config.initializeDriver("chrome");
```

2. **Test Step Monitoring Etkinleştirme:**
```java
// Sadece test adımlarıyla ilgili network trafiğini izle
config.enableTestStepMonitoring();
```

3. **Akıllı Filtreleme:**
- Statik kaynaklar (CSS, JS, resimler) filtrelenir
- Google Analytics çağrıları filtrelenir
- Sadece test adımlarıyla ilgili istekler loglanır

### Örnek Log Çıktısı

```
[TEST-STEP][Network] GET 12345 -> https://automationexercise.com/ | 🏠 HOME_PAGE
[TEST-STEP][Network] Response ✅ 200 12345 -> https://automationexercise.com/ (150ms) | 🏠 HOME_PAGE

[TEST-STEP][Network] GET 12346 -> https://automationexercise.com/signup | 🔐 AUTH_PAGE
[TEST-STEP][Network] Response ✅ 200 12346 -> https://automationexercise.com/signup (120ms) | 🔐 AUTH_PAGE

[TEST-STEP][Network] POST 12347 -> https://automationexercise.com/signup | ✅ SIGNUP_SUBMIT
[TEST-STEP][Network] Response ✅ 200 12347 -> https://automationexercise.com/signup (200ms) | ✅ SIGNUP_SUBMIT
```

## 📁 Proje Yapısı

```
TestAutomation_with_DevTools/
├── pom.xml                          # Maven konfigürasyonu
├── README.md                        # Proje dokümantasyonu
└── src/
    └── test/
        ├── java/proje/com/saucedemo/
        │   ├── AutomationExerciseCompleteTest.java  # Ana test dosyası
        │   ├── config/
        │   │   └── WebDriverConfig.java             # WebDriver + DevTools konfigürasyonu
        │   ├── utils/
        │   │   ├── DevToolsHelper.java              # DevTools yardımcı sınıfı
        │   │   └── TestDataGenerator.java           # Test verisi üretici
        │   ├── pages/                               # Page Object Model
        │   │   ├── HomePage.java                    # Ana sayfa
        │   │   ├── SignupLoginPage.java             # Kayıt/Giriş sayfası
        │   │   ├── ProductsPage.java                # Ürünler sayfası
        │   │   ├── CartPage.java                    # Sepet sayfası
        │   │   ├── CheckoutPage.java                # Ödeme sayfası
        │   │   └── PaymentPage.java                 # Ödeme işlemi
        │   └── verification/
        │       └── VerificationHelper.java          # Doğrulama yardımcısı
        └── resources/
            └── logback-test.xml                     # Logging konfigürasyonu
```

## 🔍 DevTools API Referansı

### DevToolsHelper Sınıfı

#### Ana Metodlar:
- `enableTestStepMonitoring()` - Test adımlarının network trafiğini izle
- `enableNetworkMonitoring()` - Tüm network trafiğini izle
- `enableConsoleLogging()` - Console log yakalama
- `getNetworkStats()` - Network istatistikleri
- `close()` - DevTools oturumunu kapat

#### Test Step Kategorileri:
- 🏠 HOME_PAGE - Ana sayfa
- 🔐 AUTH_PAGE - Giriş/Kayıt sayfaları
- 🛍️ PRODUCTS_PAGE - Ürün sayfaları
- 🛒 CART_PAGE - Sepet sayfası
- 💳 CHECKOUT_PAGE - Ödeme sayfası
- ✅ SIGNUP_SUBMIT - Kayıt gönderimi
- ✅ ADD_TO_CART - Sepete ekleme

### WebDriverConfig Sınıfı

#### DevTools Metodları:
- `enableTestStepMonitoring()` - Test adımlarının network trafiğini izle
- `enableDevToolsMonitoring()` - Tüm monitoring özelliklerini etkinleştir
- `getNetworkStats()` - Network istatistikleri
- `isDevToolsAvailable()` - DevTools kullanılabilir mi?

## 🐛 Sorun Giderme

### Kritik DevTools Sorunları

1. **Chrome versiyonu uyumsuzluğu:**
   ```
   WARNING: Unable to find an exact match for CDP version 139, returning the closest version; found: 138
   ```
   **Çözüm:** Chrome'u güncelleyin veya selenium-devtools paketini değiştirin

2. **DevTools session hatası:**
   ```
   DevTools not available for test step monitoring
   ```
   **Çözüm:** WebDriver'ı yeniden başlatın, Chrome'u tamamen kapatıp açın

3. **Network monitoring çalışmıyor:**
   **Çözüm:** Chrome options'da `--remote-allow-origins=*` olduğundan emin olun

### Versiyon Kontrol Listesi

- [ ] Chrome versiyonu: 139
- [ ] Selenium versiyonu: 4.34.0
- [ ] selenium-devtools-v138 paketi
- [ ] Java versiyonu: 21+
- [ ] Maven versiyonu: 3.6+

## 📈 Performans İpuçları

1. **Test Step Monitoring'i sadece gerektiğinde etkinleştirin**
2. **Büyük test suite'lerde DevTools session'larını düzgün kapatın**
3. **Memory leak'leri önlemek için listener'ları temizleyin**
4. **Network istatistiklerini test sonunda kontrol edin**

## 🔗 Faydalı Linkler

- [Chrome DevTools Protocol](https://chromedevtools.github.io/devtools-protocol/)
- [Selenium DevTools Documentation](https://www.selenium.dev/documentation/webdriver/bidirectional/chrome_devtools/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
- [JUnit 5](https://junit.org/junit5/)
- [AutomationExercise Test Site](https://www.automationexercise.com/)

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

---

**TestAutomation_with_DevTools** - Modern web test otomasyonu için güçlü DevTools entegrasyonu ile geliştirilmiştir.