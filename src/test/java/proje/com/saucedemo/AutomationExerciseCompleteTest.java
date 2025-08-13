package proje.com.saucedemo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import io.qameta.allure.*;
// import io.qameta.allure.junit5.AllureJunit5;
import proje.com.saucedemo.config.WebDriverConfig;
import proje.com.saucedemo.pages.*;
import proje.com.saucedemo.utils.TestDataGenerator;
import proje.com.saucedemo.utils.HTMLReportGenerator;

import proje.com.saucedemo.verification.VerificationHelper;

import java.util.List;

/**
 * Selenium DevTools Otomasyonu - Tam AutomationExercise test otomasyonu
 * Kayıt/giriş'ten sipariş tamamlamaya kadar tam e-ticaret akışını test eder
 * DevTools API kullanarak kapsamlı network izleme içerir
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutomationExerciseCompleteTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AutomationExerciseCompleteTest.class);
    private static final String BASE_URL = "https://www.automationexercise.com";
    
    private static WebDriver driver;
    private static WebDriverConfig webDriverConfig;
    private static VerificationHelper verificationHelper;

    
    // Sayfa Nesneleri
    private static HomePage homePage;
    private static SignupLoginPage signupLoginPage;
    private static ProductsPage productsPage;
    private static CartPage cartPage;
    private static CheckoutPage checkoutPage;
    private static PaymentPage paymentPage;
    
    // Test Verileri
    private static String userEmail;
    private static String userPassword;
    private static String userName;
    
    @BeforeAll
    static void setUp() {
        try {
            logger.info("=== Selenium Otomasyon test paketi kuruluyor ===");
            
            // WebDriver'ı başlat
            webDriverConfig = new WebDriverConfig();
            driver = webDriverConfig.initializeDriver("chrome");
            
            // Test adımı izlemeyi etkinleştir - sadece kullanıcı etkileşimlerini izle
            if (webDriverConfig.isDevToolsAvailable()) {
                webDriverConfig.enableTestStepMonitoring();
                logger.info("Test adımı izleme etkinleştirildi - sadece kullanıcı etkileşimleri ve form gönderimleri loglanacak");
            } else {
                logger.warn("DevTools kullanılamıyor - test adımı izleme devre dışı");
            }
            
            // Doğrulama yardımcısını başlat
            verificationHelper = new VerificationHelper(driver);
            

            
            // Sayfa nesnelerini başlat
            homePage = new HomePage(driver);
            signupLoginPage = new SignupLoginPage(driver);
            productsPage = new ProductsPage(driver);
            cartPage = new CartPage(driver);
            checkoutPage = new CheckoutPage(driver);
            paymentPage = new PaymentPage(driver);
            
            // Test verilerini oluştur
            TestDataGenerator.UserInfo userInfo = TestDataGenerator.generateUserInfo();
            userEmail = userInfo.getEmail();
            userPassword = userInfo.getPassword();
            userName = userInfo.getFirstName() + " " + userInfo.getLastName();
            
            logger.info("Selenium Otomasyon kurulumu başarıyla tamamlandı");
            
        } catch (Exception e) {
            logger.error("Test kurulumu başarısız: {}", e.getMessage());
            throw new RuntimeException("Test kurulumu başarısız", e);
        }
    }
    
    @AfterAll
    static void tearDown() {
        try {
            logger.info("=== Selenium Otomasyon test kaynakları temizleniyor ===");
            

            
            // HTML Raporları oluştur
            logger.info("=== HTML Raporları Oluşturuluyor ===");
            HTMLReportGenerator reportGenerator = new HTMLReportGenerator();
            reportGenerator.generateAllReports();
            logger.info("HTML raporları başarıyla oluşturuldu");
            
            // WebDriver'ı kapat
            if (driver != null) {
                webDriverConfig.quitDriver();
                logger.info("WebDriver başarıyla kapatıldı");
            }
            
            logger.info("=== Selenium Otomasyon test paketi temizliği tamamlandı ===");
            
        } catch (Exception e) {
            logger.error("Test temizliği başarısız: {}", e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Adım 1: AutomationExercise'e Git ve Hesap Oluştur")
    void testCreateAccount() {
        long startTime = System.currentTimeMillis();
        
        try {
            logger.info("=== Adım 1: Yeni hesap oluşturuluyor ===");
            
            // Siteye açık bekleme ile git
            logger.info("Siteye gidiliyor: {}", BASE_URL);
            driver.get(BASE_URL);
            
            // Sayfanın tamamen yüklenmesini bekle
            Thread.sleep(3000);
            logger.info("Sayfa başarıyla yüklendi");
            

            
            // Kayıt/giriş linkine yeniden deneme mekanizması ile tıkla
            logger.info("Kayıt/giriş linkine tıklamaya çalışılıyor...");
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    homePage.clickSignupLogin();
                    logger.info("Kayıt/giriş linkine başarıyla tıklandı");
                    break;
                } catch (Exception e) {
                    logger.warn("Deneme {} kayıt/giriş tıklaması başarısız: {}", i + 1, e.getMessage());
                    if (i == maxRetries - 1) throw e;
                    Thread.sleep(2000);
                }
            }
            
            // Kayıt sayfasının yüklenmesini bekle
            Thread.sleep(3000);
            
            // Kayıt sürecini başlat
            logger.info("Kullanıcı için kayıt başlatılıyor: {} e-posta ile: {}", userName, userEmail);
            signupLoginPage.startSignup(userName, userEmail);
            
            // E-posta zaten var mı kontrol et
            Thread.sleep(2000);
            if (signupLoginPage.isSignupEmailExists()) {
                logger.info("E-posta zaten mevcut, farklı e-posta ile deneme yapılıyor");
                userEmail = "test" + System.currentTimeMillis() + "@example.com";
                signupLoginPage.startSignup(userName, userEmail);
                Thread.sleep(2000);
            }
            
            // Hesap bilgilerini doldur
            logger.info("Hesap bilgileri dolduruluyor...");
            TestDataGenerator.AccountInfo accountInfo = TestDataGenerator.generateAccountInfo();
            signupLoginPage.fillAccountInformation(
                accountInfo.getTitle(),
                userPassword,
                accountInfo.getDay(),
                accountInfo.getMonth(),
                accountInfo.getYear(),
                accountInfo.getFirstName(),
                accountInfo.getLastName(),
                accountInfo.getCompany(),
                accountInfo.getAddress1(),
                accountInfo.getAddress2(),
                accountInfo.getCountry(),
                accountInfo.getState(),
                accountInfo.getCity(),
                accountInfo.getZipcode(),
                accountInfo.getMobileNumber()
            );
            logger.info("Hesap bilgileri başarıyla dolduruldu");
            
            // Hesabı oluştur
            logger.info("Hesap oluşturuluyor...");
            signupLoginPage.createAccount();
            
            // Hesap oluşturma işlemini bekle
            Thread.sleep(5000);
            
            // Hesap oluşturmayı doğrula
            boolean accountCreated = signupLoginPage.isAccountCreated();
            verificationHelper.verifyAccountCreated(accountCreated);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Hesap oluşturma doğrulaması {} ms'de tamamlandı", duration);

            
            // Bu test adımı için ağ istatistiklerini logla
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Adım 1 Ağ İstatistikleri: {}", networkStats);
            }
            
            logger.info("=== Adım 1 tamamlandı: Hesap başarıyla oluşturuldu ===");
            
        } catch (Exception e) {
            logger.error("Adım 1 başarısız: {}", e.getMessage());
            logger.error("Stack trace: ", e);

            throw new RuntimeException("Adım 1 başarısız", e);
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Adım 2: Sepete Rastgele Ürünler Ekle")
    void testAddProductsToCart() {
        try {
            logger.info("=== Adım 2: Sepete ürünler ekleniyor ===");
            
            // Ürünler sayfasına yeniden deneme ile git
            logger.info("Ürünler sayfasına gidiliyor...");
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    productsPage.navigateToProducts();
                    Thread.sleep(3000);
                    verificationHelper.verifyPageLoaded("Ürünler sayfası", productsPage.isPageLoaded());
                    logger.info("Ürünler sayfasına başarıyla gidildi");
                    break;
                } catch (Exception e) {
                    logger.warn("Deneme {} ürünler sayfasına gitme başarısız: {}", i + 1, e.getMessage());
                    if (i == maxRetries - 1) throw e;
                    Thread.sleep(2000);
                }
            }
            
            // İlk ürünü yeniden deneme ile ekle
            logger.info("İlk ürün sepete ekleniyor...");
            int productRetries = 3;
            for (int i = 0; i < productRetries; i++) {
                try {
                    productsPage.addRandomProductToCart();
                    Thread.sleep(3000);
                    productsPage.clickContinueShopping();
                    Thread.sleep(2000);
                    logger.info("İlk ürün sepete başarıyla eklendi");
                    break;
                } catch (Exception e) {
                    logger.warn("Deneme {} ilk ürün ekleme başarısız: {}", i + 1, e.getMessage());
                    if (i == productRetries - 1) throw e;
                    Thread.sleep(3000);
                }
            }
            
            // İkinci ürünü yeniden deneme ile ekle
            logger.info("İkinci ürün sepete ekleniyor...");
            for (int i = 0; i < productRetries; i++) {
                try {
                    productsPage.addRandomProductToCart();
                    Thread.sleep(3000);
                    logger.info("İkinci ürün sepete başarıyla eklendi");
                    break;
                } catch (Exception e) {
                    logger.warn("Deneme {} ikinci ürün ekleme başarısız: {}", i + 1, e.getMessage());
                    if (i == productRetries - 1) throw e;
                    Thread.sleep(3000);
                }
            }
            

            
            // Bu test adımı için ağ istatistiklerini logla
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Adım 2 Ağ İstatistikleri: {}", networkStats);
            }
            
            logger.info("=== Adım 2 tamamlandı: Ürünler sepete eklendi ===");
            
        } catch (Exception e) {
            logger.error("Adım 2 başarısız: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("Adım 2 başarısız", e);
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Adım 3: Sepete Git ve Ürünleri Doğrula")
    void testVerifyCartProducts() {
        try {
            logger.info("=== Adım 3: Sepet ürünleri doğrulanıyor ===");
            
            // Sepete yeniden deneme ile git
            logger.info("Sepet sayfasına gidiliyor...");
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    cartPage.navigateToCart();
                    Thread.sleep(3000);
                    verificationHelper.verifyPageLoaded("Sepet sayfası", cartPage.isPageLoaded());
                    logger.info("Sepet sayfasına başarıyla gidildi");
                    break;
                } catch (Exception e) {
                    logger.warn("Deneme {} sepete gitme başarısız: {}", i + 1, e.getMessage());
                    if (i == maxRetries - 1) throw e;
                    Thread.sleep(2000);
                }
            }
            
            // Sepetin boş olmadığını doğrula
            Thread.sleep(2000);
            verificationHelper.verifyCartNotEmpty(cartPage.getCartItemsCount() > 0);
            
            // Ürünleri al ve doğrula
            List<String> cartProductNames = cartPage.getProductNames();
            logger.info("Sepetteki ürünler: {}", cartProductNames);
            
            for (String productName : cartProductNames) {
                verificationHelper.verifyProductInCart(cartPage.containsProduct(productName), productName);
            }
            
            logger.info("Sepet Ürünleri: {}", String.join(", ", cartProductNames));
            
            // Bu test adımı için ağ istatistiklerini logla
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Adım 3 Ağ İstatistikleri: {}", networkStats);
            }
            
            logger.info("=== Adım 3 tamamlandı: Sepet doğrulaması başarılı ===");
            
        } catch (Exception e) {
            logger.error("Adım 3 başarısız: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("Adım 3 başarısız", e);
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Adım 4: Ödeme ve Ödeme İşlemini Tamamla")
    void testCompleteCheckoutAndPayment() {
        try {
            logger.info("=== Adım 4: Ödeme ve ödeme işlemi tamamlanıyor ===");
            
            // Ödemeye yeniden deneme ile devam et
            logger.info("Ödemeye devam ediliyor...");
            int checkoutRetries = 3;
            for (int i = 0; i < checkoutRetries; i++) {
                try {
                    cartPage.clickProceedToCheckout();
                    Thread.sleep(5000);
                    logger.info("Ödemeye başarıyla devam edildi");
                    break;
                } catch (Exception e) {
                    logger.warn("Deneme {} ödemeye devam etme başarısız: {}", i + 1, e.getMessage());
                    if (i == checkoutRetries - 1) throw e;
                    Thread.sleep(3000);
                }
            }
            
            // Teslimat adresini doldur
            logger.info("Teslimat adresi dolduruluyor...");
            TestDataGenerator.CheckoutInfo checkoutInfo = TestDataGenerator.generateCheckoutInfo();
            
            checkoutPage.fillDeliveryAddress(
                checkoutInfo.getFirstName() + " " + checkoutInfo.getLastName(),
                userEmail,
                checkoutInfo.getAddress(),
                checkoutInfo.getCity(),
                checkoutInfo.getState(),
                checkoutInfo.getPostalCode(),
                checkoutInfo.getPhone(),
                checkoutInfo.getCountry()
            );
            Thread.sleep(2000);
            
            // Yorum ekle ve sipariş ver
            logger.info("Yorum ekleniyor ve sipariş veriliyor...");
            checkoutPage.addComment("DevTools test siparişi - " + System.currentTimeMillis());
            checkoutPage.clickPlaceOrder();
            Thread.sleep(5000);
            
            logger.info("Ödeme Detayları - İsim: {} {} | Adres: {} | Şehir: {} | Ülke: {}", 
                checkoutInfo.getFirstName(), checkoutInfo.getLastName(),
                checkoutInfo.getAddress(), checkoutInfo.getCity(), checkoutInfo.getCountry());
            
            logger.info("Sipariş başarıyla verildi");
            
            // Ödemeyi tamamla
            logger.info("Ödeme tamamlanıyor...");
            paymentPage.completePaymentWithRandomData();
            Thread.sleep(3000);
            
            verificationHelper.verifyOrderPlaced(paymentPage.isOrderPlaced());
            
            String confirmationMessage = paymentPage.getOrderConfirmationMessage();
            logger.info("Sipariş onayı: {}", confirmationMessage);
            
            // Faturayı indir ve devam et
            logger.info("Fatura indiriliyor ve devam ediliyor...");
            paymentPage.clickDownloadInvoice();
            Thread.sleep(2000);
            paymentPage.clickContinue();
            Thread.sleep(2000);
            

            // Bu test adımı için ağ istatistiklerini logla
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Adım 4 Ağ İstatistikleri: {}", networkStats);
            }
            
            logger.info("Ödeme başarıyla tamamlandı");
            
            logger.info("=== Adım 4 tamamlandı: Tam ödeme ve ödeme işlemi başarılı ===");
            
        } catch (Exception e) {
            logger.error("Adım 4 başarısız: {}", e.getMessage());
            logger.error("Stack trace: ", e);

            throw new RuntimeException("Adım 4 başarısız", e);
        }
    }
} 