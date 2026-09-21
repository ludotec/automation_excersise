package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private final By nameOnCard = By.cssSelector("input[data-qa='name-on-card']");
    private final By cardNumber = By.cssSelector("input[data-qa='card-number']");
    private final By cvc = By.cssSelector("input[data-qa='cvc']");
    private final By expirationMonth = By.cssSelector("input[data-qa='expiry-month']");
    private final By expirationYear = By.cssSelector("input[data-qa='expiry-year']");
    private final By payButton = By.cssSelector("button[data-qa='pay-button']");
    private final By invoiceLink = By.xpath("//a[contains(normalize-space(), 'Download Invoice')]");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public void completePayment(String name, String number, String securityCode, String expiryMonthValue,
                                String expiryYearValue) {
        type(nameOnCard, name);
        type(cardNumber, number);
        type(cvc, securityCode);
        type(expirationMonth, expiryMonthValue);
        type(expirationYear, expiryYearValue);
    }

    public void clearCardNumber() {
        var field = esperarVisible(cardNumber);
        field.clear();
    }

    public void confirmPayment() {
        click(payButton);
    }

    public boolean invoiceAvailable() {
        return isVisible(invoiceLink);
    }

    public boolean paymentFormVisible() {
        return isVisible(nameOnCard);
    }
}
