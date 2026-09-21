package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By cartRows = By.cssSelector("tr.cart_product");
    private final By quantityButtons = By.cssSelector("#cart_info_table td.cart_quantity button");
    private final By deleteButtons = By.cssSelector("a.cart_quantity_delete");
    private final By checkoutButton = By.cssSelector("a.check_out");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void openCart() {
        driver.get(utils.DriverFactory.BASE_URL + "view_cart");
    }

    public int productCount() {
        return driver.findElements(deleteButtons).size();
    }

    public int firstQuantity() {
        return Integer.parseInt(driver.findElements(quantityButtons).get(0).getText());
    }

    public void updateFirstQuantity(int quantity) {
        int current = firstQuantity();
        if (quantity <= current) {
            return;
        }

        String productId = driver.findElements(deleteButtons).get(0).getAttribute("data-product-id");
        driver.get(utils.DriverFactory.BASE_URL + "product_details/" + productId);
        var quantityInput = wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .visibilityOfElementLocated(By.id("quantity")));
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity - current));
        click(By.cssSelector("button.cart"));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("#cartModal.show")));
        driver.get(utils.DriverFactory.BASE_URL + "view_cart");
    }

    public void deleteFirstProduct() {
        int before = productCount();
        click(deleteButtons);
        wait.until(currentDriver -> currentDriver.findElements(deleteButtons).size() < before);
    }

    public void proceedToCheckout() {
        click(checkoutButton);
    }

}
