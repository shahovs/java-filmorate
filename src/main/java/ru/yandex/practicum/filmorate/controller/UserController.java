package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static String EMPTY_STRING = "";
    private static HashMap<Integer, User> users = new HashMap<>();
    private static int id = 0;

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUser(user);
        if (EMPTY_STRING.equals(user.getName())) {
            user.setName(user.getLogin());
        }
        if (users.containsValue(user)) {
            throw new UserAlreadyExistException("This user is already exist");
        }
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    private void validateUser(User user) {
        String email = user.getEmail();
        if (EMPTY_STRING.equals(email) || !email.contains("@")) {
            throw new UserIsNotCorrectException("User is not correct. Email is wrong.");
        }
        String login = user.getLogin();
        if (EMPTY_STRING.equals(login) || login.contains(" ")) {
            throw new UserIsNotCorrectException("User is not correct. Login is wrong.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserIsNotCorrectException("User is not correct. Birthday is wrong");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateUser(user);
        Integer id = user.getId();
        if (id == null) {
            throw new UserIsNotCorrectException("User's id is null");
        }
        if (!users.containsKey(id)) {
            throw new UserIsNotExistException("This user is not exist");
        }
        User updatedUser = users.get(id);
        updatedUser.setEmail(user.getEmail());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setName(user.getName());
        updatedUser.setBirthday(user.getBirthday());
        return updatedUser;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

}