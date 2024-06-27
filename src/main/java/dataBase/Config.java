package dataBase;

import java.io.InputStream;
import java.util.Properties;

public class Config {


    private Properties properties = new Properties();

    public Config() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Не удалось найти config.properties.");
            }
            properties.load(input);
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка загрузки конфигурации.", ex);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
