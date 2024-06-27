package dataBase;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface CompanyInterface {

    List<Company> getCompaniesByActiveStatus(boolean isActive) throws SQLException;

    LocalDateTime getCompanyDeletedAt(int id) throws SQLException;

}
