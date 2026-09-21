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

    public void search(String product) {
        openProducts();
        type(searchInput, product);
        click(searchButton);
    }

    public boolean hasProducts() {
        return !driver.findElements(productCards).isEmpty();
    }

    public String firstProductName() {
        return driver.findElements(productNames).get(0).getText();
    }

    public void addFirstProductToCart() {
        click(addToCartButtons);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cartModal.show")));
    }

    public void addSecondProductToCart() {
        var products = driver.findElements(addToCartButtons);
        if (products.size() < 2) {
            addFirstProductToCart();
            return;
        }
        click(products.get(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cartModal.show")));
    }

    public void continueShopping() {
        if (!driver.findElements(continueShopping).isEmpty()) {
            click(continueShopping);
        }
    }

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
