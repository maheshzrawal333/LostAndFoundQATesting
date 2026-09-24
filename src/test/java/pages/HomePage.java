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

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//button[contains(text(), 'Sign In')]")
    private WebElement btnSignIn;

    @FindBy(xpath = "//button[contains(., '+ Report Item')]")
    private WebElement btnReportItem;

    @FindBy(xpath = "//div[contains(@class, 'group') and .//span[contains(text(), 'Verified User')]]")
    private WebElement btnProfileDropdown;

    @FindBy(xpath = "//button[contains(., 'Send Feedback')]")
    private WebElement btnSendFeedback;

    @FindBy(xpath = "//input[@placeholder='e.g. Wallet, Sony, Keys...']")
    private WebElement txtSearchItem;

    @FindBy(xpath = "//input[@placeholder='e.g. Kathmandu, Bus Park...']")
    private WebElement txtSearchLocation;

    @FindBy(xpath = "//button[contains(., 'Search Items')]")
    private WebElement btnExecuteSearch;

    @FindBy(xpath = "//button[contains(., 'My Posts')]")
    private WebElement tabMyPosts;

    @FindBy(xpath = "//button[contains(., 'Page')]/following-sibling::button")
    private WebElement btnNextPage;

    @FindBy(xpath = "//button[contains(text(), 'Privacy Policy')]")
    private WebElement linkPrivacyPolicy;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public LoginDrawerPage clickSignIn() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSignIn)).click();
        return new LoginDrawerPage(driver);
    }

    public ReportItemPage clickReportItem() {
        wait.until(ExpectedConditions.elementToBeClickable(btnReportItem)).click();
        return new ReportItemPage(driver);
    }

    public AccountSettingsPage openAccountSettings() {
        wait.until(ExpectedConditions.elementToBeClickable(btnProfileDropdown)).click();
        return new AccountSettingsPage(driver);
    }

    public FeedbackPage openFeedbackModal() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSendFeedback)).click();
        return new FeedbackPage(driver);
    }

    public void performSearch(String item, String location) {
        wait.until(ExpectedConditions.visibilityOf(txtSearchItem)).clear();
        txtSearchItem.sendKeys(item);
        txtSearchLocation.clear();
        txtSearchLocation.sendKeys(location);

        // Javascript click bypasses the sticky React Navigation bar overlay
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnExecuteSearch);

        try { Thread.sleep(1500); } catch (Exception ignored) {}
    }

    public void filterByMyPosts() {
        WebElement btnMyPosts = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(., 'My Posts')]")));

        // Scroll the element into view to clear top fixed headers
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnMyPosts);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnMyPosts)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            // Fallback to JavaScript click if an overlay or sticky header intercepts the standard click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnMyPosts);
        }
    }

    public void goToNextPage() {
        wait.until(ExpectedConditions.elementToBeClickable(btnNextPage)).click();
    }

    public LegalPage openPrivacyPolicy() {
        wait.until(ExpectedConditions.elementToBeClickable(linkPrivacyPolicy)).click();
        return new LegalPage(driver);
    }
}