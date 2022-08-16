package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPA getMpa(int mpaId) {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA where MPA_ID = ?";
        List<MPA> mpa = jdbcTemplate.query(sqlQuery, (ResultSet resultSet, int rowNum) ->
                        new MPA(
                                resultSet.getInt("MPA_ID"),
                                resultSet.getString("MPA_NAME")),
                mpaId);
        if (mpa.size() == 0) {
            throw new ObjectNotFoundException("Mpa c id " + mpaId + " не найден");
        }
        return mpa.get(0);
    }

    public List<MPA> getAllMpa() {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA";
        return jdbcTemplate.query(sqlQuery, (ResultSet resultSet, int rowNum) ->
                new MPA(
                        resultSet.getInt("MPA_ID"),
                        resultSet.getString("MPA_NAME"))
        );
    }

}
