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
import ru.yandex.practicum.filmorate.storage.impl.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final FilmGenreDbStorage filmGenreDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final UserStorage userStorage;

    public Film getFilm(Long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new FilmIsNotExistException("This film does not exist");
        }
        filmGenreDbStorage.setFilmGenres(film);
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        for (Film film : films) {
            filmGenreDbStorage.setFilmGenres(film);
        }
        return films;
    }

    public Film saveFilm(Film film) {
        Film savedFilm = filmStorage.saveFilm(film);
        filmGenreDbStorage.saveFilmGenres(savedFilm);
        return savedFilm;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        filmGenreDbStorage.saveFilmGenres(updatedFilm);
        return updatedFilm;
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
        likeDbStorage.likeFilm(film, user);
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
        likeDbStorage.deleteLike(film, user);
    }

    public List<Film> getMostPopularFilms(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Ошибка. Количество фильмов не может быть меньше или равно 0");
        }
        List<Film> films = likeDbStorage.getMostPopularFilms(count);
        for (Film film : films) {
            filmGenreDbStorage.setFilmGenres(film);
        }
        return films;
    }

    public Mpa getMpa(int mpaId) {
        return mpaDbStorage.getMpa(mpaId);
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

    public Genre getGenre(int genreId) {
        return genreDbStorage.getGenre(genreId);
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

}
