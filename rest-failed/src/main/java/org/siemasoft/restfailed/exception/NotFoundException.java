package org.siemasoft.restfailed.exception;

import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class NotFoundException extends RestFailedException {

    public NotFoundException(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public NotFoundException(ProblemDetail problemDetail, Throwable cause) {
        super(problemDetail.getMessage(), cause, Problems.notFound(Collections.singletonList(problemDetail)), HttpStatus.NOT_FOUND);
    }
}
