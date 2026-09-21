package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By addressDetails = By.cssSelector(".address_firstname");
    private final By orderReview = By.cssSelector("#cart_info");
    private final By comment = By.cssSelector("textarea[name='message']");
    private final By placeOrder = By.cssSelector("a[href='/payment']");
    private final By registerLogin = By.cssSelector("a[href='/login']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasAddressDetails() {
        return isVisible(addressDetails);
    }

    public boolean hasOrderReview() {
        return isVisible(orderReview) || isVisible(By.cssSelector(".cart_info"));
    }

    public void addComment(String value) {
        type(comment, value);
    }

    public void continueToPayment() {
        click(placeOrder);
    }

    public boolean offersRegisterOrLogin() {
        return isVisible(registerLogin);
    }

    public boolean deliveryAddressContains(String expected) {
        return driver.findElements(By.cssSelector(".address_delivery")).stream()
                .anyMatch(element -> element.getText().contains(expected));
    }

    public boolean billingAddressContains(String expected) {
        return driver.findElements(By.cssSelector(".address_invoice")).stream()
                .anyMatch(element -> element.getText().contains(expected));
    }
}
