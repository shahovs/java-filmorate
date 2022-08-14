package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenre(int genreId) {
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRES where GENRE_ID = ?";
        List<Genre> genre = jdbcTemplate.query(sqlQuery, (ResultSet resultSet, int rowNub) ->
                new Genre(
                        resultSet.getInt("GENRE_ID"),
                        resultSet.getString("GENRE_NAME")),
                genreId);
        if (genre.size() == 0) {
            throw new ObjectNotFoundException("Жанр c id " + genreId + " не найден");
        }
        return genre.get(0);
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRES";
        return jdbcTemplate.query(sqlQuery, (ResultSet resultSet, int rowNum) ->
                new Genre(
                        resultSet.getInt("GENRE_ID"),
                        resultSet.getString("GENRE_NAME"))
        );
    }

}
