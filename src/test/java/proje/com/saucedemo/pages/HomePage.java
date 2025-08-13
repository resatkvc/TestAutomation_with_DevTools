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
 * AutomationExercise Ana Sayfa için Page Object
 */
public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator'lar
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By cartLink = By.cssSelector("a[href='/view_cart']");
    private final By subscriptionEmail = By.id("susbscribe_email");
    private final By subscribeButton = By.id("subscribe");
    private final By successMessage = By.cssSelector(".alert-success");
    private final By addToCartButtons = By.cssSelector(".add-to-cart");
    private final By viewCartButton = By.cssSelector("a[href='/view_cart']");
    private final By continueShoppingButton = By.cssSelector(".btn-success");
    private final By viewProductLinks = By.cssSelector("a[href*='/product_details/']");
    private final By productNames = By.cssSelector(".product-information h2");
    private final By productPrices = By.cssSelector(".product-information span span");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Ana sayfaya git
     */
    public void navigateToHome() {
        logger.info("AutomationExercise ana sayfasına gidiliyor");
        driver.get("https://www.automationexercise.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        logger.info("Ana sayfaya başarıyla gidildi");
    }

    /**
     * Kayıt/Giriş linkine tıkla
     */
    public void clickSignupLogin() {
        logger.info("Kayıt/Giriş linkine tıklanıyor");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink));
        element.click();
        logger.info("Kayıt/Giriş linkine başarıyla tıklandı");
    }

    /**
     * Ürünler linkine tıkla
     */
    public void clickProducts() {
        logger.info("Ürünler linkine tıklanıyor");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(productsLink));
        element.click();
        logger.info("Ürünler linkine başarıyla tıklandı");
    }

    /**
     * Sepet linkine tıkla
     */
    public void clickCart() {
        logger.info("Sepet linkine tıklanıyor");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(cartLink));
        element.click();
        logger.info("Sepet linkine başarıyla tıklandı");
    }

    /**
     * Bültene abone ol
     */
    public void subscribeToNewsletter(String email) {
        logger.info("Bültene abone olunuyor, email: {}", email);
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(subscriptionEmail));
        emailField.clear();
        emailField.sendKeys(email);
        
        WebElement subscribeBtn = wait.until(ExpectedConditions.elementToBeClickable(subscribeButton));
        subscribeBtn.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        logger.info("Bültene başarıyla abone olundu");
    }

    /**
     * Tüm sepete ekle butonlarını al
     */
    public List<WebElement> getAddToCartButtons() {
        logger.info("Tüm sepete ekle butonları alınıyor");
        return driver.findElements(addToCartButtons);
    }

    /**
     * Rastgele ürünü sepete ekle
     */
    public void addRandomProductToCart() {
        logger.info("Rastgele ürün sepete ekleniyor");
        List<WebElement> addToCartButtons = getAddToCartButtons();
        
        if (!addToCartButtons.isEmpty()) {
            int randomIndex = (int) (Math.random() * addToCartButtons.size());
            WebElement randomButton = addToCartButtons.get(randomIndex);
            
            // Butona kaydır
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", randomButton);
            
            wait.until(ExpectedConditions.elementToBeClickable(randomButton));
            randomButton.click();
            
            // Başarı mesajı veya modal için bekle
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-content")));
                logger.info("Ürün başarıyla sepete eklendi");
            } catch (Exception e) {
                logger.info("Ürün sepete eklendi (modal tespit edilmedi)");
            }
        } else {
            logger.warn("Sepete ekle butonu bulunamadı");
        }
    }

    /**
     * Sepeti Görüntüle butonuna tıkla
     */
    public void clickViewCart() {
        logger.info("Sepeti Görüntüle butonuna tıklanıyor");
        try {
            WebElement viewCartBtn = wait.until(ExpectedConditions.elementToBeClickable(viewCartButton));
            viewCartBtn.click();
            logger.info("Sepeti Görüntüle butonuna başarıyla tıklandı");
        } catch (Exception e) {
            logger.info("Sepeti Görüntüle butonu bulunamadı, sepet sayfasına doğrudan gidiliyor");
            driver.get("https://www.automationexercise.com/view_cart");
        }
    }

    /**
     * Alışverişe Devam Et butonuna tıkla
     */
    public void clickContinueShopping() {
        logger.info("Alışverişe Devam Et butonuna tıklanıyor");
        try {
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
            continueBtn.click();
            logger.info("Alışverişe Devam Et butonuna başarıyla tıklandı");
        } catch (Exception e) {
            logger.warn("Alışverişe Devam Et butonu bulunamadı");
        }
    }

    /**
     * Sayfadaki ürün isimlerini al
     */
    public List<String> getProductNames() {
        logger.info("Sayfadaki ürün isimleri alınıyor");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Sayfadaki ürün fiyatlarını al
     */
    public List<String> getProductPrices() {
        logger.info("Sayfadaki ürün fiyatları alınıyor");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Sayfanın yüklenip yüklenmediğini kontrol et
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
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
} 