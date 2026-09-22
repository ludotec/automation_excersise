package steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.openqa.selenium.WebDriver;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.PaymentPage;
import utils.DriverFactory;

public class PaymentSteps {

    private final WebDriver driver = DriverFactory.getDriver();

    /** Prepara el escenario con sesión iniciada, productos en el carrito y página
     * de pago lista. La etiqueta "con pedido armado" se usa para escenarios que
     * requieren el carrito poblado. */
    @Dado("que estoy en el paso de pago con un pedido armado")
    public void estoyEnPagoConPedido() {
        prepararPago();
    }

    /** Igual al anterior pero se usa cuando el step no asume estado del carrito. */
    @Dado("que estoy en el paso de pago")
    public void estoyEnPago() {
        prepararPago();
    }

    /** Completa el formulario de pago con una tarjeta de prueba. */
    @Cuando("completo Name on Card, Card Number, CVC y Expiration con datos válidos")
    public void completoTarjeta() {
        ScenarioContext.paymentPage().completePayment(
                "Automation User", "4111111111111111", "123", "12", "2030");
    }

    /** Click en "Pay and Confirm Order" para finalizar la compra. */
    @Cuando("confirmo el pago")
    public void confirmoPago() {
        ScenarioContext.paymentPage().confirmPayment();
    }

    /** Borra el campo Card Number antes de confirmar, para validar que el sitio
     * bloquee el envío con datos incompletos. */
    @Cuando("intento confirmar sin completar Card Number")
    public void confirmoSinNumero() {
        ScenarioContext.paymentPage().clearCardNumber();
        ScenarioContext.paymentPage().confirmPayment();
    }

    /** Verifica que el formulario de pago sigue visible tras un intento fallido
     * (el sitio no permite enviar con campos requeridos vacíos). */
    @Entonces("el formulario no se envía")
    public void formularioNoSeEnvia() {
        assertTrue(ScenarioContext.paymentPage().paymentFormVisible());
    }

    /** Verifica que la URL permanece en /payment tras el intento fallido. */
    @Entonces("se me solicita completar el campo obligatorio")
    public void campoObligatorio() {
        assertTrue(driver.getCurrentUrl().contains("payment"));
    }

    /** Prepara el escenario con sesión, carrito y checkout listos para el pago. */
    private void prepararPago() {
        AccountSupport.ensureAccount(driver);
        new steps.CheckoutSteps().tengoSesionYProductos();
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
    }
}
