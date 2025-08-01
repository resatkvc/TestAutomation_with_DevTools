package proje.com.saucedemo.utils;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Test data generator utility for SauceDemo tests
 */
public class TestDataGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    // SauceDemo credentials
    public static final String STANDARD_USER = "standard_user";
    public static final String STANDARD_PASSWORD = "secret_sauce";
    
    // Product names for random selection
    private static final String[] PRODUCT_NAMES = {
        "Sauce Labs Backpack",
        "Sauce Labs Bike Light", 
        "Sauce Labs Bolt T-Shirt",
        "Sauce Labs Fleece Jacket",
        "Sauce Labs Onesie",
        "Test.allTheThings() T-Shirt (Red)"
    };
    
    /**
     * Generate random first name
     * @return Random first name
     */
    public static String generateFirstName() {
        String firstName = faker.name().firstName();
        logger.debug("Generated first name: {}", firstName);
        return firstName;
    }
    
    /**
     * Generate random last name
     * @return Random last name
     */
    public static String generateLastName() {
        String lastName = faker.name().lastName();
        logger.debug("Generated last name: {}", lastName);
        return lastName;
    }
    
    /**
     * Generate random postal code
     * @return Random postal code
     */
    public static String generatePostalCode() {
        String postalCode = faker.address().zipCode();
        logger.debug("Generated postal code: {}", postalCode);
        return postalCode;
    }
    
    /**
     * Generate random product names for cart
     * @param count Number of products to generate
     * @return List of random product names
     */
    public static List<String> generateRandomProducts(int count) {
        List<String> selectedProducts = new ArrayList<>();
        List<String> availableProducts = new ArrayList<>(List.of(PRODUCT_NAMES));
        
        for (int i = 0; i < count && !availableProducts.isEmpty(); i++) {
            int randomIndex = random.nextInt(availableProducts.size());
            String selectedProduct = availableProducts.remove(randomIndex);
            selectedProducts.add(selectedProduct);
            logger.debug("Selected product: {}", selectedProduct);
        }
        
        logger.info("Generated {} random products: {}", selectedProducts.size(), selectedProducts);
        return selectedProducts;
    }
    
    /**
     * Generate random product name
     * @return Random product name
     */
    public static String generateRandomProduct() {
        String product = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
        logger.debug("Generated random product: {}", product);
        return product;
    }
    
    /**
     * Generate random checkout information
     * @return CheckoutInfo object with random data
     */
    public static CheckoutInfo generateCheckoutInfo() {
        CheckoutInfo checkoutInfo = new CheckoutInfo(
            generateFirstName(),
            generateLastName(),
            generatePostalCode()
        );
        
        logger.info("Generated checkout info: {}", checkoutInfo);
        return checkoutInfo;
    }
    
    /**
     * Generate random email
     * @return Random email address
     */
    public static String generateEmail() {
        String email = faker.internet().emailAddress();
        logger.debug("Generated email: {}", email);
        return email;
    }
    
    /**
     * Generate random phone number
     * @return Random phone number
     */
    public static String generatePhoneNumber() {
        String phone = faker.phoneNumber().phoneNumber();
        logger.debug("Generated phone number: {}", phone);
        return phone;
    }
    
    /**
     * Generate random address
     * @return Random address
     */
    public static String generateAddress() {
        String address = faker.address().fullAddress();
        logger.debug("Generated address: {}", address);
        return address;
    }
    
    /**
     * Generate random city
     * @return Random city
     */
    public static String generateCity() {
        String city = faker.address().city();
        logger.debug("Generated city: {}", city);
        return city;
    }
    
    /**
     * Generate random state
     * @return Random state
     */
    public static String generateState() {
        String state = faker.address().state();
        logger.debug("Generated state: {}", state);
        return state;
    }
    
    /**
     * Checkout information data class
     */
    public static class CheckoutInfo {
        private final String firstName;
        private final String lastName;
        private final String postalCode;
        
        public CheckoutInfo(String firstName, String lastName, String postalCode) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.postalCode = postalCode;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public String getPostalCode() {
            return postalCode;
        }
        
        @Override
        public String toString() {
            return "CheckoutInfo{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", postalCode='" + postalCode + '\'' +
                    '}';
        }
    }
} 