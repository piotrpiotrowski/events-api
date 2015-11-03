package org.siemasoft.restfailed.handler;

import org.siemasoft.httpproblem.Problem;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestMethodNotSupportedExceptionHandler extends AbstractRestExceptionHandler<HttpRequestMethodNotSupportedException> {

    public HttpRequestMethodNotSupportedExceptionHandler() {
        super(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public Problem createProblem(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        return Problems.methodNotAllowed();
    }

    @Override
    public HttpHeaders createHeaders(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        HttpHeaders headers = super.createHeaders(exception, request);
        if (!StringUtils.isEmpty(exception.getSupportedMethods())) {
            headers.setAllow(exception.getSupportedHttpMethods());
        }
        return headers;
    }
}
