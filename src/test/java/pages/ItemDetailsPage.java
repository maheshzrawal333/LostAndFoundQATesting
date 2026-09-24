package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ItemDetailsPage {
    private WebDriverWait wait;

    @FindBy(xpath = "(//div[contains(@class, 'cursor-pointer') and .//h3])[1]")
    private WebElement firstItemCard;

    @FindBy(xpath = "//button[contains(., 'Claim This Item') or contains(., 'I Found This Item')]")
    private WebElement btnClaimItem;

    @FindBy(xpath = "//button[contains(., 'Open Secure Inbox') or contains(., 'Manage in Secure Inbox')]")
    private WebElement btnOpenOwnerInbox;

    @FindBy(xpath = "//button[contains(., 'Acknowledge')]")
    private WebElement btnAcknowledge;

    @FindBy(xpath = "//input[@placeholder='------']")
    private WebElement txtClaimOtp;

    @FindBy(xpath = "//button[contains(., 'Verify')]")
    private WebElement btnVerifyClaim;

    @FindBy(xpath = "//button[contains(., 'Close & Open Inbox')]")
    private WebElement btnOpenInbox;

    @FindBy(xpath = "//div[contains(@class, 'cursor-zoom-in')]")
    private WebElement imgThumbnail;

    @FindBy(xpath = "//img[@alt='Fullscreen preview']")
    private WebElement imgFullscreen;

    @FindBy(xpath = "//button[@title='Close Fullscreen']")
    private WebElement btnCloseFullscreen;

    public ItemDetailsPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void openFirstItemAndClickClaim() {
        wait.until(ExpectedConditions.elementToBeClickable(firstItemCard)).click();
        wait.until(ExpectedConditions.elementToBeClickable(btnClaimItem)).click();
    }

    public void testImageLightbox() {
        wait.until(ExpectedConditions.elementToBeClickable(firstItemCard)).click();
        wait.until(ExpectedConditions.elementToBeClickable(imgThumbnail)).click();
        wait.until(ExpectedConditions.visibilityOf(imgFullscreen));
        wait.until(ExpectedConditions.elementToBeClickable(btnCloseFullscreen)).click();
        wait.until(ExpectedConditions.invisibilityOf(imgFullscreen));
    }

    public MessagesInboxPage openFirstItemAndGoToInbox(WebDriver driver) {
        // 1. Locate and click the first item card to trigger the item details modal/view
        By itemCardLocator = By.xpath("(//div[contains(@class, 'cursor-pointer') and .//h3])[1]");
        try {
            WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(itemCardLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", card);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(card)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
            }
        } catch (Exception ignored) {
            // Modal may already be active on screen
        }

        // 2. Short pause for modal animation and rendering
        try { Thread.sleep(500); } catch (Exception ignored) {}

        // 3. Locate the inbox button with broad matching across Poster and Claimer UI states
        By inboxBtnLocator = By.xpath(
                "//button[contains(., 'Open Secure Inbox') or contains(., 'Manage in Secure Inbox') or contains(., 'Open Chat') or contains(., 'View Chat') or contains(., 'Go to Inbox') or contains(., 'Chat') or contains(., 'Inbox')]"
        );

        WebElement inboxBtn = wait.until(ExpectedConditions.presenceOfElementLocated(inboxBtnLocator));

        // 4. Scroll element into view and execute a JS click to bypass the modal backdrop overlay (z-[105])
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inboxBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", inboxBtn);

        return new MessagesInboxPage(driver);
    }

    public void acknowledgeSecurityWarning() {
        wait.until(ExpectedConditions.elementToBeClickable(btnAcknowledge)).click();
        wait.until(ExpectedConditions.visibilityOf(txtClaimOtp));

        // navigator.geolocation is async. We must pause to let the browser
        // fetch coordinates and the backend to generate the new Claim OTP.
        try { Thread.sleep(2500); } catch (Exception ignored) {}
    }

    public MessagesInboxPage submitOtpAndOpenInbox(String otp, WebDriver driver) {
        txtClaimOtp.sendKeys(otp);
        btnVerifyClaim.click();
        wait.until(ExpectedConditions.elementToBeClickable(btnOpenInbox)).click();
        return new MessagesInboxPage(driver);
    }
}