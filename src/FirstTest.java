import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("automationNane", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/Users/lenachagina/JavaAppiumAutomation/apks/org." +
                "wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

    }

    @After
    public void tearDown() {
        driver.quit();
    }

    // Проверка поиска по тексту
    @Test
    public void testSearch() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Текст ошибки",
                5);
        waitForElementPresent(
                By.xpath("//*[contains(@text, 'Object-oriented programming language')]"),
                "В поисковой выдаче не отображен нужный результат",
                15);
    }

    // Проверка отмены поиска
    @Test
    public void testCancelSearch() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Ошибка ввода текста в строку поиска",
                5);
        waitForElementAndClear(
                By.xpath("//*[contains(@text, 'Java')]"),
                "Ошибка очистки поля ввода",
                5 );
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc=\"Clear query\"]"),
                "Не найден крестик в строке поиска",
                5);
        waitForElementNotPresent(
                By.xpath("//android.widget.ImageView[@content-desc=\"Clear query\"]"),
                "Крестик не должен отображаться в строке поиска",
                5);
    }

    // Проверка соответствия заголовка открытой статьи
    @Test
    public void testCompareArticleTitle() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Ошибка ввода текста в строку поиска",
                5);
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Java (programming language)')]"),
                "В выдаче не найдена страница Java (programming language)",
                5);
        WebElement title_element = waitForElementPresent(
                By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget." +
                        "FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget." +
                        "FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.view." +
                        "ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/" +
                        "android.widget.TextView[1]"),
                "Не найден заголовок страницы",
                5);
        String article_title = title_element.getText();
        Assert.assertEquals(
                "Некорректный заголовок страницы",
                "Java (programming language)",
                article_title);
    }

    // Проверка текста плейсхолдера в строке поиска
    @Test
    public void testComparePlaceholderSearchText() {
        assertElementHasText(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@class='android.widget.TextView']"),
                "Search Wikipedia",
                "Текст плейсхолдера в строке поиска не соответствует ожидаемому" );
    }


    // Ожидание присутствия элемента на странице с явным указанием таймаута
    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSessions) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSessions);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    // Ожидание отсутствия элемента на странице с явным указанием таймаута
    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSessions) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSessions);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    // Ожидание элемента по xpath с дефолтным таймаутом в 5 сек.
    private WebElement waitForElementPresent(By by, String error_message) {

        return waitForElementPresent(by, error_message, 5);
    }

    // Ожидание и клик с явной передачей таймаута
    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSessions) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSessions);
        element.click();
        return element;
    }

    // Ожидание и ввод текста с явной передачей таймаута
    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSessions) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSessions);
        element.sendKeys(value);
        return element;
    }

    // Ожидание и очистка поля с явной передачей таймаута
    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    // Проверка соответствия текста элемента ожидаемому
    private void assertElementHasText(By by, String expected_text, String error_message) {
        Assert.assertEquals(
                error_message,
                expected_text,
                waitForElementPresent(by, error_message).getText()
        );
    }
}


