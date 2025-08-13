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
 * AutomationExercise Sepet Sayfası için Page Object
 */
public class CartPage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator'lar - GitHub projesine göre güncellendi
    private final By cartTitle = By.cssSelector("#cart_info_table");
    private final By cartItems = By.cssSelector("#cart_info_table tbody tr");
    private final By productNames = By.cssSelector(".cart_description h4 a");
    private final By productPrices = By.cssSelector(".cart_price p");
    private final By productQuantities = By.cssSelector(".cart_quantity button");
    private final By totalPrices = By.cssSelector(".cart_total_price p");
    private final By deleteButtons = By.cssSelector(".cart_quantity_delete");
    private final By proceedToCheckoutButton = By.cssSelector(".btn.check_out");
    private final By continueShoppingButton = By.cssSelector(".btn.btn-default");
    private final By emptyCartMessage = By.cssSelector(".empty_cart");
    private final By cartEmptyMessage = By.cssSelector(".empty_cart p");
    private final By quantityInputs = By.cssSelector(".cart_quantity input");
    private final By updateCartButton = By.cssSelector(".btn.btn-default.btn-xs");
    private final By cartTotal = By.cssSelector(".cart_total_price p");
    private final By cartPageTitle = By.cssSelector(".breadcrumbs h2");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Sepet sayfasına git
     */
    public void navigateToCart() {
        logger.info("Sepet sayfasına gidiliyor");
        driver.get("https://www.automationexercise.com/view_cart");
        
        // Sepet sayfasının yüklenmesini bekle - GitHub projesine göre birden fazla locator dene
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTitle));
        } catch (Exception e) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitle));
            } catch (Exception e2) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".breadcrumbs")));
            }
        }
        
        logger.info("Sepet sayfasına başarıyla gidildi");
    }

    /**
     * Tüm sepet öğelerini al
     */
    public List<WebElement> getCartItems() {
        logger.info("Tüm sepet öğeleri alınıyor");
        return driver.findElements(cartItems);
    }

    /**
     * Sepetteki ürün isimlerini al
     */
    public List<String> getProductNames() {
        logger.info("Sepetteki ürün isimleri alınıyor");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Sepetteki ürün fiyatlarını al
     */
    public List<String> getProductPrices() {
        logger.info("Sepetteki ürün fiyatları alınıyor");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Sepetteki ürün miktarlarını al
     */
    public List<String> getProductQuantities() {
        logger.info("Sepetteki ürün miktarları alınıyor");
        List<WebElement> elements = driver.findElements(productQuantities);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Sepetteki toplam fiyatları al
     */
    public List<String> getTotalPrices() {
        logger.info("Sepetteki toplam fiyatlar alınıyor");
        List<WebElement> elements = driver.findElements(totalPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * İndekse göre ürünü sepetten sil
     */
    public void deleteProductByIndex(int index) {
        logger.info("İndekse göre ürün sepetten siliniyor: {}", index);
        List<WebElement> deleteButtons = driver.findElements(this.deleteButtons);
        
        if (index >= 0 && index < deleteButtons.size()) {
            WebElement deleteButton = deleteButtons.get(index);
            deleteButton.click();
            logger.info("Ürün sepetten silindi");
        } else {
            logger.warn("Geçersiz indeks: {}", index);
        }
    }

    /**
     * Sepetteki tüm ürünleri sil
     */
    public void deleteAllProducts() {
        logger.info("Sepetteki tüm ürünler siliniyor");
        List<WebElement> deleteButtons = driver.findElements(this.deleteButtons);
        
        for (WebElement deleteButton : deleteButtons) {
            deleteButton.click();
            try {
                Thread.sleep(1000); // Silme işleminin tamamlanmasını bekle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Sepetteki tüm ürünler silindi");
    }

    /**
     * İndekse göre ürün miktarını güncelle
     */
    public void updateProductQuantity(int index, String newQuantity) {
        logger.info("İndekse göre ürün miktarı güncelleniyor: {} to: {}", index, newQuantity);
        List<WebElement> quantityInputs = driver.findElements(this.quantityInputs);
        
        if (index >= 0 && index < quantityInputs.size()) {
            WebElement quantityInput = quantityInputs.get(index);
            quantityInput.clear();
            quantityInput.sendKeys(newQuantity);
            
            // Güncelle butonu varsa tıkla
            try {
                WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(updateCartButton));
                updateButton.click();
                logger.info("Ürün miktarı güncellendi");
            } catch (Exception e) {
                logger.warn("Güncelle butonu bulunamadı");
            }
        } else {
            logger.warn("Geçersiz indeks: {}", index);
        }
    }

    /**
     * Ödemeye Geç butonuna tıkla
     */
    public void clickProceedToCheckout() {
        logger.info("Ödemeye Geç butonuna tıklanıyor");
        WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutButton));
        proceedButton.click();
        logger.info("Ödemeye Geç butonuna başarıyla tıklandı");
    }

    /**
     * Alışverişe Devam Et butonuna tıkla
     */
    public void clickContinueShopping() {
        logger.info("Alışverişe Devam Et butonuna tıklanıyor");
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
        continueButton.click();
        logger.info("Alışverişe Devam Et butonuna başarıyla tıklandı");
    }

    /**
     * Sepetin boş olup olmadığını kontrol et
     */
    public boolean isCartEmpty() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sepet boş mesajını al
     */
    public String getCartEmptyMessage() {
        try {
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cartEmptyMessage));
            return messageElement.getText();
        } catch (Exception e) {
            return "Sepet boş değil";
        }
    }

    /**
     * Sepet öğeleri sayısını al
     */
    public int getCartItemsCount() {
        logger.info("Sepet öğeleri sayısı alınıyor");
        List<WebElement> items = getCartItems();
        return items.size();
    }

    /**
     * Toplam sepet değerini al
     */
    public String getTotalCartValue() {
        logger.info("Toplam sepet değeri alınıyor");
        try {
            List<WebElement> totalElements = driver.findElements(cartTotal);
            if (!totalElements.isEmpty()) {
                return totalElements.get(totalElements.size() - 1).getText();
            }
        } catch (Exception e) {
            logger.warn("Toplam sepet değeri alınamadı");
        }
        return "0";
    }

    /**
     * Sayfanın yüklenip yüklenmediğini kontrol et
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitle));
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
     * Sepetin belirli ürünü içerip içermediğini doğrula
     */
    public boolean containsProduct(String productName) {
        logger.info("Sepetin ürün içerip içermediği kontrol ediliyor: {}", productName);
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
        List<WebElement> quantityElements = driver.findElements(quantityInputs);
        
        for (int i = 0; i < productElements.size(); i++) {
            if (productElements.get(i).getText().contains(productName)) {
                return quantityElements.get(i).getAttribute("value");
            }
        }
        return "0";
    }
} 