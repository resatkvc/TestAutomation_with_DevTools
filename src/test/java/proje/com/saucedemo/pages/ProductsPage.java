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
 * Page Object for AutomationExercise Products Page
 */
public class ProductsPage {
    private static final Logger logger = LoggerFactory.getLogger(ProductsPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to products page
     */
    public void navigateToProducts() {
        logger.info("Navigating to products page");
        driver.get("https://www.automationexercise.com/products");
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsTitle));
        logger.info("Successfully navigated to products page");
    }

    /**
     * Get all product cards
     */
    public List<WebElement> getProductCards() {
        logger.info("Getting all product cards");
        return driver.findElements(productCards);
    }

    /**
     * Get product names
     */
    public List<String> getProductNames() {
        logger.info("Getting product names");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get product prices
     */
    public List<String> getProductPrices() {
        logger.info("Getting product prices");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get all add to cart buttons
     */
    public List<WebElement> getAddToCartButtons() {
        logger.info("Getting all add to cart buttons");
        return driver.findElements(addToCartButtons);
    }

    /**
     * Add random product to cart
     */
    public void addRandomProductToCart() {
        logger.info("Adding random product to cart");
        List<WebElement> addToCartButtons = getAddToCartButtons();
        
        if (!addToCartButtons.isEmpty()) {
            int randomIndex = (int) (Math.random() * addToCartButtons.size());
            WebElement randomButton = addToCartButtons.get(randomIndex);
            
            // Scroll to the button
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", randomButton);
            
            wait.until(ExpectedConditions.elementToBeClickable(randomButton));
            randomButton.click();
            
            // Wait for success message or modal
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-content")));
                logger.info("Product added to cart successfully");
            } catch (Exception e) {
                logger.info("Product added to cart (no modal detected)");
            }
        } else {
            logger.warn("No add to cart buttons found");
        }
    }

    /**
     * Add specific product to cart by index
     */
    public void addProductToCartByIndex(int index) {
        logger.info("Adding product to cart by index: {}", index);
        List<WebElement> addToCartButtons = getAddToCartButtons();
        
        if (index >= 0 && index < addToCartButtons.size()) {
            WebElement button = addToCartButtons.get(index);
            
            // Scroll to the button
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            
            wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
            
            // Wait for success message or modal
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-content")));
                logger.info("Product added to cart successfully");
            } catch (Exception e) {
                logger.info("Product added to cart (no modal detected)");
            }
        } else {
            logger.warn("Invalid index: {}", index);
        }
    }

    /**
     * Search for products
     */
    public void searchProducts(String searchTerm) {
        logger.info("Searching for products: {}", searchTerm);
        
        WebElement searchInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        searchInputElement.clear();
        searchInputElement.sendKeys(searchTerm);
        
        WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchBtn.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchResults));
        logger.info("Search completed");
    }

    /**
     * Click on category
     */
    public void clickCategory(String category) {
        logger.info("Clicking on category: {}", category);
        
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
                logger.warn("Unknown category: {}", category);
                return;
        }
        
        WebElement categoryElement = wait.until(ExpectedConditions.elementToBeClickable(categoryLocator));
        categoryElement.click();
        logger.info("Category clicked successfully");
    }

    /**
     * Click on brand
     */
    public void clickBrand(String brand) {
        logger.info("Clicking on brand: {}", brand);
        
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
                logger.warn("Unknown brand: {}", brand);
                return;
        }
        
        WebElement brandElement = wait.until(ExpectedConditions.elementToBeClickable(brandLocator));
        brandElement.click();
        logger.info("Brand clicked successfully");
    }

    /**
     * Click on View Cart button
     */
    public void clickViewCart() {
        logger.info("Clicking on View Cart button");
        try {
            WebElement viewCartBtn = wait.until(ExpectedConditions.elementToBeClickable(viewCartButton));
            viewCartBtn.click();
            logger.info("Successfully clicked on View Cart button");
        } catch (Exception e) {
            logger.info("View Cart button not found, navigating to cart page directly");
            driver.get("https://www.automationexercise.com/view_cart");
        }
    }

    /**
     * Click on Continue Shopping button
     */
    public void clickContinueShopping() {
        logger.info("Clicking on Continue Shopping button");
        try {
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
            continueBtn.click();
            logger.info("Successfully clicked on Continue Shopping button");
        } catch (Exception e) {
            logger.warn("Continue Shopping button not found");
        }
    }

    /**
     * Get search results count
     */
    public int getSearchResultsCount() {
        logger.info("Getting search results count");
        List<WebElement> results = driver.findElements(searchResults);
        return results.size();
    }

    /**
     * Check if page is loaded
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
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
} 