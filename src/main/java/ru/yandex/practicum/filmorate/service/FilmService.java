package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.exception.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {

    //@Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    //@Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public Film getFilm(Long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new FilmIsNotExistException("This film does not exist");
        }
        return film;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film saveFilm(Film film) {
        return filmStorage.saveFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void likeFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        if (film == null) {
            throw new FilmIsNotExistException("This film does not exist");
        }
        if (user == null) {
            throw new UserIsNotExistException("This user does not exist");
        }
        filmStorage.likeFilm(film, user);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        if (film == null) {
            throw new FilmIsNotExistException("This film does not exist");
        }
        if (user == null) {
            throw new UserIsNotExistException("This user does not exist");
        }
        filmStorage.deleteLike(film, user);
    }

    public List<Film> getMostPopularFilms(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Ошибка. Количество фильмов не может быть меньше или равно 0");
        }
        return filmStorage.getMostPopularFilms(count);
    }

    public Mpa getMpa(int mpaId) {
        return filmStorage.getMpa(mpaId);
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Genre getGenre(int genreId) {
        return filmStorage.getGenre(genreId);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

}
