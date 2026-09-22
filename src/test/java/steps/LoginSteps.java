package steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.junit.jupiter.api.Assertions;
import pages.LoginPage;
import utils.DriverFactory;

public class LoginSteps {

    /** Garantiza que existe una cuenta temporal y abre /login. La cuenta se crea
     * por API si no existe, y la sesión queda cerrada para empezar el escenario
     * desde cero. */
    @Dado("que tengo una cuenta registrada")
    public void tengoUnaCuentaRegistrada() {
        ScenarioContext.loginPage(new LoginPage(DriverFactory.getDriver()));
        AccountSupport.ensureAccount(DriverFactory.getDriver());
        AccountSupport.ensureLoggedOut(DriverFactory.getDriver());
        ScenarioContext.loginPage().openLogin();
    }

    /** Abre la página de login sin crear cuenta ni tocar sesión. */
    @Dado("que estoy en la página de Signup \\/ Login")
    public void estoyEnLogin() {
        ScenarioContext.loginPage(new LoginPage(DriverFactory.getDriver()));
        ScenarioContext.loginPage().openLogin();
    }

    /** Login con las credenciales temporales recién creadas. */
    @Cuando("inicio sesión con mi email y password correctos")
    public void inicioSesionCorrecto() {
        ScenarioContext.loginPage().loginFromCurrentPage(
                ScenarioContext.createdEmail(), ScenarioContext.createdPassword());
    }

    /** Login con la cuenta temporal pero contraseña incorrecta para verificar
     * el mensaje de error. */
    @Cuando("inicio sesión con un password incorrecto")
    public void inicioSesionIncorrecto() {
        AccountSupport.ensureAccount(DriverFactory.getDriver());
        AccountSupport.ensureLoggedOut(DriverFactory.getDriver());
        ScenarioContext.loginPage().openLogin();
        ScenarioContext.loginPage().loginFromCurrentPage(
                ScenarioContext.createdEmail(), "password-invalido");
    }

    /** Verifica que el header muestra el indicador "Logged in as ...". */
    @Entonces("veo {string} en el header")
    public void veoUsuarioEnHeader(String expected) {
        assertTrue(ScenarioContext.loginPage().isLoggedIn(), "No se encontró el usuario autenticado");
        assertTrue(ScenarioContext.loginPage().accountIndicatorText().contains("Logged in as"),
                "El indicador de sesión no tiene el formato esperado");
    }

    /** Verifica que la página muestra el mensaje de error esperado. */
    @Entonces("veo el mensaje {string}")
    public void veoMensajeLogin(String expected) {
        assertTrue(ScenarioContext.loginPage().hasLoginError(expected),
                "No se encontró el mensaje: " + expected);
    }

    /** Verifica que la sesión no quedó abierta tras un login fallido. */
    @Y("no se inicia la sesión")
    public void noSeIniciaSesion() {
        assertFalse(ScenarioContext.loginPage().isLoggedIn(), "La sesión no debería iniciarse");
    }

    /** Garantiza que existe una cuenta temporal y que la sesión está activa. */
    @Dado("que tengo una sesión iniciada")
    public void tengoSesionIniciada() {
        ScenarioContext.loginPage(new LoginPage(DriverFactory.getDriver()));
        AccountSupport.ensureAccount(DriverFactory.getDriver());
        Assertions.assertTrue(ScenarioContext.loginPage().isLoggedIn(), "No se pudo iniciar la sesión");
    }

    /** Click en el enlace "Logout" del header. */
    @Cuando("selecciono la opción de cerrar sesión")
    public void cierroSesion() {
        ScenarioContext.loginPage().logout();
    }

    /** Verifica que la URL vuelve a /login tras el logout. */
    @Entonces("vuelvo a la página de Signup \\/ Login")
    public void vuelvoALogin() {
        assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("/login"),
                "No se volvió a la página de login");
    }

    /** Verifica que tras el logout ya no existe sesión activa. */
    @Y("la sesión queda cerrada")
    public void sesionCerrada() {
        assertFalse(ScenarioContext.loginPage().isLoggedIn(), "La sesión continúa abierta");
    }
}
