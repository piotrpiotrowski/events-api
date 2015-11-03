package org.siemasoft.httpproblem;

import java.util.List;

public class ProblemBuilder {

    private final Problem problem = new Problem();

    public ProblemBuilder withMessage(String message) {
        problem.setMessage(message);
        return this;
    }

    public ProblemBuilder withCode(String code) {
        problem.setCode(code);
        return this;
    }

    public ProblemBuilder withDetails(List<ProblemDetail> details) {
        if (details != null) {
            problem.getDetails().addAll(details);
        }
        return this;
    }

    public Problem build() {
        return problem;
    }
}
