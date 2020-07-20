DROP TABLE IF EXISTS frame;
DROP TABLE IF EXISTS game;

CREATE TABLE game
(
    id                   UUID PRIMARY KEY,
    final_score          INT,
    current_frame_number INT
);

CREATE TABLE frame
(
    id          UUID PRIMARY KEY,
    game_id     UUID,
    number      INT,
    first_shot  INT,
    second_shot INT,
    foreign key (game_id) references game (id)
);
