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

    /** Indica si la página de checkout cargó la sección de dirección de envío
     * del usuario. */
    public boolean hasAddressDetails() {
        return isVisible(addressDetails);
    }

    /** Indica si la página de checkout cargó el resumen del pedido. */
    public boolean hasOrderReview() {
        return isVisible(orderReview) || isVisible(By.cssSelector(".cart_info"));
    }

    /** Escribe un comentario sobre el pedido antes de continuar al pago. */
    public void addComment(String value) {
        type(comment, value);
    }

    /** Click en "Place Order" y espera a que /payment termine de renderizar el
     * formulario. La página puede tardar varios segundos por los iframes de
     * AdSense; limpiamos las imágenes y esperamos al documento interactivo. */
    public void continueToPayment() {
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "var imgs=document.images;for(var i=0;i<imgs.length;i++){if(imgs[i].src.indexOf('google')>=0){imgs[i].remove();}}");
        } catch (RuntimeException ignored) {
        }
        click(placeOrder);
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(20))
                .until(d -> {
                    Object ready = ((org.openqa.selenium.JavascriptExecutor) d)
                            .executeScript("return document.readyState;");
                    boolean hasInputs = d.findElements(org.openqa.selenium.By.tagName("input")).size() > 0;
                    return ("interactive".equals(ready) || "complete".equals(ready)) && hasInputs;
                });
    }

    /** Indica si el modal de Register/Login está visible (caso de checkout sin
     * sesión). */
    public boolean offersRegisterOrLogin() {
        return isVisible(registerLogin);
    }

    /** Verifica si la dirección de envío contiene el texto esperado. */
    public boolean deliveryAddressContains(String expected) {
        return driver.findElements(By.cssSelector(".address_delivery")).stream()
                .anyMatch(element -> element.getText().contains(expected));
    }

    /** Verifica si la dirección de facturación contiene el texto esperado. */
    public boolean billingAddressContains(String expected) {
        return driver.findElements(By.cssSelector(".address_invoice")).stream()
                .anyMatch(element -> element.getText().contains(expected));
    }
}
