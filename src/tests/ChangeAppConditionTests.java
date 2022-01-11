package tests;

import lib.CoreTestCase;
import org.junit.Test;
import ui.ArticlePageObject;
import ui.SearhPageObject;

public class ChangeAppConditionTests extends CoreTestCase {

    // Проверка соответствия результатов поиска при изменении ориентации экрана
    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        SearhPageObject SearhPageObject = new SearhPageObject(driver);
        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        String search_word = "Java";

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine(search_word);
        SearhPageObject.clickByArticleWithSubstring("Object-oriented programming language");
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
        SearhPageObject SearhPageObject = new SearhPageObject(driver);
        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        String search_word = "Java";

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine(search_word);
        SearhPageObject.waitForSearchResult("Object-oriented programming language");
        this.backgroundApp(2);
        SearhPageObject.waitForSearchResult("Object-oriented programming language");
    }

}
