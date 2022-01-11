package tests;

import lib.CoreTestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import ui.*;

// ---------------------------------- Тесты связанные с детальными страницами статей ----------------------------------
public class ArticleTests extends CoreTestCase {

    private ui.MainPageObject MainPageObject;
    private ui.SearchPageObject SearchPageObject;
    private ui.ArticlePageObject ArticlePageObject;

    protected void setUp() throws Exception {
        super.setUp();
        MainPageObject = new MainPageObject(driver);
        SearchPageObject = new SearchPageObject(driver);
        ArticlePageObject = new ArticlePageObject(driver);
    }

    // Проверка соответствия заголовка открытой статьи
    @Test
    public void testCompareArticleTitle() {
        String search_word = "Java";
        String expected_substring = "Object-oriented programming language";
        String expected_title = "Java (programming language)";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.clickByArticleWithSubstring(expected_substring);
        String actual_title = ArticlePageObject.getArticleTitle();
        assertEquals(
                "Некорректный заголовок страницы",
                expected_title,
                actual_title);
    }

    // Свайп экрана вниз внутри статьи
    @Test
    public void testSwipeArticle() {
        String search_word = "Appium";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.clickByArticleWithSubstring(search_word);
        ArticlePageObject.waitForTitleElement();
        ArticlePageObject.swipeToFooter();
    }

    // Проверка отображения заголовка статьи без ожидания
    @Test
    public void testPresentTitle() {
        String search_word = "Appium";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.clickByArticleWithSubstring(search_word);
        MainPageObject.assertElementPresent(
                By.xpath(ArticlePageObject.TITLE),
                "Не найден заголовок страницы"
        );
    }
}
