package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User getUser(Long userId);

    Collection<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getAllFriends(Long userId);

    List<User> getCommonFriends(Long firstUserId, Long secondUserId);

}
