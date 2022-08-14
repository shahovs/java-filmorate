package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void likeFilm(Film film, User user) {
        String sqlQuery = "insert into LIKES (USER_ID, FILM_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        sqlQuery = "update FILMS set likes_count = likes_count + 1 where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    public void deleteLike(Film film, User user) {
        String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        sqlQuery = "update FILMS set likes_count = likes_count - 1 where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    public List<Film> getMostPopularFilms(int count) {
        String sqlQuery = "" +
                "select FILMS.FILM_ID, FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, FILMS.MPA_ID, MPA_NAME " +
                "from FILMS " +
                "join MPA on FILMS.MPA_ID = MPA.MPA_ID " +
                "order by likes_count desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRawToFilm, count);
    }

}
