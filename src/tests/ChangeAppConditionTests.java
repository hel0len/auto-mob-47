package tests;

import lib.CoreTestCase;
import org.junit.Test;
import ui.*;

// -------------------------------- Тесты связанные изменениями состояния приложения ----------------------------------
public class ChangeAppConditionTests extends CoreTestCase {

    private ui.SearchPageObject SearchPageObject;
    private ui.ArticlePageObject ArticlePageObject;

    protected void setUp() throws Exception {
        super.setUp();
        SearchPageObject = new SearchPageObject(driver);
        ArticlePageObject = new ArticlePageObject(driver);
    }

    // Проверка соответствия результатов поиска при изменении ориентации экрана
    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        String search_word = "Java";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");
        String title_before_rotation = ArticlePageObject.getArticleTitle();
        this.rotateScreeLandScape();
        String title_after_rotation = ArticlePageObject.getArticleTitle();
        assertEquals(
                "Название статьи было изменено после поворота экрана в ландскейп",
                title_before_rotation,
                title_after_rotation);
        this.rotateScreenPortrait();
        String title_after_second_rotation = ArticlePageObject.getArticleTitle();
        assertEquals(
                "Название статьи было изменено после поворота экрана начальное положение",
                title_before_rotation,
                title_after_second_rotation);
    }

    // Проверка сохранения выдачи поиска после сворачивания приложения в трей
    @Test
    public void testCheckSearchArticleInBackground() {
        String search_word = "Java";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.waitForSearchResult("Object-oriented programming language");
        this.backgroundApp(2);
        SearchPageObject.waitForSearchResult("Object-oriented programming language");
    }
}
