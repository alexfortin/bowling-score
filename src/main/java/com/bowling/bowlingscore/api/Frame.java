package com.bowling.bowlingscore.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Frame {
    UUID id;
    Integer firstShot;
    Integer secondShot;
    Integer score;
}

