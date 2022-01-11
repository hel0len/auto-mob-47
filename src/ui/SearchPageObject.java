package ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class SearchPageObject extends MainPageObject {

    public static final String

            // ------------------------------- Локаторы элементов связанные с поиском ---------------------------------
            SEARCH_INIT_ELEMENT = "//*[contains(@text, 'Search Wikipedia')]",
            SEARCH_INPUT = "//*[contains(@text, 'Search…')]",
            SEARCH_CANCEL_BUTTON = "//android.widget.ImageView[@content-desc=\"Clear query\"]",
            SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='{SUBSTRING}']",
            SEARCH_RESULT_ELEMENTS = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
                    "/*[@resource-id='org.wikipedia:id/page_list_item_container']",
            SEARCH_EMPTY_RESULT_ELEMENT = "//*[@text='No results found']",
            SEARCH_LINE_PLACEHOLDER = "//*[@resource-id='org.wikipedia:id/search_container']" +
                    "//*[@class='android.widget.TextView']",
            SEARCH_RESULTS_TITLES = "//*[@resource-id='org.wikipedia:id/page_list_item_title']",

            // -------------------------------- Статичные значения текстовых элементов --------------------------------
            TEXT_PLACEHOLDER = "Search Wikipedia";

    public SearchPageObject(AppiumDriver driver) {

        super(driver);
    }

    // Инициирование поиска
    public void initSearchInput() {
        this.waitForElementPresent(
                By.xpath(SEARCH_INIT_ELEMENT),
                "На экране не найдена строка поиска");
        this.waitForElementAndClick(
                By.xpath(SEARCH_INIT_ELEMENT),
                "Не удалось кликнуть на строку поиска",
                5);
    }

    // Ввод переданного значения в строку поиска
    public void typeSearchLine(String value) {
        this.waitForElementAndSendKeys(
                By.xpath(SEARCH_INPUT),
                value,
                "Ошибка ввода текста в строку поиска",
                5);
    }

    // Проверка отображения в поисоковой выдаче результата с переданным текстом
    public void waitForSearchResult(String substring) {
        String result_locator = getLocator(SEARCH_RESULT_BY_SUBSTRING_TPL, substring);
        this.waitForElementPresent(
                By.xpath(result_locator),
                "В поисковой выдаче не отображен результат с подстрокой: " + substring,
                10);
    }

    // Клик по результату с переданной подстрокой в поисковой выдаче
    public void clickByArticleWithSubstring(String substring) {
        String result_locator = getLocator(SEARCH_RESULT_BY_SUBSTRING_TPL, substring);
        this.waitForElementAndClick(
                By.xpath(result_locator),
                "Не удалось кликнуть по результату в поисковой выдаче с подстрокой: " + substring,
                10);
    }

    // Проверка наличия кнопки 'x' в строке поиска
    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(
                By.xpath(SEARCH_CANCEL_BUTTON),
                "Не найдена кнопка 'х' в строке поиска",
                5);
    }

    // Проверка отсутствия кнопки 'x' в строке поиска
    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(
                By.xpath(SEARCH_CANCEL_BUTTON),
                "Кнопка 'х' не должна отображаться в строке поиска",
                5);
    }

    // Клик на кнопку отмены ('x') в строке поиска
    public void clickCancelSearch() {
        this.waitForElementAndClick(
                By.xpath(SEARCH_CANCEL_BUTTON),
                "Не удалось нажатие на кнопку 'х' в строке поиска",
                5);
    }

    // Возвращает количество элементов в поисковой выдаче
    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(
                By.xpath(SEARCH_RESULT_ELEMENTS),
                "По запросу не найдены результаты",
                10);
        return this.getAmountOfElements(By.xpath(SEARCH_RESULT_ELEMENTS));
    }

    // Проверка наличия на экране сообщения об отсутствии результатов в поисковой выдаче
    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(
                By.xpath(SEARCH_EMPTY_RESULT_ELEMENT),
                "Не найдено сообщение об отсутствии результатов по поисковому запросу: 'No results found'",
                15);
    }

    // Проверка отсутствия результатов поиска на экране
    public void assertThereIsNoResultOfSearch() {
        this.assertElementNotPresent(
                By.xpath(SEARCH_RESULT_ELEMENTS),
                "По запросу не должны быть найдены результаты");
    }

    // Проверка соответствия плейсхолдела в строке поиска ожидаемому
    public void assertComparePlaceholder() {
        this.assertElementHasText(
                By.xpath(SEARCH_LINE_PLACEHOLDER),
                TEXT_PLACEHOLDER,
                "Текст плейсхолдера в строке поиска не соответствует ожидаемому");
    }

}
