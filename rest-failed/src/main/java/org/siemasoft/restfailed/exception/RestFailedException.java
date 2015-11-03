package org.siemasoft.restfailed.exception;

import lombok.Getter;
import org.siemasoft.httpproblem.Problem;
import org.springframework.http.HttpStatus;

@Getter
public class RestFailedException extends RuntimeException {

    private final Problem problem;

    private final HttpStatus httpStatus;

    public RestFailedException(String message, Throwable cause, Problem problem, HttpStatus httpStatus) {
        super(resolveMessage(message, cause), cause);
        this.problem = problem;
        this.httpStatus = httpStatus;
    }

    private static String resolveMessage(String message, Throwable cause) {
        return cause == null ? message : message + ":" + cause.getMessage();
    }
}
