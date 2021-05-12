package by.itacademy.jdbc;

import by.itacademy.jdbc.util.ConnectionManager;

import java.sql.*;

import static by.itacademy.jdbc.util.QueryUtil.ADD_COUNTRY;
import static by.itacademy.jdbc.util.QueryUtil.GET_BOOKS_WITH_AUTHORS;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcDemo {

    private static final String URL = "jdbc:postgresql://localhost:5432/country_repository";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pass";
    public static final String GET_ALL_AUTHORS = "SELECT id, name, birth_day, country_id FROM book_storage.author";

    public static void main(String[] args) {
        loadDriver();
        //test1();
        //test2();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOKS_WITH_AUTHORS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String bookName = resultSet.getString("book_name");
                String authorName = resultSet.getString("author_name");
                System.out.println(bookName + " " + authorName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void test2() {
        final String countryName = "США2";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_COUNTRY,
                     RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, countryName);
            System.out.println("Inserting count: " + preparedStatement.executeUpdate());
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                System.out.println(generatedKeys.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void test1() {
        try (
            Connection connection = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD); PreparedStatement statement = connection.prepareStatement(GET_ALL_AUTHORS)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getLong(1)+ " " + resultSet.getString(2));
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
