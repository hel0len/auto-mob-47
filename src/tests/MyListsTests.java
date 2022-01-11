package tests;

import lib.CoreTestCase;
import org.junit.Test;
import ui.*;

// -------------------------------- Тесты связанные с пользовательскими списками статей -------------------------------

public class MyListsTests extends CoreTestCase {

    private ui.SearchPageObject SearchPageObject;
    private ui.NavigationUI NavigationUI;
    private ui.MyListsPageObject MyListsPageObject;
    private ui.ArticlePageObject ArticlePageObject;

    protected void setUp() throws Exception {
        super.setUp();
        SearchPageObject = new SearchPageObject(driver);
        NavigationUI = new NavigationUI(driver);
        MyListsPageObject = new MyListsPageObject(driver);
        ArticlePageObject = new ArticlePageObject(driver);
    }

    // Добавление и удаление одной статьи из списка 'My lists'
    @Test
    public void testSaveFirstArticleToMyList() {
        String search_word = "Java";
        String expected_substring = "Object-oriented programming language";
        String folder_name = "Learning programming";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(search_word);
        SearchPageObject.clickByArticleWithSubstring(expected_substring);
        ArticlePageObject.waitForTitleElement();
        String article_title = ArticlePageObject.getArticleTitle();
        ArticlePageObject.addArticleToNewMyList(folder_name);
        ArticlePageObject.closeArticle();
        NavigationUI.clickMyLists();
        MyListsPageObject.openFolderByName(folder_name);
        MyListsPageObject.swipeArticleToDelete(article_title);
        MyListsPageObject.waitForArticleToDisappearByTitle(article_title);
    }

    // Добавление двух статей и удаление одной из списка 'My lists'
    @Test
    public void testSaveTwoArticlesToMyList() {
        String first_search_word = "Java";
        String expected_substring = "Object-oriented programming language";
        String folder_name = "Learning programming";
        String second_search_word = "Appium";

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(first_search_word);
        SearchPageObject.clickByArticleWithSubstring(expected_substring);
        ArticlePageObject.waitForTitleElement();
        String first_article_title = ArticlePageObject.getArticleTitle();
        ArticlePageObject.addArticleToNewMyList(folder_name);
        ArticlePageObject.closeArticle();
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine(second_search_word);
        SearchPageObject.clickByArticleWithSubstring(second_search_word);
        String old_second_article_title = ArticlePageObject.getArticleTitle();
        ArticlePageObject.addArticleToOldMyList(folder_name);
        ArticlePageObject.closeArticle();
        NavigationUI.clickMyLists();
        MyListsPageObject.openFolderByName(folder_name);
        MyListsPageObject.swipeArticleToDelete(first_article_title);
        MyListsPageObject.openArticleInListByTitle(old_second_article_title);
        String new_second_article_title = ArticlePageObject.getArticleTitle();
        assertEquals(
                "Заголовок статьи оставшейся в списке: " + new_second_article_title +
                        " не совпадает с заголовком статьи, добавленной в список: " + old_second_article_title,
                old_second_article_title,
                new_second_article_title);
    }
}
