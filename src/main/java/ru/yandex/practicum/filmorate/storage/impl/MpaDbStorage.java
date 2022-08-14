package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpa(int mpaId) {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA where MPA_ID = ?";
        List<Mpa> mpa = jdbcTemplate.query(sqlQuery, (ResultSet resultSet, int rowNum) ->
                        new Mpa(
                                resultSet.getInt("MPA_ID"),
                                resultSet.getString("MPA_NAME")),
                mpaId);
        if (mpa.size() == 0) {
            throw new ObjectNotFoundException("Mpa c id " + mpaId + " не найден");
        }
        return mpa.get(0);
    }

    public List<Mpa> getAllMpa() {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA";
        return jdbcTemplate.query(sqlQuery, (ResultSet resultSet, int rowNum) ->
                new Mpa(
                        resultSet.getInt("MPA_ID"),
                        resultSet.getString("MPA_NAME"))
        );
    }

}
