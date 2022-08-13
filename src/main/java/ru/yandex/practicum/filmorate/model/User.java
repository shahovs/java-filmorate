package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode//(of = "id")
//@NoArgsConstructor
@AllArgsConstructor
public class User {

    @EqualsAndHashCode.Exclude
    private Long id;
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;

    //boolean isConfirmedFriendship; // или Map<Boolean, Integer> friendsIds; ???

//    ЭТОГО ДЕЛАТЬ НЕ НАДО:
//    Добавьте статус для связи «дружба» между двумя пользователями:
//    неподтверждённая — когда один пользователь отправил запрос на добавление другого пользователя в друзья,
//    подтверждённая — когда второй пользователь согласился на добавление.


//    @JsonIgnore
//    private final Set<Long> friendsIds = new HashSet<>();
}
