package org.siemasoft.restfailed.handler;

import org.siemasoft.httpproblem.Problem;
import org.siemasoft.httpproblem.Problems;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class HttpMediaTypeNotSupportedExceptionHandler extends AbstractRestExceptionHandler<HttpMediaTypeNotSupportedException> {

    public HttpMediaTypeNotSupportedExceptionHandler() {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    public Problem createProblem(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        return Problems.unsupportedMediaType();
    }

    @Override
    public HttpHeaders createHeaders(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        HttpHeaders headers = super.createHeaders(exception, request);
        List<MediaType> mediaTypes = exception.getSupportedMediaTypes();
        if (CollectionUtils.isEmpty(mediaTypes)) {
            return headers;
        }
        headers.setAccept(mediaTypes);
        return headers;
    }
}
