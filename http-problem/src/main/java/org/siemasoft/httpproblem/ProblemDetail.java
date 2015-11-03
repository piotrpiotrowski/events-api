package org.siemasoft.httpproblem;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ProblemDetail {

    private String key;

    private String message;

    private String field;

    private Object value;

    private Map<String, String> parameters = new HashMap<>();
}
