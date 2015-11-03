package org.siemasoft.events.web.service.exception;

import org.siemasoft.restfailed.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DateChangeForbiddenException extends ForbiddenException {

    public static final String KEY = "date.change.forbidden";

    public DateChangeForbiddenException(String message, String field, LocalDateTime dateTime) {
        super(ProblemDetailFactory.createWithParameters(KEY, message, createParameters(field, dateTime)));
    }

    private static Map<String, String> createParameters(String field, LocalDateTime dateTime) {
        String dateTimeAsString = dateTime == null ? null : dateTime.toString();
        Map<String, String> parameters = new HashMap<>();
        parameters.put(field, dateTimeAsString);
        return parameters;
    }
}
