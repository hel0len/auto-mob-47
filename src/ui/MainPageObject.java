package ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPageObject {

    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {

        this.driver = driver;
    }

    // Ожидание присутствия элемента на странице с явным указанием таймаута
    public WebElement waitForElementPresent(By by, String error_message, long timeoutInSessions) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSessions);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    // Ожидание и клик с явной передачей таймаута
    public WebElement waitForElementAndClick(By by, String error_message, long timeoutInSessions) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSessions);
        element.click();
        return element;
    }

    // Ожидание отсутствия элемента на странице с явным указанием таймаута
    public boolean waitForElementNotPresent(By by, String error_message, long timeoutInSessions) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSessions);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    // Ожидание элемента по xpath с дефолтным таймаутом в 5 сек.
    public WebElement waitForElementPresent(By by, String error_message) {

        return waitForElementPresent(by, error_message, 5);
    }

    // Ожидание и ввод текста с явной передачей таймаута
    public WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSessions) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSessions);
        element.sendKeys(value);
        return element;
    }

    // Ожидание и очистка поля с явной передачей таймаута
    public WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    // Проверка соответствия текста элемента ожидаемому
    public void assertElementHasText(By by, String expected_text, String error_message) {
        Assert.assertEquals(
                error_message,
                expected_text,
                waitForElementPresent(by, error_message).getText()
        );
    }

    // Получение списка элементов на странице по локатору
    public List<WebElement> waitForListElements(By by) {
        return driver.findElements(by);
    }

    // Свайп экрана вверх
    public void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

        action
                .press(x, start_y)
                .waitAction(timeOfSwipe)
                .moveTo(x, end_y)
                .release()
                .perform();
    }

    // Бытрый свайп вверх
    public void swipeUpQuick() {
        swipeUp(200);
    }

    // Свайп вверх до переданного элемента
    public void swipeUpToFindElement(By by, String errorMessage, int maxSwipes) {
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0) {
            if (already_swiped > maxSwipes) {
                waitForElementPresent(
                        by,
                        "Не найден элемент после свайпа \n" + errorMessage,
                        0);
                return;
            }
            swipeUpQuick();
            already_swiped++;
        }
    }

    // Свайп экрана влево по переданному элементу
    public void swipeElementToLeft(By by, String error_message) {
        WebElement element = waitForElementPresent(
                by,
                error_message,
                10);
        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(300)
                .moveTo(left_x, middle_y)
                .release()
                .perform();
    }

    // Возвращает количество элементов на экране по переданному локатору
    public int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();
    }

    // Проверка отсутствия элементов на экране по переданному локатору
    public void assertElementNotPresent(By by, String error_message) {
        int amountOfElements = getAmountOfElements(by);
        if (amountOfElements > 0) {
            String default_message = "Элемент с локатором '" + by.toString() + "' должен отсутствовать на экране";
            throw new AssertionError(default_message + "\n" + error_message);
        }
    }

    // Получение значения переданного атрибута
    public String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    // Выбрасывает ошибку в случае если не найден элемент по переданному локатору
    public void assertElementPresent(By by, String error_message) {
        int amountOfElements = getAmountOfElements(by);
        if (amountOfElements == 0) {
            String default_message = "Элемент с локатором '" + by.toString() + "' отсутствует на экране";
            throw new AssertionError(default_message + "\n" + error_message);
        }
    }

}
