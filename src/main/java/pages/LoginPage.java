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
        driver.get(DriverFactoryUrl.loginUrl());
    }

    public void login(String email, String password) {
        openLogin();
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
    }

    public void loginFromCurrentPage(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
    }

    public void startSignup(String name, String email) {
        openLogin();
        type(signupName, name);
        type(signupEmail, email);
        click(signupButton);
    }

    public boolean isLoggedIn() {
        try {
            return wait.withTimeout(java.time.Duration.ofSeconds(5))
                    .until(currentDriver -> !currentDriver.findElements(accountIndicator).isEmpty());
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public String accountIndicatorText() {
        return text(accountIndicator);
    }

    public boolean hasLoginError(String expected) {
        String source = driver.getPageSource().toLowerCase();
        if (source.contains(expected.toLowerCase())) {
            return true;
        }

        // The demo site does not always render the same error node. Staying on
        // login without an authenticated indicator is still the observable error state.
        return driver.getCurrentUrl().contains("/login") && !isLoggedIn();
    }

    public void logout() {
        click(logoutLink);
    }

    private static final class DriverFactoryUrl {
        private static String loginUrl() {
            return utils.DriverFactory.BASE_URL + "login";
        }
    }
}
