package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * AutomationExercise Ödeme Sayfası için Page Object
 */
public class PaymentPage {
    private static final Logger logger = LoggerFactory.getLogger(PaymentPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator'lar
    private final By paymentTitle = By.cssSelector(".breadcrumbs h2");
    private final By cardNameField = By.id("name_on_card");
    private final By cardNumberField = By.id("card_number");
    private final By cvcField = By.id("cvc");
    private final By expiryMonthField = By.id("expiry_month");
    private final By expiryYearField = By.id("expiry_year");
    private final By payAndConfirmOrderButton = By.id("submit");
    private final By orderPlacedMessage = By.cssSelector(".alert-success");
    private final By orderDetails = By.cssSelector(".table-responsive");
    private final By downloadInvoiceButton = By.cssSelector("a[href='/download_invoice/0']");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");
    private final By orderConfirmationMessage = By.cssSelector(".alert-success p");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Ödeme sayfasına git
     */
    public void navigateToPayment() {
        logger.info("Ödeme sayfasına gidiliyor");
        driver.get("https://www.automationexercise.com/payment");
        wait.until(ExpectedConditions.visibilityOfElementLocated(paymentTitle));
        logger.info("Ödeme sayfasına başarıyla gidildi");
    }

    /**
     * Kart bilgilerini doldur
     */
    public void fillCardInformation(String cardName, String cardNumber, String cvc, 
                                   String expiryMonth, String expiryYear) {
        logger.info("Kart bilgileri dolduruluyor");
        
        // Kart sahibi adını doldur
        WebElement cardNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNameField));
        cardNameElement.clear();
        cardNameElement.sendKeys(cardName);
        
        // Kart numarasını doldur
        WebElement cardNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberField));
        cardNumberElement.clear();
        cardNumberElement.sendKeys(cardNumber);
        
        // CVC doldur
        WebElement cvcElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cvcField));
        cvcElement.clear();
        cvcElement.sendKeys(cvc);
        
        // Son kullanma ayını doldur
        WebElement expiryMonthElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryMonthField));
        expiryMonthElement.clear();
        expiryMonthElement.sendKeys(expiryMonth);
        
        // Son kullanma yılını doldur
        WebElement expiryYearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryYearField));
        expiryYearElement.clear();
        expiryYearElement.sendKeys(expiryYear);
        
        logger.info("Kart bilgileri başarıyla dolduruldu");
    }

    /**
     * Öde ve Siparişi Onayla butonuna tıkla
     */
    public void clickPayAndConfirmOrder() {
        logger.info("Öde ve Siparişi Onayla butonuna tıklanıyor");
        WebElement payButton = wait.until(ExpectedConditions.elementToBeClickable(payAndConfirmOrderButton));
        payButton.click();
        logger.info("Öde ve Siparişi Onayla butonuna başarıyla tıklandı");
    }

    /**
     * Siparişin başarıyla verilip verilmediğini kontrol et
     */
    public boolean isOrderPlaced() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(orderPlacedMessage));
            logger.info("Sipariş başarıyla verildi");
            return true;
        } catch (Exception e) {
            logger.warn("Sipariş verme başarısız");
            return false;
        }
    }

    /**
     * Sipariş onay mesajını al
     */
    public String getOrderConfirmationMessage() {
        logger.info("Sipariş onay mesajı alınıyor");
        try {
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderConfirmationMessage));
            return messageElement.getText();
        } catch (Exception e) {
            logger.warn("Sipariş onay mesajı alınamadı");
            return "";
        }
    }

    /**
     * Sipariş detaylarını al
     */
    public String getOrderDetails() {
        logger.info("Sipariş detayları alınıyor");
        try {
            WebElement detailsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderDetails));
            return detailsElement.getText();
        } catch (Exception e) {
            logger.warn("Sipariş detayları alınamadı");
            return "";
        }
    }

    /**
     * Fatura İndir butonuna tıkla
     */
    public void clickDownloadInvoice() {
        logger.info("Fatura İndir butonuna tıklanıyor");
        try {
            WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(downloadInvoiceButton));
            downloadButton.click();
            logger.info("Fatura İndir butonuna başarıyla tıklandı");
        } catch (Exception e) {
            logger.warn("Fatura İndir butonu bulunamadı");
        }
    }

    /**
     * Devam Et butonuna tıkla
     */
    public void clickContinue() {
        logger.info("Devam Et butonuna tıklanıyor");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();
        logger.info("Devam Et butonuna başarıyla tıklandı");
    }

    /**
     * Rastgele kart verileriyle ödeme sürecini tamamla
     */
    public void completePaymentWithRandomData() {
        logger.info("Rastgele kart verileriyle ödeme tamamlanıyor");
        
        // Rastgele kart verileri oluştur
        String cardName = "Test User " + System.currentTimeMillis();
        String cardNumber = "4111111111111111"; // Test kart numarası
        String cvc = String.valueOf(100 + (int)(Math.random() * 900)); // Rastgele 3 haneli CVC
        String expiryMonth = String.valueOf(1 + (int)(Math.random() * 12)); // Rastgele ay 1-12
        String expiryYear = String.valueOf(2025 + (int)(Math.random() * 10)); // Rastgele yıl 2025-2034
        
        fillCardInformation(cardName, cardNumber, cvc, expiryMonth, expiryYear);
        clickPayAndConfirmOrder();
        
        logger.info("Rastgele verilerle ödeme tamamlandı");
    }

    /**
     * Sayfanın yüklenip yüklenmediğini kontrol et
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(paymentTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sayfa başlığını al
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Mevcut URL'yi al
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Ödeme formunun görüntülenip görüntülenmediğini doğrula
     */
    public boolean isPaymentFormDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cardNameField));
            wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberField));
            wait.until(ExpectedConditions.visibilityOfElementLocated(cvcField));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Kart sahibi adı alanı değerini al
     */
    public String getCardNameValue() {
        try {
            WebElement cardNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNameField));
            return cardNameElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Kart numarası alanı değerini al
     */
    public String getCardNumberValue() {
        try {
            WebElement cardNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberField));
            return cardNumberElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * CVC alanı değerini al
     */
    public String getCvcValue() {
        try {
            WebElement cvcElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cvcField));
            return cvcElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Son kullanma ayı alanı değerini al
     */
    public String getExpiryMonthValue() {
        try {
            WebElement expiryMonthElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryMonthField));
            return expiryMonthElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Son kullanma yılı alanı değerini al
     */
    public String getExpiryYearValue() {
        try {
            WebElement expiryYearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryYearField));
            return expiryYearElement.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }
} 