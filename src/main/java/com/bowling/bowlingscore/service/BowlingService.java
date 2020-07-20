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
    public static final int TOTAL_FRAMES = 11; // 10 frames plus bonus frame
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
        GameEntity gameEntity = getGame(id);
        scoreFrame(gameEntity, score);
        return gameEntity;
    }

    private void scoreFrame(GameEntity gameEntity, int score) {
        FrameEntity scoringFrame = gameEntity.getCurrentFrame();
        if (gameEntity.isFinalFrame()) {
            scoreFinalFrame(gameEntity.getFrames(), scoringFrame, score);
        } else {
            if (scoringFrame.isComplete()) {
                scoringFrame = gameEntity.getNextFrame();
            }
            scoringFrame.score(score);
        }
        gameEntity.setFinalScore(recalculateScore(gameEntity.getFrames()));
        gameRepository.save(gameEntity);
    }

    private FrameEntity scoreFinalFrame(List<FrameEntity> frames, FrameEntity finalFrame, int score) {
        if (finalFrame.hasFirstShot() == false) {
            finalFrame.setFirstShot(score);
        } else if (finalFrame.hasSecondShot() == false && finalFrame.isStrike() == false) {
            finalFrame.setSecondShot(score);
        } else if (finalFrame.isSpare() || finalFrame.isStrike() || finalFrame.getSecondShot() == FrameEntity.TOTAL_PINS) {
            finalFrame = frames.get(10);
            finalFrame.score(score);
        }
        return finalFrame;
    }

    private int recalculateScore(List<FrameEntity> frames) {
        int finalScore = 0;
        for (int i = 0; i < 10; i++) {
            FrameEntity frameEntity = frames.get(i);
            if (frameEntity.isStrike()) {
                finalScore += calculateStrikeScore(frames, i);
            } else if (frameEntity.isSpare()) {
                finalScore += calculateSpare(frames, i);
            } else {
                finalScore += frameEntity.getScore();
            }
        }
        return finalScore;
    }

    private int calculateSpare(List<FrameEntity> frames, int frameIndex) {
        FrameEntity frameEntity = frames.get(frameIndex);
        FrameEntity nextFrameEntity = frames.get(frameIndex + 1);
        return frameEntity.getScore() + nextFrameEntity.getFirstShot();
    }

    private int calculateStrikeScore(List<FrameEntity> frames, int frameIndex) {
        FrameEntity frameEntity = frames.get(frameIndex);
        int score = frameEntity.getScore();
        FrameEntity nextFrameEntity = frames.get(frameIndex + 1);
        if (nextFrameEntity.isStrike() && frameIndex < 9) {
            score += nextFrameEntity.getScore();
            FrameEntity thirdFrame = frames.get(frameIndex + 2);
            score += thirdFrame.getFirstShot();
        } else {
            score += nextFrameEntity.getScore();
        }
        return score;
    }
}
