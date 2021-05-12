package by.itacademy.jdbc.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public final class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    public static Connection getConnection  () throws SQLException {
        return DriverManager.getConnection(PropertiesUtil.get(URL_KEY),PropertiesUtil.get(USER_KEY),PropertiesUtil.get(PASSWORD_KEY));
    }

}
