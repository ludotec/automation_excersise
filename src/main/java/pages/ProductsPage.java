package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductsPage extends BasePage {

    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By productCards = By.cssSelector(".productinfo");
    private final By productNames = By.cssSelector(".productinfo p");
    private final By addToCartButtons = By.cssSelector(".productinfo a.add-to-cart");
    private final By continueShopping = By.cssSelector("button.close-modal");
    private final By viewCart = By.cssSelector("a[href='/view_cart']");
    private final By modalViewCart = By.cssSelector("div.modal-content a[href='/view_cart']");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public void openProducts() {
        driver.get(utils.DriverFactory.BASE_URL + "products");
    }

    /** Busca un producto por nombre en la página de Products. */
    public void search(String product) {
        openProducts();
        type(searchInput, product);
        click(searchButton);
    }

    /** Indica si hay productos renderizados en la página actual. */
    public boolean hasProducts() {
        return !driver.findElements(productCards).isEmpty();
    }

    /** Devuelve el nombre del primer producto listado. */
    public String firstProductName() {
        return driver.findElements(productNames).get(0).getText();
    }

    /** Agrega el primer producto de la lista al carrito y espera el modal de
     * confirmación. */
    public void addFirstProductToCart() {
        click(addToCartButtons);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cartModal.show")));
    }

    /** Agrega el segundo producto de la lista. Si no hay un segundo disponible,
     * agrega el primero para mantener la continuidad del flujo. */
    public void addSecondProductToCart() {
        var products = driver.findElements(addToCartButtons);
        if (products.size() < 2) {
            addFirstProductToCart();
            return;
        }
        click(products.get(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cartModal.show")));
    }

    /** Cierra el modal de producto agregado si está presente. Si no hay modal,
     * no hace nada. */
    public void continueShopping() {
        if (!driver.findElements(continueShopping).isEmpty()) {
            click(continueShopping);
        }
    }

    /** Navega al carrito. Prioriza el botón del modal de "Added!" porque aparece
     * tras agregar un producto. Si el modal no está, intenta el enlace del header;
     * si tampoco está, navega directamente a la URL. */
    public void viewCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(modalViewCart));
            click(modalViewCart);
        } catch (TimeoutException exception) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(viewCart));
                click(viewCart);
            } catch (TimeoutException ignored) {
                driver.get(utils.DriverFactory.BASE_URL + "view_cart");
            }
        }
    }
}
