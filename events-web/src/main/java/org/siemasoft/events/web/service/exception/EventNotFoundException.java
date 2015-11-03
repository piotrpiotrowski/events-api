package org.siemasoft.events.web.service.exception;

import lombok.Getter;
import org.siemasoft.restfailed.exception.NotFoundException;

@Getter
public class EventNotFoundException extends NotFoundException {

    public static final String KEY = "event.not.found";

    public static final String MESSAGE_TEMPLATE = "Event with '%s' not found";

    public EventNotFoundException(long id) {
        super(ProblemDetailFactory.createWithId(KEY, String.format(MESSAGE_TEMPLATE, id), id));
    }
}
