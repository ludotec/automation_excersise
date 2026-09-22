package steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.PaymentPage;
import pages.ProductsPage;
import pages.SignupPage;
import utils.DriverFactory;

public class E2ESteps {

    private final WebDriver driver = DriverFactory.getDriver();

    /** Prepara la portada para escenarios que empiezan sin navegación previa. */
    @Dado("que soy un visitante nuevo en el home")
    public void visitanteNuevo() {
        DriverFactory.navegarABase();
    }

    /** Registra un usuario nuevo vía el formulario del sitio. Mantiene email y
     * password en `ScenarioContext` para reuso en steps posteriores. */
    @Cuando("me registro como usuario")
    public void registroUsuario() {
        String email = "qa" + Instant.now().toEpochMilli() + "@correo.com";
        String password = "Password123!";
        LoginPage login = new LoginPage(driver);
        login.startSignup("QA Automation", email);
        SignupPage signup = new SignupPage(driver);
        signup.completeAccount("QA Automation", password);
        assertTrue(signup.isAccountCreated());
        signup.continueToHome();
        ScenarioContext.createdEmail(email);
        ScenarioContext.createdPassword(password);
    }

    /** Inicia sesión con la cuenta recién registrada si todavía no hay sesión. */
    @Y("inicio sesión con la cuenta creada")
    public void loginCuentaCreada() {
        if (!new LoginPage(driver).isLoggedIn()) {
            new LoginPage(driver).login(ScenarioContext.createdEmail(), ScenarioContext.createdPassword());
        }
    }

    /** Busca un producto específico y lo agrega al carrito. */
    @Y("busco un producto y lo agrego al carrito")
    public void buscoProductoYAgrego() {
        ProductsPage products = new ProductsPage(driver);
        products.search("Blue Top");
        products.addFirstProductToCart();
        products.continueShopping();
        ScenarioContext.productsPage(products);
    }

    /** Confirma el pedido en el checkout y deja la página de pago lista. */
    @Y("avanzo al checkout confirmando dirección y pedido")
    public void avanzoConfirmandoPedido() {
        ScenarioContext.productsPage().viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().addComment("Compra E2E");
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
    }

    /** Prepara una cuenta temporal por API y abre la página de login. */
    @Dado("que tengo una cuenta registrada y no inicié sesión")
    public void cuentaSinSesion() {
        AccountSupport.ensureAccount(driver);
        AccountSupport.ensureLoggedOut(driver);
        new LoginPage(driver).openLogin();
    }

    /** Garantiza que la cuenta temporal exista y queda lista para usarse. */
    @Y("inicio sesión con mis credenciales válidas para pagar")
    public void loginCredencialesValidasGenerico() {
        AccountSupport.ensureAccount(driver);
    }

    /** Agrega un segundo producto distinto al carrito. */
    @Y("agrego otro producto al carrito")
    public void agregoOtroProducto() {
        ProductsPage products = new ProductsPage(driver);
        products.openProducts();
        products.addSecondProductToCart();
        products.continueShopping();
    }

    /** Suma una unidad más al primer producto del carrito (queda en 2). */
    @Y("actualizo la cantidad del producto")
    public void actualizoCantidad() {
        new ProductsPage(driver).viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().updateFirstQuantity(2);
    }

    /** Quita el primer producto del carrito. */
    @Y("elimino un producto del carrito")
    public void eliminoUnProducto() {
        ScenarioContext.cartPage().deleteFirstProduct();
    }

    /** Flujo completo desde el carrito hasta el pago. Requiere sesión iniciada y al
     * menos un producto en el carrito. */
    @Y("avanzo al checkout y completo el pago")
    public void checkoutYPago() {
        AccountSupport.ensureAccount(driver);
        driver.get(utils.DriverFactory.BASE_URL + "view_cart");
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
        pagar();
    }

    /** Prepara un producto en el carrito sin iniciar sesión (FL03 sin login
     * después del registro). */
    @Dado("que tengo un producto en el carrito y no inicié sesión")
    public void productoSinSesion() {
        ProductsPage products = new ProductsPage(driver);
        products.openProducts();
        products.addFirstProductToCart();
        products.continueShopping();
        products.viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
    }

