package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void init() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        filmController = new FilmController(new FilmService(
                new FilmDbStorage(jdbcTemplate), new UserDbStorage(jdbcTemplate)));
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