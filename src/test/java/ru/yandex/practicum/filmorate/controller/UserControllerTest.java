package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void validateUserControllerTest() {
        final UserController userController = new UserController();
        final User user = new User(0, "login", "name", "email@email.com", LocalDate.now());

        user.setEmail("");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
        user.setEmail("emailWithoutAtSign");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
        user.setEmail("email@email.com");

        user.setLogin("");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
        user.setLogin("login withSpace");
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
        user.setLogin("login");

        LocalDate localDate = LocalDate.of(2030, 1, 1);
        user.setBirthday(localDate);
        assertThrows(RuntimeException.class, () -> userController.validateUser(user));
        user.setBirthday(LocalDate.now());

    }


}