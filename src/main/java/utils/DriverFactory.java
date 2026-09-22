package utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/** Crea y mantiene el navegador compartido entre las páginas del proyecto.
 * Usamos una sola instancia porque la sesión de cookies y de AdSense es estable
 * durante toda la suite y re-crear el navegador entre steps ralentiza mucho. */
public final class DriverFactory {

    /** URL base del sitio bajo prueba. */
    public static final String BASE_URL = "https://www.automationexercise.com/";

    private static WebDriver driver;

    private DriverFactory() {
    }

    /** Devuelve el navegador activo o crea uno nuevo con la configuración por
     * defecto (Chrome con PageLoadStrategy.EAGER). */
    public static WebDriver getDriver() {
        if (driver == null) {
            crearDriver(System.getProperty("browser", "chrome"));
        }
        return driver;
    }

    /** Crea el navegador solicitado. Por defecto usa Chrome con `EAGER` para no
     * bloquear la finalización del evento `load` por culpa de AdSense. */
    public static void crearDriver(String navegador) {
        String browser = navegador == null ? "chrome" : navegador.toLowerCase();

        switch (browser) {
            case "firefox" -> driver = new FirefoxDriver();
            case "edge" -> driver = new EdgeDriver();
            default -> {
                ChromeOptions options = new ChromeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                options.addArguments(
                        "--start-maximized",
                        "--disable-notifications",
                        "--disable-extensions",
                        "--disable-popup-blocking");
                if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                    options.addArguments("--headless=new", "--window-size=1440,900");
                }
                driver = new ChromeDriver(options);
            }
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
    }

    /** Cierra el navegador y libera la referencia. Llamado desde los hooks
     * `@After` para que cada escenario empiece con un navegador limpio. */
    public static void cerrarDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }

    /** Navega a la portada del sitio. */
    public static void navegarABase() {
        getDriver().get(BASE_URL);
    }
}
