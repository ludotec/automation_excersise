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

    /** Completa el formulario de pago. La página /payment suele tardar en
     * renderizar los inputs porque los iframes de AdSense bloquean el evento
     * `load`. Limpiamos imágenes, refrescamos si hace falta y luego tipeamos. */
    public void completePayment(String name, String number, String securityCode, String expiryMonthValue,
                                String expiryYearValue) {
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "var imgs=document.images;for(var i=0;i<imgs.length;i++){if(imgs[i].src.indexOf('google')>=0){imgs[i].remove();}}");
        } catch (RuntimeException ignored) {
        }
        // Si el documento no tiene los inputs esperados, refrescamos la página.
        long inputCount = ((Number) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.querySelectorAll('input').length;")).longValue();
        if (inputCount < 6) {
            driver.navigate().refresh();
        }
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(nameOnCard));
        type(nameOnCard, name);
        type(cardNumber, number);
        type(cvc, securityCode);
        type(expirationMonth, expiryMonthValue);
        type(expirationYear, expiryYearValue);
    }

    /** Limpia el campo "Card Number" para validar el flujo de pago con datos
     * incompletos. */
    public void clearCardNumber() {
        var field = esperarVisible(cardNumber);
        field.clear();
    }

    /** Click en "Pay and Confirm Order" para finalizar la compra. */
    public void confirmPayment() {
        click(payButton);
    }

    /** Indica si el enlace "Download Invoice" está visible tras confirmar el
     * pedido. */
    public boolean invoiceAvailable() {
        return isVisible(invoiceLink);
    }

    /** Indica si el formulario de pago está visible. */
    public boolean paymentFormVisible() {
        return isVisible(nameOnCard);
    }
}
