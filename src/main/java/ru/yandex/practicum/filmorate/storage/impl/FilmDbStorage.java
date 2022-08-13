package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
//@Qualifier("filmDbStorage")
//@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(Long filmId) {
//        List<Genre> genres = getGenresById(filmId);
        String sqlQuery = "" +
                "select FILMS.FILM_ID, FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, FILMS.MPA_ID, MPA_NAME " +
                "from FILMS " +
                "join MPA on FILMS.MPA_ID = MPA.MPA_ID " +
                "where FILMS.FILM_ID = ?";

        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRawToFilm, filmId);
        } catch (Exception e) {
            throw new FilmIsNotExistException("Ошибка. Фильм с id " + filmId + " не найден.");
        }
        return film;
    }

    private Film mapRawToFilm(ResultSet resultSet, int row) throws SQLException {
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
    public String getMpa(int mpaId) {
        String sqlQuery = "select MPA_NAME from MPA where MPA_ID = ?";
        return jdbcTemplate.query(sqlQuery, (ResultSet rs) -> rs.getString("MPA_NAME"), mpaId);
    }

    @Override
    public List<String> getAllMpa() {
        String sqlQuery = "select MPA_NAME from MPA";
        return jdbcTemplate.query(sqlQuery,
                (ResultSet resultSet, int rowNum) -> resultSet.getString("MPA_NAME"));
    }

    @Override
    public List<String> getGenresById(int genreId) {
        String sqlQuery = "select GENRE_NAME from GENRES where GENRE_ID = ?";
//        String sqlQuery = "" +
//                "select GENRE_NAME " +
//                "from FILMS " +
//                "join FILM_GENRES on FILMS.FILM_ID = FILM_GENRES.FILM_ID " +
//                "join GENRES on FILM_GENRES.GENRE_ID = GENRES.GENRE_ID " +
//                "where FILMS.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery,
                (ResultSet resultSet, int rowNub) -> resultSet.getString("GENRE_NAME"), genreId);
    }

    @Override
    public List<String> getAllGenres() {
        String sqlQuery = "select GENRE_NAME from GENRES";
        return jdbcTemplate.query(sqlQuery,
                (ResultSet resultSet, int rowNum) -> resultSet.getString("GENRE_NAME"));

    }

}
