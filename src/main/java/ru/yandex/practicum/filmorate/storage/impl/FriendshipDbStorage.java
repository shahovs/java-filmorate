package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.List;

@Component
@Primary
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(Long userId, Long friendId) {
        String sqlQuery =
                "merge into FRIENDSHIPS (user_id, friend_id) " + // можно insert into
                        "values (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (Exception e) {
            throw new UserIsNotExistException("Ошибка. Пользователь не найден.");
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery =
                "delete from FRIENDSHIPS " +
                        "where USER_ID = ? " +
                        "and FRIEND_ID = ?";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (Exception e) {
            throw new UserIsNotExistException("Ошибка. Пользователь не найден.");
        }
    }

    public List<User> getAllFriends(Long userId) {
        String sqlQuery =
                "select USERS.USER_ID, LOGIN, USER_NAME, EMAIL, BIRTHDAY " +
                        "from FRIENDSHIPS " +
                        "join USERS on FRIEND_ID = USERS.USER_ID " +
                        "where FRIENDSHIPS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser, userId);
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        String sqlQuery =
                "select distinct USERS.USER_ID, LOGIN, USER_NAME, EMAIL, BIRTHDAY " +
                        "from FRIENDSHIPS " +
                        "join USERS on FRIEND_ID = USERS.USER_ID " +
                        "where (FRIENDSHIPS.USER_ID = ? " +
                        "or FRIENDSHIPS.USER_ID = ?) " +
                        "and FRIEND_ID != ? " +
                        "and FRIEND_ID != ?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser,
                firstUserId, secondUserId, firstUserId, secondUserId);
    }

}
