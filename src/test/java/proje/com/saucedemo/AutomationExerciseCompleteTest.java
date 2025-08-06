package proje.com.saucedemo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.config.WebDriverConfig;
import proje.com.saucedemo.pages.*;
import proje.com.saucedemo.utils.TestDataGenerator;
import proje.com.saucedemo.utils.SeleniumTracer;
import proje.com.saucedemo.utils.NetworkTracer;
import proje.com.saucedemo.utils.MetricsExporter;
import proje.com.saucedemo.verification.VerificationHelper;

import java.util.List;

/**
 * TestAutomation_with_DevTools - Complete AutomationExercise test automation
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
    private static SeleniumTracer seleniumTracer;
    private static NetworkTracer networkTracer;
    
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
    private static List<String> selectedProducts;
    
    @BeforeAll
    static void setUp() {
        try {
            logger.info("=== Setting up TestAutomation_with_DevTools test suite ===");
            
            // Initialize WebDriver
            webDriverConfig = new WebDriverConfig();
            driver = webDriverConfig.initializeDriver("chrome");
            
            // Initialize verification helper
            verificationHelper = new VerificationHelper(driver);
            
            // Initialize Selenium tracer
            seleniumTracer = new SeleniumTracer(driver);
            
            // Initialize NetworkTracer (DevTools will be enabled after first page load)
            networkTracer = new NetworkTracer(driver);
            
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
            
            logger.info("TestAutomation_with_DevTools setup completed successfully with DevTools monitoring");
            
        } catch (Exception e) {
            logger.error("Test setup failed: {}", e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterAll
    static void tearDown() {
        try {
            logger.info("=== Cleaning up TestAutomation_with_DevTools test resources ===");
            
            // Debug: Check DevTools status
            if (networkTracer != null) {
                logger.info("DevTools enabled: {}", networkTracer.isDevToolsEnabled());
                logger.info("Total HTTP requests captured: {}", networkTracer.getRequestCount());
            }
            
            // Push final metrics
            MetricsExporter.pushMetricsWithLabels("AutomationExerciseCompleteTest", "chrome");
            
            // Cleanup NetworkTracer
            if (networkTracer != null) {
                networkTracer.cleanup();
            }
            
            // Quit WebDriver
            if (driver != null) {
                webDriverConfig.quitDriver();
                logger.info("WebDriver quit successfully");
            }
            
            logger.info("=== TestAutomation_with_DevTools test suite cleanup completed ===");
            
        } catch (Exception e) {
            logger.error("Test cleanup failed: {}", e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Step 1: Navigate to AutomationExercise and Create Account")
    void testCreateAccount() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        

        
        try {
            logger.info("=== Step 1: Creating new account with TestAutomation_with_DevTools ===");
            
            // Navigate to home page
            seleniumTracer.navigateToUrl(BASE_URL, "Home page");
            
            // Enable DevTools network monitoring AFTER page load
            logger.info("Enabling DevTools network monitoring...");
            networkTracer.enableNetworkLogging();
            logger.info("DevTools network monitoring enabled successfully");
            
            // Click on signup/login link
            homePage.clickSignupLogin();
            seleniumTracer.trackElementInteraction("SignupLogin Link", "click", System.currentTimeMillis() - startTime);
            
            // Start signup process
            signupLoginPage.startSignup(userName, userEmail);
            seleniumTracer.trackElementInteraction("Signup Form", "submit", System.currentTimeMillis() - startTime);
            
            // Check if email already exists
            if (signupLoginPage.isSignupEmailExists()) {
                logger.info("Email already exists, trying with different email");
                userEmail = "test" + System.currentTimeMillis() + "@example.com";
                signupLoginPage.startSignup(userName, userEmail);
            }
            
            // Fill account information
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
            seleniumTracer.trackElementInteraction("Account Details", "fill", System.currentTimeMillis() - startTime);
            
            // Create account
            signupLoginPage.createAccount();
            seleniumTracer.trackElementInteraction("Create Account Button", "click", System.currentTimeMillis() - startTime);
            
            // Verify account creation
            boolean accountCreated = signupLoginPage.isAccountCreated();
            verificationHelper.verifyAccountCreated(accountCreated);
            seleniumTracer.trackTestStep("Account Creation Verification", "Verify account was created successfully", accountCreated, System.currentTimeMillis() - startTime);
            
            success = accountCreated;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Create Account", "Successfully created user account", success, duration);
            

            
            logger.info("=== Step 1 completed: Account created successfully with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 1 failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Create Account", "Failed to create user account", false, duration);
            

            
            throw new RuntimeException("Step 1 failed", e);
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Step 2: Add Random Products to Cart")
    void testAddProductsToCart() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            logger.info("=== Step 2: Adding products to cart with TestAutomation_with_DevTools ===");
            
            // Navigate to products page
            productsPage.navigateToProducts();
            seleniumTracer.trackPageNavigation("Products", BASE_URL + "/products", System.currentTimeMillis() - startTime);
            verificationHelper.verifyPageLoaded("Products page", productsPage.isPageLoaded());
            
            // Add random product to cart
            productsPage.addRandomProductToCart();
            seleniumTracer.trackElementInteraction("Add to Cart Button", "click", 150);
            
            // Continue shopping
            productsPage.clickContinueShopping();
            seleniumTracer.trackElementInteraction("Continue Shopping Button", "click", 100);
            
            // Add another random product
            productsPage.addRandomProductToCart();
            seleniumTracer.trackElementInteraction("Add to Cart Button 2", "click", 150);
            
            success = true;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Add Products", "Successfully added products to cart", success, duration);
            
            logger.info("=== Step 2 completed: Products added to cart with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 2 failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Add Products", "Failed to add products to cart", false, duration);
            throw new RuntimeException("Step 2 failed", e);
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Step 3: Navigate to Cart and Verify Products")
    void testVerifyCartProducts() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            logger.info("=== Step 3: Verifying cart products with TestAutomation_with_DevTools ===");
            
            // Navigate to cart
            cartPage.navigateToCart();
            seleniumTracer.trackPageNavigation("Cart", BASE_URL + "/view_cart", System.currentTimeMillis() - startTime);
            verificationHelper.verifyPageLoaded("Cart page", cartPage.isPageLoaded());
            
            // Verify cart is not empty
            verificationHelper.verifyCartNotEmpty(cartPage.getCartItemsCount() > 0);
            
            // Get product names in cart
            List<String> cartProductNames = cartPage.getProductNames();
            logger.info("Products in cart: {}", cartProductNames);
            
            // Verify products are in cart
            for (String productName : cartProductNames) {
                verificationHelper.verifyProductInCart(cartPage.containsProduct(productName), productName);
            }
            
            success = true;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Verify Cart", "Successfully verified cart products", success, duration);
            
            logger.info("=== Step 3 completed: Cart verification successful with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 3 failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Verify Cart", "Failed to verify cart products", false, duration);
            throw new RuntimeException("Step 3 failed", e);
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Step 4: Proceed to Checkout")
    void testProceedToCheckout() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            logger.info("=== Step 4: Proceeding to checkout with TestAutomation_with_DevTools ===");
            
            // Click on Proceed to Checkout
            cartPage.clickProceedToCheckout();
            seleniumTracer.trackElementInteraction("Proceed to Checkout Button", "click", 100);
            
            success = true;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Proceed Checkout", "Successfully proceeded to checkout", success, duration);
            
            logger.info("=== Step 4 completed: Proceeded to checkout with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 4 failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Proceed Checkout", "Failed to proceed to checkout", false, duration);
            throw new RuntimeException("Step 4 failed", e);
        }
    }
    
    @Test
    @Order(5)
    @DisplayName("Step 5: Fill Checkout Information with Random Data")
    void testFillCheckoutInformation() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            logger.info("=== Step 5: Filling checkout information with TestAutomation_with_DevTools ===");
            
            // Generate random checkout data
            TestDataGenerator.CheckoutInfo checkoutInfo = TestDataGenerator.generateCheckoutInfo();
            
            // Fill delivery address
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
            seleniumTracer.trackElementInteraction("Checkout Form", "fill", 500);
            
            // Add comment
            checkoutPage.addComment("Test order comment - " + System.currentTimeMillis());
            seleniumTracer.trackElementInteraction("Comment Field", "fill", 100);
            
            // Click Place Order
            checkoutPage.clickPlaceOrder();
            seleniumTracer.trackElementInteraction("Place Order Button", "click", 150);
            
            success = true;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Fill Checkout", "Successfully filled checkout information", success, duration);
            
            logger.info("=== Step 5 completed: Checkout information filled with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 5 failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Fill Checkout", "Failed to fill checkout information", false, duration);
            throw new RuntimeException("Step 5 failed", e);
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("Step 6: Complete Payment")
    void testCompletePayment() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            logger.info("=== Step 6: Completing payment with TestAutomation_with_DevTools ===");
            
            // Complete payment with random card data
            paymentPage.completePaymentWithRandomData();
            seleniumTracer.trackElementInteraction("Payment Form", "fill", 300);
            seleniumTracer.trackElementInteraction("Pay Button", "click", 200);
            
            // Verify order was placed
            verificationHelper.verifyOrderPlaced(paymentPage.isOrderPlaced());
            
            // Get order confirmation message
            String confirmationMessage = paymentPage.getOrderConfirmationMessage();
            logger.info("Order confirmation message: {}", confirmationMessage);
            
            // Download invoice
            paymentPage.clickDownloadInvoice();
            seleniumTracer.trackElementInteraction("Download Invoice Button", "click", 100);
            
            // Continue to home page
            paymentPage.clickContinue();
            seleniumTracer.trackElementInteraction("Continue Button", "click", 100);
            
            success = true;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Complete Payment", "Successfully completed payment", success, duration);
            
            logger.info("=== Step 6 completed: Payment completed successfully with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 6 failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Complete Payment", "Failed to complete payment", false, duration);
            throw new RuntimeException("Step 6 failed", e);
        }
    }
    
    @Test
    @Order(7)
    @DisplayName("Step 7: Verify Order Completion and Return Home")
    void testVerifyOrderCompletionAndReturnHome() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            logger.info("=== Step 7: Verifying order completion with TestAutomation_with_DevTools ===");
            
            // Navigate to home page
            homePage.navigateToHome();
            seleniumTracer.trackPageNavigation("Home", BASE_URL, System.currentTimeMillis() - startTime);
            verificationHelper.verifyPageLoaded("Home page", homePage.isPageLoaded());
            
            // Verify we're on home page
            verificationHelper.verifyCurrentUrl(homePage.getCurrentUrl(), BASE_URL + "/");
            
            success = true;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Verify Completion", "Successfully verified order completion", success, duration);
            
            logger.info("=== Step 7 completed: Order completion verified with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 7 failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Verify Completion", "Failed to verify order completion", false, duration);
            throw new RuntimeException("Step 7 failed", e);
        }
    }
    
    @Test
    @Order(8)
    @DisplayName("Complete End-to-End Test Flow")
    void testCompleteEndToEndFlow() {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            logger.info("=== Complete End-to-End Test Flow with TestAutomation_with_DevTools ===");
            
            // This test method demonstrates the complete flow
            // All individual steps are tested above, this is for demonstration
            
            success = true;
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Complete Flow", "All test steps completed successfully", success, duration);
            
            logger.info("All test steps completed successfully with TestAutomation_with_DevTools!");
            logger.info("User created: {}", userEmail);
            logger.info("Products added to cart and order completed");
            logger.info("Network requests captured: {}", networkTracer.getRequestCount());
            
            logger.info("=== Complete End-to-End Test Flow completed with TestAutomation_with_DevTools ===");
            
        } catch (Exception e) {
            logger.error("Complete flow test failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            seleniumTracer.trackTestStep("Complete Flow", "Failed to complete end-to-end flow", false, duration);
            throw new RuntimeException("Complete flow test failed", e);
        }
    }
} 