package org.siemasoft.events.web.service.exception;

import lombok.Getter;
import org.siemasoft.restfailed.exception.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class EventAlreadyExistsException extends BadRequestException {

    public static final String KEY = "event.already.exists";

    public static final String MESSAGE_TEMPLATE = "Event '%s' already exists for given time: %s";

    public EventAlreadyExistsException(String title, LocalDateTime started, DataIntegrityViolationException cause) {
        super(ProblemDetailFactory.createWithParameters(KEY, String.format(MESSAGE_TEMPLATE, title, started), createParameters(title, started)), cause);
    }

    private static Map<String, String> createParameters(String title, LocalDateTime started) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("started", started.toString());
        return parameters;
    }
}
