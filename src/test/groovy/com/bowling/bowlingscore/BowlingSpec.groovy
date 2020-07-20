package com.bowling.bowlingscore

import com.bowling.bowlingscore.api.Game
import com.bowling.bowlingscore.jpa.FrameEntity
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

    Random aRandom = new Random()

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

    def 'Should be able to score consecutive open frames'() {
        given:
        Game game = bowlingResource.createGame()

        when:
        int firstScore = aRandom.nextInt(4)
        int secondScore = aRandom.nextInt(4)
        int thirdScore = aRandom.nextInt(4)
        bowlingResource.score(game.id, firstScore)
        bowlingResource.score(game.id, secondScore)
        bowlingResource.score(game.id, thirdScore)

        then:
        FrameEntity firstResult = frameRepository.findById(game.getFrames()[0].id).get()
        FrameEntity secondResult = frameRepository.findById(game.getFrames()[1].id).get()
        firstResult.firstShot == firstScore
        firstResult.secondShot == secondScore
        firstResult.getScore() == firstScore + secondScore
        firstResult.isStrike() == false
        firstResult.isSpare() == false
        secondResult.firstShot == thirdScore
    }

    def 'Should be able to score a spare'() {
        given:
        Game game = bowlingResource.createGame()

        when:
        int firstScore = aRandom.nextInt(10)
        int secondScore = 10 - firstScore
        bowlingResource.score(game.id, firstScore)
        bowlingResource.score(game.id, secondScore)

        then:
        FrameEntity result = frameRepository.findById(game.getFrames()[0].id).get()
        result.firstShot == firstScore
        result.secondShot == secondScore
        result.getScore() == firstScore + secondScore
        result.isStrike() == false
        result.isSpare() == true
    }

    def 'Should be able to score consecutive strikes'() {
        given:
        Game game = bowlingResource.createGame()

        when:
        bowlingResource.score(game.id, 10)
        bowlingResource.score(game.id, 10)

        then:
        FrameEntity result1 = frameRepository.findById(game.getFrames()[0].id).get()
        FrameEntity result2 = frameRepository.findById(game.getFrames()[0].id).get()
        result1.firstShot == 10
        result1.getScore() == 10
        result1.isStrike() == true
        result2.firstShot == 10
        result2.getScore() == 10
        result2.isStrike() == true
    }
}
