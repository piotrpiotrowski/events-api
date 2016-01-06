package org.siemasoft.restfailed.handler;

import org.siemasoft.httpproblem.Problem;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

public class DefaultRestExceptionHandler extends AbstractRestExceptionHandler<Exception> {

    private final Problem problem;

    public static DefaultRestExceptionHandler createForNotFound() {
        return create(Problems.notFound(), HttpStatus.NOT_FOUND);
    }

    public static DefaultRestExceptionHandler createForNotAcceptable() {
        return create(Problems.notAcceptable(), NOT_ACCEPTABLE);
    }

    public static DefaultRestExceptionHandler createForBadRequest() {
        return create(Problems.badRequest(), BAD_REQUEST);
    }

    public static DefaultRestExceptionHandler createForInternalServerError() {
        return create(Problems.internalServerError(), INTERNAL_SERVER_ERROR);
    }

    public static DefaultRestExceptionHandler createForUnprocessableEntity() {
        return create(Problems.unprocessableEntity(), UNPROCESSABLE_ENTITY);
    }

    public static DefaultRestExceptionHandler create(Problem problem, HttpStatus httpStatus) {
        return new DefaultRestExceptionHandler(problem, httpStatus);
    }

    private DefaultRestExceptionHandler(Problem problem, HttpStatus httpStatus) {
        super(httpStatus);
        this.problem = problem;
    }

    @Override
    public Problem createProblem(Exception ex, HttpServletRequest req) {
        return problem;
    }
}

