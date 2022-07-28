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
    Long id;

    String name;
    String description;
    LocalDate releaseDate;
    int duration;

    @JsonIgnore
    Set<Long> usersIdsLikes = new HashSet<>();

}
