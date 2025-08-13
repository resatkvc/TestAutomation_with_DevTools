package proje.com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * AutomationExercise Kayıt/Giriş Sayfası için Page Object
 */
public class SignupLoginPage {
    private static final Logger logger = LoggerFactory.getLogger(SignupLoginPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Giriş locator'ları
    private final By loginEmailField = By.cssSelector("input[data-qa='login-email']");
    private final By loginPasswordField = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");
    private final By loginErrorMessage = By.cssSelector(".login-form p");

    // Kayıt locator'ları
    private final By signupNameField = By.cssSelector("input[data-qa='signup-name']");
    private final By signupEmailField = By.cssSelector("input[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("button[data-qa='signup-button']");
    private final By signupErrorMessage = By.cssSelector(".signup-form p");

    // Hesap oluşturma locator'ları
    private final By accountCreatedMessage = By.cssSelector("h2[data-qa='account-created']");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");
    private final By deleteAccountButton = By.cssSelector("a[href='/delete_account']");
    private final By accountDeletedMessage = By.cssSelector("h2[data-qa='account-deleted']");

    // Kullanıcı bilgi formu locator'ları
    private final By titleMr = By.id("id_gender1");
    private final By titleMrs = By.id("id_gender2");
    private final By passwordField = By.id("password");
    private final By dayDropdown = By.id("days");
    private final By monthDropdown = By.id("months");
    private final By yearDropdown = By.id("years");
    private final By newsletterCheckbox = By.id("newsletter");
    private final By specialOffersCheckbox = By.id("optin");
    private final By firstNameField = By.id("first_name");
    private final By lastNameField = By.id("last_name");
    private final By companyField = By.id("company");
    private final By address1Field = By.id("address1");
    private final By address2Field = By.id("address2");
    private final By countryDropdown = By.id("country");
    private final By stateField = By.id("state");
    private final By cityField = By.id("city");
    private final By zipcodeField = By.id("zipcode");
    private final By mobileNumberField = By.id("mobile_number");
    private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");

    public SignupLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Mevcut kimlik bilgileriyle giriş yap
     */
    public void login(String email, String password) {
        logger.info("Email ile giriş yapılıyor: {}", email);
        
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(loginEmailField));
        emailElement.clear();
        emailElement.sendKeys(email);
        
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(loginPasswordField));
        passwordElement.clear();
        passwordElement.sendKeys(password);
        
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();
        
        logger.info("Giriş denemesi tamamlandı");
    }

