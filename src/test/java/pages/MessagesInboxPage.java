package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class MessagesInboxPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//textarea")
    private WebElement txtChatInput;

    // XPath: Flexible locator for send button whether inside or outside a <form>
    @FindBy(xpath = "//button[@type='submit'] | //textarea/following-sibling::button | //textarea/parent::*//button[not(@type='file')]")
    private WebElement btnSendMessage;

    // XPath: Relaxed form restriction for file input
    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileUploadChat;

    // --- Actions ---
    @FindBy(xpath = "//button[contains(., 'Generate Code')]")
    private WebElement btnGenerateCode;

    @FindBy(xpath = "//button[contains(., 'Enter Code')]")
    private WebElement btnEnterCode;

    @FindBy(xpath = "//button[contains(., 'Not a Match')]")
    private WebElement btnNotAMatch;

    @FindBy(xpath = "//button[contains(., 'Cancel Request')]")
    private WebElement btnCancelRequest;

    // --- Resolution & Tour ---
    @FindBy(xpath = "//button[contains(., 'Next')]")
    private WebElement btnTourNext;

    @FindBy(xpath = "//button[contains(., 'Got it!')]")
    private WebElement btnTourGotIt;

    @FindBy(xpath = "//span[contains(@class, 'tracking-[0.3em]')]")
    private WebElement txtGeneratedOtpDisplay;

    @FindBy(xpath = "//input[@placeholder='------']")
    private WebElement txtResolveOtpInput;

    @FindBy(xpath = "//button[contains(., 'Verify & Resolve')]")
    private WebElement btnVerifyAndResolve;

    @FindBy(xpath = "//p[contains(text(), 'Chat Closed')]")
    private WebElement msgChatClosed;

    public MessagesInboxPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void typeAndSendMessage(String message) {
        wait.until(ExpectedConditions.visibilityOf(txtChatInput)).clear();
        txtChatInput.sendKeys(message);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnSendMessage)).click();
        } catch (Exception e) {
            // Fallback 1: Press ENTER inside textarea
            try {
                txtChatInput.sendKeys(Keys.ENTER);
            } catch (Exception ex) {
                // Fallback 2: Execute JavaScript click on the send button
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSendMessage);
            }
        }
    }

    public void sendAttachment(String absolutePath) {
        File file = new File(absolutePath);
        fileUploadChat.sendKeys(file.getAbsolutePath());

        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnSendMessage)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSendMessage);
        }
    }

    public void requestAndCancelClosure() {
        wait.until(ExpectedConditions.elementToBeClickable(btnNotAMatch)).click();

        // Handle potential tour
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnTourNext)).click();
            btnNotAMatch.click(); // Click again after tour dismisses
        } catch (Exception ignored) {}

        wait.until(ExpectedConditions.elementToBeClickable(btnCancelRequest)).click();
    }

    public String generateResolutionCodeAsPoster() {
        wait.until(ExpectedConditions.elementToBeClickable(btnGenerateCode)).click();

        // Handle potential tour
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnTourNext)).click();
            wait.until(ExpectedConditions.elementToBeClickable(btnTourGotIt)).click();
            btnGenerateCode.click(); // Click again after tour dismisses
        } catch (Exception ignored) {}

        return wait.until(ExpectedConditions.visibilityOf(txtGeneratedOtpDisplay)).getText();
    }

    public void enterResolutionCodeAsClaimer(String otpCode) {
        wait.until(ExpectedConditions.elementToBeClickable(btnEnterCode)).click();

        // Handle potential tour
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnTourNext)).click();
            wait.until(ExpectedConditions.elementToBeClickable(btnTourGotIt)).click();
            btnEnterCode.click(); // Click again after tour dismisses
        } catch (Exception ignored) {}

        wait.until(ExpectedConditions.visibilityOf(txtResolveOtpInput)).sendKeys(otpCode);
        btnVerifyAndResolve.click();

        // Assert the chat successfully locks down to RESOLVED state
        wait.until(ExpectedConditions.visibilityOf(msgChatClosed));
    }

    public void dismissOnboardingTour() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

            // Step 1: Click 'Next'
            WebElement nextButton = shortWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Next')]")));
            nextButton.click();

            // Step 2: Click final action button ('Done', 'Got it', 'Finish', or 'Next')
            WebElement finishButton = shortWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Done') or contains(text(),'Got it') or contains(text(),'Next') or contains(text(),'Finish')]")));
            finishButton.click();
        } catch (Exception e) {
            // Tour was either not present or already dismissed
        }
    }
}