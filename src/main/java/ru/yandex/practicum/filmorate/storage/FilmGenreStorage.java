package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmGenreStorage {

    void setFilmGenres(Film film);

    void saveFilmGenres(Film film);

}

