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
 * AutomationExercise Ürünler Sayfası için Page Object
 */
public class ProductsPage {
    private static final Logger logger = LoggerFactory.getLogger(ProductsPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator'lar
    private final By productsTitle = By.cssSelector(".features_items h2");
    private final By productCards = By.cssSelector(".single-products");
    private final By productNames = By.cssSelector(".single-products h2");
    private final By productPrices = By.cssSelector(".single-products p");
    private final By addToCartButtons = By.cssSelector(".add-to-cart");
    private final By viewProductButtons = By.cssSelector("a[href*='/product_details/']");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchResults = By.cssSelector(".features_items .single-products");
    private final By categoryWomen = By.cssSelector("a[href='#Women']");
    private final By categoryMen = By.cssSelector("a[href='#Men']");
    private final By categoryKids = By.cssSelector("a[href='#Kids']");
    private final By brandPolo = By.cssSelector("a[href='/brand_products/Polo']");
    private final By brandHM = By.cssSelector("a[href='/brand_products/H&M']");
    private final By brandMadame = By.cssSelector("a[href='/brand_products/Madame']");
    private final By brandMastHarbour = By.cssSelector("a[href='/brand_products/Mast & Harbour']");
    private final By brandBabyhug = By.cssSelector("a[href='/brand_products/Babyhug']");
    private final By brandAllenSolly = By.cssSelector("a[href='/brand_products/Allen Solly Junior']");
    private final By brandKookieKids = By.cssSelector("a[href='/brand_products/Kookie Kids']");
    private final By brandBiba = By.cssSelector("a[href='/brand_products/Biba']");
    private final By viewCartButton = By.cssSelector("a[href='/view_cart']");
    private final By continueShoppingButton = By.cssSelector(".btn-success");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Ürünler sayfasına git
     */
    public void navigateToProducts() {
        logger.info("Ürünler sayfasına gidiliyor");
        driver.get("https://www.automationexercise.com/products");
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsTitle));
        logger.info("Ürünler sayfasına başarıyla gidildi");
    }

    /**
     * Tüm ürün kartlarını al
     */
    public List<WebElement> getProductCards() {
        logger.info("Tüm ürün kartları alınıyor");
        return driver.findElements(productCards);
    }

    /**
     * Ürün isimlerini al
     */
    public List<String> getProductNames() {
        logger.info("Ürün isimleri alınıyor");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Ürün fiyatlarını al
     */
    public List<String> getProductPrices() {
        logger.info("Ürün fiyatları alınıyor");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
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
     * Belirli indeksteki ürünü sepete ekle
     */
    public void addProductToCartByIndex(int index) {
        logger.info("İndekse göre ürün sepete ekleniyor: {}", index);
        List<WebElement> addToCartButtons = getAddToCartButtons();
        
        if (index >= 0 && index < addToCartButtons.size()) {
            WebElement button = addToCartButtons.get(index);
            
            // Butona kaydır
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            
            wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
            
            // Başarı mesajı veya modal için bekle
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-content")));
                logger.info("Ürün başarıyla sepete eklendi");
            } catch (Exception e) {
                logger.info("Ürün sepete eklendi (modal tespit edilmedi)");
            }
        } else {
            logger.warn("Geçersiz indeks: {}", index);
        }
    }

    /**
     * Ürün ara
     */
    public void searchProducts(String searchTerm) {
        logger.info("Ürün aranıyor: {}", searchTerm);
        
        WebElement searchInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        searchInputElement.clear();
        searchInputElement.sendKeys(searchTerm);
        
        WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchBtn.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchResults));
        logger.info("Arama tamamlandı");
    }

    /**
     * Kategoriye tıkla
     */
    public void clickCategory(String category) {
        logger.info("Kategoriye tıklanıyor: {}", category);
        
        By categoryLocator;
        switch (category.toLowerCase()) {
            case "women":
                categoryLocator = categoryWomen;
                break;
            case "men":
                categoryLocator = categoryMen;
                break;
            case "kids":
                categoryLocator = categoryKids;
                break;
            default:
                logger.warn("Bilinmeyen kategori: {}", category);
                return;
        }
        
        WebElement categoryElement = wait.until(ExpectedConditions.elementToBeClickable(categoryLocator));
        categoryElement.click();
        logger.info("Kategori başarıyla tıklandı");
    }

    /**
     * Markaya tıkla
     */
    public void clickBrand(String brand) {
        logger.info("Markaya tıklanıyor: {}", brand);
        
        By brandLocator;
        switch (brand.toLowerCase()) {
            case "polo":
                brandLocator = brandPolo;
                break;
            case "h&m":
            case "hm":
                brandLocator = brandHM;
                break;
            case "madame":
                brandLocator = brandMadame;
                break;
            case "mast & harbour":
            case "mastandharbour":
                brandLocator = brandMastHarbour;
                break;
            case "babyhug":
                brandLocator = brandBabyhug;
                break;
            case "allen solly junior":
            case "allensolly":
                brandLocator = brandAllenSolly;
                break;
            case "kookie kids":
            case "kookiekids":
                brandLocator = brandKookieKids;
                break;
            case "biba":
                brandLocator = brandBiba;
                break;
            default:
                logger.warn("Bilinmeyen marka: {}", brand);
                return;
        }
        
        WebElement brandElement = wait.until(ExpectedConditions.elementToBeClickable(brandLocator));
        brandElement.click();
        logger.info("Marka başarıyla tıklandı");
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
     * Arama sonuçları sayısını al
     */
    public int getSearchResultsCount() {
        logger.info("Arama sonuçları sayısı alınıyor");
        List<WebElement> results = driver.findElements(searchResults);
        return results.size();
    }

    /**
     * Sayfanın yüklenip yüklenmediğini kontrol et
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productsTitle));
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