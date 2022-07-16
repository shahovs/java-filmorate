package ru.yandex.practicum.filmorate.exception;

public class FilmIsNotExistException extends RuntimeException {
    public FilmIsNotExistException(String message) {
        super(message);
    }
}
