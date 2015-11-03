package org.siemasoft.restfailed.handler;

import org.siemasoft.httpproblem.Problem;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface RestExceptionHandler<E extends Exception> {

    ResponseEntity<Problem> handleException(E exception, HttpServletRequest request);
}
