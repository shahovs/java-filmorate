package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @EqualsAndHashCode.Exclude
    private Long id;

    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    @JsonIgnore
    private final Set<Long> usersIdsLikes = new HashSet<>();

}
