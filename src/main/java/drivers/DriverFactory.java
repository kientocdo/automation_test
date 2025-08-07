package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import utils.BrowserManager;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> isHeadless = ThreadLocal.withInitial(() -> false);

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = BrowserManager.getBrowser();
            WebDriver webDriver;

            switch (browser.toLowerCase()) {
                case "firefox":
                    System.out.println("Initializing Firefox driver");
                    webDriver = new FirefoxDriver(getFirefoxOptions());
                    break;

                case "chrome":
                    System.out.println("Initializing Chrome driver");
                    webDriver = new ChromeDriver(getChromeOptions(null));
                    break;

                case "safari":
                    System.out.println("Initializing Safari driver");
                    webDriver = new SafariDriver();
                    break;

                case "edge":
                    System.out.println("Initializing Edge driver");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--headless", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.6367.91 Safari/537.36 Edg/124.0.2478.80");
                    webDriver = new EdgeDriver(edgeOptions);
                    break;

                case "brave":
                    System.out.println("Initializing Brave driver");
                    String bravePath = getBravePath();
                    webDriver = new ChromeDriver(getChromeOptions(bravePath)); // Brave dùng ChromeDriver với binary path
                    break;


                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            webDriver.manage().window().maximize();
            driver.set(webDriver);
        }
        return driver.get();
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
                Thread.sleep(500); // tránh lỗi shutdown quá sớm
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driver.remove();
                isHeadless.remove();
            }
        }
    }

    public static void setHeadless(boolean headless) {
        isHeadless.set(headless);
    }

    private static ChromeOptions getChromeOptions(String bravePath) {
        ChromeOptions options = new ChromeOptions();

        if (bravePath != null && !bravePath.isEmpty()) {
            options.setBinary(bravePath); // Chỉ định binary path cho Brave nếu có
        }

        options.addArguments("--disable-features=BraveShield", "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.6367.91 Safari/537.36");

        if (isHeadless.get()) {
            options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080", "--disable-notifications");
        } else {
            options.addArguments(
                    "--disable-notifications");
        }

        return options;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("general.useragent.override", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7; rv:124.0) Gecko/20100101 Firefox/124.0");

        if (isHeadless.get()) {
            options.addArguments("-headless");
            options.addPreference("dom.webnotifications.enabled", false);
            options.addPreference("dom.push.enabled", false);
        } else {
            options.addPreference("dom.webnotifications.enabled", false);
            options.addPreference("dom.push.enabled", false);
        }

        return options;
    }

    private static String getBravePath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return "/Applications/Brave Browser.app/Contents/MacOS/Brave Browser";
        } else if (os.contains("win")) {
            return "C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe";
        } else {
            return "/usr/bin/brave-browser";
        }
    }

}
