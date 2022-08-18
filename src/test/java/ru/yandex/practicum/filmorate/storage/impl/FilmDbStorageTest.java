package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;

    @BeforeEach
    void init() {
        Film film = new Film(0L, "name", LocalDate.now(), "description", 1,
                new MPA(1, ""), null);
        filmDbStorage.saveFilm(film);
    }

    @Test
    public void testFindFilmById() {
        Film film = filmDbStorage.getFilm(1L);
        assertEquals(1, film.getId());
        assertEquals("name", film.getName());
    }
}