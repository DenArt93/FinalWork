package xClient;

import dataBase.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

public class ApiClient implements ApiInterface {


    private static String BASE_URL;
    private static String AUTH_URL;
    private static String COMPANY_URL;
    private static String EMPLOYEE_URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String TOKEN;

    static {
        Config config = new Config();
        BASE_URL = config.getProperty("api.base.url");
        AUTH_URL = config.getProperty("api.auth.url");
        COMPANY_URL = config.getProperty("api.company.url");
        EMPLOYEE_URL = config.getProperty("api.employee.url");
        USERNAME = config.getProperty("api.username");
        PASSWORD = config.getProperty("api.password");
    }

    public static String getToken() {
        String creds = "{\"username\": \"" + USERNAME + "\",\"password\": \"" + PASSWORD + "\"}";
        TOKEN = given()
                .log().all()
                .body(creds)
                .contentType(ContentType.JSON)
                .when().post(AUTH_URL)
                .then().log().all()
                .statusCode(201)
                .extract().path("userToken");

        return TOKEN;
    }

    @Override
    public int createNewCompany(ApiCompany company) {
        return given()
                .log().all()
                .body(company)
                .header("x-client-token", TOKEN)
                .contentType(ContentType.JSON)
                .when().post(COMPANY_URL)
                .then()
                .log().all()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");
    }

    @Override
    public int createNewEmployee(Employee employee) {
        return given()
                .log().all()
                .body(employee)
                .header("x-client-token", TOKEN)
                .contentType(ContentType.JSON)
                .when().post(EMPLOYEE_URL)
                .then()
                .log().all()
                .statusCode(201)
                .contentType("application/json; charset=utf-8")
                .body("id", greaterThan(1))
                .extract().path("id");
    }

    @Override
    public Employee patchEmployeeInfo(int employeeId) {
        // Создаем тело запроса вручную
        // Значение isActive игнорируется и всегда устанавливается в false
        String requestBody = "{ \"isActive\": false }";
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("x-client-token", TOKEN)
                .body(requestBody)
                .when().patch(EMPLOYEE_URL + "/" + employeeId)
                .then().log().all()
                .statusCode(200)
                .extract().as(Employee.class);
    }

    @Override
    public Employee getEmployeeInfo(int employeeId) {
        Response response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(EMPLOYEE_URL + "/" + employeeId)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        return response.body().as(Employee.class);
    }

    @Override
    public List<Employee> getListOfEmployees(int companyId) {
        Response response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Cache-Control", "no-cache")
                .header("Pragma", "no-cache")
                .when()
                .get(EMPLOYEE_URL + "?company=" + companyId)
                .then()
                .assertThat().statusCode(200)
                .extract().response();

        return response.body().jsonPath()
                .getList(".", Employee.class);
    }

    @Override
    public Response createEmployee(Employee employee, String token) {
        return given()
                .log().all()
                .body(employee)
                .header("x-client-token", token)
                .contentType(ContentType.JSON)
                .when().post(EMPLOYEE_URL)
                .then().log().all()
                .extract().response();
    }

    @Override
    public Response deleteCompany(int companyId) {
        return given()
                .log().all()
                .header("x-client-token", TOKEN)
                .contentType(ContentType.JSON)
                .when().get(BASE_URL + "/company/delete/" + companyId)
                .then().log().all()
                .extract().response();
    }
}
