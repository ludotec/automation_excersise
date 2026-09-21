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

/** Clase común del Page Object Model y Page Factory. */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navegar() {
        DriverFactory.navegarABase();
    }

    protected WebElement esperarVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement esperarClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement esperarVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void click(WebElement element) {
        esperarVisible(element);
        scrollTo(element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException exception) {
            javascriptClick(element);
        }
    }

    protected void click(By locator) {
        WebElement element = esperarVisible(locator);
        scrollTo(element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException exception) {
            javascriptClick(element);
        }
    }

    public void type(WebElement element, String text) {
        esperarVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    protected void type(By locator, String text) {
        WebElement element = esperarVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected boolean isVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    protected String text(By locator) {
        return esperarVisible(locator).getText();
    }

    public String obtenerTexto(WebElement element) {
        return esperarVisible(element).getText();
    }

    public String obtenerUrlActual() {
        return driver.getCurrentUrl();
    }

    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }

    private void javascriptClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
