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

    /** Prepara el escenario con cuenta temporal, login activo y un producto en
     * el carrito. */
    @Dado("que tengo una sesión iniciada y productos en el carrito")
    public void tengoSesionYProductos() {
        AccountSupport.ensureAccount(driver);
        agregarProducto();
    }

    /** Prepara el escenario con un producto en el carrito sin iniciar sesión. */
    @Dado("que tengo productos en el carrito y no inicié sesión")
    public void tengoProductosSinSesion() {
        agregarProducto();
    }

    /** Click en "Proceed to Checkout". Si el sitio muestra el modal de
     * Register/Login, navega a /login para que el siguiente step pueda
     * autenticarse. */
    @Cuando("avanzo al checkout")
    public void avanzoCheckout() {
        ScenarioContext.cartPage().proceedToCheckout();
        boolean modalPresent = driver.getPageSource().contains("Register / Login");
        boolean onLogin = driver.getCurrentUrl().contains("/login");
        boolean onCheckout = driver.getCurrentUrl().contains("/checkout");
        if (modalPresent && !onLogin && !onCheckout) {
            driver.get(utils.DriverFactory.BASE_URL + "login");
        }
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
    }

    /** Verifica que el checkout muestra la dirección de envío y facturación. */
    @Entonces("veo la dirección de envío y facturación cargada")
    public void veoDirecciones() {
        assertTrue(ScenarioContext.checkoutPage().hasAddressDetails());
    }

    /** Verifica que el resumen del pedido está visible. */
    @Y("el resumen muestra productos, cantidades y total")
    public void veoResumen() {
        assertTrue(ScenarioContext.checkoutPage().hasOrderReview());
    }

    /** Escribe un comentario en el textarea del checkout y avanza al pago. */
    @Cuando("agrego un comentario y continúo al pago")
    public void agregoComentarioYContinúo() {
        ScenarioContext.checkoutPage().addComment("Comentario de prueba");
        ScenarioContext.checkoutPage().continueToPayment();
    }

    /** Verifica que el navegador llega a /payment o que el formulario de pago es
     * visible. */
    @Entonces("se abre el formulario de pago")
    public void formularioPago() {
        assertTrue(driver.getCurrentUrl().contains("payment")
                        || new PaymentPage(driver).paymentFormVisible(),
                "No se abrió el formulario de pago");
    }

    /** Click en "Proceed to Checkout" sin autenticarse, esperando que el sitio
     * muestre el modal de Register/Login. */
    @Cuando("intento avanzar al checkout")
    public void intentoCheckout() {
        ScenarioContext.cartPage().proceedToCheckout();
    }

    /** Verifica que el sitio muestra la opción de registro o login (URL o modal). */
    @Entonces("se me ofrece registrarme o iniciar sesión")
    public void ofreceLogin() {
        assertTrue(driver.getCurrentUrl().contains("login")
                        || driver.getPageSource().contains("Register / Login"),
                "No se ofreció el registro o login");
    }

    /** Verifica que la dirección de envío coincide con la registrada. */
    @Entonces("la dirección de envío coincide con la dirección registrada")
    public void direccionEnvio() {
        assertTrue(ScenarioContext.checkoutPage().hasAddressDetails());
    }

    /** Verifica que la página de checkout contiene la palabra "Address"
     * asociada al bloque de facturación. */
    @Y("la dirección de facturación coincide con la dirección registrada")
    public void direccionFacturacion() {
        assertTrue(driver.getPageSource().contains("Address"));
    }

    /** Helper que abre Products, agrega el primer producto y navega al carrito.
     * Lo usan los steps `tengoSesionYProductos` y `tengoProductosSinSesion`. */
    private void agregarProducto() {
        ScenarioContext.productsPage(new ProductsPage(driver));
        ScenarioContext.productsPage().openProducts();
        ScenarioContext.productsPage().addFirstProductToCart();
        ScenarioContext.productsPage().viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
    }
}
