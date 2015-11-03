package org.siemasoft.restfailed.exception;

import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class InternalServerException extends RestFailedException {

    public InternalServerException(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public InternalServerException(ProblemDetail problemDetail, Throwable cause) {
        super(problemDetail.getMessage(), cause, Problems.internalServerError(Collections.singletonList(problemDetail)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
