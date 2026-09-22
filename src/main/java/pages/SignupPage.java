package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class SignupPage extends BasePage {

    private final By title = By.id("id_gender1");
    private final By password = By.id("password");
    private final By days = By.id("days");
    private final By months = By.id("months");
    private final By years = By.id("years");
    private final By firstName = By.cssSelector("input[data-qa='first_name']");
    private final By lastName = By.cssSelector("input[data-qa='last_name']");
    private final By address = By.cssSelector("input[data-qa='address']");
    private final By country = By.cssSelector("select[data-qa='country']");
    private final By state = By.cssSelector("input[data-qa='state']");
    private final By city = By.cssSelector("input[data-qa='city']");
    private final By zipcode = By.cssSelector("input[data-qa='zipcode']");
    private final By mobile = By.cssSelector("input[data-qa='mobile_number']");
    private final By createAccount = By.cssSelector("button[data-qa='create-account']");
    private final By accountCreated = By.cssSelector("h2[data-qa='account-created']");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    /** Completa el formulario de creación de cuenta con valores fijos para que el
     * escenario no dependa de generación aleatoria. El usuario recibe un nombre
     * dinámico y la contraseña llega por parámetro. */
    public void completeAccount(String name, String passwordValue) {
        click(title);
        type(password, passwordValue);
        new Select(driver.findElement(days)).selectByVisibleText("1");
        new Select(driver.findElement(months)).selectByVisibleText("January");
        new Select(driver.findElement(years)).selectByVisibleText("1990");
        type(firstName, name);
        type(lastName, "Automation");
        type(address, "Automation Street 123");
        new Select(driver.findElement(country)).selectByVisibleText("India");
        type(state, "Buenos Aires");
        type(city, "Buenos Aires");
        type(zipcode, "1000");
        type(mobile, "1122334455");
        click(createAccount);
    }

    /** Indica si el sitio muestra el mensaje de "Account Created!". */
    public boolean isAccountCreated() {
        return isVisible(accountCreated);
    }

    /** Click en "Continue" para volver al home ya autenticado. */
    public void continueToHome() {
        click(continueButton);
    }
}
