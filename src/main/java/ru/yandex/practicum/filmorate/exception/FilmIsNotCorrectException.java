package ru.yandex.practicum.filmorate.exception;

public class FilmIsNotCorrectException extends RuntimeException {
    public FilmIsNotCorrectException(String message) {
        super(message);
    }
}
