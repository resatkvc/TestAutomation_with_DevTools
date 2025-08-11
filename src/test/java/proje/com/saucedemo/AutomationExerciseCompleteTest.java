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

import proje.com.saucedemo.verification.VerificationHelper;

import java.util.List;

/**
 * Selenium DevTools Automation - Complete AutomationExercise test automation
 * Tests the full e-commerce flow from signup/login to order completion
 * Includes comprehensive network monitoring using DevTools API
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutomationExerciseCompleteTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AutomationExerciseCompleteTest.class);
    private static final String BASE_URL = "https://www.automationexercise.com";
    
    private static WebDriver driver;
    private static WebDriverConfig webDriverConfig;
    private static VerificationHelper verificationHelper;

    
    // Page Objects
    private static HomePage homePage;
    private static SignupLoginPage signupLoginPage;
    private static ProductsPage productsPage;
    private static CartPage cartPage;
    private static CheckoutPage checkoutPage;
    private static PaymentPage paymentPage;
    
    // Test Data
    private static String userEmail;
    private static String userPassword;
    private static String userName;
    
    @BeforeAll
    static void setUp() {
        try {
            logger.info("=== Setting up Selenium Automation test suite ===");
            
            // Initialize WebDriver
            webDriverConfig = new WebDriverConfig();
            driver = webDriverConfig.initializeDriver("chrome");
            
            // Enable test step monitoring - sadece kullanıcı etkileşimlerini izle
            if (webDriverConfig.isDevToolsAvailable()) {
                webDriverConfig.enableTestStepMonitoring();
                logger.info("Test step monitoring enabled - only user interactions and form submissions will be logged");
            } else {
                logger.warn("DevTools not available - test step monitoring disabled");
            }
            
            // Initialize verification helper
            verificationHelper = new VerificationHelper(driver);
            

            
            // Initialize page objects
            homePage = new HomePage(driver);
            signupLoginPage = new SignupLoginPage(driver);
            productsPage = new ProductsPage(driver);
            cartPage = new CartPage(driver);
            checkoutPage = new CheckoutPage(driver);
            paymentPage = new PaymentPage(driver);
            
            // Generate test data
            TestDataGenerator.UserInfo userInfo = TestDataGenerator.generateUserInfo();
            userEmail = userInfo.getEmail();
            userPassword = userInfo.getPassword();
            userName = userInfo.getFirstName() + " " + userInfo.getLastName();
            
            logger.info("Selenium Automation setup completed successfully");
            
        } catch (Exception e) {
            logger.error("Test setup failed: {}", e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterAll
    static void tearDown() {
        try {
            logger.info("=== Cleaning up Selenium Automation test resources ===");
            

            
            // Quit WebDriver
            if (driver != null) {
                webDriverConfig.quitDriver();
                logger.info("WebDriver quit successfully");
            }
            
            logger.info("=== Selenium Automation test suite cleanup completed ===");
            
        } catch (Exception e) {
            logger.error("Test cleanup failed: {}", e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Step 1: Navigate to AutomationExercise and Create Account")
    void testCreateAccount() {
        long startTime = System.currentTimeMillis();
        
        try {
            logger.info("=== Step 1: Creating new account ===");
            
            // Navigate to the site with explicit wait
            logger.info("Navigating to: {}", BASE_URL);
            driver.get(BASE_URL);
            
            // Wait for page to load completely
            Thread.sleep(3000);
            logger.info("Page loaded successfully");
            

            
            // Click signup/login with retry mechanism
            logger.info("Attempting to click signup/login link...");
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    homePage.clickSignupLogin();
                    logger.info("Successfully clicked signup/login link");
                    break;
                } catch (Exception e) {
                    logger.warn("Attempt {} failed to click signup/login: {}", i + 1, e.getMessage());
                    if (i == maxRetries - 1) throw e;
                    Thread.sleep(2000);
                }
            }
            
            // Wait for signup page to load
            Thread.sleep(3000);
            
            // Start signup process
            logger.info("Starting signup for user: {} with email: {}", userName, userEmail);
            signupLoginPage.startSignup(userName, userEmail);
            
            // Check if email already exists
            Thread.sleep(2000);
            if (signupLoginPage.isSignupEmailExists()) {
                logger.info("Email already exists, trying with different email");
                userEmail = "test" + System.currentTimeMillis() + "@example.com";
                signupLoginPage.startSignup(userName, userEmail);
                Thread.sleep(2000);
            }
            
            // Fill account information
            logger.info("Filling account information...");
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
            logger.info("Account information filled successfully");
            
            // Create account
            logger.info("Creating account...");
            signupLoginPage.createAccount();
            
            // Wait for account creation
            Thread.sleep(5000);
            
            // Verify account creation
            boolean accountCreated = signupLoginPage.isAccountCreated();
            verificationHelper.verifyAccountCreated(accountCreated);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Account creation verification completed in {} ms", duration);

            
            // Log network statistics for this test step
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Step 1 Network Statistics: {}", networkStats);
            }
            
            logger.info("=== Step 1 completed: Account created successfully ===");
            
        } catch (Exception e) {
            logger.error("Step 1 failed: {}", e.getMessage());
            logger.error("Stack trace: ", e);

            throw new RuntimeException("Step 1 failed", e);
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Step 2: Add Random Products to Cart")
    void testAddProductsToCart() {
        try {
            logger.info("=== Step 2: Adding products to cart ===");
            
            // Navigate to products page with retry
            logger.info("Navigating to products page...");
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    productsPage.navigateToProducts();
                    Thread.sleep(3000);
                    verificationHelper.verifyPageLoaded("Products page", productsPage.isPageLoaded());
                    logger.info("Successfully navigated to products page");
                    break;
                } catch (Exception e) {
                    logger.warn("Attempt {} failed to navigate to products: {}", i + 1, e.getMessage());
                    if (i == maxRetries - 1) throw e;
                    Thread.sleep(2000);
                }
            }
            
            // Add first product with retry
            logger.info("Adding first product to cart...");
            int productRetries = 3;
            for (int i = 0; i < productRetries; i++) {
                try {
                    productsPage.addRandomProductToCart();
                    Thread.sleep(3000);
                    productsPage.clickContinueShopping();
                    Thread.sleep(2000);
                    logger.info("Added first product to cart successfully");
                    break;
                } catch (Exception e) {
                    logger.warn("Attempt {} failed to add first product: {}", i + 1, e.getMessage());
                    if (i == productRetries - 1) throw e;
                    Thread.sleep(3000);
                }
            }
            
            // Add second product with retry
            logger.info("Adding second product to cart...");
            for (int i = 0; i < productRetries; i++) {
                try {
                    productsPage.addRandomProductToCart();
                    Thread.sleep(3000);
                    logger.info("Added second product to cart successfully");
                    break;
                } catch (Exception e) {
                    logger.warn("Attempt {} failed to add second product: {}", i + 1, e.getMessage());
                    if (i == productRetries - 1) throw e;
                    Thread.sleep(3000);
                }
            }
            

            
            // Log network statistics for this test step
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Step 2 Network Statistics: {}", networkStats);
            }
            
            logger.info("=== Step 2 completed: Products added to cart ===");
            
        } catch (Exception e) {
            logger.error("Step 2 failed: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("Step 2 failed", e);
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Step 3: Navigate to Cart and Verify Products")
    void testVerifyCartProducts() {
        try {
            logger.info("=== Step 3: Verifying cart products ===");
            
            // Navigate to cart with retry
            logger.info("Navigating to cart page...");
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    cartPage.navigateToCart();
                    Thread.sleep(3000);
                    verificationHelper.verifyPageLoaded("Cart page", cartPage.isPageLoaded());
                    logger.info("Successfully navigated to cart page");
                    break;
                } catch (Exception e) {
                    logger.warn("Attempt {} failed to navigate to cart: {}", i + 1, e.getMessage());
                    if (i == maxRetries - 1) throw e;
                    Thread.sleep(2000);
                }
            }
            
            // Verify cart is not empty
            Thread.sleep(2000);
            verificationHelper.verifyCartNotEmpty(cartPage.getCartItemsCount() > 0);
            
            // Get and verify products
            List<String> cartProductNames = cartPage.getProductNames();
            logger.info("Products in cart: {}", cartProductNames);
            
            for (String productName : cartProductNames) {
                verificationHelper.verifyProductInCart(cartPage.containsProduct(productName), productName);
            }
            
            logger.info("Cart Products: {}", String.join(", ", cartProductNames));
            
            // Log network statistics for this test step
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Step 3 Network Statistics: {}", networkStats);
            }
            
            logger.info("=== Step 3 completed: Cart verification successful ===");
            
        } catch (Exception e) {
            logger.error("Step 3 failed: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("Step 3 failed", e);
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Step 4: Complete Checkout and Payment")
    void testCompleteCheckoutAndPayment() {
        try {
            logger.info("=== Step 4: Completing checkout and payment ===");
            
            // Proceed to checkout with retry
            logger.info("Proceeding to checkout...");
            int checkoutRetries = 3;
            for (int i = 0; i < checkoutRetries; i++) {
                try {
                    cartPage.clickProceedToCheckout();
                    Thread.sleep(5000);
                    logger.info("Successfully proceeded to checkout");
                    break;
                } catch (Exception e) {
                    logger.warn("Attempt {} failed to proceed to checkout: {}", i + 1, e.getMessage());
                    if (i == checkoutRetries - 1) throw e;
                    Thread.sleep(3000);
                }
            }
            
            // Fill delivery address
            logger.info("Filling delivery address...");
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
            
            // Add comment and place order
            logger.info("Adding comment and placing order...");
            checkoutPage.addComment("DevTools test order - " + System.currentTimeMillis());
            checkoutPage.clickPlaceOrder();
            Thread.sleep(5000);
            
            logger.info("Checkout Details - Name: {} {} | Address: {} | City: {} | Country: {}", 
                checkoutInfo.getFirstName(), checkoutInfo.getLastName(),
                checkoutInfo.getAddress(), checkoutInfo.getCity(), checkoutInfo.getCountry());
            
            logger.info("Order placed successfully");
            
            // Complete payment
            logger.info("Completing payment...");
            paymentPage.completePaymentWithRandomData();
            Thread.sleep(3000);
            
            verificationHelper.verifyOrderPlaced(paymentPage.isOrderPlaced());
            
            String confirmationMessage = paymentPage.getOrderConfirmationMessage();
            logger.info("Order confirmation: {}", confirmationMessage);
            
            // Download invoice and continue
            logger.info("Downloading invoice and continuing...");
            paymentPage.clickDownloadInvoice();
            Thread.sleep(2000);
            paymentPage.clickContinue();
            Thread.sleep(2000);
            

            // Log network statistics for this test step
            if (webDriverConfig.isDevToolsAvailable()) {
                var networkStats = webDriverConfig.getNetworkStats();
                logger.info("Step 4 Network Statistics: {}", networkStats);
            }
            
            logger.info("Payment completed successfully");
            
            logger.info("=== Step 4 completed: Full checkout and payment successful ===");
            
        } catch (Exception e) {
            logger.error("Step 4 failed: {}", e.getMessage());
            logger.error("Stack trace: ", e);

            throw new RuntimeException("Step 4 failed", e);
        }
    }
} 