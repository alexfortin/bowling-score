package com.bowling.bowlingscore.service;

import com.bowling.bowlingscore.jpa.FrameEntity;
import com.bowling.bowlingscore.jpa.FrameRepository;
import com.bowling.bowlingscore.jpa.GameEntity;
import com.bowling.bowlingscore.jpa.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BowlingService {
    public static final int TOTAL_FRAMES = 10;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    FrameRepository frameRepository;

    public GameEntity createGame() {
        GameEntity gameEntity = new GameEntity();
        gameRepository.save(gameEntity);
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            FrameEntity frameEntity = FrameEntity.builder().gameId(gameEntity.getId()).build();
            frameRepository.save(frameEntity);
        }
        return gameEntity;
    }
}
