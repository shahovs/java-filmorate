package ru.yandex.practicum.filmorate.exception;

public class UserIsNotCorrectException extends RuntimeException {
    public UserIsNotCorrectException(String message) {
        super(message);
    }
}
