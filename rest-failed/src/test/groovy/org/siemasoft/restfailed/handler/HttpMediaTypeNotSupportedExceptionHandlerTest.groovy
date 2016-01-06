package org.siemasoft.restfailed.handler
import org.siemasoft.httpproblem.Problems
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.HttpMediaTypeNotSupportedException
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class HttpMediaTypeNotSupportedExceptionHandlerTest extends Specification {

    HttpMediaTypeNotSupportedExceptionHandler httpMediaTypeNotSupportedExceptionHandler = new HttpMediaTypeNotSupportedExceptionHandler()

    def "should create response with problem unsupportedMediaType as body and status code: UNSUPPORTED_MEDIA_TYPE"() {
        given:
        def exception = new HttpMediaTypeNotSupportedException("exception")
        def request = Mock(HttpServletRequest)

        when:
        def responseEntity = httpMediaTypeNotSupportedExceptionHandler.handleException(exception, request)

        then:
        responseEntity.body == Problems.unsupportedMediaType()
        responseEntity.statusCode == HttpStatus.UNSUPPORTED_MEDIA_TYPE
    }


    def "should create response with problem unsupportedMediaType as body and status code: UNSUPPORTED_MEDIA_TYPE and header ACCEPT"() {
        given:
        def exception = new HttpMediaTypeNotSupportedException(MediaType.APPLICATION_OCTET_STREAM, Collections.singletonList(MediaType.APPLICATION_JSON))
        def request = Mock(HttpServletRequest)

        when:
        def responseEntity = httpMediaTypeNotSupportedExceptionHandler.handleException(exception, request)

        then:
        responseEntity.body == Problems.unsupportedMediaType()
        responseEntity.statusCode == HttpStatus.UNSUPPORTED_MEDIA_TYPE
        responseEntity.headers != null
        responseEntity.headers[HttpHeaders.ACCEPT] != null
        responseEntity.headers[HttpHeaders.ACCEPT][0] == MediaType.APPLICATION_JSON_VALUE
    }
}
