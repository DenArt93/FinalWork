package dataBase;

import xClient.Employee;

import java.sql.*;

public class EmployeeDB {


    private final String connectionString;
    private final String user;
    private final String password;

    public EmployeeDB(String connectionString, String user, String password) {
        this.connectionString = connectionString;
        this.user = user;
        this.password = password;
    }

    public Employee getEmployeeByName(String name) throws SQLException {
        String query = "SELECT * FROM employee WHERE first_name = ?";
        try (Connection connection = DriverManager.getConnection(connectionString, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setMiddleName(resultSet.getString("middle_name"));
                employee.setCompanyId(resultSet.getInt("company_id"));
                employee.setEmail(resultSet.getString("email"));
                employee.setUrl(resultSet.getString("avatar_url"));
                employee.setPhone(resultSet.getString("phone"));
                employee.setBirthdate(resultSet.getString("birthdate"));
                employee.setIsActive(resultSet.getBoolean("is_active"));
                return employee;
            } else {
                return null;
            }
        }
    }
}
