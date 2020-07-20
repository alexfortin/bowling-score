package com.bowling.bowlingscore.service;

import com.bowling.bowlingscore.jpa.FrameEntity;
import com.bowling.bowlingscore.jpa.FrameRepository;
import com.bowling.bowlingscore.jpa.GameEntity;
import com.bowling.bowlingscore.jpa.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
public class BowlingService {
    public static final int TOTAL_FRAMES = 10;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    FrameRepository frameRepository;

    public GameEntity getGame(UUID id) {
        Optional<GameEntity> gameEntity = gameRepository.findById(id);
        return gameEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found", null));
    }

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
