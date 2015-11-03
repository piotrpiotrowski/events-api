package org.siemasoft.restfailed.exception;

import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class NoFoundException extends RestFailedException {

    public NoFoundException(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public NoFoundException(ProblemDetail problemDetail, Throwable cause) {
        super(problemDetail.getMessage(), cause, Problems.notFound(Collections.singletonList(problemDetail)), HttpStatus.NOT_FOUND);
    }
}
