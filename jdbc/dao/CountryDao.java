package by.itacademy.jdbc.dao;

import by.itacademy.jdbc.entity.Country;
import by.itacademy.jdbc.entity.Language;
import by.itacademy.jdbc.exception.DaoException;
import by.itacademy.jdbc.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sun.plugin.com.event.COMEventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryDao {

    private static final CountryDao INSTANCE = new CountryDao();
    private static final String SAVE = "INSERT INTO country_storage.country (name, populaton, area, language) " +
            "VALUES (?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM country_storage.country WHERE id = ?";
    private static final String GET_BY_ID = "SELECT id, name, populaton as population, area, language " +
            "FROM country_storage.country WHERE id = ?";

    public Optional<Country> getById(Integer id) {
        Country country = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                country = Country.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .population(resultSet.getInt("population"))
                        .area(resultSet.getInt("area"))
                        .language(Language.getByName(resultSet.getString("language")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(country);
    }

    public Integer save(Country country) {
        Integer id = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, country.getName());
            preparedStatement.setInt(2, country.getPopulation());
            preparedStatement.setInt(3, country.getArea());
            preparedStatement.setString(4, country.getLanguage().getName());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
                country.setId(id);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return id;
    }

    public boolean delete(Integer id) {
        boolean result = false;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }


    public static CountryDao getInstance() {
        return INSTANCE;
    }
}
