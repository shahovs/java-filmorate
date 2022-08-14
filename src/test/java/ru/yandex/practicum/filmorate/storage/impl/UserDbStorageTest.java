package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userDbStorage;

    @BeforeEach
    void init() {
    }

    @Test
    public void testFindUserById() {
        User user = new User(0L, "login", "name", "email@email.com", LocalDate.now());
        userDbStorage.saveUser(user);
        User userFromDB = userDbStorage.getUser(1L);

        assertEquals(1, userFromDB.getId());
        assertEquals("name", userFromDB.getName());
        assertThat(userFromDB).hasFieldOrPropertyWithValue("id", 1L);

        List<User> users = userDbStorage.getAllUsers();
        assertThat(users).hasSize(1);

    }

}