package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AccountSettingsPage {
    private WebDriverWait wait;

    @FindBy(xpath = "//label[contains(text(), 'Full Name')]/parent::div/following-sibling::input")
    private WebElement txtFullName;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileUploadAvatar;

    @FindBy(xpath = "//button[contains(., 'Save Changes')]")
    private WebElement btnSaveChanges;

    @FindBy(xpath = "//input[@placeholder='------']")
    private WebElement txtVerificationOtp;

    @FindBy(xpath = "//button[contains(., 'Verify & Save')]")
    private WebElement btnVerifyAndSave;

    @FindBy(xpath = "//button[contains(., 'Sign Out')]")
    private WebElement btnSignOut;

    public AccountSettingsPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void uploadProfilePicture(String absolutePath) {
        fileUploadAvatar.sendKeys(absolutePath);
    }

    public void updateFullNameAndSave(String newName) {
        wait.until(ExpectedConditions.visibilityOf(txtFullName)).clear();
        txtFullName.sendKeys(newName);
        btnSaveChanges.click();
        wait.until(ExpectedConditions.visibilityOf(txtVerificationOtp));
    }

    public void verifyProfileUpdate(String otp) {
        wait.until(ExpectedConditions.visibilityOf(txtVerificationOtp));
        txtVerificationOtp.clear();
        txtVerificationOtp.sendKeys(otp);
        wait.until(ExpectedConditions.elementToBeClickable(btnVerifyAndSave)).click();

        wait.until(ExpectedConditions.invisibilityOf(txtVerificationOtp));
    }

    public void performLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSignOut)).click();
    }
}