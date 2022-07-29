package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmIsNotCorrectException;
import ru.yandex.practicum.filmorate.exception.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Long generator = 0L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film getFilm(Long filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException("This film does already exist");
        }
        film.setId(++generator);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Long id = film.getId();
        if (id == null) {
            throw new FilmIsNotCorrectException("Film's id is null");
        }
        if (!films.containsKey(id)) {
            throw new FilmIsNotExistException("This film does not exist");
        }
        films.put(id, film);
        return film;
    }

    @Override
    public void likeFilm(Film film, User user) {
        film.getUsersIdsLikes().add(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        film.getUsersIdsLikes().remove(user.getId());
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return films.values().stream()
                .sorted((film1, film2) -> film2.getUsersIdsLikes().size() - film1.getUsersIdsLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
