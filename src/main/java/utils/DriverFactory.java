package utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/** Administra el ciclo de vida del navegador compartido por las páginas. */
public final class DriverFactory {

    public static final String BASE_URL = "https://www.automationexercise.com/";

    private static WebDriver driver;

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            crearDriver(System.getProperty("browser", "chrome"));
        }
        return driver;
    }

    public static void crearDriver(String navegador) {
        String browser = navegador == null ? "chrome" : navegador.toLowerCase();

        switch (browser) {
            case "firefox" -> driver = new FirefoxDriver();
            case "edge" -> driver = new EdgeDriver();
            default -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized", "--disable-notifications");
                if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                    options.addArguments("--headless=new", "--window-size=1440,900");
                }
                driver = new ChromeDriver(options);
            }
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
    }

    public static void cerrarDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }

    public static void navegarABase() {
        getDriver().get(BASE_URL);
    }
}
