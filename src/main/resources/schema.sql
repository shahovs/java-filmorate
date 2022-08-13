CREATE TABLE IF NOT EXISTS mpa(
    mpa_id INTEGER --auto_increment
    primary key,
    mpa_name VARCHAR(8) NOT NULL
    );

CREATE TABLE IF NOT EXISTS genres(
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(40) NOT NULL
    );

CREATE TABLE IF NOT EXISTS films(
    film_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name VARCHAR(80) NOT NULL,
    release_date DATE,
    description VARCHAR(80),
    duration INT,
    mpa_id INT NOT NULL,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id),
    CONSTRAINT positive_duration CHECK (duration > 0)
    );

CREATE TABLE IF NOT EXISTS film_genres(
    film_id LONG NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    genre_id INT NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id),
    PRIMARY KEY (film_id, genre_id)
    );

CREATE TABLE IF NOT EXISTS users(
    user_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login VARCHAR(40) NOT NULL UNIQUE,
    user_name VARCHAR(80),
    email VARCHAR(200) UNIQUE NOT NULL,
    birthday DATE
    );

CREATE TABLE IF NOT EXISTS friendships(
    user_id      LONG NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    friend_id    LONG NOT NULL,
    FOREIGN KEY (friend_id) REFERENCES users (user_id),
--     is_confirmed BOOLEAN DEFAULT 'false',
    PRIMARY KEY (user_id, friend_id)
    );

CREATE TABLE IF NOT EXISTS likes(
    user_id LONG NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    film_id LONG NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    PRIMARY KEY (user_id, film_id)
    );