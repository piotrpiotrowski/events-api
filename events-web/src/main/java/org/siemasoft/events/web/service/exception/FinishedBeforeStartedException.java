package org.siemasoft.events.web.service.exception;

import lombok.Getter;
import org.siemasoft.restfailed.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class FinishedBeforeStartedException extends BadRequestException {

    public static final String KEY = "event.finished.before.started";

    public static final String MESSAGE_TEMPLATE = "Event (id:%d) finished date (%s) before started date (%s)";

    public FinishedBeforeStartedException(long id, LocalDateTime finished, LocalDateTime started) {
        super(ProblemDetailFactory.createWithParameters(KEY, String.format(MESSAGE_TEMPLATE, id, started, finished), createParameters(id, started, finished)));
    }

    private static Map<String, String> createParameters(long id, LocalDateTime started, LocalDateTime finished) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", Long.toString(id));
        parameters.put("started", started.toString());
        parameters.put("finished", finished.toString());
        return parameters;
    }
}
