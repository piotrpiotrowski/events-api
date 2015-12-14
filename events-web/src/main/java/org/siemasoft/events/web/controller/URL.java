package org.siemasoft.events.web.controller;

public class URL {

    public static final String EVENTS = "/events";

    public static final String EVENTS_BY_MONTH = EVENTS + "/{" + Params.YEAR + "}/{" + Params.MONTH + "}";

    public static final String EVENTS_BY_DAY = EVENTS_BY_MONTH + "/{" + Params.DAY + "}";

    public static final String EVENT_BY_ID = EVENTS + "/{" + Params.EVENT_ID + "}";

    public static final String EVENTS_BY_WEEK = EVENTS + "/week/{" + Params.YEAR + "}/{" + Params.WEEK + "}";

    public static final String OVERDUE_EVENT = EVENTS + "/overdue";

    public static String getAll() {
        return null;
    }
}
