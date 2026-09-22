package hooks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import steps.ScenarioContext;
import utils.DriverFactory;

/** Preparación y limpieza común para futuros escenarios Cucumber. */
public class Hooks {

    private static final Path ALLURE_RESULTS = Path.of("target", "allure-results");

    private WebDriver driver;

    @BeforeAll
    public static void prepareAllureMetadata() {
        try {
            Files.createDirectories(ALLURE_RESULTS);
            copyClasspathResource("allure/environment.properties", ALLURE_RESULTS.resolve("environment.properties"));
            copyClasspathResource("categories.json", ALLURE_RESULTS.resolve("categories.json"));
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudieron preparar los metadatos de Allure.", exception);
        }
    }

    private static void copyClasspathResource(String classpathLocation, Path destination) throws IOException {
        try (InputStream input = Hooks.class.getClassLoader().getResourceAsStream(classpathLocation)) {
            if (input == null) {
                throw new IOException("No se encontró el recurso " + classpathLocation);
            }
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Before
    public void setUp() {
        ScenarioContext.clear();
        driver = DriverFactory.getDriver();
        try {
            driver.manage().deleteAllCookies();
        } catch (RuntimeException ignored) {
            // El navegador todavía no está navegando.
        }
        DriverFactory.navegarABase();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(
                        "Evidencia de fallo - " + scenario.getName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png");
            } catch (Exception ignored) {
                // La limpieza del navegador no debe depender de la captura.
            }
        }
        DriverFactory.cerrarDriver();
    }
}
