package proje.com.saucedemo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proje.com.saucedemo.config.WebDriverConfig;
import proje.com.saucedemo.pages.*;
import proje.com.saucedemo.utils.TestDataGenerator;
import proje.com.saucedemo.utils.ZipkinTracer;
import proje.com.saucedemo.verification.VerificationHelper;

import java.util.List;

/**
 * Complete SauceDemo test automation with Zipkin integration
 * Tests the full e-commerce flow from login to order completion
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SauceDemoCompleteTest {
    
    private static final Logger logger = LoggerFactory.getLogger(SauceDemoCompleteTest.class);
    private static final String BASE_URL = "https://www.saucedemo.com";
    
    private static WebDriver driver;
    private static WebDriverConfig webDriverConfig;
    private static VerificationHelper verificationHelper;
    
    // Page Objects
    private static LoginPage loginPage;
    private static InventoryPage inventoryPage;
    private static CartPage cartPage;
    private static CheckoutPage checkoutPage;
    private static CheckoutOverviewPage checkoutOverviewPage;
    private static CheckoutCompletePage checkoutCompletePage;
    
    // Test Data
    private static List<String> selectedProducts;
    
    @BeforeAll
    static void setUp() {
        try {
            logger.info("=== Starting SauceDemo Complete Test Suite ===");
            
            // Initialize Zipkin tracing
            ZipkinTracer.initializeTracing();
            logger.info("Zipkin tracing initialized");
            
            // Initialize WebDriver
            webDriverConfig = new WebDriverConfig();
            driver = webDriverConfig.initializeDriver("chrome", false); // Use local Chrome for now
            
            // Initialize verification helper
            verificationHelper = new VerificationHelper(driver);
            
            // Initialize page objects
            loginPage = new LoginPage(driver);
            inventoryPage = new InventoryPage(driver);
            cartPage = new CartPage(driver);
            checkoutPage = new CheckoutPage(driver);
            checkoutOverviewPage = new CheckoutOverviewPage(driver);
            checkoutCompletePage = new CheckoutCompletePage(driver);
            
            logger.info("Test setup completed successfully");
            
        } catch (Exception e) {
            logger.error("Test setup failed: {}", e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterAll
    static void tearDown() {
        try {
            logger.info("=== Cleaning up test resources ===");
            
            // Quit WebDriver
            if (driver != null) {
                webDriverConfig.quitDriver();
                logger.info("WebDriver quit successfully");
            }
            
            // Close Zipkin tracing
            ZipkinTracer.closeTracing();
            logger.info("Zipkin tracing closed");
            
            logger.info("=== Test suite cleanup completed ===");
            
        } catch (Exception e) {
            logger.error("Test cleanup failed: {}", e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Step 1: Navigate to SauceDemo and Login")
    void testLogin() {
        try {
            ZipkinTracer.startSpan("test-login");
            ZipkinTracer.addTag("test_step", "login");
            
            logger.info("=== Step 1: Login Test ===");
            
            // Navigate to login page
            loginPage.navigateToLoginPage(BASE_URL);
            
            // Perform login with standard user
            loginPage.login(TestDataGenerator.STANDARD_USER, TestDataGenerator.STANDARD_PASSWORD);
            
            // Verify login was successful
            boolean loginSuccessful = verificationHelper.verifyLoginSuccessful();
            
            Assertions.assertTrue(loginSuccessful, "Login should be successful");
            logger.info("Login test passed successfully");
            
            ZipkinTracer.addTag("login_test_passed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Login test failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Login test failed", e);
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Step 2: Add Random Products to Cart")
    void testAddProductsToCart() {
        try {
            ZipkinTracer.startSpan("test-add-products-to-cart");
            ZipkinTracer.addTag("test_step", "add_products");
            
            logger.info("=== Step 2: Add Products to Cart Test ===");
            
            // Wait for inventory page to load
            inventoryPage.waitForPageLoad();
            
            // Generate random products to add (2 products as requested)
            selectedProducts = TestDataGenerator.generateRandomProducts(2);
            logger.info("Selected products to add: {}", selectedProducts);
            
            // Add products to cart
            int addedCount = inventoryPage.addMultipleProductsToCart(selectedProducts);
            
            // Verify products were added
            Assertions.assertEquals(selectedProducts.size(), addedCount, 
                    "All selected products should be added to cart");
            
            // Verify cart badge count
            int cartBadgeCount = inventoryPage.getCartBadgeCount();
            Assertions.assertEquals(selectedProducts.size(), cartBadgeCount, 
                    "Cart badge count should match number of added products");
            
            logger.info("Add products test passed successfully. Added {} products", addedCount);
            
            ZipkinTracer.addTag("products_added_count", String.valueOf(addedCount));
            ZipkinTracer.addTag("add_products_test_passed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Add products test failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Add products test failed", e);
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Step 3: Navigate to Cart and Verify Products")
    void testVerifyCartProducts() {
        try {
            ZipkinTracer.startSpan("test-verify-cart-products");
            ZipkinTracer.addTag("test_step", "verify_cart");
            
            logger.info("=== Step 3: Verify Cart Products Test ===");
            
            // Navigate to cart
            inventoryPage.clickCartLink();
            
            // Wait for cart page to load
            cartPage.waitForPageLoad();
            
            // Verify products are in cart
            boolean productsVerified = verificationHelper.verifyProductsInCart(selectedProducts);
            
            Assertions.assertTrue(productsVerified, "All selected products should be in cart");
            
            // Verify cart item count
            int cartItemCount = cartPage.getCartItemCount();
            Assertions.assertEquals(selectedProducts.size(), cartItemCount, 
                    "Cart item count should match number of selected products");
            
            logger.info("Cart verification test passed successfully");
            
            ZipkinTracer.addTag("cart_verification_passed", "true");
            ZipkinTracer.addTag("cart_items_count", String.valueOf(cartItemCount));
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Cart verification test failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Cart verification test failed", e);
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Step 4: Proceed to Checkout")
    void testProceedToCheckout() {
        try {
            ZipkinTracer.startSpan("test-proceed-to-checkout");
            ZipkinTracer.addTag("test_step", "proceed_checkout");
            
            logger.info("=== Step 4: Proceed to Checkout Test ===");
            
            // Click checkout button
            cartPage.clickCheckoutButton();
            
            // Wait for checkout page to load
            checkoutPage.waitForPageLoad();
            
            // Verify checkout page is displayed
            boolean checkoutPageDisplayed = verificationHelper.verifyCheckoutPageDisplayed();
            
            Assertions.assertTrue(checkoutPageDisplayed, "Checkout page should be displayed");
            
            logger.info("Proceed to checkout test passed successfully");
            
            ZipkinTracer.addTag("checkout_page_displayed", "true");
            ZipkinTracer.addTag("proceed_checkout_test_passed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Proceed to checkout test failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Proceed to checkout test failed", e);
        }
    }
    
    @Test
    @Order(5)
    @DisplayName("Step 5: Fill Checkout Information with Random Data")
    void testFillCheckoutInformation() {
        try {
            ZipkinTracer.startSpan("test-fill-checkout-information");
            ZipkinTracer.addTag("test_step", "fill_checkout_info");
            
            logger.info("=== Step 5: Fill Checkout Information Test ===");
            
            // Fill checkout information with random data
            checkoutPage.fillCheckoutInformationWithRandomData();
            
            // Click continue button
            checkoutPage.clickContinueButton();
            
            // Wait for overview page to load
            checkoutOverviewPage.waitForPageLoad();
            
            // Verify overview page is displayed
            boolean overviewPageDisplayed = verificationHelper.verifyCheckoutOverviewDisplayed();
            
            Assertions.assertTrue(overviewPageDisplayed, "Checkout overview page should be displayed");
            
            logger.info("Fill checkout information test passed successfully");
            
            ZipkinTracer.addTag("checkout_info_filled", "true");
            ZipkinTracer.addTag("overview_page_displayed", "true");
            ZipkinTracer.addTag("fill_checkout_info_test_passed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Fill checkout information test failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Fill checkout information test failed", e);
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("Step 6: Complete Order")
    void testCompleteOrder() {
        try {
            ZipkinTracer.startSpan("test-complete-order");
            ZipkinTracer.addTag("test_step", "complete_order");
            
            logger.info("=== Step 6: Complete Order Test ===");
            
            // Click finish button
            checkoutOverviewPage.clickFinishButton();
            
            // Wait for completion page to load
            checkoutCompletePage.waitForPageLoad();
            
            // Verify order completion message
            boolean orderCompletionVerified = verificationHelper.verifyOrderCompletionMessage();
            
            Assertions.assertTrue(orderCompletionVerified, "Order completion message should be displayed");
            
            logger.info("Complete order test passed successfully");
            
            ZipkinTracer.addTag("order_completed", "true");
            ZipkinTracer.addTag("completion_message_verified", "true");
            ZipkinTracer.addTag("complete_order_test_passed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Complete order test failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Complete order test failed", e);
        }
    }
    
    @Test
    @Order(7)
    @DisplayName("Step 7: Verify Order Completion and Return Home")
    void testVerifyOrderCompletionAndReturnHome() {
        try {
            ZipkinTracer.startSpan("test-verify-order-completion-and-return-home");
            ZipkinTracer.addTag("test_step", "verify_completion_and_return");
            
            logger.info("=== Step 7: Verify Order Completion and Return Home Test ===");
            
            // Verify order completion details
            boolean orderCompleted = checkoutCompletePage.verifyOrderCompletion();
            
            Assertions.assertTrue(orderCompleted, "Order completion should be verified");
            
            // Click back home button
            checkoutCompletePage.clickBackHomeButton();
            
            // Verify returned to home page
            boolean backToHomeSuccessful = verificationHelper.verifyBackToHome();
            
            Assertions.assertTrue(backToHomeSuccessful, "Should successfully return to home page");
            
            logger.info("Verify order completion and return home test passed successfully");
            
            ZipkinTracer.addTag("order_completion_verified", "true");
            ZipkinTracer.addTag("back_to_home_successful", "true");
            ZipkinTracer.addTag("verify_completion_and_return_test_passed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Verify order completion and return home test failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Verify order completion and return home test failed", e);
        }
    }
    
    @Test
    @Order(8)
    @DisplayName("Complete End-to-End Test Flow")
    void testCompleteEndToEndFlow() {
        try {
            ZipkinTracer.startSpan("test-complete-end-to-end-flow");
            ZipkinTracer.addTag("test_step", "complete_e2e_flow");
            
            logger.info("=== Complete End-to-End Test Flow ===");
            
            // This test verifies that all previous steps worked together
            // and the complete flow is successful
            
            // Verify we're back on inventory page
            inventoryPage.waitForPageLoad();
            
            // Verify cart is empty (since we completed the order)
            int cartBadgeCount = inventoryPage.getCartBadgeCount();
            Assertions.assertEquals(0, cartBadgeCount, "Cart should be empty after order completion");
            
            logger.info("Complete end-to-end test flow passed successfully");
            logger.info("=== All test steps completed successfully ===");
            
            ZipkinTracer.addTag("e2e_flow_completed", "true");
            ZipkinTracer.addTag("cart_empty_after_order", "true");
            ZipkinTracer.addTag("complete_e2e_test_passed", "true");
            ZipkinTracer.finishSpan();
            
        } catch (Exception e) {
            logger.error("Complete end-to-end test flow failed: {}", e.getMessage());
            ZipkinTracer.addError(e);
            ZipkinTracer.finishSpan();
            throw new RuntimeException("Complete end-to-end test flow failed", e);
        }
    }
} 