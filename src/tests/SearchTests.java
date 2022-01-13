package tests;

import lib.CoreTestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import ui.MainPageObject;
import ui.SearchPageObject;

import java.util.List;

// ----------------------------------- Тесты связанные с пользовательскими поиском ------------------------------------
public class SearchTests extends CoreTestCase {

    private ui.MainPageObject MainPageObject;
    private ui.SearchPageObject SearchPageObject;

    protected void setUp() throws Exception {
        super.setUp();
        MainPageObject = new MainPageObject(driver);
        SearchPageObject = new SearchPageObject(driver);
    }

    // Проверка поиска по тексту
    @Test
    public void testSearch() {
        String search_word = "Java";
        String expected_substring = "Object-oriented programming language";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.waitForSearchResult(expected_substring);
    }

    // Проверка отмены поиска
    @Test
    public void testCancelSearch() {
        SearchPageObject.initSearchInput();
        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelSearch();
        SearchPageObject.waitForCancelButtonToDisappear();
    }

    // Проверка текста плейсхолдера в строке поиска
    @Test
    public void testComparePlaceholderSearchText() {
        SearchPageObject.assertComparePlaceholder();
    }

    // Проверка наличия в поисковой выдаче результатов после отмены поиска
    @Test
    public void testCancelSearchWithResults() {
        String search_word = "Test";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        int amountElements = SearchPageObject.getAmountOfFoundArticles();
        assertTrue(
                "В поисковой выдаче отсутствуют результаты",
                amountElements != 0);
        SearchPageObject.clickCancelSearch();
        SearchPageObject.assertThereIsNoResultOfSearch();
    }

    // Проверка наличия искомого слова во всех результатах поиска
    @Test
    public void testMatchResults() {
        String search_word = "Java";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        List listElements = MainPageObject.waitForListElements(
                By.xpath(SearchPageObject.SEARCH_RESULTS_TITLES));
        MainPageObject.assertCompareTextElementsInList(listElements, search_word);
    }

    // Проверка количества результатов по запросу с 1 результатом
    @Test
    public void testAmountOfNotEmptySearch() {
        String search_word = "Linkin Park diskography";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        int amount_of_search_results = SearchPageObject.getAmountOfFoundArticles();
        assertTrue(
                "Количество результатов: " + amount_of_search_results + " не соответствует ожидаемому: 1",
                amount_of_search_results == 1);
    }

    // Проверка отсутствия результатов по запросу
    @Test
    public void testAmountOfEmptySearch() {
        String search_word = "qazwsxedc";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.waitForEmptyResultsLabel();
        SearchPageObject.assertThereIsNoResultOfSearch();
    }

    // Проверка наличия искомого слова в 3 результатах поиска c локатором, зависящим от двух подстрок
    @Test
    public void testMatchResultsWithCompositeLocator() {
        String search_word = "Alexander";
        String description_1 = "Male given name",
               description_2 = "King of Macedon",
               description_3 = "First Secretary of the Treasury and Founding Father of the United States (1757-1804)";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.waitForElementByTitleAndDescription(search_word, description_1);
        SearchPageObject.waitForElementByTitleAndDescription(search_word, description_2);
        SearchPageObject.waitForElementByTitleAndDescription(search_word, description_3);
    }
}
