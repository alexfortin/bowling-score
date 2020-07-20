DROP TABLE IF EXISTS frame;
DROP TABLE IF EXISTS game;

CREATE TABLE game
(
    id          UUID PRIMARY KEY,
    final_score INT
);

CREATE TABLE frame
(
    id          UUID PRIMARY KEY,
    game_id     UUID,
    first_shot  INT,
    second_shot INT,
    score       INT,
    foreign key (game_id) references game (id)
);
