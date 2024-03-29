package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmIsNotCorrectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Long filmId) {
        log.info("Получен запрос к эндпоинту: GET /films/{}", filmId);
        return filmService.getFilm(filmId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен запрос к эндпоинту: GET /films");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film saveFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: POST /films, Создан объект из тела запроса:'{}'", film);
        validateFilm(film);
        return filmService.saveFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: PUT /films, Создан объект из тела запроса:'{}'", film);
        validateFilm(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту: PUT /films/{}/like/{}", filmId, userId);
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту: DELETE /films/{}/like/{}", filmId, userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос к эндпоинту: GET /films/popular, count = {}", count);
        return filmService.getMostPopularFilms(count);
    }

    void validateFilm(Film film) {
        final String EMPTY_STRING = "";
        final LocalDate CINEMA_BIRTHDATE = LocalDate.of(1895, 12, 28);

        if (EMPTY_STRING.equals(film.getName())) {
            log.warn("Исключение. Name is empty. Объект из тела запроса:'{}'", film);
            throw new FilmIsNotCorrectException("Film is not correct. Name is empty.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Исключение. Description is more than 200 chars. Объект из тела запроса:'{}'", film);
            throw new FilmIsNotCorrectException("Film is not correct. Description is more than 200 chars.");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDATE)) {
            log.warn("Исключение. Release date is before than CINEMA_BIRTHDATE. Объект из тела запроса:'{}'", film);
            throw new FilmIsNotCorrectException("Film is not correct. Release date is before than " + CINEMA_BIRTHDATE);
        }
        if (film.getDuration() <= 0) {
            log.warn("Исключение. Duration is not positive. Объект из тела запроса:'{}'", film);
            throw new FilmIsNotCorrectException("Film is not correct. Duration is not positive.");
        }
    }

}
