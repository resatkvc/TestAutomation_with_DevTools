package proje.com.saucedemo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import proje.com.saucedemo.config.WebDriverConfig;
import proje.com.saucedemo.pages.*;
import proje.com.saucedemo.utils.TestDataGenerator;
import proje.com.saucedemo.utils.ChromeDevToolsManager;
import proje.com.saucedemo.verification.VerificationHelper;

import java.util.List;

/**
 * Selenium DevTools Automation - Complete AutomationExercise test automation
 * Tests the full e-commerce flow from signup/login to order completion
 * Includes comprehensive network monitoring using DevTools API with Allure reporting
 */
@Epic("E-Commerce Automation")
@Feature("Full Purchase Flow")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(AllureJunit5.class)
public class AutomationExerciseCompleteTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AutomationExerciseCompleteTest.class);
    private static final String BASE_URL = "https://www.automationexercise.com";
    
    private static WebDriver driver;
    private static WebDriverConfig webDriverConfig;
    private static VerificationHelper verificationHelper;
    private static ChromeDevToolsManager cdpManager;
    
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
            logger.info("=== Setting up Selenium DevTools Automation test suite ===");
            
            // Initialize WebDriver
            webDriverConfig = new WebDriverConfig();
            driver = webDriverConfig.initializeDriver("chrome");
            
            // Initialize verification helper
            verificationHelper = new VerificationHelper(driver);
            
            // Initialize comprehensive CDP Manager
            cdpManager = new ChromeDevToolsManager(driver);
            
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
            
            logger.info("Selenium DevTools Automation setup completed successfully with DevTools monitoring");
            
        } catch (Exception e) {
            logger.error("Test setup failed: {}", e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterAll
    static void tearDown() {
        try {
            logger.info("=== Cleaning up Selenium DevTools Automation test resources ===");
            
            // Debug: Check DevTools status
            if (cdpManager != null) {
                logger.info("CDP Manager initialized: {}", cdpManager.isInitialized());
                logger.info("Network requests captured: {}", cdpManager.getNetworkRequestCount());
                logger.info("Console logs captured: {}", cdpManager.getConsoleLogCount());
                logger.info("JavaScript errors: {}", cdpManager.getJavaScriptErrorCount());
                
                // Attach final comprehensive CDP summary
                cdpManager.attachToAllureReport();
                
                // Cleanup CDP Manager
                cdpManager.cleanup();
            }
            
            // Quit WebDriver
            if (driver != null) {
                webDriverConfig.quitDriver();
                logger.info("WebDriver quit successfully");
            }
            
            logger.info("=== Selenium DevTools Automation test suite cleanup completed ===");
            
        } catch (Exception e) {
            logger.error("Test cleanup failed: {}", e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Navigate to AutomationExercise website and create a new user account with DevTools monitoring")
    @DisplayName("Step 1: Navigate to AutomationExercise and Create Account")
    void testCreateAccount() {
        long startTime = System.currentTimeMillis();
        
        try {
            Allure.step("Starting account creation process", () -> {
                logger.info("=== Step 1: Creating new account with Selenium DevTools ===");
            });
            
            Allure.step("Navigate to home page", () -> {
                driver.get(BASE_URL);
                logger.info("Navigated to: {}", BASE_URL);
            });
            
            Allure.step("Enable comprehensive DevTools monitoring", () -> {
                logger.info("Enabling comprehensive Chrome DevTools Protocol monitoring...");
                
                // Enable all CDP domains
                cdpManager.enableAllMonitoring();
                
                logger.info("✅ Full CDP monitoring suite enabled successfully");
                
                // Attach comprehensive CDP status to report
                cdpManager.attachToAllureReport();
            });
            
            Allure.step("Click on signup/login link", () -> {
                homePage.clickSignupLogin();
                logger.info("Clicked signup/login link");
            });
            
            Allure.step("Start signup process", () -> {
                signupLoginPage.startSignup(userName, userEmail);
                logger.info("Started signup for user: {} with email: {}", userName, userEmail);
                
                // Attach user data to report
                Allure.addAttachment("Test User Data", 
                    String.format("Name: %s\nEmail: %s", userName, userEmail));
            });
            
            Allure.step("Handle email already exists scenario", () -> {
                if (signupLoginPage.isSignupEmailExists()) {
                    logger.info("Email already exists, trying with different email");
                    userEmail = "test" + System.currentTimeMillis() + "@example.com";
                    signupLoginPage.startSignup(userName, userEmail);
                    
                    Allure.addAttachment("Updated Email", userEmail);
                }
            });
            
            Allure.step("Fill account information", () -> {
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
                
                // Attach account details to report
                Allure.addAttachment("Account Details", 
                    String.format("Title: %s\nCompany: %s\nCountry: %s\nState: %s\nCity: %s", 
                        accountInfo.getTitle(), accountInfo.getCompany(), accountInfo.getCountry(), 
                        accountInfo.getState(), accountInfo.getCity()));
                
                logger.info("Filled account information");
            });
            
            Allure.step("Create account", () -> {
                signupLoginPage.createAccount();
                logger.info("Clicked create account button");
            });
            
            Allure.step("Verify account creation", () -> {
                boolean accountCreated = signupLoginPage.isAccountCreated();
                verificationHelper.verifyAccountCreated(accountCreated);
                
                long duration = System.currentTimeMillis() - startTime;
                logger.info("Account creation verification completed in {} ms", duration);
                
                Allure.addAttachment("Test Results", 
                    String.format("Account Created: %s\nDuration: %d ms\nNetwork Requests: %d\nConsole Logs: %d", 
                        accountCreated, duration, cdpManager.getNetworkRequestCount(), cdpManager.getConsoleLogCount()));
            });
            
            logger.info("=== Step 1 completed: Account created successfully with Selenium DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 1 failed: {}", e.getMessage());
            
            // Attach error details to Allure report
            Allure.addAttachment("Error Details", e.getMessage());
            Allure.addAttachment("CDP Status", cdpManager.getDevToolsSummary());
            
            throw new RuntimeException("Step 1 failed", e);
        }
    }
    
    @Test
    @Order(2)
    @Story("Product Selection")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navigate to products page and add random products to cart")
    @DisplayName("Step 2: Add Random Products to Cart")
    void testAddProductsToCart() {
        try {
            Allure.step("Navigate to products page", () -> {
                productsPage.navigateToProducts();
                verificationHelper.verifyPageLoaded("Products page", productsPage.isPageLoaded());
                logger.info("Successfully navigated to products page");
            });
            
            Allure.step("Add first random product to cart", () -> {
                productsPage.addRandomProductToCart();
                productsPage.clickContinueShopping();
                logger.info("Added first product to cart");
            });
            
            Allure.step("Add second random product to cart", () -> {
                productsPage.addRandomProductToCart();
                logger.info("Added second product to cart");
                
                // Attach comprehensive CDP data
                cdpManager.attachToAllureReport();
                
                Allure.addAttachment("Network Summary", 
                    String.format("HTTP Requests: %d | Console Logs: %d | JS Errors: %d", 
                        cdpManager.getNetworkRequestCount(),
                        cdpManager.getConsoleLogCount(),
                        cdpManager.getJavaScriptErrorCount()));
            });
            
            logger.info("=== Step 2 completed: Products added to cart with DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 2 failed: {}", e.getMessage());
            Allure.addAttachment("Error Details", e.getMessage());
            throw new RuntimeException("Step 2 failed", e);
        }
    }
    
    @Test
    @Order(3)
    @Story("Shopping Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navigate to cart and verify products are correctly added")
    @DisplayName("Step 3: Navigate to Cart and Verify Products")
    void testVerifyCartProducts() {
        try {
            Allure.step("Navigate to cart page", () -> {
                cartPage.navigateToCart();
                verificationHelper.verifyPageLoaded("Cart page", cartPage.isPageLoaded());
                logger.info("Successfully navigated to cart page");
            });
            
            Allure.step("Verify cart contents", () -> {
                verificationHelper.verifyCartNotEmpty(cartPage.getCartItemsCount() > 0);
                
                List<String> cartProductNames = cartPage.getProductNames();
                logger.info("Products in cart: {}", cartProductNames);
                
                for (String productName : cartProductNames) {
                    verificationHelper.verifyProductInCart(cartPage.containsProduct(productName), productName);
                }
                
                Allure.addAttachment("Cart Products", String.join(", ", cartProductNames));
                
                // Attach comprehensive CDP monitoring data
                cdpManager.attachToAllureReport();
            });
            
            logger.info("=== Step 3 completed: Cart verification successful with DevTools ===");
            
        } catch (Exception e) {
            logger.error("Step 3 failed: {}", e.getMessage());
            Allure.addAttachment("Error Details", e.getMessage());
            throw new RuntimeException("Step 3 failed", e);
        }
    }
    
    @Test
    @Order(4)
    @Story("Checkout Process")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Complete the full checkout and payment process")
    @DisplayName("Step 4: Complete Checkout and Payment")
    void testCompleteCheckoutAndPayment() {
        try {
            Allure.step("Proceed to checkout", () -> {
                cartPage.clickProceedToCheckout();
                logger.info("Clicked proceed to checkout");
            });
            
            Allure.step("Fill checkout information", () -> {
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
                
                checkoutPage.addComment("DevTools test order - " + System.currentTimeMillis());
                checkoutPage.clickPlaceOrder();
                
                Allure.addAttachment("Checkout Details", 
                    String.format("Name: %s %s\nAddress: %s\nCity: %s\nCountry: %s", 
                        checkoutInfo.getFirstName(), checkoutInfo.getLastName(),
                        checkoutInfo.getAddress(), checkoutInfo.getCity(), checkoutInfo.getCountry()));
                
                logger.info("Filled checkout information and placed order");
            });
            
            Allure.step("Complete payment", () -> {
                paymentPage.completePaymentWithRandomData();
                verificationHelper.verifyOrderPlaced(paymentPage.isOrderPlaced());
                
                String confirmationMessage = paymentPage.getOrderConfirmationMessage();
                logger.info("Order confirmation: {}", confirmationMessage);
                
                paymentPage.clickDownloadInvoice();
                paymentPage.clickContinue();
                
                Allure.addAttachment("Order Confirmation", confirmationMessage);
                
                // Attach final comprehensive CDP summary
                cdpManager.attachToAllureReport();
                
                Allure.addAttachment("Final CDP Summary", cdpManager.getDevToolsSummary());
                
                logger.info("Payment completed successfully");
            });
            
            logger.info("=== Step 4 completed: Full checkout and payment successful with DevTools monitoring ===");
            
        } catch (Exception e) {
            logger.error("Step 4 failed: {}", e.getMessage());
            Allure.addAttachment("Error Details", e.getMessage());
            Allure.addAttachment("CDP Status at Failure", cdpManager.getDevToolsSummary());
            throw new RuntimeException("Step 4 failed", e);
        }
    }
} 