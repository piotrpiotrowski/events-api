package org.siemasoft.restfailed.handler;

import org.siemasoft.httpproblem.Problem;
import org.siemasoft.restfailed.exception.RestFailedException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public class RestFailedExceptionHandler implements RestExceptionHandler<RestFailedException> {

    @Override
    public ResponseEntity<Problem> handleException(RestFailedException exception, HttpServletRequest request) {
        return new ResponseEntity<>(exception.getProblem(), exception.getHttpStatus());
    }
}