    /** Abre el formulario de signup en la página de login. */
    @Y("selecciono la opción Register \\/ Login")
    public void seleccionoRegisterLogin() {
        new LoginPage(driver).startSignup("QA E2E",
                "qa" + Instant.now().toEpochMilli() + "@correo.com");
    }

    /** Completa el formulario de registro. Usado para FL03. */
    @Y("completo el registro con datos válidos")
    public void completoRegistro() {
        ScenarioContext.createdEmail("qa" + Instant.now().toEpochMilli() + "@correo.com");
        ScenarioContext.createdPassword("Password123!");
        new SignupPage(driver).completeAccount("QA E2E", ScenarioContext.createdPassword());
        new SignupPage(driver).continueToHome();
    }

    /** Completa el flujo de FL03: abre login, autentica con la cuenta recién
     * creada, navega al carrito, hace checkout y paga. */
    @Y("vuelvo al checkout y completo el pago")
    public void vuelvoCheckoutPago() {
        new LoginPage(driver).openLogin();
        new LoginPage(driver).loginFromCurrentPage(
                ScenarioContext.createdEmail(), ScenarioContext.createdPassword());
        driver.get(utils.DriverFactory.BASE_URL + "view_cart");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(20))
                .until(d -> {
                    int checkouts = d.findElements(org.openqa.selenium.By.cssSelector("a.check_out")).size();
                    int empty = d.findElements(org.openqa.selenium.By.id("empty_cart")).size();
                    return checkouts > 0 || empty > 0;
                });
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
        pagar();
    }

    /** Para FL04: abre la página de Products si no hay ventana activa. */
    @Dado("que estoy en la página de Products y no inicié sesión")
    public void productsSinSesion() {
        if (driver.getWindowHandles().isEmpty()) {
            DriverFactory.navegarABase();
        }
        ScenarioContext.productsPage(new ProductsPage(driver));
        ScenarioContext.productsPage().openProducts();
    }

    /** Realiza una búsqueda por nombre en Products. */
    @Cuando("busco un producto por su nombre")
    public void buscoProducto() {
        ScenarioContext.productsPage().search("Blue Top");
    }

    /** Agrega el primer resultado de búsqueda al carrito. */
    @Y("agrego un resultado de búsqueda al carrito")
    public void agregoResultado() {
        ScenarioContext.productsPage().addFirstProductToCart();
        ScenarioContext.productsPage().continueShopping();
        ScenarioContext.productsPage().viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
    }

    /** Completa el pago del pedido activo. La página /cart debe tener al menos un
     * producto antes de invocar este step. */
    @Y("completo el pago")
    public void completoElPago() {
        driver.get(utils.DriverFactory.BASE_URL + "view_cart");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(20))
                .until(d -> d.findElements(org.openqa.selenium.By.cssSelector("a.check_out")).size() > 0
                        || d.findElements(org.openqa.selenium.By.id("empty_cart")).size() > 0);
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(d -> d.getCurrentUrl().contains("/payment")
                        || !d.findElements(org.openqa.selenium.By.cssSelector("input[data-qa='name-on-card']")).isEmpty());
        ScenarioContext.paymentPage(new PaymentPage(driver));
        pagar();
    }

    /** Versión simplificada de completar el pago cuando ya estamos en /payment. */
    @Y("pago con datos de tarjeta válidos")
    public void pagoConDatosValidos() {
        pagar();
    }

    /** Agrega un producto cualquiera al carrito desde Products. */
    @Y("agrego un producto al carrito")
    public void agregoUnProductoAlCarrito() {
        ProductsPage products = new ProductsPage(driver);
        products.openProducts();
        products.addFirstProductToCart();
        products.continueShopping();
    }

    /** Completa el formulario de pago y confirma el pedido. */
    private void pagar() {
        ScenarioContext.paymentPage().completePayment(
                "Automation User", "4111111111111111", "123", "12", "2030");
        ScenarioContext.paymentPage().confirmPayment();
    }
}
