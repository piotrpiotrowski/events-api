package org.siemasoft.restfailed.handler;

import org.siemasoft.httpproblem.Problem;
import org.siemasoft.httpproblem.ProblemDetail;
import org.siemasoft.httpproblem.ProblemDetailBuilder;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodArgumentNotValidExceptionHandler extends AbstractRestExceptionHandler<MethodArgumentNotValidException> {

    public static final String GLOBAL_ERROR_KEY = "global.error";

    public MethodArgumentNotValidExceptionHandler() {
        super(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Problem createProblem(MethodArgumentNotValidException exception, HttpServletRequest request) {
        BindingResult result = exception.getBindingResult();
        Stream<ProblemDetail> globalErrorsStream = result.getGlobalErrors()
                .stream()
                .map(this::createProblemDetailOfGlobalError);
        Stream<ProblemDetail> fieldErrorsStream = result.getFieldErrors()
                .stream()
                .map(this::createProblemDetailOfFieldError);
        List<ProblemDetail> problemDetails = Stream.concat(globalErrorsStream, fieldErrorsStream)
                .collect(Collectors.toList());
        return Problems.validationFailed(problemDetails);
    }

    private ProblemDetail createProblemDetailOfGlobalError(ObjectError objectError) {
        return new ProblemDetailBuilder()
                .withKey(GLOBAL_ERROR_KEY)
                .withMessage(objectError.getDefaultMessage())
                .build();
    }

    private ProblemDetail createProblemDetailOfFieldError(FieldError fieldError) {
        return new ProblemDetailBuilder()
                .withKey(fieldError.getCode())
                .withField(fieldError.getField())
                .withValue(fieldError.getRejectedValue())
                .withMessage(fieldError.getDefaultMessage())
                .build();
    }
}
