package com.bowling.bowlingscore

import com.bowling.bowlingscore.api.Game
import com.bowling.bowlingscore.jpa.FrameRepository
import com.bowling.bowlingscore.jpa.GameRepository
import com.bowling.bowlingscore.resource.BowlingResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
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

        when:
        Game result = bowlingResource.getGame(game.id)

        then:
        result.id == game.id
        result.finalScore == 0
        result.frames.size() == 10
    }

    def 'Should get a not found exception when requesting a game that doesnt exist'() {
        when:
        bowlingResource.getGame(UUID.randomUUID())

        then:
        ResponseStatusException ex = thrown(ResponseStatusException)
        ex.status == HttpStatus.NOT_FOUND
    }
}
