package com.bowling.bowlingscore.resource;

import com.bowling.bowlingscore.api.Game;
import com.bowling.bowlingscore.jpa.GameEntity;
import com.bowling.bowlingscore.service.BowlingService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class BowlingResource {
    @Autowired
    BowlingService bowlingService;

    Mapper mapper = DozerBeanMapperBuilder.create().build();

    /**
     *
     * @param id
     * @return game for given id. If game with id is not found, will return 404 Not Found Exception
     */
    @GetMapping("/game/{id}")
    public Game getGame(@PathVariable(value = "id") UUID id) {
        GameEntity gameEntity = bowlingService.getGame(id);
        return mapper.map(gameEntity, Game.class);
    }

    /**
     *
     * @return initializes a bowling game with all frames. Returns newly created game.
     */
    @PostMapping("/game")
    public Game createGame() {
        GameEntity gameEntity = bowlingService.createGame();
        return mapper.map(gameEntity, Game.class);
    }

    /**
     *
     * @param id
     * @param score
     * @return This endpoint is used to score points. Score must be a positive int, cannot exceed 10, and both combined rolls on a frame must not exceed 10.
     * You do not need to specify the frame the score goes to, the service figures it out for you.
     * Returns new state of game after score is recorded.
     */
    @PostMapping("/game/{id}/score")
    public Game score(@PathVariable(value = "id") UUID id, @RequestParam() int score) {
        GameEntity gameEntity = bowlingService.score(id, score);
        return mapper.map(gameEntity, Game.class);
    }
}
