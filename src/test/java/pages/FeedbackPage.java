package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class FeedbackPage {
    private WebDriverWait wait;

    @FindBy(xpath = "//button[contains(., 'Report Bug')]")
    private WebElement btnReportBug;

    @FindBy(xpath = "//button[contains(., 'Suggestion')]")
    private WebElement btnSuggestion;

    @FindBy(xpath = "//button[contains(., 'General')]")
    private WebElement btnGeneral;

    @FindBy(xpath = "//textarea")
    private WebElement txtFeedbackDetails;

    @FindBy(xpath = "//button[contains(., 'Send Feedback') and not(contains(@class, 'group'))]")
    private WebElement btnSubmitFeedback;

    @FindBy(xpath = "//h3[contains(., 'Thank You!')]")
    private WebElement successHeader;

    public FeedbackPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void submitFeedback(String type, String details) {
        if (type.equals("BUG")) {
            wait.until(ExpectedConditions.elementToBeClickable(btnReportBug)).click();
        } else if (type.equals("SUGGESTION")) {
            wait.until(ExpectedConditions.elementToBeClickable(btnSuggestion)).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(btnGeneral)).click();
        }

        wait.until(ExpectedConditions.visibilityOf(txtFeedbackDetails)).sendKeys(details);
        btnSubmitFeedback.click();
        wait.until(ExpectedConditions.visibilityOf(successHeader));
    }
}