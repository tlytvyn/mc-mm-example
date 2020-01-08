package merchants.us;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static java.util.Objects.nonNull;

public class MovemberTest {

    @BeforeMethod
    public void setupBrowser() {
        ChromeOptions chromeOptions = (ChromeOptions) getDefaultChromeLocalCapabilities();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverRunner.setWebDriver(driver);
        com.codeborne.selenide.Configuration.timeout = 10000;
        maximizeScreenResolution(driver);

    }

    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        if (nonNull(WebDriverRunner.getWebDriver())) {
            WebDriverRunner.getWebDriver().quit();
        }
    }

    @Test
    public void movemberMertchantMonitoringTest() {
        open("https://us.movember.com/donate");
        $x("//*[@id='generalDonateBtn']").click();
        $x("//*[text()='$100']").click();
        $x("//input[@id='receiptDetailsForm--firstName']").sendKeys("Testingname");
        $x("//input[@id='receiptDetailsForm--lastName']").sendKeys("Testinglastname");
        $x("//input[@id='receiptDetailsForm--email']").sendKeys("testingMail@gmail.com");
        $x("//*[text()='Click to pay']").click();

        $x("//*[@id='masterpass-button']").click();

        switchToCheckoutWindow();

        $("#card-number").shouldBe(Condition.enabled);
    }

    private final Capabilities getDefaultChromeLocalCapabilities() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--no-sandbox");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--start-maximized");

        return options;
    }

    private void maximizeScreenResolution(WebDriver driver) {
        driver.manage().window().maximize();
        if (nonNull(driver)) {
            int width = NumberUtils.createInteger(((RemoteWebDriver) driver)
                    .executeScript("return window.screen.availWidth").toString());
            int height = NumberUtils.createInteger(((RemoteWebDriver) driver)
                    .executeScript("return window.screen.availHeight").toString());
            driver.manage().window().setSize(new Dimension(width, height));
        }
    }

    protected void switchToCheckoutWindow() {
        String parentWindow = WebDriverRunner.getWebDriver().getWindowHandle();
        new WebDriverWait(WebDriverRunner.getWebDriver(), 15)
                .until(ExpectedConditions.numberOfWindowsToBe(2));
        WebDriverRunner.getWebDriver()
                .switchTo()
                .window(WebDriverRunner.getWebDriver()
                        .getWindowHandles()
                        .stream()
                        .filter(window -> !window.equalsIgnoreCase(parentWindow))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchWindowException("No new window found"))
                );
    }
}
