package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.exception.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    public Film getFilm(Long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new FilmIsNotExistException("This film does not exist");
        }
        filmGenreStorage.setFilmGenres(film);
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        for (Film film : films) {
            filmGenreStorage.setFilmGenres(film);
        }
        return films;
    }

    public Film saveFilm(Film film) {
        Film savedFilm = filmStorage.saveFilm(film);
        filmGenreStorage.saveFilmGenres(savedFilm);
        return savedFilm;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        filmGenreStorage.saveFilmGenres(updatedFilm);
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
        likeStorage.likeFilm(film, user);
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
        likeStorage.deleteLike(film, user);
    }

    public List<Film> getMostPopularFilms(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Ошибка. Количество фильмов не может быть меньше или равно 0");
        }
        List<Film> films = likeStorage.getMostPopularFilms(count);
        for (Film film : films) {
            filmGenreStorage.setFilmGenres(film);
        }
        return films;
    }

    public MPA getMpa(int mpaId) {
        return mpaStorage.getMpa(mpaId);
    }

    public List<MPA> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Genre getGenre(int genreId) {
        return genreStorage.getGenre(genreId);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

}
