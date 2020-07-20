package com.bowling.bowlingscore.service;

import com.bowling.bowlingscore.jpa.FrameEntity;
import com.bowling.bowlingscore.jpa.FrameRepository;
import com.bowling.bowlingscore.jpa.GameEntity;
import com.bowling.bowlingscore.jpa.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BowlingService {
    public static final int TOTAL_FRAMES = 10;
    public static final ResponseStatusException NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found", null);

    @Autowired
    GameRepository gameRepository;

    @Autowired
    FrameRepository frameRepository;

    public GameEntity getGame(UUID id) {
        Optional<GameEntity> gameEntity = gameRepository.findById(id);
        return gameEntity.orElseThrow(() -> NOT_FOUND_EXCEPTION);
    }

    public GameEntity createGame() {
        GameEntity gameEntity = new GameEntity();
        gameEntity = gameRepository.save(gameEntity);
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            FrameEntity frameEntity = FrameEntity.builder().gameId(gameEntity.getId()).number(i).build();
            gameEntity.getFrames().add(frameEntity);
            frameRepository.save(frameEntity);
        }
        return gameEntity;
    }

    public GameEntity score(UUID id, int score) {
        // assert not last frame
        GameEntity gameEntity = getGame(id);
        scoreFrame(gameEntity, score);
        return gameEntity;
    }

    private void scoreFrame(GameEntity gameEntity, int score) {
        FrameEntity scoringFrame = gameEntity.getCurrentFrame();
        if (scoringFrame.isComplete()) {
            scoringFrame = gameEntity.getNextFrame();
        }
        scoringFrame.score(score);
        gameRepository.save(gameEntity);
//        frameRepository.save(scoringFrame);
    }
}
