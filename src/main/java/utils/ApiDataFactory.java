package utils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/** Crea cuentas de prueba vía la API del sitio (`/api/createAccount`) para evitar
 * depender de los formularios de registro, que son inestables y bloquean los
 * escenarios E2E. */
public final class ApiDataFactory {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private ApiDataFactory() {
    }

    /** Crea la cuenta en el sitio vía API. Devuelve `true` si la respuesta es
     * 200/201, `false` en caso contrario (incluyendo errores de red). */
    public static boolean createAccount(String email, String password) {
        String body = form(
                "name", "QA Automation",
                "email", email,
                "password", password,
                "title", "Mr",
                "birth_date", "1",
                "birth_month", "1",
                "birth_year", "1990",
                "firstname", "QA",
                "lastname", "Automation",
                "company", "QA",
                "address1", "Automation Street 123",
                "address2", "",
                "country", "India",
                "state", "Buenos Aires",
                "city", "Buenos Aires",
                "zipcode", "1000",
                "mobile_number", "1122334455");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DriverFactory.BASE_URL + "api/createAccount"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception exception) {
            return false;
        }
    }

    /** Construye el body `application/x-www-form-urlencoded` a partir de pares
     * clave/valor. */
    private static String form(String... values) {
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < values.length; i += 2) {
            if (body.length() > 0) body.append('&');
            body.append(URLEncoder.encode(values[i], StandardCharsets.UTF_8));
            body.append('=');
            body.append(URLEncoder.encode(values[i + 1], StandardCharsets.UTF_8));
        }
        return body.toString();
    }
}
