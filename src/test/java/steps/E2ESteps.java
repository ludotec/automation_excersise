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

    @Dado("que soy un visitante nuevo en el home")
    public void visitanteNuevo() {
        DriverFactory.navegarABase();
    }

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

    @Y("inicio sesión con la cuenta creada")
    public void loginCuentaCreada() {
        if (!new LoginPage(driver).isLoggedIn()) {
            new LoginPage(driver).login(ScenarioContext.createdEmail(), ScenarioContext.createdPassword());
        }
    }

    @Y("busco un producto y lo agrego al carrito")
    public void buscoProductoYAgrego() {
        ProductsPage products = new ProductsPage(driver);
        products.search("Blue Top");
        products.addFirstProductToCart();
        products.continueShopping();
        ScenarioContext.productsPage(products);
    }

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

    @Dado("que tengo una cuenta registrada y no inicié sesión")
    public void cuentaSinSesion() {
        AccountSupport.ensureAccount(driver);
        AccountSupport.ensureLoggedOut(driver);
        new LoginPage(driver).openLogin();
    }

    @Cuando("inicio sesión con mis credenciales válidas")
    public void loginCredencialesValidas() {
        AccountSupport.ensureAccount(driver);
    }

    @Y("agrego otro producto al carrito")
    public void agregoOtroProducto() {
        ProductsPage products = new ProductsPage(driver);
        products.openProducts();
        products.addSecondProductToCart();
        products.continueShopping();
    }

    @Y("actualizo la cantidad del producto")
    public void actualizoCantidad() {
        new ProductsPage(driver).viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().updateFirstQuantity(2);
    }

    @Y("elimino un producto del carrito")
    public void eliminoUnProducto() {
        ScenarioContext.cartPage().deleteFirstProduct();
    }

    @Y("avanzo al checkout y completo el pago")
    public void checkoutYPago() {
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
        pagar();
    }

    @Dado("que tengo un producto en el carrito y no inicié sesión")
    public void productoSinSesion() {
        ProductsPage products = new ProductsPage(driver);
        products.openProducts();
        products.addFirstProductToCart();
        products.continueShopping();
        products.viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
    }

    @Y("selecciono la opción Register \\/ Login")
    public void seleccionoRegisterLogin() {
        new LoginPage(driver).startSignup("QA E2E", "qa" + Instant.now().toEpochMilli() + "@correo.com");
    }

    @Y("completo el registro con datos válidos")
    public void completoRegistro() {
        String email = "qa" + Instant.now().toEpochMilli() + "@correo.com";
        String password = "Password123!";
        // El formulario inicial ya debe haber sido presentado por el paso anterior.
        new SignupPage(driver).completeAccount("QA E2E", password);
        new SignupPage(driver).continueToHome();
        ScenarioContext.createdEmail(email);
        ScenarioContext.createdPassword(password);
    }

    @Y("vuelvo al checkout y completo el pago")
    public void vuelvoCheckoutPago() {
        new ProductsPage(driver).viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
        pagar();
    }

    @Dado("que estoy en la página de Products y no inicié sesión")
    public void productsSinSesion() {
        ScenarioContext.productsPage(new ProductsPage(driver));
        ScenarioContext.productsPage().openProducts();
    }

    @Cuando("busco un producto por su nombre")
    public void buscoProducto() {
        ScenarioContext.productsPage().search("Blue Top");
    }

    @Y("agrego un resultado al carrito")
    public void agregoResultado() {
        ScenarioContext.productsPage().addFirstProductToCart();
        ScenarioContext.productsPage().continueShopping();
        ScenarioContext.productsPage().viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
    }

    @Y("completo el pago")
    public void completoElPago() {
        new ProductsPage(driver).viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
        ScenarioContext.cartPage().proceedToCheckout();
        ScenarioContext.checkoutPage(new CheckoutPage(driver));
        ScenarioContext.checkoutPage().continueToPayment();
        ScenarioContext.paymentPage(new PaymentPage(driver));
        pagar();
    }

    @Y("agrego un producto al carrito")
    public void agregoUnProductoAlCarrito() {
        ProductsPage products = new ProductsPage(driver);
        products.openProducts();
        products.addFirstProductToCart();
        products.continueShopping();
    }

    private void pagar() {
        ScenarioContext.paymentPage().completePayment(
                "Automation User", "4111111111111111", "123", "12", "2030");
        ScenarioContext.paymentPage().confirmPayment();
    }
}
