package com.bowling.bowlingscore.resource;

import com.bowling.bowlingscore.api.Game;
import com.bowling.bowlingscore.jpa.GameEntity;
import com.bowling.bowlingscore.service.BowlingService;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class BowlingResource {
    @Autowired
    BowlingService bowlingService;

    Mapper mapper = DozerBeanMapperBuilder.create().build();

    @PostMapping("/game")
    public Game createGame() {
        GameEntity gameEntity = bowlingService.createGame();
        return mapper.map(gameEntity, Game.class);
    }
}
