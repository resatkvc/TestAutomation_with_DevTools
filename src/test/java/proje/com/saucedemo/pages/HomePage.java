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
 * Page Object for AutomationExercise Home Page
 */
public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to the home page
     */
    public void navigateToHome() {
        logger.info("Navigating to AutomationExercise home page");
        driver.get("https://www.automationexercise.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        logger.info("Successfully navigated to home page");
    }

    /**
     * Click on Signup/Login link
     */
    public void clickSignupLogin() {
        logger.info("Clicking on Signup/Login link");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink));
        element.click();
        logger.info("Successfully clicked on Signup/Login link");
    }

    /**
     * Click on Products link
     */
    public void clickProducts() {
        logger.info("Clicking on Products link");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(productsLink));
        element.click();
        logger.info("Successfully clicked on Products link");
    }

    /**
     * Click on Cart link
     */
    public void clickCart() {
        logger.info("Clicking on Cart link");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(cartLink));
        element.click();
        logger.info("Successfully clicked on Cart link");
    }

    /**
     * Subscribe to newsletter
     */
    public void subscribeToNewsletter(String email) {
        logger.info("Subscribing to newsletter with email: {}", email);
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(subscriptionEmail));
        emailField.clear();
        emailField.sendKeys(email);
        
        WebElement subscribeBtn = wait.until(ExpectedConditions.elementToBeClickable(subscribeButton));
        subscribeBtn.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        logger.info("Successfully subscribed to newsletter");
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
     * Get product names from the page
     */
    public List<String> getProductNames() {
        logger.info("Getting product names from the page");
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Get product prices from the page
     */
    public List<String> getProductPrices() {
        logger.info("Getting product prices from the page");
        List<WebElement> elements = driver.findElements(productPrices);
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Check if page is loaded
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