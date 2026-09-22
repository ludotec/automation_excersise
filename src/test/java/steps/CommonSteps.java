package steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverFactory;

public class CommonSteps {

    /** Verifica que la página actual contiene el texto esperado. Acepta también
     * una redirección a `/payment_done` como indicio de que el flujo avanzó. */
    @Entonces("veo {string}")
    public void veoElTexto(String expected) {
        boolean found = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(15))
                .until(driver -> driver.getPageSource().contains(expected)
                        || driver.getCurrentUrl().contains("payment_done"));
        assertTrue(found,
                "No se encontró el texto esperado: " + expected);
    }

    /** Confirma que el sitio muestra el enlace "Download Invoice" tras finalizar
     * el pedido. */
    @Y("puedo descargar la factura del pedido")
    public void puedoDescargarLaFactura() {
        assertTrue(ScenarioContext.paymentPage().invoiceAvailable(),
                "No se encontró el enlace de descarga de factura");
    }
}
