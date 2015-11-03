package org.siemasoft.events.web.controller;

import org.siemasoft.events.core.domain.LocalDateRange;
import org.siemasoft.events.web.controller.group.Create;
import org.siemasoft.events.web.controller.group.Update;
import org.siemasoft.events.web.controller.validator.smart.EventModelValidator;
import org.siemasoft.events.web.model.EventModel;
import org.siemasoft.events.web.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class EventController {

    public static final String LOCATION = "location";

    @Autowired
    private EventService eventService;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private EventModelValidator eventModelValidator;

    @InitBinder("eventModel")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(eventModelValidator);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = URL.EVENTS, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventModel create(@RequestBody @Validated(Create.class) EventModel eventModel) {
        EventModel createdEvent = eventService.create(eventModel);
        response.setHeader(LOCATION, createdEvent.getResourceLink());
        return createdEvent;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = URL.EVENTS, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated(Update.class) EventModel eventModel) {
        eventService.update(eventModel);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = URL.EVENTS_BY_DAY, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventModel> getByDay(@PathVariable(Params.YEAR) int year,
                                     @PathVariable(Params.MONTH) int month,
                                     @PathVariable(Params.DAY) int day) {
        LocalDateRange range = LocalDateRange.ofDay(year, month, day);
        Page<EventModel> page = eventService.getFromRange(range, new PageRequest(0, Integer.MAX_VALUE));
        return page.getContent();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = URL.EVENT_BY_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventModel getById(@PathVariable(Params.EVENT_ID) int eventId) {
        return eventService.getById(eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = URL.OVERDUE_EVENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<EventModel> getByOverdue(Pageable pageable) {
        return eventService.getOverdue(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = URL.EVENTS_BY_MONTH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<EventModel> getByMonth(@PathVariable(Params.YEAR) int year,
                                       @PathVariable(Params.MONTH) int month,
                                       Pageable pageable) {
        LocalDateRange range = LocalDateRange.ofMonth(year, month);
        return eventService.getFromRange(range, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = URL.EVENTS_BY_WEEK, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<EventModel> getByWeek(@PathVariable(Params.YEAR) int year,
                                      @PathVariable(Params.WEEK) int week,
                                      Pageable pageable) {
        LocalDateRange range = LocalDateRange.ofWeek(year, week);
        return eventService.getFromRange(range, pageable);
    }
}
