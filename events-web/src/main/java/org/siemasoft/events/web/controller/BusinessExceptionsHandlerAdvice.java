package org.siemasoft.events.web.controller;

import org.siemasoft.events.core.domain.exception.DomainObjectException;
import org.siemasoft.httpproblem.Problem;
import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.ProblemDetailBuilder;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

@ControllerAdvice
public class BusinessExceptionsHandlerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainObjectException.class)
    public Problem handleValidationExceptions(DomainObjectException exception) {
        ProblemDetail problemDetail = new ProblemDetailBuilder()
                .withKey(exception.getKey())
                .withMessage(exception.getMessage())
                .withParameters(exception.getParameters())
                .build();
        return Problems.badRequest(Collections.singletonList(problemDetail));
    }
}
