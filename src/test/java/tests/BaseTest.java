package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        Map prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.geolocation", 1); // 1 means 'Allow'
        options.setExperimentalOption("prefs", prefs);

        // Start Chrome
        ChromeDriver chromeDriver = new ChromeDriver(options);
        driver = chromeDriver;

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Forcefully grant the Geolocation permission via DevTools
        chromeDriver.executeCdpCommand("Browser.grantPermissions", Map.of(
                "origin", "http://localhost:5173",
                "permissions", List.of("geolocation")
        ));

        // Inject the fake Kathmandu coordinates
        chromeDriver.executeCdpCommand("Emulation.setGeolocationOverride", Map.of(
                "latitude", 27.7172,
                "longitude", 85.3240,
                "accuracy", 100
        ));

        driver.get("http://localhost:5173");

        // Wipe local storage to ensure a clean state between test runs
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.clear();");
        driver.navigate().refresh();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}