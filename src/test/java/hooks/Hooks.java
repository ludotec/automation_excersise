package hooks;

import java.io.ByteArrayInputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import utils.DriverFactory;

/** Preparación y limpieza común para futuros escenarios Cucumber. */
public class Hooks {

    private WebDriver driver;

    @Before
    public void setUp() {
        driver = DriverFactory.getDriver();
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
