package steps;

import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.PaymentPage;
import pages.ProductsPage;
import pages.SignupPage;

public final class ScenarioContext {

    public static final String DEMO_EMAIL = "demo@correo.com";
    public static final String DEMO_PASSWORD = "1234";

    private static LoginPage loginPage;
    private static ProductsPage productsPage;
    private static CartPage cartPage;
    private static CheckoutPage checkoutPage;
    private static PaymentPage paymentPage;
    private static SignupPage signupPage;
    private static String createdEmail;
    private static String createdPassword;

    private ScenarioContext() {
    }

    public static void clear() {
        loginPage = null;
        productsPage = null;
        cartPage = null;
        checkoutPage = null;
        paymentPage = null;
        signupPage = null;
        createdEmail = null;
        createdPassword = null;
    }

    public static LoginPage loginPage() { return loginPage; }
    public static void loginPage(LoginPage page) { loginPage = page; }
    public static ProductsPage productsPage() { return productsPage; }
    public static void productsPage(ProductsPage page) { productsPage = page; }
    public static CartPage cartPage() { return cartPage; }
    public static void cartPage(CartPage page) { cartPage = page; }
    public static CheckoutPage checkoutPage() { return checkoutPage; }
    public static void checkoutPage(CheckoutPage page) { checkoutPage = page; }
    public static PaymentPage paymentPage() { return paymentPage; }
    public static void paymentPage(PaymentPage page) { paymentPage = page; }
    public static SignupPage signupPage() { return signupPage; }
    public static void signupPage(SignupPage page) { signupPage = page; }
    public static String createdEmail() { return createdEmail; }
    public static void createdEmail(String email) { createdEmail = email; }
    public static String createdPassword() { return createdPassword; }
    public static void createdPassword(String password) { createdPassword = password; }
}
