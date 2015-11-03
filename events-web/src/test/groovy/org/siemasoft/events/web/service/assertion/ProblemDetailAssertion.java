package org.siemasoft.events.web.service.assertion;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.siemasoft.httpproblem.ProblemDetail;

public class ProblemDetailAssertion extends GenericAssert<ProblemDetailAssertion, ProblemDetail> {

    private ProblemDetailAssertion(ProblemDetail actual) {
        super(ProblemDetailAssertion.class, actual);
    }

    public static ProblemDetailAssertion assertThat(ProblemDetail actual) {
        return new ProblemDetailAssertion(actual);
    }

    public ProblemDetailAssertion hasKey(String key) {
        Assertions.assertThat(actual.getKey()).isEqualTo(key);
        return this;
    }

    public ProblemDetailAssertion hasParameter(String key, Object value) {
        Assertions.assertThat(actual.getParameters().get(key)).isEqualTo(value.toString());
        return this;
    }
}
