package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.FriendshipDbStorage;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    public User getUser(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserIsNotExistException("This user does not exists");
        }
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User saveUser(User user) {
        checkUserName(user);
        return userStorage.saveUser(user);
    }

    public User updateUser(User user) {
        checkUserName(user);
        User updatedUser = userStorage.updateUser(user);
        if (updatedUser == null) {
            throw new UserIsNotExistException("Ошибка. Пользователь с id " + user.getId() + " не найден");
        }
        return userStorage.updateUser(user);
    }

    private void checkUserName(User user) {
        String EMPTY_STRING = "";
        if (EMPTY_STRING.equals(user.getName())) {
            user.setName(user.getLogin());
        }
    }

    public void addFriend(Long userId, Long friendId) {
        friendshipDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendshipDbStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getAllFriends(Long userId) {
        return friendshipDbStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        return friendshipDbStorage.getCommonFriends(firstUserId, secondUserId);
    }

}
