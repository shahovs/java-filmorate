package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class MpaAndGenreController {

    private final FilmService filmService;

    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpa(@PathVariable int mpaId) {
        log.info("Получен запрос к эндпоинту: GET /films/mpa/{}", mpaId);
        return filmService.getMpa(mpaId);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        log.info("Получен запрос к эндпоинту: GET /films/mpa");
        return filmService.getAllMpa();
    }

    @GetMapping("genres/{genreId}")
    public Genre getGenre(@PathVariable int genreId) {
        log.info("Получен запрос к эндпоинту: GET /films/genres/{}", genreId);
        return filmService.getGenre(genreId);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        log.info("Получен запрос к эндпоинту: GET /films/genres");
        return filmService.getAllGenres();
    }

}
