package kobiton.com.testng;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidAppTest {
    AndroidDriver<WebElement> driver;
    String KOBITON_USERNAME = System.getenv("KOBITON_USERNAME");
    String KOBITON_API_KEY = System.getenv("KOBITON_API_KEY");

    URL kobitonServerUrl() {
        try {
            return new URL("https://" + KOBITON_USERNAME + ":" + KOBITON_API_KEY + "@api.kobiton.com/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    DesiredCapabilities desiredCapabilitiesAndroidApp() {
        String deviceName = System.getenv("KOBITON_DEVICE_NAME") != null ? System.getenv("KOBITON_DEVICE_NAME") : "Galaxy*";
        String platformVersion = System.getenv("KOBITON_SESSION_PLATFORM_VERSION") != null ? System.getenv("KOBITON_SESSION_PLATFORM_VERSION") : "*";
        String platformName = System.getenv("KOBITON_DEVICE_PLATFORM_NAME") != null ? System.getenv("KOBITON_DEVICE_PLATFORM_NAME") : "Android";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("sessionName", "Azure Devops Kobiton Integration");
        capabilities.setCapability("app", "https://appium.github.io/appium/assets/ApiDemos-debug.apk");
        capabilities.setCapability("deviceGroup", "KOBITON");
        capabilities.setCapability("appPackage", "io.appium.android.apis");
        capabilities.setCapability("appActivity", ".ApiDemos");
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("platformName", platformName);
        return capabilities;
    }

    @BeforeTest
    public void Setup() {
        driver = new AndroidDriver<WebElement>(kobitonServerUrl(), desiredCapabilitiesAndroidApp());
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        String kobitonSessionId = driver.getSessionDetails().get("kobitonSessionId").toString();
        System.out.println("https://portal.kobiton.com/sessions/" + kobitonSessionId);
    }

    @AfterTest
    public void Teardown() {
        try {
            if (driver != null)
                driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        WebElement element = driver.findElement(By.className("android.widget.TextView"));
        String text = element.getText();
        Assert.assertEquals(text.toLowerCase(), "api demos");
    }
}
