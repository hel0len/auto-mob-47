package tests;

import lib.CoreTestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ui.MainPageObject;
import ui.SearhPageObject;

import java.util.List;

// Тесты связанные с поиском
public class SearchTests extends CoreTestCase {

    // Удалить после рефакторинга по пейдж-обджект
    private ui.MainPageObject MainPageObject;

    protected void setUp() throws Exception {

        super.setUp();

        MainPageObject = new MainPageObject(driver);
    } // Удалить после рефакторинга по пейдж-обджект


    // Проверка поиска по тексту
    @Test
    public void testSearch() {

        SearhPageObject SearhPageObject = new SearhPageObject(driver);

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine("Java");
        SearhPageObject.waitForSearchResult("Object-oriented programming language");
    }

    // Проверка отмены поиска
    @Test
    public void testCancelSearch() {
        SearhPageObject SearhPageObject = new SearhPageObject(driver);

        SearhPageObject.initSearchInput();
        SearhPageObject.waitForCancelButtonToAppear();
        SearhPageObject.clickCancelSearch();
        SearhPageObject.waitForCancelButtonToDisappear();
    }

    // Проверка текста плейсхолдера в строке поиска
    @Test
    public void testComparePlaceholderSearchText() {
        MainPageObject.assertElementHasText(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@class='android.widget.TextView']"),
                "Search Wikipedia",
                "Текст плейсхолдера в строке поиска не соответствует ожидаемому");
    }

    // Проверка наличия в поисковой выдаче результатов после отмены поиска
    @Test
    public void testCancelSearchWithResults() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Test",
                "Ошибка ввода текста в строку поиска",
                5);
        List oldListElements = MainPageObject.waitForListElements(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']"));
        assertTrue(
                "В поисковой выдаче отсутствуют результаты",
                oldListElements.size() != 0);
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc=\"Clear query\"]"),
                "Не найден крестик в строке поиска",
                5);
        List newListElements = MainPageObject.waitForListElements(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']"));
        assertTrue(
                "В поисковой выдаче присутствуют результаты",
                newListElements.size() == 0);
    }

    // Проверка наличия искомого слова во всех результатах поиска
    @Test
    public void testMatchResults() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        String searchWord = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                searchWord,
                "Ошибка ввода текста в строку поиска",
                5);
        List listElements = MainPageObject.waitForListElements(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title']"));
        for (Object element : listElements) {
            WebElement webElement = (WebElement) element;
            String title = webElement.getText();
            assertTrue(
                    "Заголовок результата: \"" + title + "\" не содержит искомого слова: \"" + searchWord + "\"",
                    title.contains(searchWord)
            );
        }
    }

    // Проверка количества результатов по запросу с 1 результатом
    @Test
    public void testAmountOfNotEmptySearch() {
        SearhPageObject SearhPageObject = new SearhPageObject(driver);
        String search_word = "Linkin Park diskography";

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine(search_word);
        int amount_of_search_results = SearhPageObject.getAmountOfFoundArticles();
        assertTrue(
                "Найдено меньше результатов поиска чем ожидалось",
                amount_of_search_results > 0);
    }

    // Проверка отсутствия результатов по запросу
    @Test
    public void testAmountOfEmptySearch() {
        SearhPageObject SearhPageObject = new SearhPageObject(driver);
        String search_word = "qazwsxedc";

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine(search_word);
        SearhPageObject.waitForEmptyResultsLabel();
        SearhPageObject.assertThereIsNoResultOfSearch();
    }
}
