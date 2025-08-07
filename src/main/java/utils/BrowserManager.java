package utils;

public class BrowserManager {
    private static final ThreadLocal<String> browser = new ThreadLocal<>();

    public static void setBrowser(String b) {
        browser.set(b);
    }

    public static String getBrowser() {
        // fallback nếu chưa được set
        String b = browser.get();
        return (b != null) ? b : "firefox"; // 👈 default nếu không chạy từ ParallelRunnerLauncher
    }
}
