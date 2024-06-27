package testDB;

import dataBase.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xClient.ApiClient;
import xClient.ApiCompany;
import xClient.Employee;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class CompanyApiTest {


    private CompanyInterface companyInterface;
    private EmployeeDB employeeDB;
    private ApiClient apiClient;
    private String token;

    @BeforeEach
    public void setUp() throws SQLException {
        Config config = new Config();
        String connectionString = config.getProperty("db.url");
        String user = config.getProperty("db.user");
        String pass = config.getProperty("db.password");

        companyInterface = new CompanyDB(connectionString, user, pass);
        employeeDB = new EmployeeDB(connectionString, user, pass);
        apiClient = new ApiClient();
        //ApiClient.getToken();
        token = ApiClient.getToken();
    }

    @Test
    public void testGetCompaniesByActiveStatus() {
        assertDoesNotThrow(() -> {
            // Test активной компании
            List<Company> activeCompanies = companyInterface.getCompaniesByActiveStatus(true);
            assertNotNull(activeCompanies, "Список активных компаний не должен быть нулевым.");
            System.out.println("Активные компании:");
            for (Company company : activeCompanies) {
                assertTrue(company.isActive(), "Компания должна быть активной");
                System.out.println("Company Name: " + company.getName());
            }

            // Test неактивной компании
            List<Company> inactiveCompanies = companyInterface.getCompaniesByActiveStatus(false);
            assertNotNull(inactiveCompanies, "Список неактивных компаний не должен быть нулевым.");
            System.out.println("Неактивные компании:");
            for (Company company : inactiveCompanies) {
                assertFalse(company.isActive(), "Компания должна быть неактивной");
                System.out.println("Company Name: " + company.getName());
            }
        });
    }

    @Test
    public void testCreateEmployeeInNonExistentCompany() {
        // Создаем сотрудника с несуществующим companyId
        Employee employee = new Employee("1990-01-01T00:00:00", "http://example.com/avatar.jpg", 2020);

        // Отправка запроса на создание сотрудника
        Response response = apiClient.createEmployee(employee, token);

        // Проверка статус кода и сообщения об ошибке
        assertEquals(500, response.getStatusCode());
        response.then().body("message", equalTo("Internal server error"));  // Проверка ожидаемого сообщения об ошибке
    }

    @Test
    public void testCreateCompanyAddEmployeesAndEdit() throws InterruptedException {
        // Создаём новую компанию.
        ApiCompany newCompany = new ApiCompany();
        int companyId = apiClient.createNewCompany(newCompany);

        // Создаём и добавляем сотрудников.
        Employee employee1 = new Employee("1990-01-01T00:00:00", "http://example.com/avatar1.jpg", companyId);
        Employee employee2 = new Employee("1990-01-01T00:00:00", "http://example.com/avatar2.jpg", companyId);
        Employee employee3 = new Employee("1990-01-01T00:00:00", "http://example.com/avatar3.jpg", companyId);

        int employeeId1 = apiClient.createNewEmployee(employee1);
        int employeeId2 = apiClient.createNewEmployee(employee2);
        int employeeId3 = apiClient.createNewEmployee(employee3);

        // Изменяем статус активности одного из сотрудников.
        apiClient.patchEmployeeInfo(employeeId3);

        // Получаем список сотрудников и проверяем их статус активности.
        List<Employee> employees = apiClient.getListOfEmployees(companyId);
        int activeEmployeesCount = 0;
        for (Employee emp: employees) {
            if (emp.getIsActive()) {
                activeEmployeesCount++;
            }
        }

        //проверяем условия.
        assertEquals(3, employees.size(), "Всего должно быть 3 сотрудника..");
        assertEquals(2, activeEmployeesCount, "Только 2 из 3 сотрудников должны быть активными.");
    }


    @Test
    public void testCreateAndDeleteCompany() throws SQLException {
        // Создаем новую компанию и сохраняем ее id
        ApiCompany newCompany = new ApiCompany();
        int companyId = apiClient.createNewCompany(newCompany);

        // Удаляем созданную компанию
        apiClient.deleteCompany(companyId);

        // Получаем deletedAt для удаленной компании и проверяем, что он не равен null
        LocalDateTime deletedAt = companyInterface.getCompanyDeletedAt(companyId);
        assertNotNull(deletedAt, "Удаленная компания должна иметь значение deletedAt != null");
    }


}
