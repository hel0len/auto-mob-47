package tests;

import lib.CoreTestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import ui.*;

public class MyListsTests extends CoreTestCase {

    // Удалить после рефакторинга по пейдж-обджект
    private ui.MainPageObject MainPageObject;

    protected void setUp() throws Exception {

        super.setUp();

        MainPageObject = new MainPageObject(driver);
    } // Удалить после рефакторинга по пейдж-обджект


    // Добавление и удаление одной статьи из списка 'My lists'
    @Test
    public void testSaveFirstArticleToMyList() {
        SearhPageObject SearhPageObject = new SearhPageObject(driver);
        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        NavigationUI NavigationUI = new NavigationUI(driver);
        MyListsPageObject MyListsPageObject = new MyListsPageObject(driver);

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine("Java");
        SearhPageObject.clickByArticleWithSubstring("Object-oriented programming language");
        ArticlePageObject.waitForTitleElement();
        String article_title = ArticlePageObject.getArticleTitle();
        String folder_name = "Learning programming";
        ArticlePageObject.addArticleToMyList(folder_name);
        ArticlePageObject.closeArticle();
        NavigationUI.clickMyLists();
        MyListsPageObject.openFolderByName(folder_name);
        MyListsPageObject.swipeArticleToDelete(article_title);
        MyListsPageObject.waitForArticleToDisappearByTitle(article_title);
    }

    // Добавление двух статей и удаление одной из списка 'My lists'
    @Test
    public void testSaveTwoArticlesToMyList() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                10);
        String first_search_word = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                first_search_word,
                "Ошибка ввода текста в строку поиска",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Java (programming language)')]"),
                "В выдаче не найдена страница Java (programming language)",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Не найдена кнопка 'More options'",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Не найдена кнопка 'Add to reading list' в выпадающем списке",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'GOT IT')]"),
                "Не найдена кнопка 'Go it' в модалке",
                10);
        MainPageObject.waitForElementAndClear(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                "Не найдено поле ввода названия списка",
                10);
        String folder_name = "Learning programming";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[@resource-id='org.wikipedia:id/text_input']"),
                folder_name,
                "Не найдено поле ввода названия списка",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Не удалось нажатие кнопки 'OK' в окне создания списка",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Не найдена кнопка закрытия статьи - 'x'",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                10);
        String second_search_word = "Appium";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                second_search_word,
                "Ошибка ввода текста в строку поиска",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][contains(@text, '" +
                        second_search_word + "')]"),
                "В выдаче не найдена страница " + second_search_word,
                15);
        String old_second_article_title = MainPageObject.waitForElementPresent(
                        By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                        "Не найден заголовок страницы",
                        10)
                .getText();
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Не найдена кнопка 'More options'",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Не найдена кнопка 'Add to reading list' в выпадающем списке",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folder_name + "']"),
                "Не найден созданный список при добавлении в него новой статьи",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Не найдена кнопка закрытия статьи - 'x'",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Не найдена кнопка нижнего меню 'My lists'",
                10);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folder_name + "']"),
                "Не найден созданный список с названием" + folder_name,
                10);
        MainPageObject.swipeElementToLeft(
                By.xpath("//*[contains(@text, 'Java (programming language)')]"),
                "Не удалось удаление статьи из списка с помощью свайпа влево");
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + second_search_word + "']"),
                "В списке не найдена НЕудаленная статья",
                10);
        String new_second_article_title = MainPageObject.waitForElementPresent(
                        By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                        "Не найден заголовок страницы",
                        10)
                .getText();
        assertEquals(
                "Заголовок статьи оставшейся в списке: " + new_second_article_title +
                        " не совпадает с заголовком статьи, добавленной в список: " + old_second_article_title,
                old_second_article_title,
                new_second_article_title);
    }
}
