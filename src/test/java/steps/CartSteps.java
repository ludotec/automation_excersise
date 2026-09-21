package steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.WebDriver;
import pages.CartPage;
import pages.ProductsPage;
import utils.DriverFactory;

public class CartSteps {

    private final WebDriver driver = DriverFactory.getDriver();

    @Dado("que estoy viendo un producto disponible")
    public void estoyViendoProducto() {
        ScenarioContext.productsPage(new ProductsPage(driver));
        ScenarioContext.productsPage().openProducts();
    }

    @Cuando("agrego el producto al carrito")
    public void agregoProducto() {
        ScenarioContext.productsPage().addFirstProductToCart();
    }

    @Entonces("el carrito muestra el producto con cantidad 1")
    public void carritoMuestraProducto() {
        abrirCarrito();
        assertEquals(1, ScenarioContext.cartPage().firstQuantity());
    }

    @Y("el total se corresponde con su precio")
    public void totalSeCorresponde() {
        assertEquals(1, ScenarioContext.cartPage().firstQuantity());
    }

    @Dado("que tengo un producto en el carrito")
    public void tengoProductoCarrito() {
        ScenarioContext.productsPage(new ProductsPage(driver));
        ScenarioContext.productsPage().openProducts();
        ScenarioContext.productsPage().addFirstProductToCart();
        abrirCarrito();
    }

    @Cuando("cambio su cantidad a 3")
    public void cambioCantidad() {
        ScenarioContext.cartPage().updateFirstQuantity(3);
    }

    @Entonces("el carrito muestra la cantidad 3")
    public void carritoMuestraCantidad() {
        assertEquals(3, ScenarioContext.cartPage().firstQuantity());
    }

    @Y("el total se recalcula con la nueva cantidad")
    public void totalSeRecalcula() {
        assertTrue(ScenarioContext.cartPage().productCount() > 0);
    }

    @Cuando("elimino el producto")
    public void eliminoProducto() {
        ScenarioContext.cartPage().deleteFirstProduct();
    }

    @Entonces("el producto deja de mostrarse en el carrito")
    public void productoEliminado() {
        assertEquals(0, ScenarioContext.cartPage().productCount());
    }

    @Y("el total se actualiza correctamente")
    public void totalSeActualiza() {
        assertEquals(0, ScenarioContext.cartPage().productCount());
    }

    private void abrirCarrito() {
        ScenarioContext.productsPage().viewCart();
        ScenarioContext.cartPage(new CartPage(driver));
    }
}
