package org.siemasoft.events.web.controller.validator.smart

import org.siemasoft.events.web.controller.group.Create
import org.siemasoft.events.web.controller.group.Update
import org.siemasoft.events.web.model.EventModel
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class EventModelValidatorTest extends Specification {

    static def ErrorCodes = EventModelValidator.EventModelErrorCodes

    EventModelValidator validator

    def setup() {
        validator = new EventModelValidator()
        validator.errorsAppender = new ErrorsAppender()
    }

    def "should report validation error when finished date before started date"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.started = LocalDateTime.now()
        eventModel.finished = eventModel.started.minusMinutes(10)

        Errors errors = new BeanPropertyBindingResult(eventModel, "eventModel")

        when:
        validator.validate(eventModel, errors, Update.class)

        then:
        errors.getErrorCount() == 1

        def fieldError = errors.getFieldError(ErrorCodes.FINISHED_BEFORE_STARTED.getField())

        fieldError != null
        fieldError.getCode() == ErrorCodes.FINISHED_BEFORE_STARTED.getCode()
        fieldError.getDefaultMessage() == ErrorCodes.FINISHED_BEFORE_STARTED.getDefaultMessage()
    }

    def "should pass validation when group Create.class"() {
        given:
        EventModel eventModel = new EventModel()

        Errors errors = new BeanPropertyBindingResult(eventModel, "eventModel")

        when:
        validator.validate(eventModel, errors, Create.class)

        then:
        errors.getErrorCount() == 0
    }

    @Unroll
    def "should pass validation when #caseName"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.started = started
        eventModel.finished = finished

        Errors errors = new BeanPropertyBindingResult(eventModel, "eventModel")

        when:
        validator.validate(eventModel, errors, Update.class)

        then:
        errors.getErrorCount() == 0

        where:
        started             | finished            | caseName
        null                | null                | "started date and finished date are null"
        null                | LocalDateTime.now() | "started date is null"
        LocalDateTime.now() | null                | "finished date is null"
        LocalDateTime.now() | LocalDateTime.now() | "started date is before finished date"

    }
}
