import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("deviceName", "and80");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/Users/lenachagina/Projects/JavaAppiumAutomation/apks/org.wikipedia.apk");
        capabilities.setCapability("orientation", "PORTRAIT");

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
                5);
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
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
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
                "Текст плейсхолдера в строке поиска не соответствует ожидаемому");
    }

    // Проверка наличия в поисковой выдаче результатов после отмены поиска
    @Test
    public void testCancelSearchWithResults() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Test",
                "Ошибка ввода текста в строку поиска",
                5);
        List oldListElements = waitForListElements(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']"));
        Assert.assertTrue(
                "В поисковой выдаче отсутствуют результаты",
                oldListElements.size() != 0);
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc=\"Clear query\"]"),
                "Не найден крестик в строке поиска",
                5);
        List newListElements = waitForListElements(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']"));
        Assert.assertTrue(
                "В поисковой выдаче присутствуют результаты",
                newListElements.size() == 0);
    }

    // Проверка наличия искомого слова во всех результатах поиска
    @Test
    public void testMatchResults() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        String searchWord = "Java";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                searchWord,
                "Ошибка ввода текста в строку поиска",
                5);
        List listElements = waitForListElements(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title']"));
        for (Object element : listElements) {
            WebElement webElement = (WebElement) element;
            String title = webElement.getText();
            Assert.assertTrue(
                    "Заголовок результата: \"" + title + "\" не содержит искомого слова: \"" + searchWord + "\"",
                    title.contains(searchWord)
            );
        }
    }

    // Свайп экрана вниз внутри статьи
    @Test
    public void testSwipeArticle() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Appium",
                "Ошибка ввода текста в строку поиска",
                5);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][contains(@text, 'Appium')]"),
                "В выдаче не найдена страница Appium",
                15);
        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок страницы",
                5);
        swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Не найден элемент в футере",
                20);
    }

    // Добавление и удаление одной статьи из списка 'My lists'
    @Test
    public void saveFirstArticleToMyList() {
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
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Не найдена кнопка 'More options'",
                5);
        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Не найдена кнопка 'Add to reading list' в выпадающем списке",
                10);
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'GOT IT')]"),
                "Не найдена кнопка 'Go it' в модалке",
                5);
        waitForElementAndClear(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                "Не найдено поле ввода названия списка",
                5);
        String folder_name = "Learning programming";
        waitForElementAndSendKeys(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                folder_name,
                "Не найдено поле ввода названия списка",
                5);
        waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Не удалось нажатие кнопки 'OK' в окне создания списка",
                5);
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Не найдена кнопка закрытия статьи - 'x'",
                5);
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Не найдена кнопка нижнего меню 'My lists'",
                10);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folder_name + "']"),
                "Не найден созданный список с названием" + folder_name,
                5);
        swipeElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Не удалось удаление статьи из списка с помощью свайпа влево");
        waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Удаленная статья отображается в списке",
                5
        );
    }

    // Проверка количества результатов по запросу с 1 результатом
    @Test
    public void testAmountOfNotEmptySearch() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        String search_word = "Linkin Park diskography";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                search_word,
                "Ошибка ввода текста в строку поиска",
                5);
        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
                "/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        waitForElementPresent(
                By.xpath(search_result_locator),
                "Не найдены результаты по запросу: " + search_word,
                15);
        int amount_of_search_results = getAmountOfElements(
                By.xpath(search_result_locator));
        Assert.assertTrue(
                "Найдено меньше результатов поиска чем ожидалось",
                amount_of_search_results > 0);
    }

    // Проверка отсутствия результатов при пустой выдаче
    @Test
    public void testAmountOfEmptySearch() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        String search_word = "qazwsxedc";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                search_word,
                "Ошибка ввода текста в строку поиска",
                5);
        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        String empty_result_label = "//*[@text='No results found']";
        waitForElementPresent(
                By.xpath(empty_result_label),
                "Не найдено сообщение о пустой выдаче по запросу:  " + search_word,
                15);
        assertElementNotPresent(
                By.xpath(search_result_locator),
                "Найдены результаты по запросу: " + search_word);
    }

    // Проверка соответствия результатов поиска при изменении ориентации экрана
    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        String search_word = "Java";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                search_word,
                "Ошибка ввода текста в строку поиска",
                5);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "В выдаче не найдена страница по запросу: " + search_word,
                5);
        String title_before_rotation = waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок статьи до поворота экрана",
                15)
                .getText();
        driver.rotate(ScreenOrientation.LANDSCAPE);
        String title_after_rotation = waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок статьи после поворота экрана в ландскейп",
                15)
                .getText();
        Assert.assertEquals(
                "Название статьи было изменено после поворота экрана в ландскейп",
                title_before_rotation,
                title_after_rotation);
        driver.rotate(ScreenOrientation.PORTRAIT);
        String title_after_second_rotation = waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок статьи после поворота экрана в начальное положение",
                15)
                .getText();
        Assert.assertEquals(
                "Название статьи было изменено после поворота экрана начальное положение",
                title_before_rotation,
                title_after_second_rotation);
    }

    // Проверка сохранения выдачи поиска после сворачивания приложения в трей
    @Test
    public void testCheckSearchArticleInBackground() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        String search_word = "Java";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                search_word,
                "Ошибка ввода текста в строку поиска",
                5);
        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "//*[@text='Object-oriented programming language']"),
                "В выдаче не найдена страница по запросу: " + search_word,
                5);

        // Свернуть приложение в трей на 2 сек и развернуть обратно
        driver.runAppInBackground(2);

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Java (programming language)']"),
                "После возврата приложения из трея статья на экране не найдена",
                5);
    }

    // Добавление двух статей и удаление одной из списка 'My lists'
    @Test
    public void saveTwoArticlesToMyList() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                10);
        String first_search_word = "Java";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                first_search_word,
                "Ошибка ввода текста в строку поиска",
                10);
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Java (programming language)')]"),
                "В выдаче не найдена страница Java (programming language)",
                10);
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Не найдена кнопка 'More options'",
                10);
        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Не найдена кнопка 'Add to reading list' в выпадающем списке",
                10);
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'GOT IT')]"),
                "Не найдена кнопка 'Go it' в модалке",
                10);
        waitForElementAndClear(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                "Не найдено поле ввода названия списка",
                10);
        String folder_name = "Learning programming";
        waitForElementAndSendKeys(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                folder_name,
                "Не найдено поле ввода названия списка",
                10);
        waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Не удалось нажатие кнопки 'OK' в окне создания списка",
                10);
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Не найдена кнопка закрытия статьи - 'x'",
                10);
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                10);
        String second_search_word = "Appium";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                second_search_word,
                "Ошибка ввода текста в строку поиска",
                10);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][contains(@text, '" +
                        second_search_word + "')]"),
                "В выдаче не найдена страница " + second_search_word,
                15);
        String old_second_article_title = waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок страницы",
                10)
                .getText();
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Не найдена кнопка 'More options'",
                10);
        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Не найдена кнопка 'Add to reading list' в выпадающем списке",
                10);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folder_name + "']"),
                "Не найден созданный список при добавлении в него новой статьи",
                10);
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Не найдена кнопка закрытия статьи - 'x'",
                10);
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Не найдена кнопка нижнего меню 'My lists'",
                10);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folder_name + "']"),
                "Не найден созданный список с названием" + folder_name,
                10);
        swipeElementToLeft(
                By.xpath("//*[contains(@text, 'Java (programming language)')]"),
                "Не удалось удаление статьи из списка с помощью свайпа влево");
        waitForElementAndClick(
                By.xpath("//*[@text='" + second_search_word + "']"),
                "В списке не найдена НЕудаленная статья",
                10);
        String new_second_article_title = waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок страницы",
                10)
                .getText();
        Assert.assertEquals(
                "Заголовок статьи оставшейся в списке: " + new_second_article_title +
                        " не совпадает с заголовком статьи, добавленной в список: " + old_second_article_title,
                old_second_article_title,
                new_second_article_title);
    }

    // Проверка отображения заголовка статьи без ожидания
    @Test
    public void testPresentTitle() {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Appium",
                "Ошибка ввода текста в строку поиска",
                5);
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][contains(@text, 'Appium')]"),
                "В выдаче не найдена страница Appium",
                15);
        assertElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок страницы"
        );
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

    // Получение списка элементов на странице по локатору
    private List<WebElement> waitForListElements(By by) {
        return driver.findElements(by);
    }

    // Свайп экрана вверх
    protected void swipeUp(int timeOfSwipe) {
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
    protected void swipeUpQuick() {
        swipeUp(200);
    }

    // Свайп вверх до переданного элемента
    protected void swipeUpToFindElement(By by, String errorMessage, int maxSwipes) {
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
    protected void swipeElementToLeft(By by, String error_message) {
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
    private int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();
    }

    // Проверка отсутствия элементов на экране по переданному локатору
    private void assertElementNotPresent(By by, String error_message) {
        int amountOfElements = getAmountOfElements(by);
        if (amountOfElements > 0) {
            String default_message = "Элемент с локатором '" + by.toString() + "' должен отсутствовать на экране";
            throw new AssertionError(default_message + "\n" + error_message);
        }
    }

    // Получение значения переданного атрибута
    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    // Выбрасывает ошибку в случае если не найден элемент по переданному локатору
    private void assertElementPresent(By by, String error_message) {
        int amountOfElements = getAmountOfElements(by);
        if (amountOfElements == 0) {
            String default_message = "Элемент с локатором '" + by.toString() + "' отсутствует на экране";
            throw new AssertionError(default_message + "\n" + error_message);
        }
    }


}


