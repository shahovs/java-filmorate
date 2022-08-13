package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Component
//@Qualifier("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    @Override
    public Film getFilm(Long filmId) {
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }


    @Override
    public Film addFilm(Film film) {

        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public void likeFilm(Film film, User user) {

    }

    @Override
    public void deleteLike(Film film, User user) {

    }
    @Override
    public List<Film> getMostPopularFilms(int count) {
        return null;
    }


    @Override
    public MPA getMpa(long filmId) {
        return null;
    }

    @Override
    public List<MPA> getAllMpa() {
        return null;
    }

    @Override
    public List<Genre> getGenres(long filmId) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

}
