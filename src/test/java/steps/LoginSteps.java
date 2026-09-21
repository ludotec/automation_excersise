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

    @Dado("que tengo una cuenta registrada")
    public void tengoUnaCuentaRegistrada() {
        ScenarioContext.loginPage(new LoginPage(DriverFactory.getDriver()));
        AccountSupport.ensureAccount(DriverFactory.getDriver());
        AccountSupport.ensureLoggedOut(DriverFactory.getDriver());
        ScenarioContext.loginPage().openLogin();
    }

    @Dado("que estoy en la página de Signup \\/ Login")
    public void estoyEnLogin() {
        ScenarioContext.loginPage(new LoginPage(DriverFactory.getDriver()));
        ScenarioContext.loginPage().openLogin();
    }

    @Cuando("inicio sesión con mi email y password correctos")
    public void inicioSesionCorrecto() {
        ScenarioContext.loginPage().loginFromCurrentPage(
                ScenarioContext.createdEmail(), ScenarioContext.createdPassword());
    }

    @Cuando("inicio sesión con un password incorrecto")
    public void inicioSesionIncorrecto() {
        AccountSupport.ensureAccount(DriverFactory.getDriver());
        AccountSupport.ensureLoggedOut(DriverFactory.getDriver());
        ScenarioContext.loginPage().openLogin();
        ScenarioContext.loginPage().loginFromCurrentPage(
                ScenarioContext.createdEmail(), "password-invalido");
    }

    @Entonces("veo {string} en el header")
    public void veoUsuarioEnHeader(String expected) {
        assertTrue(ScenarioContext.loginPage().isLoggedIn(), "No se encontró el usuario autenticado");
        assertTrue(ScenarioContext.loginPage().accountIndicatorText().contains("Logged in as"),
                "El indicador de sesión no tiene el formato esperado");
    }

    @Entonces("veo el mensaje {string}")
    public void veoMensajeLogin(String expected) {
        assertTrue(ScenarioContext.loginPage().hasLoginError(expected),
                "No se encontró el mensaje: " + expected);
    }

    @Y("no se inicia la sesión")
    public void noSeIniciaSesion() {
        assertFalse(ScenarioContext.loginPage().isLoggedIn(), "La sesión no debería iniciarse");
    }

    @Dado("que tengo una sesión iniciada")
    public void tengoSesionIniciada() {
        ScenarioContext.loginPage(new LoginPage(DriverFactory.getDriver()));
        AccountSupport.ensureAccount(DriverFactory.getDriver());
        Assertions.assertTrue(ScenarioContext.loginPage().isLoggedIn(), "No se pudo iniciar la sesión");
    }

    @Cuando("selecciono la opción de cerrar sesión")
    public void cierroSesion() {
        ScenarioContext.loginPage().logout();
    }

    @Entonces("vuelvo a la página de Signup \\/ Login")
    public void vuelvoALogin() {
        assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("/login"),
                "No se volvió a la página de login");
    }

    @Y("la sesión queda cerrada")
    public void sesionCerrada() {
        assertFalse(ScenarioContext.loginPage().isLoggedIn(), "La sesión continúa abierta");
    }
}
