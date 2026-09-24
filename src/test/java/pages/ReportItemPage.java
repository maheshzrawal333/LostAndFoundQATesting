package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class ReportItemPage {
    private WebDriverWait wait;

    @FindBy(xpath = "//button[contains(text(), 'I Lost Something')]")
    private WebElement btnLostType;

    @FindBy(xpath = "//input[@placeholder='e.g. Sony Headphones']")
    private WebElement txtTitle;

    @FindBy(xpath = "//label[contains(text(),'Category')]/following-sibling::select")
    private WebElement ddlCategory;

    @FindBy(xpath = "//textarea[@placeholder='Describe the item clearly...']")
    private WebElement txtDescription;

    @FindBy(xpath = "//label[contains(text(),'State')]/following-sibling::select")
    private WebElement ddlProvince;

    @FindBy(xpath = "//label[contains(text(),'District')]/following-sibling::select")
    private WebElement ddlDistrict;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileUpload;

    @FindBy(xpath = "//button[contains(., 'Next Step')]")
    private WebElement btnNextStep;

    @FindBy(xpath = "//input[@placeholder='------']")
    private WebElement txtOtp;

    @FindBy(xpath = "//button[contains(., 'Publish Post')]")
    private WebElement btnPublish;

    public ReportItemPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void fillReportDetails(String title, String categoryText, String desc, String province, String district) {
        wait.until(ExpectedConditions.elementToBeClickable(btnLostType)).click();
        txtTitle.sendKeys(title);
        new Select(ddlCategory).selectByVisibleText(categoryText);
        txtDescription.sendKeys(desc);
        new Select(ddlProvince).selectByVisibleText(province);
        new Select(ddlDistrict).selectByVisibleText(district);
    }

    public void uploadImage(String relativeFilePath) {
        // Selenium requires absolute paths for file uploads
        File file = new File(relativeFilePath);
        fileUpload.sendKeys(file.getAbsolutePath());
    }

    public void submitFormAndWaitForOtpScreen() {
        btnNextStep.click();
        wait.until(ExpectedConditions.visibilityOf(txtOtp));
    }

    public void enterOtpAndPublish(String otp) {
        txtOtp.sendKeys(otp);
        btnPublish.click();
        wait.until(ExpectedConditions.invisibilityOf(txtOtp));
    }
}
