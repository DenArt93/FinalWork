package dataBase;



import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompanyDB implements CompanyInterface {


    private final String connectionString;
    private final String user;
    private final String password;

    public CompanyDB(String connectionString, String user, String password) {
        this.connectionString = connectionString;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<Company> getCompaniesByActiveStatus(boolean isActive) throws SQLException {
        List<Company> companies = new ArrayList<>();

        String query = "SELECT * FROM company WHERE is_active = ?";
        try (Connection connection = DriverManager.getConnection(connectionString, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, isActive);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Company company = new Company();
                company.setId(resultSet.getInt("id"));
                company.setActive(resultSet.getBoolean("is_active"));
                company.setCreateTimestamp(resultSet.getTimestamp("create_timestamp").toLocalDateTime());
                company.setChangeTimestamp(resultSet.getTimestamp("change_timestamp") != null
                        ? resultSet.getTimestamp("change_timestamp").toLocalDateTime() : null);
                company.setName(resultSet.getString("name"));
                company.setDescription(resultSet.getString("description"));
                company.setDeletedAt(resultSet.getTimestamp("deleted_at") != null
                        ? resultSet.getTimestamp("deleted_at").toLocalDateTime() : null);
                companies.add(company);
            }
        }

        return companies;
    }

    @Override
    public LocalDateTime getCompanyDeletedAt(int id) throws SQLException {
        String query = "SELECT deleted_at FROM company WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(connectionString, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("deleted_at");
                return timestamp != null ? timestamp.toLocalDateTime() : null;
            } else {
                return null;
            }
        }
    }


}
