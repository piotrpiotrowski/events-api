package org.siemasoft.restfailed.handler

import org.siemasoft.httpproblem.Problems
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

class DefaultRestExceptionHandlerTest extends Specification {

    @Unroll
    def "should create response with #problem as body and status code: #statusCode"() {
        given:
        HttpServletRequest request = Mock(HttpServletRequest)

        when:
        def responseEntity = defaultRestExceptionHandler.handleException(new Exception(), request)

        then:
        responseEntity.body == problem
        responseEntity.statusCode == statusCode

        where:
        defaultRestExceptionHandler                                | problem                        | statusCode
        DefaultRestExceptionHandler.createForNotFound()            | Problems.notFound()            | HttpStatus.NOT_FOUND
        DefaultRestExceptionHandler.createForNotAcceptable()       | Problems.notAcceptable()       | HttpStatus.NOT_ACCEPTABLE
        DefaultRestExceptionHandler.createForBadRequest()          | Problems.badRequest()          | HttpStatus.BAD_REQUEST
        DefaultRestExceptionHandler.createForInternalServerError() | Problems.internalServerError() | HttpStatus.INTERNAL_SERVER_ERROR
        DefaultRestExceptionHandler.createForUnprocessableEntity() | Problems.unprocessableEntity() | HttpStatus.UNPROCESSABLE_ENTITY
    }
}