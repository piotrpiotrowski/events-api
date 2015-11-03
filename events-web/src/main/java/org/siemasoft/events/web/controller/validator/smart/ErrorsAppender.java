package org.siemasoft.events.web.controller.validator.smart;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ErrorsAppender {

    public void append(Errors errors, ErrorCode errorCode) {
        errors.rejectValue(errorCode.getField(), errorCode.getCode(), errorCode.getDefaultMessage());
    }
}
