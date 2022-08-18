package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(Long userId) {
        String sqlQuery =
                "select USER_ID, LOGIN, USER_NAME, EMAIL, BIRTHDAY " +
                        "from USERS " +
                        "where USER_ID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, UserDbStorage::mapRowToUser, userId);
        } catch (Exception e) {
            throw new UserIsNotExistException("Ошибка. Пользователь с id " + userId + " не найден.");
        }
        return user;
    }

    static User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getLong("user_id"),
                resultSet.getString("login"),
                resultSet.getString("user_name"),
                resultSet.getString("email"),
                resultSet.getDate("birthday").toLocalDate());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery =
                "select USER_ID, LOGIN, USER_NAME, EMAIL, BIRTHDAY " +
                        "from USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser);
    }

    @Override
    public User saveUser(User user) {
        String sqlQuery =
                "insert into USERS (LOGIN, USER_NAME, EMAIL, BIRTHDAY) " +
                        "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, Date.valueOf(birthday));
            }
            return statement;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery =
                "update USERS " +
                        "set LOGIN = ?, USER_NAME = ?, EMAIL = ?, BIRTHDAY = ?" +
                        "where USER_ID = ?";
        int successRecord = jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        if (successRecord > 0) {
            return user;
        } else {
            return null;
        }
    }

}
