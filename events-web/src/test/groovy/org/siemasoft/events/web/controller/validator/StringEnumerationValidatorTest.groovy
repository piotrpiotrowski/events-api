package org.siemasoft.events.web.controller.validator

import org.siemasoft.events.core.domain.EventPriority
import org.siemasoft.events.web.controller.constrain.StringEnumeration
import spock.lang.Specification
import spock.lang.Unroll

class StringEnumerationValidatorTest extends Specification {

    StringEnumerationValidator validator = new StringEnumerationValidator();

    @Unroll
    def "should return #expectedResult when try to build priority with #givenPriorityValue"() {
        given:
        StringEnumeration stringEnumeration = Mock(StringEnumeration)
        stringEnumeration.value() >> EventPriority.class

        validator.initialize(stringEnumeration)

        expect:
        validator.isValid(givenPriorityValue, null) == expectedResult

        where:
        givenPriorityValue          | expectedResult
        "siema"                     | false
        null                        | true
        EventPriority.Low.name()    | true
        EventPriority.Medium.name() | true
        EventPriority.High.name()   | true
    }
}
