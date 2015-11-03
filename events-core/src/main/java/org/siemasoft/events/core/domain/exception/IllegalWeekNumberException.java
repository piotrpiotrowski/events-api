package org.siemasoft.events.core.domain.exception;

public class IllegalWeekNumberException extends DomainObjectException {

    public static final String KEY = "illegal.week.number";

    public IllegalWeekNumberException(int week) {
        super(KEY, String.format("Invalid week number: %d", week));
        addParameter("week", week);
    }
}
