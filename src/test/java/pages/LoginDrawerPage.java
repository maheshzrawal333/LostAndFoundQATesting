package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginDrawerPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//input[@type='email']")
    private WebElement txtEmail;

    @FindBy(xpath = "//input[@type='password' and @placeholder='••••••••']")
    private WebElement txtPassword;

    // --- NEW: Signup & Forgot Password Elements ---
    @FindBy(xpath = "//button[contains(., 'Create a secure account')]")
    private WebElement btnToggleSignup;

    @FindBy(xpath = "//button[contains(., 'Forgot password?')]")
    private WebElement btnForgotPwd;

    @FindBy(xpath = "//input[@type='tel']")
    private WebElement txtPhone;

    @FindBy(xpath = "(//input[@type='password'])[2]")
    private WebElement txtConfirmPassword;

    @FindBy(xpath = "//input[@placeholder='------']")
    private WebElement txtOtp;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement btnSubmitAction; // Dynamic button (Login / Signup / Verify)

    public LoginDrawerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public HomePage performLogin(String email, String password) {
        wait.until(ExpectedConditions.visibilityOf(txtEmail)).sendKeys(email);
        txtPassword.sendKeys(password);
        btnSubmitAction.click();
        wait.until(ExpectedConditions.invisibilityOf(txtEmail));
        return new HomePage(driver);
    }

    public void initiateSignup(String email, String phone, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(btnToggleSignup)).click();
        wait.until(ExpectedConditions.visibilityOf(txtPhone)).sendKeys(phone);
        txtEmail.sendKeys(email);
        txtPassword.sendKeys(password);
        txtConfirmPassword.sendKeys(password);
        btnSubmitAction.click();
        wait.until(ExpectedConditions.visibilityOf(txtOtp));
    }

    public void submitVerificationOtp(String otp) {
        wait.until(ExpectedConditions.visibilityOf(txtOtp)).sendKeys(otp);
        btnSubmitAction.click();
        wait.until(ExpectedConditions.invisibilityOf(txtOtp));
    }
}