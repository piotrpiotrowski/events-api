package org.siemasoft.events.web.service

import org.siemasoft.events.core.domain.EventPriority
import org.siemasoft.events.core.domain.LocalDateRange
import org.siemasoft.events.core.repository.EventRepository
import org.siemasoft.events.web.model.EventModel
import org.siemasoft.events.web.service.exception.*
import org.siemasoft.events.web.spring.WebConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.OptimisticLockException
import java.time.LocalDateTime

import static org.siemasoft.events.web.service.assertion.ProblemDetailAssertion.assertThat

@WebAppConfiguration
@ActiveProfiles("local")
@ContextConfiguration(classes = WebConfiguration.class)
class EventServiceTest extends Specification {

    @Autowired
    EventService eventService

    @Autowired
    EventRepository eventRepository

    def cleanup() {
        eventRepository.deleteAll()
    }

    def "should get events for given range ordered by started date"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 19, 0, 0)
        eventModel.priority = EventPriority.High
        def event0InRange = eventService.create(eventModel)

        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        def event1InRange = eventService.create(eventModel)

        eventModel.started = LocalDateTime.of(2015, 9, 5, 19, 0, 0)
        eventService.create(eventModel)

        eventModel.started = LocalDateTime.of(2015, 9, 7, 19, 0, 0)
        eventService.create(eventModel)

        def givenRange = LocalDateRange.ofDay(2015, 9, 6)

        when:
        def events = eventService.getFromRange(givenRange, new PageRequest(0, Integer.MAX_VALUE))

        then:
        events != null
        events.size() == 2
        events[0] == event0InRange
        events[1] == event1InRange
    }

    def "should create event"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High

        when:
        def createdEvent = eventService.create(eventModel)

        then:
        createdEvent != null
        createdEvent.id > 0
        createdEvent.title == eventModel.title
        createdEvent.priority == eventModel.priority
        createdEvent.started == eventModel.started
    }

    def "should throws EventAlreadyExistsException when event with title and startDate exists"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High

        when:
        eventService.create(eventModel)
        eventService.create(eventModel)

        then:
        def exception = thrown(EventAlreadyExistsException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(EventAlreadyExistsException.KEY)
                .hasParameter("title", eventModel.title)
                .hasParameter("started", eventModel.started)
        exception.httpStatus == HttpStatus.BAD_REQUEST
    }

    def "should throw EventNotFoundException when event has been removed during create"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High


        def eventRepositoryMock = Mock(EventRepository)
        eventRepositoryMock.saveAndFlush(_) >> { throw new OptimisticLockException() }
        eventService.eventRepository = eventRepositoryMock

        when:
        eventService.create(eventModel)

        then:
        def exception = thrown(EventNotFoundException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(EventNotFoundException.KEY)
                .hasParameter("id", eventModel.id)
        exception.httpStatus == HttpStatus.NOT_FOUND

        cleanup:
        eventService.eventRepository = eventRepository
    }

    @Unroll("should throws EventNotFoundException when event does not exist in method: #method")
    def "should throws EventNotFoundException when event does not exist"() {
        when:
        eventService."$method"(* params)

        then:
        def exception = thrown(EventNotFoundException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(EventNotFoundException.KEY)
                .hasParameter("id", expectedNotExistedEventId)
        exception.httpStatus == HttpStatus.NOT_FOUND

        where:
        method    | params                    | expectedNotExistedEventId
        "getById" | [100]                     | "100"
        "update"  | [[id: 100] as EventModel] | "100"
    }


    def "should get event by id"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High

        def createdEvent = eventService.create(eventModel)

        when:
        EventModel foundEvent = eventService.getById(createdEvent.id)

        then:
        foundEvent != null
        foundEvent == createdEvent
    }

    def "should throws EventAlreadyExistsException when update to existing event"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event 1 title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High
        def createdEvent1 = eventService.create(eventModel)

        eventModel.title = "event 2 title"
        def createdEvent2 = eventService.create(eventModel)

        createdEvent1.title = createdEvent2.title

        when:
        eventService.update(createdEvent1)

        then:
        def exception = thrown(EventAlreadyExistsException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(EventAlreadyExistsException.KEY)
                .hasParameter("title", eventModel.title)
                .hasParameter("started", eventModel.started)
        exception.httpStatus == HttpStatus.BAD_REQUEST
    }

    def "should throws EventFinishedAlreadyException when update event is finished"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event 1 title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High
        def createdEvent = eventService.create(eventModel)

        eventModel.id = createdEvent.id

        def eventFinishedDate = eventModel.started.plusMinutes(12)
        eventModel.finished = eventFinishedDate
        eventService.update(eventModel)

        eventModel.finished = eventFinishedDate.plusMinutes(20)

        when:
        eventService.update(eventModel)

        then:
        def exception = thrown(EventFinishedAlreadyException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(EventFinishedAlreadyException.KEY)
                .hasParameter("id", eventModel.id)
                .hasParameter("finished", eventFinishedDate)
        exception.httpStatus == HttpStatus.FORBIDDEN
    }

    def "should throw EventNotFoundException when event has been removed during update"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High
        def createdEvent = eventService.create(eventModel)

        def eventRepositoryMock = Mock(EventRepository)
        eventRepositoryMock.saveAndFlush(_) >> { throw new OptimisticLockException() }
        eventService.eventRepository = eventRepositoryMock

        eventModel.id = createdEvent.id
        eventModel.title = "other event title"

        when:
        eventService.update(eventModel)

        then:
        def exception = thrown(EventNotFoundException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(EventNotFoundException.KEY)
                .hasParameter("id", eventModel.id)
        exception.httpStatus == HttpStatus.NOT_FOUND

        cleanup:
        eventService.eventRepository = eventRepository
    }

    def "should throw FinishedBeforeStartedException"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High

        def createdEvent = eventService.create(eventModel)
        eventModel.id = createdEvent.id
        eventModel.title = "other event title"
        eventModel.finished = createdEvent.started.minusMinutes(10)

        when:
        eventService.update(eventModel)

        then:
        def exception = thrown(FinishedBeforeStartedException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(FinishedBeforeStartedException.KEY)
                .hasParameter("id", eventModel.id)
                .hasParameter("started", eventModel.started)
                .hasParameter("finished", eventModel.finished)
        exception.httpStatus == HttpStatus.BAD_REQUEST
    }

    def "should throw DateChangeForbiddenException"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High

        def createdEvent = eventService.create(eventModel)
        eventModel.id = createdEvent.id
        eventModel.title = "other event title"
        eventModel.finished = createdEvent.started.plusMinutes(10)
        eventModel.started = eventModel.finished

        when:
        eventService.update(eventModel)

        then:
        def exception = thrown(DateChangeForbiddenException)
        def problem = exception.problem

        problem != null
        problem.details != null
        problem.details.size() == 1
        assertThat(problem.details[0])
                .isNotNull()
                .hasKey(DateChangeForbiddenException.KEY)
                .hasParameter("started", eventModel.started)
        exception.httpStatus == HttpStatus.FORBIDDEN
    }

    @Unroll("should update event field: #fieldName")
    def "should update event"() {
        given:
        EventModel eventModel = new EventModel()
        eventModel.title = "event title"
        eventModel.started = LocalDateTime.of(2015, 9, 6, 20, 0, 0)
        eventModel.priority = EventPriority.High
        eventModel.description = "event description"

        def createdEvent = eventService.create(eventModel)
        eventModel.id = createdEvent.id
        eventModel[fieldName] = fieldValue

        when:
        eventService.update(eventModel)

        then:
        def updatedEvent = eventRepository.findOne(eventModel.id)

        updatedEvent != null
        updatedEvent.id == eventModel.id
        updatedEvent.started == eventModel.started
        updatedEvent.title == eventModel.title
        updatedEvent.priority == eventModel.getPriorityAsEnum()
        updatedEvent.description == eventModel.description

        where:
        fieldName     | fieldValue
        "title"       | "other event title"
        "priority"    | "Low"
        "description" | "new event description"
        "started"     | LocalDateTime.of(2015, 10, 6, 20, 0, 0)
        "finished"    | LocalDateTime.of(2015, 10, 6, 20, 0, 0)
    }

    def "should get overdue events"() {
        given:
        EventModel overdueEvent1 = new EventModel()
        overdueEvent1.title = "overdue event title 1"
        overdueEvent1.started = LocalDateTime.of(2015, 9, 6, 19, 0, 0)
        overdueEvent1.priority = EventPriority.High
        overdueEvent1 = eventService.create(overdueEvent1)

        EventModel overdueEvent2 = new EventModel()
        overdueEvent2.title = "overdue event title 2"
        overdueEvent2.started = LocalDateTime.of(2015, 9, 7, 19, 0, 0)
        overdueEvent2.priority = EventPriority.High
        overdueEvent2 = eventService.create(overdueEvent2)

        overdueEvent2.finished = LocalDateTime.of(2015, 9, 9, 19, 0, 0)
        eventService.update(overdueEvent2)

        EventModel event = new EventModel()
        event.title = "event title"
        event.started = LocalDateTime.now().plusDays(1)
        event.priority = EventPriority.High
        eventService.create(event)

        when:
        def page = eventService.getOverdue(new PageRequest(0, 3))

        then:
        page != null
        page.number == 0
        page.numberOfElements == 1

        def events = page.content
        events[0] == overdueEvent1
    }
}