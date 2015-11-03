package org.siemasoft.events.core.domain.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class DomainObjectException extends RuntimeException {

    private final String key;

    private final Map<String, String> parameters = new HashMap<>();

    public DomainObjectException(String key, String message) {
        super(message);
        this.key = key;
    }

    protected void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    protected void addParameter(String name, long value) {
        addParameter(name, Long.toString(value));
    }
}
