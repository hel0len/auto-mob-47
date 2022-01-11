package tests;

import lib.CoreTestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import ui.ArticlePageObject;
import ui.MainPageObject;
import ui.SearhPageObject;

public class ArticleTests extends CoreTestCase {

    // Удалить после рефакторинга по пейдж-обджект
    private ui.MainPageObject MainPageObject;

    protected void setUp() throws Exception {

        super.setUp();

        MainPageObject = new MainPageObject(driver);
    } // Удалить после рефакторинга по пейдж-обджект

    // Проверка соответствия заголовка открытой статьи
    @Test
    public void testCompareArticleTitle() {
        SearhPageObject SearhPageObject = new SearhPageObject(driver);
        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine("Java");
        SearhPageObject.clickByArticleWithSubstring("Object-oriented programming language");
        String article_title = ArticlePageObject.getArticleTitle();
        assertEquals(
                "Некорректный заголовок страницы",
                "Java (programming language)",
                article_title);
    }

    // Свайп экрана вниз внутри статьи
    @Test
    public void testSwipeArticle() {
        SearhPageObject SearhPageObject = new SearhPageObject(driver);
        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);

        SearhPageObject.initSearchInput();
        SearhPageObject.typeSearchLine("Appium");
        SearhPageObject.clickByArticleWithSubstring("Appium");
        ArticlePageObject.waitForTitleElement();
        ArticlePageObject.swipeToFooter();
    }

    // Проверка отображения заголовка статьи без ожидания
    @Test
    public void testPresentTitle() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Не найдена строка поиска",
                5);
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Appium",
                "Ошибка ввода текста в строку поиска",
                5);
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][contains(@text, 'Appium')]"),
                "В выдаче не найдена страница Appium",
                15);
        MainPageObject.assertElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/view_page_title_text']"),
                "Не найден заголовок страницы"
        );
    }
}
