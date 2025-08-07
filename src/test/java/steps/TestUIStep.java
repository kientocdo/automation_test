package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.ElementPage;
import utils.ScreenshotUtil;  // Import ScreenshotUtil
import java.util.List;
import java.util.logging.Logger;

public class TestUIStep {
    ElementPage elementPage = new ElementPage();
    private static final Logger logger = Logger.getLogger(String.valueOf(TestUIStep.class));  // Logger for logging

    @Given("I open the webpage {string}")
    public void iOpenTheWebpage(String url) {
        try {
            logger.info("Opening webpage: " + url);
            elementPage.navigateToUrl(url);
        } catch (Exception e) {
            logger.severe("Error while opening the webpage: " + url);
            takeScreenshot("openWebpageError.png");
            Assert.fail("Failed to open webpage: " + url);
        }
    }

    @Then("Capitalize the first letter of each word in the table title, excluding prepositions")
    public void capitalizeTableTitle() {
        try {
            List<WebElement> rows = elementPage.getRows();
            for (WebElement row : rows) {
                String title = row.findElement(By.xpath(".//td[1]")).getText();
                String capitalizedTitle = elementPage.capitalizeWords(title);
                logger.info("Original Title: " + title);
                logger.info("Capitalized Title: " + capitalizedTitle);

                // Assert if the title matches the capitalized version
                if (!title.equals(capitalizedTitle)) {
                    logger.severe("Title is not capitalized correctly: " + title);
                    takeScreenshot("capitalizeTitleError.png");
                    Assert.fail("Title is not capitalized correctly for: " + title);
                }
            }
        } catch (Exception e) {
            logger.severe("Error while capitalizing table title");
            takeScreenshot("capitalizeTableTitleError.png");
            Assert.fail("Error during title capitalization");
        }
    }

    @Then("Ensure all roles have salaries ≥ $100k and exclude Manual roles")
    public void ensureAllRolesHaveSalariesGreaterThan120k() {
        try {
            List<WebElement> rows = elementPage.getRows();
            for (WebElement row : rows) {
                String role = row.findElement(By.xpath(".//td[2]")).getText();
                String salary = row.findElement(By.xpath(".//td[3]")).getText();
                logger.info("Validating salary for role: " + role + " with salary: " + salary);

                boolean isSalaryValid = elementPage.validateSalary(role, salary);
                if (!isSalaryValid) {
                    logger.severe("Salary condition failed for role: " + role + " with salary: " + salary);
                    takeScreenshot("salaryValidationError.png");
                    Assert.fail("Salary condition failed for role: " + role + " with salary: " + salary);
                }
            }
        } catch (Exception e) {
            logger.severe("Error while validating salary conditions");
            takeScreenshot("salaryValidationError.png");
            Assert.fail("Error during salary validation");
        }
    }

    @Then("I perform the Email me action with {string} and {string}")
    public void iPerformTheEmailMeActionWithNameAndEmail(String name, String email) {
        try {
            logger.info("Performing Email Me action with Name: " + name + ", Email: " + email);
            elementPage.performEmailMeAction(name, email);
        } catch (Exception e) {
            logger.severe("Error while performing Email Me action");
            takeScreenshot("emailMeActionError.png");
            Assert.fail("Failed to perform Email Me action");
        }
    }

    private void takeScreenshot(String fileName) {
        ScreenshotUtil.takeScreenshot((WebDriver) elementPage, fileName);  // Gọi phương thức từ ScreenshotUtil
    }
}