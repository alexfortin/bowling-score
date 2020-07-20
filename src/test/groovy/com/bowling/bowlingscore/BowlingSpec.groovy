package com.bowling.bowlingscore

import com.bowling.bowlingscore.api.Game
import com.bowling.bowlingscore.jpa.FrameRepository
import com.bowling.bowlingscore.jpa.GameEntity
import com.bowling.bowlingscore.jpa.GameRepository
import com.bowling.bowlingscore.resource.BowlingResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class BowlingSpec extends Specification {
    @Autowired
    GameRepository gameRepository

    @Autowired
    FrameRepository frameRepository

    @Autowired
    BowlingResource bowlingResource

    def 'Should be able to create new game'() {
        given:
        Game game = bowlingResource.createGame()

        expect:
        GameEntity result = gameRepository.findById(game.id).get()
        result.id == game.id
        result.finalScore == 0
        result.frames.size() == 10
    }
}
