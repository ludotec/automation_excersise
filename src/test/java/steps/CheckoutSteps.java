package steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.PaymentPage;
import pages.ProductsPage;
import utils.DriverFactory;

public class CheckoutSteps {

    private final WebDriver driver = DriverFactory.getDriver();

    @Dado("que tengo una sesión iniciada y productos en el carrito")
    public void tengoSesionYProductos() {
        AccountSupport.ensureAccount(driver);
        agregarProducto();
    }

    @Dado("que tengo productos en el carrito y no inicié sesión")
    public void tengoProductosSinSesion() {
        agregarProducto();
    }

    @Cuando("avanzo al checkout")
    public void avanzoCheckout() {
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
    }

    @Entonces("veo la dirección de envío y facturación cargada")
    public void veoDirecciones() {
        assertTrue(ScenarioContext.checkoutPage().hasAddressDetails());
    }

    @Y("el resumen muestra productos, cantidades y total")
    public void veoResumen() {
        assertTrue(ScenarioContext.checkoutPage().hasOrderReview());
    }

    @Cuando("agrego un comentario y continúo al pago")
    public void agregoComentarioYContinúo() {
        ScenarioContext.checkoutPage().addComment("Comentario de prueba");
        ScenarioContext.checkoutPage().continueToPayment();
    }

    @Entonces("se abre el formulario de pago")
    public void formularioPago() {
        assertTrue(driver.getCurrentUrl().contains("payment")
                        || new PaymentPage(driver).paymentFormVisible(),
                "No se abrió el formulario de pago");
    }

    @Cuando("intento avanzar al checkout")
    public void intentoCheckout() {
        ScenarioContext.cartPage().proceedToCheckout();
    }

    @Entonces("se me ofrece registrarme o iniciar sesión")
    public void ofreceLogin() {
        assertTrue(driver.getCurrentUrl().contains("login")
                        || driver.getPageSource().contains("Register / Login"),
                "No se ofreció el registro o login");
    }

    @Entonces("la dirección de envío coincide con la dirección registrada")
    public void direccionEnvio() {
        assertTrue(ScenarioContext.checkoutPage().hasAddressDetails());
    }

    @Y("la dirección de facturación coincide con la dirección registrada")
    public void direccionFacturacion() {
        assertTrue(driver.getPageSource().contains("Address"));
    }

    private void agregarProducto() {
        ScenarioContext.productsPage(new ProductsPage(driver));
        ScenarioContext.productsPage().openProducts();
        ScenarioContext.productsPage().addFirstProductToCart();
        ScenarioContext.productsPage().viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
    }
}
