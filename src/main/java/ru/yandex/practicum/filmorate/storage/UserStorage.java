package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User getUser(Long userId);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getAllFriends(User user);

    List<User> getCommonFriends(User firstUser, User secondUser);

}
