package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    private LocalDate releaseDate;
    private String description;
    private int duration;
    private int rate;
    private MPA mpa;
    private List<Genre> genres;

//    @JsonIgnore
//    private final Set<Long> usersIdsLikes = new HashSet<>();

}
