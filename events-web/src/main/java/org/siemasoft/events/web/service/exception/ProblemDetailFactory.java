package org.siemasoft.events.web.service.exception;

import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.ProblemDetailBuilder;

import java.util.Map;

final class ProblemDetailFactory {

    private ProblemDetailFactory() {
    }

    public static ProblemDetail createWithId(String key, String message, long id) {
        return new ProblemDetailBuilder()
                .withKey(key)
                .withMessage(message)
                .withParameter("id", Long.toString(id))
                .build();
    }

    public static ProblemDetail createWithParameters(String key, String message, Map<String, String> parameters) {
        return new ProblemDetailBuilder()
                .withKey(key)
                .withMessage(message)
                .withParameters(parameters)
                .build();
    }
}
