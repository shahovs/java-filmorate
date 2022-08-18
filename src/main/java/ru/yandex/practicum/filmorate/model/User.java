package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode//(of = "id")
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Exclude
    private Long id;

    private String login;
    private String name;
    private String email;
    private LocalDate birthday;
}
