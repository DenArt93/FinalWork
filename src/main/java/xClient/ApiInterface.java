package xClient;

import io.restassured.response.Response;

import java.util.List;

public interface ApiInterface {

    int createNewCompany(ApiCompany company);
    int createNewEmployee(Employee employee);
    Employee patchEmployeeInfo(int employeeId);
    Employee getEmployeeInfo(int employeeId);
    List<Employee> getListOfEmployees(int companyId);
    Response deleteCompany(int companyId);
    Response createEmployee(Employee employee, String token);

}
