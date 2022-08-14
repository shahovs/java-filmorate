package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film getFilm(Long filmId);

    Collection<Film> getAllFilms();

    Film saveFilm(Film film);

    Film updateFilm(Film film);

}
