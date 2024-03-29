package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserIsNotCorrectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту: GET /users/{}", userId);
        return userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получен запрос к эндпоинту: GET /users/");
        return userService.getAllUsers();
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        log.info("Получен запрос к эндпоинту: POST /users, Создан объект из тела запроса:'{}'", user);
        validateUser(user);
        return userService.saveUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос к эндпоинту: PUT /users, Создан объект из тела запроса:'{}'", user);
        validateUser(user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос к эндпоинту: PUT /users/{}/friends/{}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос к эндпоинту: DELETE /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable("id") Long userId) {
        log.info("Получен запрос к эндпоинту: GET /users/{}/friends", userId);
        return userService.getAllFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable("id") Long firstUserId,
            @PathVariable("otherId") Long secondUserId) {
        log.info("Получен запрос к эндпоинту: GET /users/{}/friends/common/{}", firstUserId, secondUserId);
        return userService.getCommonFriends(firstUserId, secondUserId);
    }

    void validateUser(User user) {
        final String EMPTY_STRING = "";
        String email = user.getEmail();
        if (EMPTY_STRING.equals(email) || !email.contains("@")) {
            log.warn("Исключение. Email is wrong. Объект из тела запроса:'{}'", user);
            throw new UserIsNotCorrectException("User is not correct. Email is wrong.");
        }
        String login = user.getLogin();
        if (EMPTY_STRING.equals(login) || login.contains(" ")) {
            log.warn("Исключение. Login is wrong. Объект из тела запроса:'{}'", user);
            throw new UserIsNotCorrectException("User is not correct. Login is wrong.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Исключение. Birthday is wrong. Объект из тела запроса:'{}'", user);
            throw new UserIsNotCorrectException("User is not correct. Birthday is wrong");
        }
    }

}
