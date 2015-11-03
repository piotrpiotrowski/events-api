package org.siemasoft.restfailed.handler;

import org.siemasoft.httpproblem.Problem;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

public class DefaultRestExceptionHandler extends AbstractRestExceptionHandler<Exception> {

    private final Problem problem;

    public static DefaultRestExceptionHandler create(Problem problem, HttpStatus httpStatus) {
        return new DefaultRestExceptionHandler(problem, httpStatus);
    }

    public static DefaultRestExceptionHandler createForNotFound() {
        return new DefaultRestExceptionHandler(Problems.notFound(), HttpStatus.NOT_FOUND);
    }

    public static DefaultRestExceptionHandler createForNotAcceptable() {
        return new DefaultRestExceptionHandler(Problems.notAcceptable(), NOT_ACCEPTABLE);
    }

    public static DefaultRestExceptionHandler createForBadRequest() {
        return new DefaultRestExceptionHandler(Problems.badRequest(), BAD_REQUEST);
    }

    public static DefaultRestExceptionHandler createForInternalServerError() {
        return new DefaultRestExceptionHandler(Problems.internalServerError(), INTERNAL_SERVER_ERROR);
    }

    public static DefaultRestExceptionHandler createForUnprocessableEntity() {
        return new DefaultRestExceptionHandler(Problems.unprocessableEntity(), UNPROCESSABLE_ENTITY);
    }

    @Override
    public Problem createProblem(Exception ex, HttpServletRequest req) {
        return problem;
    }

    private DefaultRestExceptionHandler(Problem problem, HttpStatus httpStatus) {
        super(httpStatus);
        this.problem = problem;
    }
}

