package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaStorage {

    MPA getMpa(int mpaId);

    List<MPA> getAllMpa();

}
