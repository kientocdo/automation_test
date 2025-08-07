package hook;

import commons.ReadEnvCommon;
import constants.Constant;
import drivers.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import utils.ExecuteCommand;
import utils.ScreenshotUtil;

import java.util.Collection;

public class Hooks {

    private WebDriver driver;

    @Before()
    public void setUp(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();
        String env = System.getProperty("EVN", "QA");

        switch (env) {
            case "STG" -> ReadEnvCommon.writeConfig("Evn.properties", "EVN", Constant.FOLDER_DATA_STG);
            case "QA" -> ReadEnvCommon.writeConfig("Evn.properties", "EVN", Constant.FOLDER_DATA_QA);
        }

        if (tags.contains("@sshRedshift")) {
            ExecuteCommand.sshToRedshift();
        } else if (tags.contains("@sshRDS")) {
            ExecuteCommand.sshToRDS();
        }

        if (tags.contains("@headless")) {
            DriverFactory.setHeadless(true);
        }
        driver = DriverFactory.getDriver();
        System.out.println("Driver initialized for: " + scenario.getName());
    }

    @After()
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            ScreenshotUtil.takeScreenshot(driver, scenario.getName());
        }
    }
}
