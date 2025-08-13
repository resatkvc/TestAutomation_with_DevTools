package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * AutomationExercise Ödeme Sayfası için Page Object
 */
public class CheckoutPage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator'lar
    private final By checkoutTitle = By.cssSelector(".breadcrumbs h2");
    private final By addressDetailsTitle = By.cssSelector(".address_details h3");
    private final By reviewOrderTitle = By.cssSelector(".review-order h3");
    private final By commentField = By.cssSelector("textarea[name='message']");
    private final By placeOrderButton = By.cssSelector("a[href='/payment']");
    private final By nameField = By.id("name");
    private final By emailField = By.id("email");
    private final By addressField = By.id("address");
    private final By cityField = By.id("city");
    private final By stateField = By.id("state");
    private final By zipcodeField = By.id("zipcode");
    private final By mobileNumberField = By.id("mobile_number");
    private final By countryField = By.id("country");
    private final By deliveryAddress = By.cssSelector("#address_delivery");
    private final By billingAddress = By.cssSelector("#address_invoice");
    private final By orderSummary = By.cssSelector(".table-responsive");
    private final By totalAmount = By.cssSelector(".cart_total_price p");
    private final By productNames = By.cssSelector(".cart_description h4 a");
    private final By productPrices = By.cssSelector(".cart_price p");
    private final By productQuantities = By.cssSelector(".cart_quantity button");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Ödeme sayfasına git
     */
    public void navigateToCheckout() {
        logger.info("Ödeme sayfasına gidiliyor");
        driver.get("https://www.automationexercise.com/checkout");
        
        // Ödeme sayfasının yüklenmesini bekle - birden fazla locator dene
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".breadcrumbs")));
        } catch (Exception e) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#address_delivery")));
            } catch (Exception e2) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".address_details")));
            }
        }
        
        // Sayfanın tamamen yüklenmesi için ek bekleme
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("Ödeme sayfasına başarıyla gidildi");
    }

    /**
     * Teslimat adresi bilgilerini doldur
     */
    public void fillDeliveryAddress(String name, String email, String address, String city, 
                                   String state, String zipcode, String mobileNumber, String country) {
        logger.info("Teslimat adresi bilgileri dolduruluyor");
        
        // İsim doldur - GitHub projesine göre birden fazla locator dene
        WebElement nameElement;
        try {
            // Önce en yaygın locator'ı dene
            nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name']")));
        } catch (Exception e) {
            try {
                // ID ile dene
                nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
            } catch (Exception e2) {
                try {
                    // Placeholder ile dene
                    nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder*='Name']")));
                } catch (Exception e3) {
                    try {
                        // Type ve name attribute ile dene
                        nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text'][name*='name']")));
                    } catch (Exception e4) {
                        try {
                            // Name içeren herhangi bir input ile dene
                            nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name*='name']")));
                        } catch (Exception e5) {
                            // İlk isim alanı ile dene
                            nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='first_name']")));
                        }
                    }
                }
            }
        }
        nameElement.clear();
        nameElement.sendKeys(name);
        
        // Email doldur
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailElement.clear();
        emailElement.sendKeys(email);
        
        // Adres doldur
        WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(addressField));
        addressElement.clear();
        addressElement.sendKeys(address);
        
        // Şehir doldur
        WebElement cityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cityField));
        cityElement.clear();
        cityElement.sendKeys(city);
        
        // Eyalet doldur
        WebElement stateElement = wait.until(ExpectedConditions.visibilityOfElementLocated(stateField));
        stateElement.clear();
        stateElement.sendKeys(state);
        
        // Posta kodu doldur
        WebElement zipcodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(zipcodeField));
        zipcodeElement.clear();
        zipcodeElement.sendKeys(zipcode);
        
        // Mobil numara doldur
        WebElement mobileElement = wait.until(ExpectedConditions.visibilityOfElementLocated(mobileNumberField));
        mobileElement.clear();
        mobileElement.sendKeys(mobileNumber);
        
        // Ülke doldur
        WebElement countryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(countryField));
        countryElement.clear();
        countryElement.sendKeys(country);
        
        logger.info("Teslimat adresi bilgileri başarıyla dolduruldu");
    }

    /**
     * Siparişe yorum ekle
     */
    public void addComment(String comment) {
        logger.info("Siparişe yorum ekleniyor: {}", comment);
        WebElement commentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(commentField));
        commentElement.clear();
        commentElement.sendKeys(comment);
        logger.info("Yorum başarıyla eklendi");
    }

    /**
     * Sipariş Ver butonuna tıkla
     */
    public void clickPlaceOrder() {
        logger.info("Sipariş Ver butonuna tıklanıyor");
        WebElement placeOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
        placeOrderBtn.click();
        logger.info("Sipariş Ver butonuna başarıyla tıklandı");
    }

    /**
     * Teslimat adresi detaylarını al
     */
    public String getDeliveryAddress() {
        logger.info("Teslimat adresi detayları alınıyor");
        try {
            WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryAddress));
            return addressElement.getText();
        } catch (Exception e) {
            logger.warn("Teslimat adresi alınamadı");
            return "";
        }
    }

    /**
     * Fatura adresi detaylarını al
     */
    public String getBillingAddress() {
        logger.info("Fatura adresi detayları alınıyor");
        try {
            WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(billingAddress));
            return addressElement.getText();
        } catch (Exception e) {
            logger.warn("Fatura adresi alınamadı");
            return "";
        }
    }

    /**
     * Sipariş özetini al
     */
    public String getOrderSummary() {
        logger.info("Sipariş özeti alınıyor");
        try {
            WebElement summaryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderSummary));
            return summaryElement.getText();
        } catch (Exception e) {
            logger.warn("Sipariş özeti alınamadı");
            return "";
        }
    }

    /**
     * Toplam tutarı al
     */
    public String getTotalAmount() {
        logger.info("Toplam tutar alınıyor");
        try {
            List<WebElement> totalElements = driver.findElements(totalAmount);
            if (!totalElements.isEmpty()) {
                return totalElements.get(totalElements.size() - 1).getText();
            }
        } catch (Exception e) {
            logger.warn("Toplam tutar alınamadı");
        }
        return "0";
    }

    /**
     * Ödeme sayfasındaki ürün isimlerini al
     */
    public List<String> getProductNames() {
        logger.info("Ödeme sayfasındaki ürün isimleri alınıyor");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Ödeme sayfasındaki ürün fiyatlarını al
     */
    public List<String> getProductPrices() {
        logger.info("Ödeme sayfasındaki ürün fiyatları alınıyor");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Ödeme sayfasındaki ürün miktarlarını al
     */
    public List<String> getProductQuantities() {
        logger.info("Ödeme sayfasındaki ürün miktarları alınıyor");
        List<WebElement> elements = driver.findElements(productQuantities);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Sayfanın yüklenip yüklenmediğini kontrol et
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutTitle));
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
     * Ödeme sayfasının belirli ürünü içerip içermediğini doğrula
     */
    public boolean containsProduct(String productName) {
        logger.info("Ödeme sayfasının ürün içerip içermediği kontrol ediliyor: {}", productName);
        List<String> productNames = getProductNames();
        return productNames.stream()
                .anyMatch(name -> name.contains(productName));
    }

    /**
     * Ürün adına göre ürün miktarını al
     */
    public String getProductQuantityByName(String productName) {
        logger.info("Ürün için miktar alınıyor: {}", productName);
        List<WebElement> productElements = driver.findElements(productNames);
        List<WebElement> quantityElements = driver.findElements(productQuantities);
        
        for (int i = 0; i < productElements.size(); i++) {
            if (productElements.get(i).getText().contains(productName)) {
                return quantityElements.get(i).getText();
            }
        }
        return "0";
    }

    /**
     * Ürün adına göre ürün fiyatını al
     */
    public String getProductPriceByName(String productName) {
        logger.info("Ürün için fiyat alınıyor: {}", productName);
        List<WebElement> productElements = driver.findElements(productNames);
        List<WebElement> priceElements = driver.findElements(productPrices);
        
        for (int i = 0; i < productElements.size(); i++) {
            if (productElements.get(i).getText().contains(productName)) {
                return priceElements.get(i).getText();
            }
        }
        return "0";
    }
} 