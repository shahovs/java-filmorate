package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film getFilm(Long filmId);

    void likeFilm(Film film, User user);

    void deleteLike(Film film, User user);

    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getMostPopularFilms(int count);
}
