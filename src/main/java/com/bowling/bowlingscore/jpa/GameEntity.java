package com.bowling.bowlingscore.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "game")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Builder.Default
    Integer finalScore = 0;
    @Builder.Default
    Integer currentFrameNumber = 0;
    @Builder.Default
    @OneToMany(fetch = EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "gameId")
    List<FrameEntity> frames = new ArrayList<>();

    public FrameEntity getCurrentFrame() {
        return frames.get(currentFrameNumber);
    }

    public boolean isFinalFrame() {
        return currentFrameNumber == 9;
    }

    public FrameEntity getNextFrame() {
        if (isFinalFrame()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        currentFrameNumber++;
        return frames.get(currentFrameNumber);
    }
}

