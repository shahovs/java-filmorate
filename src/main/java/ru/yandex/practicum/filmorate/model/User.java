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
    Long id;

    String login;
//    @NotBlank // todo find import
    String name;
    String email;
//    @Past // todo find import
    LocalDate birthday;

    @JsonIgnore
    Set<Long> friendsIds = new HashSet<>();
}
