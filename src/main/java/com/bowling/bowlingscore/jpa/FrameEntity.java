package com.bowling.bowlingscore.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    UUID gameId;
    Integer firstShot;
    Integer secondShot;
    Integer score;

    @ManyToOne
    @JoinColumn(name = "gameId", insertable = false, updatable = false)
    GameEntity gameEntity;
}

