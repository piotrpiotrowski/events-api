package org.siemasoft.restfailed.exception;

import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class BadRequestException extends RestFailedException {

    public BadRequestException(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public BadRequestException(ProblemDetail problemDetail, Throwable cause) {
        super(problemDetail.getMessage(), cause, Problems.badRequest(Collections.singletonList(problemDetail)), HttpStatus.BAD_REQUEST);
    }
}
