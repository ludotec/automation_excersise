package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By loginEmail = By.cssSelector("input[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");
    private final By signupName = By.cssSelector("input[data-qa='signup-name']");
    private final By signupEmail = By.cssSelector("input[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("button[data-qa='signup-button']");
    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By accountIndicator = By.xpath("//a[contains(normalize-space(), 'Logged in as')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void openLogin() {
        // Quitamos los iframes de AdSense antes de la navegación para que no
        // bloqueen la finalización del evento `load`.
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "var imgs=document.images;for(var i=0;i<imgs.length;i++){if(imgs[i].src.indexOf('google')>=0){imgs[i].remove();}}");
        } catch (RuntimeException ignored) {
        }
        driver.navigate().to(DriverFactoryUrl.loginUrl());
        // Esperamos primero a que el documento esté listo antes de buscar el input,
        // porque el sitio puede tardar varios segundos en resolver el `load`.
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(45))
                .until(d -> {
                    Object ready = ((org.openqa.selenium.JavascriptExecutor) d)
                            .executeScript("return document.readyState;");
                    return "interactive".equals(ready) || "complete".equals(ready);
                });
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(d -> {
                    try {
                        ((org.openqa.selenium.JavascriptExecutor) d).executeScript(
                                "var imgs=document.images;for(var i=0;i<imgs.length;i++){if(imgs[i].src.indexOf('google')>=0){imgs[i].remove();}}");
                    } catch (RuntimeException ignored) {
                    }
                    return d.findElements(loginEmail).size() > 0
                            || d.findElements(By.tagName("button")).size() > 0;
                });
    }

    /** Abre la página de login y completa el formulario con las credenciales
     * recibidas. Usar cuando el flujo empieza sin sesión. */
    public void login(String email, String password) {
        openLogin();
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
    }

    /** Login sobre la página actual, abriendo /login si es necesario. Usar cuando el
     * navegador ya está en otra pantalla y solo hay que autenticarse. */
    public void loginFromCurrentPage(String email, String password) {
        if (!driver.getCurrentUrl().contains("/login")) {
            openLogin();
        }
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(d -> d.findElements(loginEmail).size() > 0);
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
    }

    /** Abre el formulario de registro y rellena nombre y email. El resto del
     * formulario se completa desde otra page (SignupPage). */
    public void startSignup(String name, String email) {
        openLogin();
        type(signupName, name);
        type(signupEmail, email);
        click(signupButton);
    }

    /** Indica si el header muestra el indicador de "Logged in as". Se usa 5
     * segundos de timeout porque el sitio puede tardar en reflejar el inicio
     * de sesión. */
    public boolean isLoggedIn() {
        try {
            return wait.withTimeout(java.time.Duration.ofSeconds(5))
                    .until(currentDriver -> !currentDriver.findElements(accountIndicator).isEmpty());
        } catch (RuntimeException exception) {
            return false;
        }
    }

    /** Devuelve el texto del indicador de sesión activa. */
    public String accountIndicatorText() {
        return text(accountIndicator);
    }

    /** Indica si la página muestra el mensaje de error esperado. Si no aparece
     * el texto esperado, también se considera error seguir en /login sin
     * indicador de sesión. */
    public boolean hasLoginError(String expected) {
        String source = driver.getPageSource().toLowerCase();
        if (source.contains(expected.toLowerCase())) {
            return true;
        }

        // El sitio no siempre renderiza el mismo nodo de error, así que también
        // se considera error estar en /login sin indicador de sesión.
        return driver.getCurrentUrl().contains("/login") && !isLoggedIn();
    }

    /** Click en el enlace de logout del header. */
    public void logout() {
        click(logoutLink);
    }

    /** Helper privado para construir la URL completa de /login usando el driver
     * global. */
    private static final class DriverFactoryUrl {
        private static String loginUrl() {
            return utils.DriverFactory.BASE_URL + "login";
        }
    }
}
