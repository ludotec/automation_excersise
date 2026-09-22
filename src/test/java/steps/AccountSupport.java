package steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.openqa.selenium.WebDriver;

import pages.LoginPage;
import pages.SignupPage;
import utils.ApiDataFactory;

/** Crea y reutiliza una cuenta temporal de pruebas. Se prefiere la API
 * (`/api/createAccount`) por ser estable; si falla, se recurre al formulario
 * de registro del sitio. */
final class AccountSupport {

    private AccountSupport() {
    }

    /** Garantiza que existe una cuenta y que la sesión está iniciada. Si no hay
     * credenciales guardadas en `ScenarioContext`, genera un email único, crea la
     * cuenta vía API y la loguea. */
    static void ensureAccount(WebDriver driver) {
        if (ScenarioContext.createdEmail() == null) {
            String email = "qa" + Instant.now().toEpochMilli() + "@correo.com";
            String password = "Password123!";
            ScenarioContext.createdEmail(email);
            ScenarioContext.createdPassword(password);

            if (!ApiDataFactory.createAccount(email, password)) {
                LoginPage login = new LoginPage(driver);
                login.startSignup("QA Automation", email);
                SignupPage signup = new SignupPage(driver);
                signup.completeAccount("QA Automation", password);
                assertTrue(signup.isAccountCreated(), "No se pudo crear la cuenta temporal");
                signup.continueToHome();
            }
        }

        LoginPage login = new LoginPage(driver);
        if (login.isLoggedIn()) return;
        login.login(ScenarioContext.createdEmail(), ScenarioContext.createdPassword());
        assertTrue(login.isLoggedIn(), "No se pudo iniciar sesión con la cuenta temporal");
    }

    /** Garantiza que no hay sesión iniciada. Si la hay, la cierra. */
    static void ensureLoggedOut(WebDriver driver) {
        LoginPage login = new LoginPage(driver);
        if (login.isLoggedIn()) {
            login.logout();
        }
    }
}
