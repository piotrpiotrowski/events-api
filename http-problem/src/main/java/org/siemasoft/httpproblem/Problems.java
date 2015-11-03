package org.siemasoft.httpproblem;

import java.util.List;

public class Problems {

    public static Problem unsupportedMediaType() {
        return unsupportedMediaType(null);
    }

    public static Problem unsupportedMediaType(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("unsupported.media.type")
                .withMessage("UNSUPPORTED_MEDIA_TYPE")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem methodNotAllowed() {
        return methodNotAllowed(null);
    }

    public static Problem methodNotAllowed(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("method.not.allowed")
                .withMessage("METHOD_NOT_ALLOWED")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem unprocessableEntity() {
        return unprocessableEntity(null);
    }

    public static Problem unprocessableEntity(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("unprocessable.entity")
                .withMessage("UNPROCESSABLE_ENTITY")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem notFound() {
        return notFound(null);
    }

    public static Problem notFound(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("not.found")
                .withMessage("NOT_FOUND")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem notAcceptable() {
        return notAcceptable(null);
    }

    public static Problem notAcceptable(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("not.acceptable")
                .withMessage("NOT_ACCEPTABLE")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem badRequest() {
        return badRequest(null);
    }

    public static Problem badRequest(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("bad.request")
                .withMessage("BAD_REQUEST")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem internalServerError() {
        return internalServerError(null);
    }

    public static Problem internalServerError(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("internal.server.error")
                .withMessage("INTERNAL_SERVER_ERROR")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem validationFailed(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("validation.failed")
                .withMessage("VALIDATION_FAILED")
                .withDetails(problemDetails)
                .build();
    }

    public static Problem forbidden(List<ProblemDetail> problemDetails) {
        return new ProblemBuilder()
                .withCode("forbidden.access")
                .withMessage("FORBIDDEN_ACCESS")
                .withDetails(problemDetails)
                .build();
    }
}