package proje.com.saucedemo.utils;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Test data generator utility for AutomationExercise tests
 */
public class TestDataGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    // Product names for random selection
    private static final String[] PRODUCT_NAMES = {
        "Blue Top",
        "Men Tshirt",
        "Sleeveless Dress",
        "Stylish Dress",
        "Winter Top",
        "Summer White Top",
        "Madame Top For Women",
        "Fancy Green Top",
        "Sleeves Printed Top - White",
        "Half Sleeves Top Schiffli Detailing - Pink",
        "Frozen Tops For Kids",
        "Full Sleeves Top Cherry - Pink",
        "Printed Off Shoulder Top - White",
        "Sleeves Top and Short - Blue & Pink",
        "Little Girls Mr. Panda Shirt",
        "Sleeveless Unicorn Patch Gown - Pink",
        "Cotton Mull Embroidered Dress",
        "Blue Cotton Indie Mickey Dress",
        "Long Maxi Tulle Fancy Dress Up Outfits -Pink",
        "Sleeveless Unicorn Print Fit & Flare Net Dress - Multi",
        "Colour Blocked Shirt – Sky Blue",
        "Pure Cotton V-Neck T-Shirt",
        "Green Side Placket Detail T-Shirt",
        "Premium Polo T-Shirts",
        "Pure Cotton Neon Green Tshirt",
        "Soft Stretch Jeans",
        "Regular Fit Straight Jeans",
        "Grunt Blue Slim Fit Jeans",
        "Rose Pink Embroidered Maxi Dress",
        "Cotton Silk Hand Block Print Saree",
        "Rust Red Linen Saree",
        "Beautiful Peacock Blue Cotton Linen Saree",
        "Lace Top For Women",
        "GRAPHIC DESIGN MEN T SHIRT - BLUE"
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
            generatePostalCode(),
            generateAddress(),
            generateCity(),
            generateState(),
            generatePhoneNumber(),
            generateCountry()
        );
        
        logger.info("Generated checkout info: {}", checkoutInfo);
        return checkoutInfo;
    }
    
    /**
     * Generate user information
     * @return UserInfo object with random data
     */
    public static UserInfo generateUserInfo() {
        UserInfo userInfo = new UserInfo(
            generateFirstName(),
            generateLastName(),
            generateEmail(),
            generatePassword()
        );
        
        logger.info("Generated user info: {}", userInfo);
        return userInfo;
    }
    
    /**
     * Generate account information
     * @return AccountInfo object with random data
     */
    public static AccountInfo generateAccountInfo() {
        AccountInfo accountInfo = new AccountInfo(
            random.nextBoolean() ? "Mr" : "Mrs",
            generateFirstName(),
            generateLastName(),
            generateCompany(),
            generateAddress(),
            generateAddress(),
            generateCountry(),
            generateState(),
            generateCity(),
            generatePostalCode(),
            generatePhoneNumber(),
            String.valueOf(1 + random.nextInt(28)),
            String.valueOf(1 + random.nextInt(12)),
            String.valueOf(1980 + random.nextInt(40))
        );
        
        logger.info("Generated account info: {}", accountInfo);
        return accountInfo;
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
     * Generate random password
     * @return Random password
     */
    public static String generatePassword() {
        String password = faker.internet().password(8, 16, true, true, true);
        logger.debug("Generated password: {}", password);
        return password;
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
     * Generate random country
     * @return Random country
     */
    public static String generateCountry() {
        String country = faker.address().country();
        logger.debug("Generated country: {}", country);
        return country;
    }
    
    /**
     * Generate random company
     * @return Random company name
     */
    public static String generateCompany() {
        String company = faker.company().name();
        logger.debug("Generated company: {}", company);
        return company;
    }
    
    /**
     * Checkout information data class
     */
    public static class CheckoutInfo {
        private final String firstName;
        private final String lastName;
        private final String postalCode;
        private final String address;
        private final String city;
        private final String state;
        private final String phone;
        private final String country;
        
        public CheckoutInfo(String firstName, String lastName, String postalCode, 
                          String address, String city, String state, String phone, String country) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.postalCode = postalCode;
            this.address = address;
            this.city = city;
            this.state = state;
            this.phone = phone;
            this.country = country;
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
        
        public String getAddress() {
            return address;
        }
        
        public String getCity() {
            return city;
        }
        
        public String getState() {
            return state;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public String getCountry() {
            return country;
        }
        
        @Override
        public String toString() {
            return "CheckoutInfo{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", postalCode='" + postalCode + '\'' +
                    ", address='" + address + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", phone='" + phone + '\'' +
                    ", country='" + country + '\'' +
                    '}';
        }
    }
    
    /**
     * User information data class
     */
    public static class UserInfo {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String password;
        
        public UserInfo(String firstName, String lastName, String email, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getPassword() {
            return password;
        }
        
        @Override
        public String toString() {
            return "UserInfo{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
    
    /**
     * Account information data class
     */
    public static class AccountInfo {
        private final String title;
        private final String firstName;
        private final String lastName;
        private final String company;
        private final String address1;
        private final String address2;
        private final String country;
        private final String state;
        private final String city;
        private final String zipcode;
        private final String mobileNumber;
        private final String day;
        private final String month;
        private final String year;
        
        public AccountInfo(String title, String firstName, String lastName, String company,
                         String address1, String address2, String country, String state,
                         String city, String zipcode, String mobileNumber, String day,
                         String month, String year) {
            this.title = title;
            this.firstName = firstName;
            this.lastName = lastName;
            this.company = company;
            this.address1 = address1;
            this.address2 = address2;
            this.country = country;
            this.state = state;
            this.city = city;
            this.zipcode = zipcode;
            this.mobileNumber = mobileNumber;
            this.day = day;
            this.month = month;
            this.year = year;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public String getCompany() {
            return company;
        }
        
        public String getAddress1() {
            return address1;
        }
        
        public String getAddress2() {
            return address2;
        }
        
        public String getCountry() {
            return country;
        }
        
        public String getState() {
            return state;
        }
        
        public String getCity() {
            return city;
        }
        
        public String getZipcode() {
            return zipcode;
        }
        
        public String getMobileNumber() {
            return mobileNumber;
        }
        
        public String getDay() {
            return day;
        }
        
        public String getMonth() {
            return month;
        }
        
        public String getYear() {
            return year;
        }
        
        @Override
        public String toString() {
            return "AccountInfo{" +
                    "title='" + title + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", company='" + company + '\'' +
                    ", address1='" + address1 + '\'' +
                    ", address2='" + address2 + '\'' +
                    ", country='" + country + '\'' +
                    ", state='" + state + '\'' +
                    ", city='" + city + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", mobileNumber='" + mobileNumber + '\'' +
                    ", day='" + day + '\'' +
                    ", month='" + month + '\'' +
                    ", year='" + year + '\'' +
                    '}';
        }
    }
} 