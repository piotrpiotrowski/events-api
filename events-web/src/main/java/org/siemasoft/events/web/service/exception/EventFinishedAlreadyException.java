package org.siemasoft.events.web.service.exception;

import lombok.Getter;
import org.siemasoft.restfailed.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class EventFinishedAlreadyException extends ForbiddenException {

    public static final String KEY = "event.finished.already";

    public static final String MESSAGE_TEMPLATE = "Event id: %d finished already at (%s)";

    public EventFinishedAlreadyException(long id, LocalDateTime finished) {
        super(ProblemDetailFactory.createWithParameters(KEY, String.format(MESSAGE_TEMPLATE, id, finished), createParameters(id, finished)));
    }

    private static Map<String, String> createParameters(long id, LocalDateTime finished) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", Long.toString(id));
        parameters.put("finished", finished.toString());
        return parameters;
    }
}
