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

    @Dado("que estoy en el paso de pago con un pedido armado")
    public void estoyEnPagoConPedido() {
        prepararPago();
    }

    @Dado("que estoy en el paso de pago")
    public void estoyEnPago() {
        prepararPago();
    }

    @Cuando("completo Name on Card, Card Number, CVC y Expiration con datos válidos")
    public void completoTarjeta() {
        ScenarioContext.paymentPage().completePayment(
                "Automation User", "4111111111111111", "123", "12", "2030");
    }

    @Cuando("confirmo el pago")
    public void confirmoPago() {
        ScenarioContext.paymentPage().confirmPayment();
    }

    @Cuando("intento confirmar sin completar Card Number")
    public void confirmoSinNumero() {
        ScenarioContext.paymentPage().clearCardNumber();
        ScenarioContext.paymentPage().confirmPayment();
    }

    @Entonces("el formulario no se envía")
    public void formularioNoSeEnvia() {
        assertTrue(ScenarioContext.paymentPage().paymentFormVisible());
    }

    @Entonces("se me solicita completar el campo obligatorio")
    public void campoObligatorio() {
        assertTrue(driver.getCurrentUrl().contains("payment"));
    }

    private void prepararPago() {
        AccountSupport.ensureAccount(driver);
        new steps.CheckoutSteps().tengoSesionYProductos();
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
    }
}
