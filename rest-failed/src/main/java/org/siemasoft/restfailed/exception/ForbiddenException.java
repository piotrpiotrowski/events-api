package org.siemasoft.restfailed.exception;

import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class ForbiddenException extends RestFailedException {

    public ForbiddenException(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public ForbiddenException(ProblemDetail problemDetail, Throwable cause) {
        super(problemDetail.getMessage(), cause, Problems.forbidden(Collections.singletonList(problemDetail)), HttpStatus.FORBIDDEN);
    }
}
