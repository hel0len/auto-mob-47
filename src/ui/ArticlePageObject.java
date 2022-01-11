package ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject{

    private static final String
        TITLE = "//*[@resource-id='org.wikipedia:id/view_page_title_text']",
        LINK_IN_FOOTER = "//*[@text='View page in browser']",
        OPTIONS_BUTTON = "//android.widget.ImageView[@content-desc='More options']",
        OPTION_ADD_TO_READING_LIST = "//*[@text='Add to reading list']",
        GOT_IT_IN_OVERLAY = "//*[contains(@text, 'GOT IT')]",
        LIST_NAME_INPUT = "//*[@resource-id='org.wikipedia:id/text_input']",
        CREATE_LIST_OK_BUTTON = "//*[@text='OK']",
        CLOSE_ARTICLE_BUTTON = "//android.widget.ImageButton[@content-desc='Navigate up']";

    public ArticlePageObject(AppiumDriver driver) {

        super(driver);
    }
    // Проверка отображения на экране заголовка статьи
    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(
                By.xpath(TITLE),
                "Не найден заголовок страницы",
                10);
    }

    // Возвращает заголовок статьи
    public String getArticleTitle() {
        WebElement title_element = waitForTitleElement();
        return title_element.getText();
    }

    // Свайп экрана вверх до футера
    public void swipeToFooter() {
        this.swipeUpToFindElement(
                By.xpath(LINK_IN_FOOTER),
                "Не удалось найти ссылку 'View page in browser' в футере",
                20);
    }

    // Добавление статьи в новый пользовательский список статей
    public void addArticleToMyList(String folderName) {

        this.waitForElementAndClick(
                By.xpath(OPTIONS_BUTTON),
                "Не найдена кнопка 'More options'",
                5);
        this.waitForElementAndClick(
                By.xpath(OPTION_ADD_TO_READING_LIST),
                "Не найдена кнопка 'Add to reading list' в выпадающем списке",
                10);
        this.waitForElementAndClick(
                By.xpath(GOT_IT_IN_OVERLAY),
                "Не найдена кнопка 'Go it' в модалке",
                5);
        this.waitForElementAndClear(
                By.xpath(LIST_NAME_INPUT),
                "Не найдено поле ввода названия списка",
                5);
        this.waitForElementAndSendKeys(
                By.xpath(LIST_NAME_INPUT),
                folderName,
                "Не найдено поле ввода названия списка",
                5);
        this.waitForElementAndClick(
                By.xpath(CREATE_LIST_OK_BUTTON),
                "Не удалось нажатие кнопки 'OK' в окне создания списка",
                5);

    }

    // Закрытие статьи
    public void closeArticle() {
        this.waitForElementAndClick(
                By.xpath(CLOSE_ARTICLE_BUTTON),
                "Не найдена кнопка закрытия статьи - 'x'",
                5);
    }
}
