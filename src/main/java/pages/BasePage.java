package pages;

import commons.WaitCommon;
import drivers.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasePage {
    protected float delta = 0.01F;

    protected WebDriver driver;
    protected WaitCommon waitCommon;
    protected JavascriptExecutor jsExecutor;
    protected Actions actions;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();  // Initialize the driver via DriverFactory
        this.waitCommon = new WaitCommon(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    protected By timeZone(String timeZone) {
        return By.xpath("(//div[@class='time-zone']//span[contains(text(),'" + timeZone + "')])[1]");
    }

    protected By optionTimeRange(String timeRange) {
        return By.xpath("//div[contains(@class,'date-range-picker')]/span[text()='" + timeRange + "']");
    }

    protected By startMonth(String month) {
        return By.xpath("//div[contains(@class,'left')]//th[text()='" + month + "']");
    }

    protected By endMonth(String month) {
        return By.xpath("//div[contains(@class,'right')]//th[text()='" + month + "']");
    }

    protected By dateText(String startOrEnd, String date) {
        return By.xpath("(//td[text()=' " + date + " ' and @data-date])[" + startOrEnd + "]");
    }

    protected By dropDownHour(String startOrEnd) {
        return By.xpath("(//div[@class='select-wrap'])[" + startOrEnd + "]");
    }

    protected By hourOption(String startOrEnd, String hour) {
        return By.xpath("(//div[@class='select-wrap'])[" + startOrEnd + "]//ul//span[text()='" + hour + "']");
    }

    protected By btnFilterCommon(String labelOfButton) {
        return By.xpath("//button//span[contains(text(),'" + labelOfButton + "')]");
    }

    // Get menu xpath by name
    protected By getMenuXPathBrand(String targetValue) {
        return By.xpath("//li[contains(@class, 'menu-item')]//a[@data-target='" + targetValue + "']");
    }

    protected By getMenuXPathPartner(String targetValue) {
        return By.xpath("//ul[@class='menu-list']//a[@href='/" + targetValue + "']");
    }

    // Get calendar xpath by name
    protected By getValueCalenderXPath(String targetValue) {
        return By.xpath("//div[@class='ranges date-range-picker__ranges']//span[text()='" + targetValue + "']");
    }

    // Get option xpath by name
    protected By getOptionReportXPath(String targetValue) {
        return By.xpath("//ul[@class='filter-list']//li//p[@class='name' and contains(text(), '" + targetValue + "')]");
    }

    // Get option xpath by name
    protected By getSubOptionReportXPath(String targetValue, String subValue) {
        return By.xpath("//ul[@class='filter-list']//li//p[@class='name' and contains(text(), '" + targetValue + "')]/ancestor::li//ul[@class='sub-filters expanded']//li/span[contains(text(), '" + subValue + "')]");
    }

    // Get xpath of column table
    protected By getXpathColumnTable(String col) {
        return By.xpath("//span[contains(text(),'" + col + "')]");
    }

    // Get xpath ascending of column table
    protected By getXpathDecreasingColumnTable(String col) {
        return By.xpath("//span[contains(text(),'" + col + "')]/parent::span/following-sibling::div//i[@class='icon icon-expand_more-padding active']");
    }

    // Get decreasing xpath of column table
    protected By getXpathAscendingColumnTable(String col) {
        return By.xpath("//span[contains(text(),'" + col + "')]/parent::span/following-sibling::div//i[@class='icon icon-expand_less-padding active']");
    }

    public void clickValueCalenderStep(String targetValue) {
        clickValueCalender(targetValue);
    }

    // Click menu by name
    public void clickMenuPartner(String targetValue) {
        waitForElementVisible(getMenuXPathPartner(targetValue));
        click(getMenuXPathPartner(targetValue));
    }

    public void clickMenuBrand(String targetValue) {
        waitForElementVisible(getMenuXPathBrand(targetValue));
        click(getMenuXPathBrand(targetValue));
    }

    // Click menu by name
    public void clickValueCalender(String targetValue) {
        waitForElementVisible(getValueCalenderXPath(targetValue));
        click(getValueCalenderXPath(targetValue));
        waitForElementInvisible(getValueCalenderXPath(targetValue));
    }

    public WebElement waitForElementVisible(By locator) {
        return waitCommon.waitForElementVisible(locator);
    }

    public void waitForElementInvisible(By locator) {
        waitCommon.waitForElementInvisible(locator);
    }

    public WebElement waitForElementClickable(By locator) {
        return waitCommon.waitForElementClickable(locator);
    }

    // Click on element
    public void click(By locator) {
        waitForElementClickable(locator).click();
    }

    // Send keys to element
    public void enterText(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void enterAreaText(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.sendKeys(text);
    }

    public void jsEnterText(By locator, String text) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].value = arguments[1];", element, text);
    }

    public void jsPasteText(By locator, String text) {
        // Find the element using the provided locator
        WebElement element = driver.findElement(locator);

        // Create an instance of JavascriptExecutor
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].value = arguments[1];", element, text);
        // Optionally trigger the 'input' event to notify any listeners
        executor.executeScript("arguments[0].dispatchEvent(new Event('input'));", element);
        // Optionally simulate 'paste' event
        executor.executeScript("var event = new ClipboardEvent('paste', {dataType: 'text/plain', data: arguments[1]}); arguments[0].dispatchEvent(event);", element, text);
    }


    // Get text from element
    public String getText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    // Select value from dropdown by visible text
    public void selectDropdownByVisibleText(By locator, String text) {
        Select dropdown = new Select(waitForElementVisible(locator));
        dropdown.selectByVisibleText(text);
    }

    // Select value from dropdown by value
    public void selectDropdownByValue(By locator, String value) {
        Select dropdown = new Select(waitForElementVisible(locator));
        dropdown.selectByValue(value);
    }

    // Scroll to element
    public void scrollToElement(By locator) {
        WebElement element = waitForElementVisible(locator);
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Check if element is displayed
    public boolean isElementDisplayed(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if element is curent displayed
    public boolean isElementCurentDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Navigate to URL
    public void navigateToUrl(String url) {
        driver.get(url);
    }

    // Get current URL
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // Switch to frame by index
    public void switchToFrame(int index) {
        driver.switchTo().frame(index);
    }

    // Switch to frame by element
    public void switchToFrame(By locator) {
        driver.switchTo().frame(waitForElementVisible(locator));
    }

    // Switch back to default content
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // Accept alert
    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    // Dismiss alert
    public void dismissAlert() {
        driver.switchTo().alert().dismiss();
    }

    // Get alert text
    public String getAlertText() {
        return driver.switchTo().alert().getText();
    }

    // Take a screenshot (could be extended with implementation)
    public void takeScreenshot(String filePath) {
        // Implementation for screenshot can be added here
    }

    // Get list of WebElements
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    // Hover over element
    public void hoverOverElement(By locator) {
        WebElement element = waitForElementVisible(locator);
        actions.moveToElement(element).perform();
    }

    // Get attribute of element
    public String getElementAttribute(By locator, String attribute) {
        return waitForElementVisible(locator).getAttribute(attribute);
    }

    // Check if checkbox is selected
    public boolean isCheckboxSelected(By locator) {
        return waitForElementVisible(locator).isSelected();
    }

    // Set checkbox state
    public void setCheckbox(By locator, boolean checked) {
        WebElement checkbox = waitForElementVisible(locator);
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
    }

    // Execute JavaScript
    public void executeJavaScript(String script, Object... args) {
        jsExecutor.executeScript(script, args);
    }

    // Upload a file
    public void uploadFile(By locator, String filePath) {
        WebElement fileInput = waitForElementVisible(locator);
        fileInput.sendKeys(filePath);
    }

    // Get title of the page
    public String getPageTitle() {
        return driver.getTitle();
    }

    // Refresh the page
    public void refreshPage() {
        driver.navigate().refresh();
    }

    // Navigate back
    public void navigateBack() {
        driver.navigate().back();
    }

    // Navigate forward
    public void navigateForward() {
        driver.navigate().forward();
    }

    // Clear text field
    public void clearTextField(By locator) {
        waitForElementVisible(locator).clear();
    }

    // Double click on element
    public void doubleClick(By locator) {
        WebElement element = waitForElementVisible(locator);
        actions.doubleClick(element).perform();
    }

    // Drag and drop from source to target
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = waitForElementVisible(sourceLocator);
        WebElement target = waitForElementVisible(targetLocator);
        actions.dragAndDrop(source, target).perform();
    }

    // Right click on element
    public void rightClick(By locator) {
        WebElement element = waitForElementVisible(locator);
        actions.contextClick(element).perform();
    }

    // Check if element is enabled
    public boolean isElementEnabled(By locator) {
        return waitForElementVisible(locator).isEnabled();
    }

    // Check if element is selected
    public boolean isElementSelected(By locator) {
        return waitForElementVisible(locator).isSelected();
    }

    // Move to element and click
    public void moveToElementAndClick(By locator) {
        WebElement element = waitForElementVisible(locator);
        actions.moveToElement(element).click().perform();
    }

    // Highlight element by adding a border (for visual debugging)
    public void highlightElement(By locator) {
        WebElement element = waitForElementVisible(locator);
        jsExecutor.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    // Scroll to the bottom of the page
    public void scrollToBottom() {
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    // Scroll to the bottom of the Container
    public void scrollToBottomContainer(By locator) {
        WebElement targetElement = driver.findElement(locator);
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", targetElement);
    }

    // Scroll to the top of the page
    public void scrollToTop() {
        jsExecutor.executeScript("window.scrollTo(0, 0)");
    }

    // Open a new tab
    public void openNewTab() {
        jsExecutor.executeScript("window.open('about:blank', '_blank');");
    }

    // Switch to tab by index
    public void switchToTab(int index) {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(index));
    }

    public void selectEndDate(String date) {
        click(dateText("last()", date));
    }

    public void selectHourRange(String startHour, String endHour) {
        click(dropDownHour("1"));
        click(hourOption("1", startHour));
        click(dropDownHour("2"));
        click(hourOption("2", endHour));
    }

    public void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        }
    }

    // Phương thức gọi AppleScript và truyền tên file
    public void uploadFileUsingAppleScript(String filePath) {
        try {
            // Gọi AppleScript và truyền đường dẫn file
            String[] cmd = {"osascript", "src/test/java/data/fileUpload/uploadFile.scpt", filePath};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileUsingRobot(String filePath) {
        try {
            Robot robot = new Robot();
            // Give time for the file dialog to appear
            Thread.sleep(2000);

            // Copy the file path to clipboard
            StringSelection stringSelection = new StringSelection(filePath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            // Paste the file path into the dialog (Ctrl + V)
            robot.setAutoDelay(100);
            robot.setAutoWaitForIdle(true);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // Press Enter to confirm the file selection
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to use xdotool for file upload dialog on Linux
    public void uploadFileUsingXdotool(String filePath) {
        try {
            // Step 1: Make sure the script is executable
            String chmodCommand = "chmod +x src/test/java/data/fileUpload/uploadFile.sh";
            Runtime.getRuntime().exec(chmodCommand);

            // Step 2: Execute the uploadFile.sh script with the file path
            String command = "bash src/test/java/data/fileUpload/uploadFile.sh '" + filePath + "'";
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileCrossOS(String filePath) {
        // Platform-specific file upload automation
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            uploadFileUsingRobot(filePath);
        } else if (os.contains("mac")) {
            uploadFileUsingAppleScript(filePath);
        } else if (os.contains("linux")) {
            uploadFileUsingXdotool(filePath);
        } else {
            throw new UnsupportedOperationException("Operating system not supported for file upload.");
        }
    }

    public String randomEmail() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000);
        String randomEmail = "kien.nguyen+" + randomNumber + "@kitodo.com";
        return randomEmail;
    }

    public void selectRandomElementJS(By locator) {
        waitForElementVisible(locator);
        List<WebElement> elements = driver.findElements(locator);
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("List elements empty.");
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(elements.size() - 1);
        WebElement randomElement = elements.get(randomIndex);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", randomElement);
    }

    public void enterTextintoListTextbox(By locator, String numbers) {
        waitForElementVisible(locator);
        List<WebElement> elements = driver.findElements(locator);
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("List elements empty.");
        }
        for (int i = 0; i < elements.toArray().length; i++) {
            // Get the textbox based on index (1-based index for human readability)
            WebElement element = elements.get(i);
            element.clear();
            element.sendKeys(Character.toString(numbers.charAt(i)));
        }
    }

    public void selectRandomElement(By locator) {
        waitForElementVisible(locator);
        List<WebElement> elements = driver.findElements(locator);
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("List elements empty.");
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(elements.size());
        WebElement randomElement = elements.get(randomIndex);
        randomElement.click();
    }

    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,15000)", "");
    }

    public void scrollUp() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,-15000)", "");
    }

    public void clickSortAscendingColumnTable(String col) {
        click(getXpathColumnTable(col));
        if (!isElementDisplayed(getXpathAscendingColumnTable(col))) {
            click(getXpathColumnTable(col));
        }
    }

    public void clickSortDecreasingColumnTable(String col) {
        click(getXpathColumnTable(col));
        if (!isElementDisplayed(getXpathDecreasingColumnTable(col))) {
            click(getXpathColumnTable(col));
        }
    }
}
