package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class GenreController {

    private final FilmService filmService;

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
