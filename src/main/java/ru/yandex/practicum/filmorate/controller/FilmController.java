package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmIsNotCorrectException;
import ru.yandex.practicum.filmorate.exception.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static String EMPTY_STRING = "";
    private final static LocalDate CINEMA_BIRTHDATE = LocalDate.of(1895, 12, 28);
    private static HashMap<Integer, Film> films = new HashMap<>();
    private static int id = 0;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException("This film is already exist");
        }
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    private void validateFilm(Film film) {
        if (EMPTY_STRING.equals(film.getName())) {
            throw new FilmIsNotCorrectException("Film is not correct. Name is empty.");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmIsNotCorrectException("Film is not correct. Description is more than 200 chars.");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDATE)) {
            throw new FilmIsNotCorrectException("Film is not correct. Release date is before than " + CINEMA_BIRTHDATE);
        }
        if (film.getDuration() <= 0) {
            throw new FilmIsNotCorrectException("Film is not correct. Duration is not positive.");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        Integer id = film.getId();
        if (id == null) {
            throw new FilmIsNotCorrectException("Film's id is null");
        }
        if (!films.containsKey(id)) {
            throw new FilmIsNotExistException("This film is not exist");
        }
        Film updatedFilm = films.get(id);
        updatedFilm.setName(film.getName());
        updatedFilm.setDescription(film.getDescription());
        updatedFilm.setReleaseDate(film.getReleaseDate());
        updatedFilm.setDuration(film.getDuration());
        return updatedFilm;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

}