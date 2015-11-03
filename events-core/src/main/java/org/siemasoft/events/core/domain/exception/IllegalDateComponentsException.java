package org.siemasoft.events.core.domain.exception;

import lombok.Getter;

@Getter
public class IllegalDateComponentsException extends DomainObjectException {

    public static final String KEY = "illegal.date.components";

    public IllegalDateComponentsException(int year, int month, int day) {
        super(KEY, String.format("Invalid one or more date components. Year: %d, Month: %d, Day: %d", year, month, day));
        addParameter("year", year);
        addParameter("month", month);
        addParameter("day", day);
    }
}
