package com.bowling.bowlingscore.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "frame")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrameEntity {
    public static final int TOTAL_PINS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    UUID gameId;
    Integer number;
    Integer firstShot;
    Integer secondShot;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "gameId", insertable = false, updatable = false)
    GameEntity gameEntity;

    public Integer getFirstShot() {
        return ObjectUtils.defaultIfNull(firstShot, 0);
    }

    public Integer getSecondShot() {
        return ObjectUtils.defaultIfNull(secondShot, 0);
    }

    public void score(int score) {
        if (firstShot == null) {
            firstShot = score;
        } else {
            secondShot = score;
        }
    }

    public int getScore() {
        return getFirstShot() + getSecondShot();
    }

    public boolean isComplete() {
        return (firstShot != null && secondShot != null) || isStrike();
    }

    public boolean isSpare() {
        return firstShot != null && secondShot != null && firstShot + secondShot == TOTAL_PINS;
    }

    public boolean isStrike() {
        return firstShot != null && firstShot == TOTAL_PINS;
    }
}

