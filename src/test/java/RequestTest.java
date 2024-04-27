import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

@Epic("CRUD")
public class RequestTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(RequestTest.class);

    @Feature("Obtener informacion")
    @Test
    @Description("Lista usuarios")
    @Step
    public void getListUser(){
        Response response = given()
                .log()
                .all()
                .get("https://reqres.in/api/users?page=2");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println("Body: " + body);
        System.out.println("Status code: " + statusCode);
    }
    @Test
    @Description("Lista usuario")
    @Step
    @Feature("Obtener registros")
    public void getSingleUser(){
        Response response = given()
                .log()
                .all()
                .get("https://reqres.in/api/users/2");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println("Body: " + body);
        System.out.println("Status code: " + statusCode);
    }
    @Test
    @Description("Usuario no encontrado")
    @Step
    @Feature("Obtener registros")
    public void getSingleUserNotFound(){
        Response response = given()
                .log()
                .all()
                .get("https://reqres.in/api/users/23");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        System.out.println("Body: " + body);
        System.out.println("Status code: " + statusCode);

    }
    @Test
    @Description("Recursos de lista")
    @Step
    @Feature("Obtener registros")
    public void getListResource(){
        Response response = given()
                .log()
                .all()
                .get("https://reqres.in/api/unknown");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println("Body: " + body);
        System.out.println("Status code: " + statusCode);
    }
    @Test
    @Description("Recurso unico encontrado")
    @Step
    @Feature("Obtener registros")
    public void getSingleResource(){
        Response response = given()
                .log()
                .all()
                .get("https://reqres.in/api/unknown/2");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println("Body: " + body);
        System.out.println("Status code: " + statusCode);
    }
    @Test
    @Description("Recurso no encontrado")
    @Step
    @Feature("Obtener registros")
    public void getSingleResourceNotFound(){
        Response response = given()
                .log()
                .all()
                .get("https://reqres.in/api/unknown/23");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        System.out.println("Body: " + body);
        System.out.println("Status code: " + statusCode);

    }
    @Test
    @Description("Creacion de usuario")
    @Step
    @Feature("Insertar registro")
    public void insertCreateUser(){
        Response response = given()
                .log()
                .all()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .post("https://reqres.in/api/users");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));
        System.out.println("Body: " + body);
        System.out.println("Status code: " + statusCode);

    }
    @Test
    @Description("Actualizar registro")
    @Step
    @Feature("Actualizar informacion")
    public void updateUser(){
        String jobUpdated = given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("job");
        System.out.println("job: " + jobUpdated);
        assertThat(jobUpdated, equalTo("zion resident"));

    }
    @Test
    @Description("Eliminacion de usuario")
    @Step
    @Feature("Eliminar registro")
    public void deleteUser(){
        given()
                .log()
                .all()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        System.out.println("Status code: " + HttpStatus.SC_NO_CONTENT);
    }
    @Test
    @Description("Registro de usuario exitoso")
    @Step
    @Feature("Insertar registro")
    public void registerSuccessful(){
        Response response = given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .post("https://reqres.in/api/register");
        String responseBody = response.getBody().asString();
        System.out.println("Response body: " + responseBody);
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        String token = jsonObject.get("token").getAsString();
    }
    @Test
    @Description("Registro de usuario sin exito")
    @Step
    @Feature("Insertar registro")
    public void registerUnsuccessful(){
        Response response = given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .post("https://reqres.in/api/register");
        String responseBody = response.getBody().asString();
        System.out.println("Response body: " + responseBody);
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        String error = jsonObject.get("error").getAsString();
    }
    @Test
    @Description("Inicio de sesion exitoso")
    @Step
    @Feature("Ingresar")
    public void loginSuccessful(){
        Response response = given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("https://reqres.in/api/login")
                .thenReturn();
        String responseBody = response.getBody().asString();
        System.out.println("Response body: " + responseBody);
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        String token = jsonObject.get("token").getAsString();

    }
    @Test
    @Description("Inicio de sesion sin exito")
    @Step
    @Feature("Ingresar")
    public void loginUnsuccessful(){
        Response response = given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .post("https://reqres.in/api/login");
        String responseBody = response.getBody().asString();
        System.out.println("Response body: " + responseBody);
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        String error = jsonObject.get("error").getAsString();
    }
}
