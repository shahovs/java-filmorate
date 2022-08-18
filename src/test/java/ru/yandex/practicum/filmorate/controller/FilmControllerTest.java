package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final FilmController filmController;
    private Film film;

    @BeforeEach
    void init() {
        film = new Film(0L, "name", LocalDate.now(), "description", 1,
                null, null);
    }

    @Test
    void emptyNameTest() {
        film.setName("");
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void tooLongDescriptionTest() {
        film.setDescription("123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_" +
                "123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_" +
                "123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_");
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void tooEarlyDataTest() {
        LocalDate localDate = LocalDate.of(1890, 1, 1);
        film.setReleaseDate(localDate);
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void notPositiveDurationTest() {
        film.setDuration(0);
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
    }

}