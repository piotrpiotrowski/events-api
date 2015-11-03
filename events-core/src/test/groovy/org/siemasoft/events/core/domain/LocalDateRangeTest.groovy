package org.siemasoft.events.core.domain

import org.siemasoft.events.core.domain.exception.IllegalDateComponentsException
import org.siemasoft.events.core.domain.exception.IllegalWeekNumberException
import spock.lang.Specification

import java.time.LocalDateTime


class LocalDateRangeTest extends Specification {

    def "should create LocalDateRange of one day"() {
        given:
        def year = 2015;
        def month = 10;
        def day = 1;

        when:
        def range = LocalDateRange.ofDay(year, month, day)

        then:
        range != null
        range.getFrom() == LocalDateTime.of(year, month, day, 0, 0)
        range.getTo() == LocalDateTime.of(year, month, day, 23, 59, 59, 999999999)
    }

    def "should throws IllegalDateComponentsException when day not exist"() {
        when:
        LocalDateRange.ofDay(year, month, day)

        then:
        def e = thrown(IllegalDateComponentsException)
        e.key == IllegalDateComponentsException.KEY
        e.parameters["year"] == year.toString()
        e.parameters["month"] == month.toString()
        e.parameters["day"] == day.toString()

        where:
        year | month | day
        2015 | 10    | 0
        2015 | 2     | 30
        2015 | 12    | 32
    }

    def "should create LocalDateRange of one month"() {
        given:
        def year = 2015;
        def month = 10;

        when:
        def range = LocalDateRange.ofMonth(year, month)

        then:
        range != null
        range.getFrom() == LocalDateTime.of(year, month, 1, 0, 0)
        range.getTo() == LocalDateTime.of(year, month, 31, 23, 59, 59, 999999999)
    }

    def "should throws IllegalDateComponentsException when month not exist"() {
        when:
        LocalDateRange.ofMonth(year, month)

        then:
        def e = thrown(IllegalDateComponentsException)
        e.key == IllegalDateComponentsException.KEY
        e.parameters["year"] == year.toString()
        e.parameters["month"] == month.toString()
        e.parameters["day"] == "1"

        where:
        year | month
        2015 | 0
        2015 | 13
    }

    def "should create LocalDateRange of one week"() {
        given:
        def year = 2015

        when:
        def range = LocalDateRange.ofWeek(year, week)

        then:
        range != null
        range.getFrom() == LocalDateTime.of(2015, month, numberOfFirstDay, 0, 0)
        range.getTo() == LocalDateTime.of(2015, month, numberOfLastDayDay, 23, 59, 59, 999999999)

        where:
        week | month | numberOfFirstDay | numberOfLastDayDay
        0    | 1     | 1                | 7
        1    | 1     | 8                | 14
        2    | 1     | 15               | 21
    }

    def "should throws IllegalWeekNumberException when week not exist"() {
        given:
        def year = 2015

        when:
        LocalDateRange.ofWeek(year, week)

        then:
        def e = thrown(IllegalWeekNumberException)
        e.key == IllegalWeekNumberException.KEY
        e.parameters["week"] == week.toString()

        where:
        week | _
        -1   | _
        53   | _
    }

}
