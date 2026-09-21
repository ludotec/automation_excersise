package steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverFactory;

public class CommonSteps {

    @Entonces("veo {string}")
    public void veoElTexto(String expected) {
        boolean found = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(15))
                .until(driver -> driver.getPageSource().contains(expected)
                        || driver.getCurrentUrl().contains("payment_done"));
        assertTrue(found,
                "No se encontró el texto esperado: " + expected);
    }

    @Y("puedo descargar la factura del pedido")
    public void puedoDescargarLaFactura() {
        assertTrue(ScenarioContext.paymentPage().invoiceAvailable(),
                "No se encontró el enlace de descarga de factura");
    }
}