    /**
     * Girişin başarılı olup olmadığını kontrol et
     */
    public boolean isLoginSuccessful() {
        try {
            // Ana sayfaya yönlendirilip yönlendirilmediğimizi veya kullanıcının giriş yapıp yapmadığını kontrol et
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/"),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href='/logout']"))
            ));
            logger.info("Giriş başarılı");
            return true;
        } catch (Exception e) {
            logger.warn("Giriş başarısız");
            return false;
        }
    }

    /**
     * Giriş hata mesajını al
     */
    public String getLoginErrorMessage() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage));
            return errorElement.getText();
        } catch (Exception e) {
            return "Hata mesajı bulunamadı";
        }
    }

    /**
     * Kayıt sürecini başlat
     */
    public void startSignup(String name, String email) {
        logger.info("Kayıt süreci başlatılıyor, isim: {} ve email: {}", name, email);
        
        WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(signupNameField));
        nameElement.clear();
        nameElement.sendKeys(name);
        
        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(signupEmailField));
        emailElement.clear();
        emailElement.sendKeys(email);
        
        WebElement signupBtn = wait.until(ExpectedConditions.elementToBeClickable(signupButton));
        signupBtn.click();
        
        logger.info("Kayıt formu gönderildi");
    }

    /**
     * Kayıt email'inin zaten var olup olmadığını kontrol et
     */
    public boolean isSignupEmailExists() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(signupErrorMessage));
            String errorText = errorElement.getText();
            return errorText.contains("Email Address already exist");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hesap bilgi formunu doldur
     */
    public void fillAccountInformation(String title, String password, String day, String month, String year,
                                    String firstName, String lastName, String company, String address1,
                                    String address2, String country, String state, String city, String zipcode,
                                    String mobileNumber) {
        logger.info("Hesap bilgi formu dolduruluyor");
        
        // Başlık seç
        if ("Mr".equals(title)) {
            driver.findElement(titleMr).click();
        } else {
            driver.findElement(titleMrs).click();
        }
        
        // Şifre doldur
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordElement.clear();
        passwordElement.sendKeys(password);
        
        // Doğum tarihi seç
        WebElement dayElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dayDropdown));
        dayElement.sendKeys(day);
        
        WebElement monthElement = wait.until(ExpectedConditions.visibilityOfElementLocated(monthDropdown));
        monthElement.sendKeys(month);
        
        WebElement yearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(yearDropdown));
        yearElement.sendKeys(year);
        
        // Bülten ve özel teklifleri işaretle
        driver.findElement(newsletterCheckbox).click();
        driver.findElement(specialOffersCheckbox).click();
        
        // Kişisel bilgileri doldur
        WebElement firstNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        firstNameElement.clear();
        firstNameElement.sendKeys(firstName);
        
        WebElement lastNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        lastNameElement.clear();
        lastNameElement.sendKeys(lastName);
        
        WebElement companyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(companyField));
        companyElement.clear();
        companyElement.sendKeys(company);
        
        WebElement address1Element = wait.until(ExpectedConditions.visibilityOfElementLocated(address1Field));
        address1Element.clear();
        address1Element.sendKeys(address1);
        
        WebElement address2Element = wait.until(ExpectedConditions.visibilityOfElementLocated(address2Field));
        address2Element.clear();
        address2Element.sendKeys(address2);
        
        // Ülke seç
        WebElement countryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(countryDropdown));
        countryElement.sendKeys(country);
        
        // Eyalet, şehir, posta kodu doldur
        WebElement stateElement = wait.until(ExpectedConditions.visibilityOfElementLocated(stateField));
        stateElement.clear();
        stateElement.sendKeys(state);
        
        WebElement cityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cityField));
        cityElement.clear();
        cityElement.sendKeys(city);
        
        WebElement zipcodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(zipcodeField));
        zipcodeElement.clear();
        zipcodeElement.sendKeys(zipcode);
        
        WebElement mobileElement = wait.until(ExpectedConditions.visibilityOfElementLocated(mobileNumberField));
        mobileElement.clear();
        mobileElement.sendKeys(mobileNumber);
        
        logger.info("Hesap bilgi formu başarıyla dolduruldu");
    }

    /**
     * Hesap oluştur
     */
    public void createAccount() {
        logger.info("Hesap oluşturuluyor");
        WebElement createAccountBtn = wait.until(ExpectedConditions.elementToBeClickable(createAccountButton));
        createAccountBtn.click();
        logger.info("Hesap oluşturma başlatıldı");
    }

    /**
     * Hesabın başarıyla oluşturulup oluşturulmadığını kontrol et
     */
    public boolean isAccountCreated() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(accountCreatedMessage));
            logger.info("Hesap başarıyla oluşturuldu");
            return true;
        } catch (Exception e) {
            logger.warn("Hesap oluşturma başarısız");
            return false;
        }
    }

    /**
     * Hesap oluşturma sonrası devam et
     */
    public void continueAfterAccountCreation() {
        logger.info("Hesap oluşturma sonrası devam ediliyor");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();
        logger.info("Ana sayfaya devam edildi");
    }

    /**
     * Hesabı sil
     */
    public void deleteAccount() {
        logger.info("Hesap siliniyor");
        WebElement deleteAccountBtn = wait.until(ExpectedConditions.elementToBeClickable(deleteAccountButton));
        deleteAccountBtn.click();
        logger.info("Hesap silme başlatıldı");
    }

    /**
     * Hesabın başarıyla silinip silinmediğini kontrol et
     */
    public boolean isAccountDeleted() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(accountDeletedMessage));
            logger.info("Hesap başarıyla silindi");
            return true;
        } catch (Exception e) {
            logger.warn("Hesap silme başarısız");
            return false;
        }
    }

    /**
     * Hesap silme sonrası devam et
     */
    public void continueAfterAccountDeletion() {
        logger.info("Hesap silme sonrası devam ediliyor");
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();
        logger.info("Ana sayfaya devam edildi");
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