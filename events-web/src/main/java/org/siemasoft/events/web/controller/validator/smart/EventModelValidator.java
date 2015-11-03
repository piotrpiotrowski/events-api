package org.siemasoft.events.web.controller.validator.smart;

import org.siemasoft.events.web.controller.group.Update;
import org.siemasoft.events.web.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.time.LocalDateTime;

@Component
public class EventModelValidator implements SmartValidator {

    @Autowired
    private ErrorsAppender errorsAppender;

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        EventModel eventModel = (EventModel) target;
        if (validationHints[0].equals(Update.class)) {
            validateOnUpdate(eventModel, errors);
        }
    }

    private void validateOnUpdate(EventModel eventModel, Errors errors) {
        LocalDateTime startedDate = eventModel.getStarted();
        LocalDateTime finishedDate = eventModel.getFinished();
        if (startedDate != null && finishedDate != null && finishedDate.isBefore(startedDate)) {
            errorsAppender.append(errors, EventModelErrorCodes.FINISHED_BEFORE_STARTED);
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EventModel.class.equals(clazz);
    }

    private enum EventModelErrorCodes implements ErrorCode {
        FINISHED_BEFORE_STARTED("finished.before.started", "finished", "Finished date time has to after started date.");

        EventModelErrorCodes(String code, String field, String defaultMessage) {
            this.code = code;
            this.field = field;
            this.defaultMessage = defaultMessage;
        }

        private String code;

        private String field;

        private String defaultMessage;

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDefaultMessage() {
            return defaultMessage;
        }

        @Override
        public String getField() {
            return field;
        }
    }
}
