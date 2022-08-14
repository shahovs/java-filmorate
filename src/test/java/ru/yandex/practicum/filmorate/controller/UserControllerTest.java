package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    void init() {
        userController = new UserController(new UserService(new UserDbStorage(new JdbcTemplate())));
        user = new User(0L, "login", "name", "email@email.com", LocalDate.now());
    }

    @Test
    void emptyEmailTest() {
        user.setEmail("");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
    }

    @Test
    void emailWithoutAtSignTest() {
        user.setEmail("emailWithoutAtSign");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
    }

    @Test
    void emptyLoginTest() {
        user.setLogin("");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
    }

    @Test
    void loginWithSpaceTest() {
        user.setLogin("login withSpace");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
    }
    @Test
    void tooLateDateOfBirthTest() {
        LocalDate localDate = LocalDate.of(2030, 1, 1);
        user.setBirthday(localDate);
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
    }

}