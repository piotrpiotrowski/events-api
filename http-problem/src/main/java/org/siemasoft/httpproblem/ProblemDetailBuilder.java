package org.siemasoft.httpproblem;

import java.util.Map;

public class ProblemDetailBuilder {

    private final ProblemDetail problemDetail = new ProblemDetail();

    public ProblemDetailBuilder withMessage(String message) {
        problemDetail.setMessage(message);
        return this;
    }

    public ProblemDetailBuilder withKey(String key) {
        problemDetail.setKey(key);
        return this;
    }

    public ProblemDetailBuilder withField(String field) {
        problemDetail.setField(field);
        return this;
    }

    public ProblemDetailBuilder withValue(Object value) {
        problemDetail.setValue(value);
        return this;
    }

    public ProblemDetailBuilder withParameter(String name, String value) {
        problemDetail.getParameters().put(name, value);
        return this;
    }

    public ProblemDetailBuilder withParameters(Map<String, String> parameters) {
        if (parameters != null) {
            problemDetail.getParameters().putAll(parameters);
        }
        return this;
    }

    public ProblemDetail build() {
        return problemDetail;
    }
}
