package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.utils.ZipkinTracer;
import proje.com.saucedemo.utils.SeleniumTracer;

import java.time.Duration;

/**
 * Login page object for SauceDemo
 */
public class LoginPage {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final SeleniumTracer seleniumTracer;
    
    // Page elements
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.seleniumTracer = new SeleniumTracer(driver);
    }
    
    /**
     * Navigate to login page
     * @param baseUrl Base URL of the application
     */
    public void navigateToLoginPage(String baseUrl) {
        try {
            ZipkinTracer.startSpan("navigate-to-login-page");
            ZipkinTracer.addTag("base_url", baseUrl);
            
            String loginUrl = baseUrl + "/";
            driver.get(loginUrl);
            
            // Wait for login page to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            
            logger.info("Successfully navigated to login page: {}", loginUrl);
            ZipkinTracer.addTag("login_page_loaded", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to navigate to login page: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Navigation to login page failed", e);
        }
    }
    
    /**
     * Enter username
     * @param username Username to enter
     */
    public void enterUsername(String username) {
        try {
            ZipkinTracer.startSpan("enter-username");
            ZipkinTracer.addTag("username", username);
            
            WebElement usernameElement = seleniumTracer.waitForElement(usernameField, "Username Field");
            seleniumTracer.sendKeys(usernameElement, username, "Username Input");
            
            logger.info("Username entered: {}", username);
            ZipkinTracer.addTag("username_entered", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to enter username: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to enter username", e);
        }
    }
    
    /**
     * Enter password
     * @param password Password to enter
     */
    public void enterPassword(String password) {
        try {
            ZipkinTracer.startSpan("enter-password");
            ZipkinTracer.addTag("password_provided", password != null ? "true" : "false");
            
            WebElement passwordElement = seleniumTracer.waitForElement(passwordField, "Password Field");
            seleniumTracer.sendKeys(passwordElement, password, "Password Input");
            
            logger.info("Password entered successfully");
            ZipkinTracer.addTag("password_entered", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to enter password: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to enter password", e);
        }
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        try {
            ZipkinTracer.startSpan("click-login-button");
            
            WebElement loginElement = seleniumTracer.waitForElement(loginButton, "Login Button");
            seleniumTracer.click(loginElement, "Login Button Click");
            
            logger.info("Login button clicked");
            ZipkinTracer.addTag("login_button_clicked", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Failed to click login button: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Failed to click login button", e);
        }
    }
    
    /**
     * Perform login with username and password
     * @param username Username
     * @param password Password
     */
    public void login(String username, String password) {
        try {
            ZipkinTracer.startSpan("perform-login");
            ZipkinTracer.addTag("username", username);
            ZipkinTracer.addTag("password_provided", password != null ? "true" : "false");
            
            enterUsername(username);
            enterPassword(password);
            clickLoginButton();
            
            logger.info("Login performed for user: {}", username);
            ZipkinTracer.addTag("login_performed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Login failed", e);
        }
    }
    
    /**
     * Check if error message is displayed
     * @return True if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-error-message");
            
            boolean errorDisplayed = driver.findElements(errorMessage).size() > 0;
            
            if (errorDisplayed) {
                String errorText = driver.findElement(errorMessage).getText();
                logger.warn("Error message displayed: {}", errorText);
                ZipkinTracer.addTag("error_message", errorText);
            }
            
            ZipkinTracer.addTag("error_displayed", String.valueOf(errorDisplayed));
            ZipkinTracer.finishSpan();
            
            return errorDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check error message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
    
    /**
     * Get error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        try {
            ZipkinTracer.startSpan("get-error-message");
            
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String errorText = errorElement.getText();
            
            logger.info("Error message retrieved: {}", errorText);
            ZipkinTracer.addTag("error_message_text", errorText);
            ZipkinTracer.finishSpan();
            
            return errorText;
            
        } catch (Exception e) {
            logger.error("Failed to get error message: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return "";
        }
    }
    
    /**
     * Check if login page is displayed
     * @return True if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        try {
            ZipkinTracer.startSpan("check-login-page-displayed");
            
            boolean loginPageDisplayed = driver.getCurrentUrl().contains("/") && 
                    driver.findElements(usernameField).size() > 0 &&
                    driver.findElements(passwordField).size() > 0;
            
            logger.info("Login page displayed: {}", loginPageDisplayed);
            ZipkinTracer.addTag("login_page_displayed", String.valueOf(loginPageDisplayed));
            ZipkinTracer.finishSpan();
            
            return loginPageDisplayed;
            
        } catch (Exception e) {
            logger.error("Failed to check login page display: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            return false;
        }
    }
} 