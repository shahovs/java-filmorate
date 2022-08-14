package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(Long filmId) {
        String sqlQuery = "" +
                "select FILMS.FILM_ID, FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, FILMS.MPA_ID, MPA_NAME " +
                "from FILMS " +
                "join MPA on FILMS.MPA_ID = MPA.MPA_ID " +
                "where FILMS.FILM_ID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, FilmDbStorage::mapRawToFilm, filmId);
        } catch (Exception e) {
            throw new FilmIsNotExistException("Ошибка. Фильм с id " + filmId + " не найден.");
        }
        return film;
    }

    static Film mapRawToFilm(ResultSet resultSet, int row) throws SQLException {
        return new Film(
                resultSet.getLong("film_id"),
                resultSet.getString("film_name"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getString("description"),
                resultSet.getInt("duration"),
                new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")),
                null
        );
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "" +
                "select FILMS.FILM_ID, FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, FILMS.MPA_ID, MPA_NAME " +
                "from FILMS " +
                "join MPA on FILMS.MPA_ID = MPA.MPA_ID ";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRawToFilm);
    }

    @Override
    public Film saveFilm(Film film) {
        String sqlQuery =
                "insert into FILMS (FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, MPA_ID) " +
                        "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            statement.setString(1, film.getName());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                statement.setNull(2, Types.DATE);
            } else {
                statement.setDate(2, Date.valueOf(releaseDate));
            }
            statement.setString(3, film.getDescription());
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "" +
                "update FILMS " +
                "set FILM_NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, DURATION = ?, MPA_ID = ?" +
                "where FILM_ID = ?";
        int successRecord = jdbcTemplate.update(sqlQuery,
                film.getName(),
                Date.valueOf(film.getReleaseDate()),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (successRecord > 0) {
            return film;
        } else {
            throw new ObjectNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
    }

}
