package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserIsNotCorrectException;
import ru.yandex.practicum.filmorate.exception.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    Long generator = 0L;
    Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        if (users.containsValue(user)) {
            throw new UserAlreadyExistException("This user is already exist");
        }
        Long userId = ++generator;
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        if (id == null) {
            throw new UserIsNotCorrectException("User's id is null");
        }
        if (!users.containsKey(id)) {
            throw new UserIsNotExistException("This user is not exist");
        }
        users.put(id, user);
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriendsIds().add(friend.getId());
        friend.getFriendsIds().add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendsIds().remove(friend.getId());
        friend.getFriendsIds().remove(user.getId());
    }

    @Override
    public List<User> getAllFriends(User user) {
//        Collection<User> friends = new ArrayList<>();
//        for (Long id : user.getFriendsIds()) {
//            if (id != null) {
//                friends.add(users.get(id));
//            }
//        }
//        return friends;
        return user.getFriendsIds().stream()
                .map(id -> users.get(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(User firstUser, User secondUser) {
        Set<Long> commonFriendsIds = new HashSet<>();
        commonFriendsIds.addAll(firstUser.getFriendsIds());
        commonFriendsIds.addAll(secondUser.getFriendsIds());
        commonFriendsIds.remove(firstUser.getId());
        commonFriendsIds.remove(secondUser.getId());
        return commonFriendsIds.stream()
                .map(id -> users.get(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
