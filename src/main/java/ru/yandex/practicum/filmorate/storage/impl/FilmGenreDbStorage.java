package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.sql.ResultSet;
import java.util.LinkedHashSet;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setFilmGenres(Film film) {
        if (film == null) {
            return;
        }
        String sqlQuery = "" +
                "select GENRES.GENRE_ID, GENRE_NAME " +
                "from FILMS " +
                "join FILM_GENRES on FILMS.FILM_ID = FILM_GENRES.FILM_ID " +
                "join GENRES on FILM_GENRES.GENRE_ID = GENRES.GENRE_ID " +
                "where FILMS.FILM_ID = ?";
        LinkedHashSet<Genre> genres = new LinkedHashSet<>(jdbcTemplate.query(sqlQuery,
                (ResultSet resultSet, int rowNum) -> new Genre(
                        resultSet.getInt("genre_id"),
                        resultSet.getString("genre_name")),
                film.getId()));
        film.setGenres(genres);
    }

    public void saveFilmGenres(Film film) {
        if (film == null) {
            return;
        }
        Long filmId = film.getId();

        String sqlQuery = "delete from FILM_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        sqlQuery = "insert into FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        }
    }

}
