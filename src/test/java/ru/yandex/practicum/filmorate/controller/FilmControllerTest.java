package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;

//    @BeforeEach
//    void init() {
//        filmController = new FilmController();
//        film = new Film(0L, "name", "description", LocalDate.now(), 1,
//                null, null, 0);
//    }

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