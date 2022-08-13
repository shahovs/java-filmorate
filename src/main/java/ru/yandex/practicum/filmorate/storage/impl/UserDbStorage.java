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
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
@Primary
//@Qualifier("userDbStorage")
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
            user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
        }
        catch (Exception e) {
            throw new UserIsNotExistException("Ошибка. Пользователь с id " + userId + " не найден.");
        }
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getLong("user_id"),
                resultSet.getString("login"),
                resultSet.getString("user_name"),
                resultSet.getString("email"),
                resultSet.getDate("birthday").toLocalDate());
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery =
                "select USER_ID, LOGIN, USER_NAME, EMAIL, BIRTHDAY " +
                "from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
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
//        user.setId(keyHolder.getKey().longValue());
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
                Date.valueOf(user.getBirthday()), //user.getBirthday(),
                user.getId());
        // The return value of the method is an int, which indicates how many records were affected by the operation.
        System.out.println(successRecord);
        if (successRecord > 0) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery =
                "merge into FRIENDSHIPS (user_id, friend_id) " + // можно insert into
                "values (?, ?)";
        try{
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch(Exception e){
            throw new UserIsNotExistException("Ошибка. Пользователь не найден.");
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery =
                "delete from FRIENDSHIPS " +
                "where USER_ID = ? " +
                "and FRIEND_ID = ?";
        try{
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch(Exception e){
            throw new UserIsNotExistException("Ошибка. Пользователь не найден.");
        }
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        String sqlQuery =
                "select USERS.USER_ID, LOGIN, USER_NAME, EMAIL, BIRTHDAY " +
                "from FRIENDSHIPS " +
                "join USERS on FRIEND_ID = USERS.USER_ID " +
                "where FRIENDSHIPS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        String sqlQuery =
                "select distinct USERS.USER_ID, LOGIN, USER_NAME, EMAIL, BIRTHDAY " +
                "from FRIENDSHIPS " +
                "join USERS on FRIEND_ID = USERS.USER_ID " +
                "where (FRIENDSHIPS.USER_ID = ? " +
                "or FRIENDSHIPS.USER_ID = ?) " +
                "and FRIEND_ID != ? " +
                "and FRIEND_ID != ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, firstUserId, secondUserId, firstUserId, secondUserId);

//        Set<Long> commonFriendsIds = new HashSet<>();
//        commonFriendsIds.addAll(jdbcTemplate.query(sqlQuery,
//                (ResultSet resultSet, int rowNum) -> resultSet.getLong("friend_id"),
//                firstUserId));
//        commonFriendsIds.addAll(jdbcTemplate.query(sqlQuery,
//                (ResultSet resultSet, int rowNum) -> resultSet.getLong("friend_id"),
//                secondUserId));
//
//        commonFriendsIds.remove(firstUserId);
//        commonFriendsIds.remove(secondUserId);
//
//        List<User> commonFriends = new ArrayList<>();
//        for (Long id : commonFriendsIds) {
//            commonFriends.add(getUser(id));
//        }
//        return commonFriends;
    }

}
