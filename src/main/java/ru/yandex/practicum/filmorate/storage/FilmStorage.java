package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film getFilm(Long filmId);

    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void likeFilm(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getMostPopularFilms(int count);

    MPA getMpa(long filmId);

    List<MPA> getAllMpa();

    List<Genre> getGenres(long filmId);

    List<Genre> getAllGenres();

}
