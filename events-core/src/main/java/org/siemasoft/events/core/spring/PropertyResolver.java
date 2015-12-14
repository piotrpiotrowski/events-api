package org.siemasoft.events.core.spring;

import org.springframework.stereotype.Component;

@Component
public class PropertyResolver {

    public String getRequiredProperty(String key) {
        String property = System.getenv(key);
        if (property == null) {
            throw new IllegalArgumentException("Provide required environment variable: " + key);
        }
        return property;
    }
}
