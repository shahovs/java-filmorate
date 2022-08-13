package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

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

    public User createUser(User user) {
        checkUserName(user);
        return userStorage.createUser(user);
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
//        User user = userStorage.getUser(userId);
//        User friend = userStorage.getUser(friendId);
//        if (user != null && friend != null) {
//            userStorage.addFriend(user, friend);
//        } else {
//            throw new UserIsNotExistException("One or two users do not exist");
//        }
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
//        User user = userStorage.getUser(userId);
//        User friend = userStorage.getUser(friendId);
//        if (user != null && friend != null) {
//            userStorage.deleteFriend(user, friend);
//        } else {
//            throw new UserIsNotExistException("One or two users do not exist");
//        }
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getAllFriends(Long userId) {
//        User user = userStorage.getUser(userId);
//        if (user != null) {
//            return userStorage.getAllFriends(user);
//        } else {
//            throw new UserIsNotExistException("This user does not exists");
//        }
        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
//        User firstUser = userStorage.getUser(firstUserId);
//        User secondUser = userStorage.getUser(secondUserId);
//        if (firstUser != null && secondUser != null) {
//            return userStorage.getCommonFriends(firstUser, secondUser);
//        } else {
//            throw new UserIsNotExistException("One or two users do not exist");
//        }
        return userStorage.getCommonFriends(firstUserId, secondUserId);
    }

}
