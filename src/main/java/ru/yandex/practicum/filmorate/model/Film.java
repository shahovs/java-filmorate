package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @EqualsAndHashCode.Exclude
    Integer id;

    String name;
    String description;
    LocalDate releaseDate;
    int duration;

}
