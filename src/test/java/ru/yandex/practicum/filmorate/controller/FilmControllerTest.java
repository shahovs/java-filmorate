package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void validateFilmControllerTest() {
        final FilmController filmController = new FilmController();
        final Film film = new Film(0, "name", "description", LocalDate.now(), 1);

        film.setName("");
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
        film.setName("name");

        film.setDescription("123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_" +
                "123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_" +
                "123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_");
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
        film.setDescription("description");

        LocalDate localDate = LocalDate.of(1890, 1, 1);
        film.setReleaseDate(localDate);
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
        film.setReleaseDate(LocalDate.now());

        film.setDuration(0);
        assertThrows(RuntimeException.class, () -> filmController.validateFilm(film));
        film.setDuration(1);
    }

}