package com.bowling.bowlingscore.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FrameRepository extends CrudRepository<FrameEntity, UUID> {
}
