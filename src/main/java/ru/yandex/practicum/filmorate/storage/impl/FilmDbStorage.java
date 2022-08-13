package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

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
        setFilmGenres(film);
        return film;
    }

    private void setFilmGenres(Film film) {
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

//    private LinkedHashSet<Genre> getFilmGenres(Long filmId) {
//        String sqlQuery = "" +
//                "select GENRES.GENRE_ID, GENRE_NAME " +
//                "from FILMS " +
//                "join FILM_GENRES on FILMS.FILM_ID = FILM_GENRES.FILM_ID " +
//                "join GENRES on FILM_GENRES.GENRE_ID = GENRES.GENRE_ID " +
//                "where FILMS.FILM_ID = ?";
//        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery,
//                (ResultSet resultSet, int rowNum) ->
//                        new Genre(
//                                resultSet.getInt("genre_id"),
//                                resultSet.getString("genre_name")),
//                filmId));
//    }

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
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRawToFilm);
        for (Film film : films) {
            setFilmGenres(film);
        }
        return films;
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
        saveFilmGenres(film);
        return film;
    }

    private void saveFilmGenres(Film film) {
        if (film == null) {
            return;
        }
        Long filmId = film.getId();

        String sqlQuery = "delete from FILM_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
//        System.out.println("\n\n\n\n\n111 saveFilmGenres(Film film) film = " + film + "\n\n\n\n\n");

        sqlQuery = "insert into FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        }


//        System.out.println("\n\n\n\n\n777 saveFilmGenres(Film film) filmAfter = " + film + "\n\n\n\n\n");


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
            saveFilmGenres(film);
            return film;
        } else {
            throw new ObjectNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
    }

    @Override
    public void likeFilm(Film film, User user) {
        String sqlQuery = "insert into LIKES (USER_ID, FILM_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        sqlQuery = "update FILMS set likes_count = likes_count + 1 where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

    }

    @Override
    public void deleteLike(Film film, User user) {
        String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        sqlQuery = "update FILMS set likes_count = likes_count - 1 where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sqlQuery = "" +
                "select FILMS.FILM_ID, FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, FILMS.MPA_ID, MPA_NAME " +
                "from FILMS " +
                "join MPA on FILMS.MPA_ID = MPA.MPA_ID " +
                "order by likes_count desc " +
                "limit ?";
//                "select FILMS.FILM_ID, FILM_NAME, RELEASE_DATE, DESCRIPTION, DURATION, FILMS.MPA_ID, MPA_NAME " +
//                "from FILMS " +
//                "join MPA on FILMS.MPA_ID = MPA.MPA_ID " +
//                "left join LIKES on FILMS.FILM_ID = LIKES.FILM_ID " +
//                "group by FILMS.FILM_ID " +
//                "order by COUNT(FILMS.FILM_ID) desc " +
//                "limit ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRawToFilm, count);
        for (Film film : films) {
            setFilmGenres(film);
        }
//        System.out.println("\n\n\n\n\n777 List<Film> films = " + films+ "\n\n\n\n\n");

        return films;
    }

    @Override
    public String getMpa(int mpaId) {
        String sqlQuery = "select MPA_NAME from MPA where MPA_ID = ?";
        List<String> mpa = jdbcTemplate.query(sqlQuery,
                (ResultSet rs, int rowNum) -> rs.getString("MPA_NAME"), mpaId);
        if (mpa.size() == 0) {
            throw new ObjectNotFoundException("Mpa c id " + mpaId + " не найден");
        }
        return mpa.get(0);
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
