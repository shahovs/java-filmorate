package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Film {

    @EqualsAndHashCode.Exclude
    private Long id;

    private String name;
    private LocalDate releaseDate;
    private String description;
    private int duration;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
}
