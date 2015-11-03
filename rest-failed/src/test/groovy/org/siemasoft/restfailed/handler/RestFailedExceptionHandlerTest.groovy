package org.siemasoft.restfailed.handler

import org.siemasoft.httpproblem.ProblemDetail
import org.siemasoft.restfailed.exception.BadRequestException
import org.siemasoft.restfailed.exception.ForbiddenException
import org.siemasoft.restfailed.exception.InternalServerException
import org.siemasoft.restfailed.exception.NotFoundException
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

class RestFailedExceptionHandlerTest extends Specification {

    @Shared
    ProblemDetail problemDetail = new ProblemDetail()

    RestFailedExceptionHandler restFailedExceptionHandler = new RestFailedExceptionHandler()

    @Unroll("should create response with #exception.problem as body and status code: #statusCode")
    def "should create response with problem as body and status code"() {
        given:
        HttpServletRequest request = Mock(HttpServletRequest)

        when:
        def responseEntity = restFailedExceptionHandler.handleException(exception, request)

        then:
        responseEntity.body == exception.problem
        responseEntity.statusCode == statusCode

        where:
        exception                                  | statusCode
        new BadRequestException(problemDetail)     | HttpStatus.BAD_REQUEST
        new ForbiddenException(problemDetail)      | HttpStatus.FORBIDDEN
        new NotFoundException(problemDetail)       | HttpStatus.NOT_FOUND
        new InternalServerException(problemDetail) | HttpStatus.INTERNAL_SERVER_ERROR
    }
}
