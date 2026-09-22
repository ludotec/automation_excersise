package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DriverFactory;

/** Base para todas las páginas del proyecto. Concentra esperas, clicks,
 * escritura y los helpers comunes para que cada Page Object se enfoque en su
 * pantalla. */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    /** Vuelve a la portada del sitio. Útil al inicio de un escenario. */
    public void navegar() {
        DriverFactory.navegarABase();
    }

    /** Espera a que el elemento sea visible en pantalla. */
    protected WebElement esperarVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Espera a que el elemento esté habilitado para recibir clicks. */
    protected WebElement esperarClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Variante del wait de visibilidad aplicada sobre un WebElement ya cargado. */
    protected WebElement esperarVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /** Click sobre un WebElement con scroll y fallback a JavaScript cuando un
     * iframe de AdSense bloquea el click real. */
    public void click(WebElement element) {
        esperarVisible(element);
        scrollTo(element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException exception) {
            javascriptClick(element);
        }
    }

    /** Variante del click aplicada sobre un locator. */
    protected void click(By locator) {
        WebElement element = esperarVisible(locator);
        scrollTo(element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException exception) {
            javascriptClick(element);
        }
    }

    /** Escribe texto sobre un WebElement esperando a que esté presente en el
     * DOM. Aceptamos inputs ocultos detrás de AdSense porque validamos presencia,
     * no visibilidad. */
    public void type(WebElement element, String text) {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("(//*[@id='" + extractId(element) + "'])[1]")));
        scrollTo(element);
        element.clear();
        element.sendKeys(text);
    }

    /** Variante del type que espera `presenceOfElementLocated` desde el locator. */
    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollTo(element);
        element.clear();
        element.sendKeys(text);
    }

    /** Devuelve el atributo `id` de un WebElement o vacío si no existe. */
    private String extractId(WebElement element) {
        try {
            String id = element.getAttribute("id");
            return id != null ? id : "";
        } catch (RuntimeException exception) {
            return "";
        }
    }

    /** Indica si el elemento es visible en el viewport. */
    protected boolean isVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    /** Devuelve el texto del primer elemento que coincida con el locator. */
    protected String text(By locator) {
        return esperarVisible(locator).getText();
    }

    /** Variante de `text` aplicada sobre un WebElement ya cargado. */
    public String obtenerTexto(WebElement element) {
        return esperarVisible(element).getText();
    }

    /** Helper que expone la URL actual del navegador. */
    public String obtenerUrlActual() {
        return driver.getCurrentUrl();
    }

    /** Hace scroll suave hasta dejar el elemento centrado en el viewport. */
    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }

    /** Click vía JavaScript como último recurso cuando el click normal falla. */
    private void javascriptClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
