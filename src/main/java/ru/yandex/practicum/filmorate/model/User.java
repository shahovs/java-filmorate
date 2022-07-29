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
public class User {

    @EqualsAndHashCode.Exclude
    private Long id;

    private String login;
    private String name;
    private String email;
    private LocalDate birthday;

    @JsonIgnore
    private final Set<Long> friendsIds = new HashSet<>();
}
