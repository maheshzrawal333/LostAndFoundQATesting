package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LegalPage {
    private WebDriverWait wait;

    @FindBy(xpath = "//h2[contains(text(), 'How It Works') or contains(text(), 'Safety Guidelines') or contains(text(), 'Privacy Policy') or contains(text(), 'Terms of Service')]")
    private WebElement modalHeader;

    @FindBy(xpath = "//button[contains(text(), 'Understood')]")
    private WebElement btnUnderstood;

    public LegalPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void verifyModalAndClose() {
        wait.until(ExpectedConditions.visibilityOf(modalHeader));
        wait.until(ExpectedConditions.elementToBeClickable(btnUnderstood)).click();
        wait.until(ExpectedConditions.invisibilityOf(modalHeader));
    }
}
