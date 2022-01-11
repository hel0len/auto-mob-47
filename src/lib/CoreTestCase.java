package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class CoreTestCase extends TestCase {

    protected AppiumDriver driver;
    private static String AppiumURL = "http://127.0.0.1:4723/wd/hub";

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("deviceName", "and80");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/Users/lenachagina/Projects/JavaAppiumAutomation/apks" +
                "/org.wikipedia.apk");
        capabilities.setCapability("orientation", "PORTRAIT");

        driver = new AndroidDriver(new URL(AppiumURL), capabilities);
    }

    @Override
    protected void tearDown() throws Exception {

        driver.quit();

        super.tearDown();
    }

    // Поворот экрана в вертикальное положение
    protected void rotateScreenPortrait() {
        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    // Поворот экрана в горизонтальное положение
    protected void rotateScreeLandScape() {
        driver.rotate(ScreenOrientation.LANDSCAPE);
    }

    // Свернуть приложение в трей и развернуть обратно через переданное время (в сек)
    protected void backgroundApp(int seconds) {
        driver.runAppInBackground(seconds);
    }
}
