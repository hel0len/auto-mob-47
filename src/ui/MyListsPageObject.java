package ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject {

    public static final String

            // ----------------------- Локаторы элементов на экране пользовательских списков --------------------------
            FOLDER_BY_NAME_TPL = "//*[@resource-id='org.wikipedia:id/item_title'][@text='{SUBSTRING}']",
            ARTICLE_BY_TITLE_TPL = "//*[@text='{SUBSTRING}']";

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    // Клик на список по переданному имени
    public void openFolderByName(String folder_name) {
        this.waitForElementAndClick(
                By.xpath(getLocatorWithOneSubstring(FOLDER_BY_NAME_TPL, folder_name)),
                "Не найден список с названием" + folder_name,
                5);
    }

    // Проверка отсутствия статьи в списке по переданному имени
    public void waitForArticleAppearByTitle(String article_title) {
        this.waitForElementNotPresent(
                By.xpath(getLocatorWithOneSubstring(ARTICLE_BY_TITLE_TPL, article_title)),
                "Cтатья: " + article_title + " не отображается в списке",
                10);
    }

    // Удаление статьи свайпом влево
    public void swipeArticleToDelete(String article_title) {
        this.waitForArticleAppearByTitle(article_title);
        this.swipeElementToLeft(
                By.xpath(getLocatorWithOneSubstring(ARTICLE_BY_TITLE_TPL, article_title)),
                "Не удалось удаление статьи " + article_title + " из списка с помощью свайпа влево");
    }

    // Проверка отсутствия статьи в списке по переданному имени
    public void waitForArticleToDisappearByTitle(String article_title) {
        this.waitForElementNotPresent(
                By.xpath(getLocatorWithOneSubstring(ARTICLE_BY_TITLE_TPL, article_title)),
                "Cтатья: " + article_title + " отображается в списке",
                10);
    }

    // Открытие статьи в списке по переданному имени
    public void openArticleInListByTitle(String article_title) {
        this.waitForElementAndClick(
                By.xpath(getLocatorWithOneSubstring(ARTICLE_BY_TITLE_TPL, article_title)),
                "Cтатья: " + article_title + " не найдена в списке",
                10);
    }
}
