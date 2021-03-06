package org.siemasoft.restfailed.handler;

import lombok.extern.log4j.Log4j2;
import org.siemasoft.httpproblem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

@Log4j2
abstract class AbstractRestExceptionHandler<E extends Exception> implements RestExceptionHandler<E> {

    private final HttpStatus httpStatus;

    public abstract Problem createProblem(E exception, HttpServletRequest request);

    public AbstractRestExceptionHandler(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public ResponseEntity<Problem> handleException(E exception, HttpServletRequest request) {
        log.warn(exception.getMessage());
        logException(exception, request);
        HttpHeaders headers = createHeaders(exception, request);
        Problem body = createProblem(exception, request);
        return new ResponseEntity<>(body, headers, getHttpStatus());
    }

    protected HttpHeaders createHeaders(E exception, HttpServletRequest request) {
        return new HttpHeaders();
    }

    protected HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private void logException(Exception exception, HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (request.getQueryString() != null) {
            uri += '?' + request.getQueryString();
        }
        if (httpStatus.value() >= 500) {
            log.error("{} {} ~> {}", exception, request.getMethod(), uri, httpStatus);
        } else {
            log.debug("{} {} ~> {}", exception, request.getMethod(), uri, httpStatus);
        }
    }
}

