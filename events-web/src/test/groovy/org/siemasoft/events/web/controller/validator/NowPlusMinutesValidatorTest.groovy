package org.siemasoft.events.web.controller.validator

import org.siemasoft.events.web.controller.constrain.NowPlusMinutes
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class NowPlusMinutesValidatorTest extends Specification {

    NowPlusMinutesValidator validator = new NowPlusMinutesValidator()

    @Unroll("should return #expectedResult when given event finished date time is #givenDateTime")
    def "should return true when event started date time is greater than or equal to now plus 1 minute"() {
        given:
        NowPlusMinutes nowPlusMinutes = Mock(NowPlusMinutes)
        nowPlusMinutes.value() >> 1
        validator.initialize(nowPlusMinutes)

        expect:
        validator.isValid(givenDateTime, null) == expectedResult

        where:
        givenDateTime                                                      | expectedResult
        LocalDateTime.now()                                                | false
        null                                                               | true
        LocalDateTime.now().plusMinutes(1)                                 | true
        LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.MINUTES) | true
        LocalDateTime.now().plusMinutes(2)                                 | true
        LocalDateTime.now().plusMinutes(100)                               | true
    }

    @Unroll("should return #expectedResult when given event finished date time is #givenDateTime")
    def "should return true when event finished date time is greater than or equal to now"() {
        given:
        NowPlusMinutes nowPlusMinutes = Mock(NowPlusMinutes)
        nowPlusMinutes.value() >> 0
        validator.initialize(nowPlusMinutes)

        expect:
        validator.isValid(givenDateTime, null) == expectedResult

        where:
        givenDateTime                                                      | expectedResult
        LocalDateTime.now().minusMinutes(1)                                | false
        null                                                               | true
        LocalDateTime.now()                                                | true
        LocalDateTime.now().plusMinutes(1)                                 | true
        LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.MINUTES) | true
        LocalDateTime.now().plusMinutes(2)                                 | true
        LocalDateTime.now().plusMinutes(100)                               | true
    }
}
