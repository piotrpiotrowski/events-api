package org.siemasoft.restfailed.handler

import org.siemasoft.httpproblem.Problems
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.HttpRequestMethodNotSupportedException
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class HttpRequestMethodNotSupportedExceptionHandlerTest extends Specification {

    HttpRequestMethodNotSupportedExceptionHandler httpRequestMethodNotSupportedExceptionHandler = new HttpRequestMethodNotSupportedExceptionHandler()

    def "should create response with problem methodNotAllowed as body and status code: METHOD_NOT_ALLOWED"() {
        given:
        def exception = new HttpRequestMethodNotSupportedException("HEAD")
        def request = Mock(HttpServletRequest)

        when:
        def responseEntity = httpRequestMethodNotSupportedExceptionHandler.handleException(exception, request)

        then:
        responseEntity.body == Problems.methodNotAllowed()
        responseEntity.statusCode == HttpStatus.METHOD_NOT_ALLOWED
    }

    def "should create response with problem methodNotAllowed as body and status code: METHOD_NOT_ALLOWED and header ALLOW"() {
        given:
        def supportedMethods = "GET"

        def exception = new HttpRequestMethodNotSupportedException("HEAD", [supportedMethods])
        def request = Mock(HttpServletRequest)

        when:
        def responseEntity = httpRequestMethodNotSupportedExceptionHandler.handleException(exception, request)

        then:
        responseEntity.body == Problems.methodNotAllowed()
        responseEntity.statusCode == HttpStatus.METHOD_NOT_ALLOWED
        responseEntity.headers != null
        responseEntity.headers[HttpHeaders.ALLOW] != null
        responseEntity.headers[HttpHeaders.ALLOW][0] == supportedMethods
    }
}
