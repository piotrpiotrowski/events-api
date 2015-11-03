package org.siemasoft.events.core.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.siemasoft.events.core.domain.exception.IllegalDateComponentsException;
import org.siemasoft.events.core.domain.exception.IllegalWeekNumberException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateRange {

    private static final int NUMBER_OF_WEEKS_IN_YEAR = 52;

    public static final int NUMBER_OF_DAYS_IN_WEEK = 7;

    private LocalDateTime from;

    private LocalDateTime to;

    public static LocalDateRange ofDay(int year, int month, int day) {
        return new LocalDateRange(year, month, day, ChronoUnit.DAYS);
    }

    public static LocalDateRange ofMonth(int year, int month) {
        return new LocalDateRange(year, month, 1, ChronoUnit.MONTHS);
    }

    public static LocalDateRange ofWeek(int year, int week) {
        if (week < 0 || week > NUMBER_OF_WEEKS_IN_YEAR) {
            throw new IllegalWeekNumberException(week);
        }
        int firstDayOfGivenWeek = NUMBER_OF_DAYS_IN_WEEK * week + 1;
        LocalDate localDate = LocalDate.ofYearDay(year, firstDayOfGivenWeek);
        return new LocalDateRange(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), ChronoUnit.WEEKS);
    }

    private LocalDateRange(int year, int month, int day, ChronoUnit chronoUnit) {
        this.from = createLocalDateTime(year, month, day);
        this.to = createLocalDateTime(year, month, day).plus(1, chronoUnit)
                .minusNanos(1);
    }

    private LocalDateTime createLocalDateTime(int year, int month, int day) {
        try {
            return LocalDateTime.of(year, month, day, 0, 0);
        } catch (DateTimeException e) {
            throw new IllegalDateComponentsException(year, month, day);
        }
    }
}
