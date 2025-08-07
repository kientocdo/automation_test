package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ElementPage extends BasePage {

    // Locators for Table and Columns
    private By tableNoId = By.xpath("//h2[contains(text(),'HTML Table with no id')]/following::table[1]//tr[position()>1]");  // Skip header row
    private By titleColumn = By.xpath(".//td[1]");  // Column 1: Title
    private By roleColumn = By.xpath(".//td[2]");   // Column 2: Role
    private By salaryColumn = By.xpath(".//td[3]"); // Column 3: Salary

    // Locator for Email Fields
    private By txtName = By.xpath("//input[@data-original_id='name']");
    private By txtEmail = By.xpath("//input[@data-original_id='email']");
    private By btnEmailMe = By.xpath("//button[@name='et_builder_submit_button']");

    // Method to get all rows of the table (skipping header row)
    public List<WebElement> getRows() {
        return driver.findElements(tableNoId);
    }

    // Method to capitalize the first letter of each word in the title, excluding prepositions
    public String capitalizeWords(String input) {
        String[] words = input.split(" ");
        StringBuilder capitalizedSentence = new StringBuilder();

        for (String word : words) {
            if (!isPreposition(word)) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);  // Capitalize the first letter
            }
            capitalizedSentence.append(word).append(" ");
        }
        return capitalizedSentence.toString().trim();
    }

    // Helper method to check if the word is a preposition
    private boolean isPreposition(String word) {
        String[] prepositions = {"in", "on", "at", "by", "for", "to", "with", "about"};
        for (String prep : prepositions) {
            if (word.equalsIgnoreCase(prep)) {
                return true;
            }
        }
        return false;
    }

    // Method to validate salary based on role
    public boolean validateSalary(String role, String salaryText) {
        int salary = Integer.parseInt(salaryText.replaceAll("[^0-9]", ""));  // Parse salary and remove non-numeric characters
        if ("Manual".equalsIgnoreCase(role)) {
            return true;  // Skip validation for Manual role
        } else {
            return salary >= 100000;  // Automation roles should have salary >= $100k
        }
    }

    // Method to perform the "Email me!" action
    public void performEmailMeAction(String name, String email) {
        enterText(txtName, name);
        enterText(txtEmail, email);
        click(btnEmailMe);
    }
}
