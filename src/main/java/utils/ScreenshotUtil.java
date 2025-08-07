package utils;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class ScreenshotUtil {
    public static void takeScreenshot(WebDriver driver, String scenarioName) {
        try {
            // Chụp ảnh màn hình
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // Attach ảnh màn hình vào Allure Report
            Allure.addAttachment(scenarioName, new ByteArrayInputStream(screenshot));
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }
}

