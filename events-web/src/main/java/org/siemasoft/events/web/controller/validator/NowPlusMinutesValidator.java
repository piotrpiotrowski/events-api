package org.siemasoft.events.web.controller.validator;

import org.siemasoft.events.web.controller.constrain.NowPlusMinutes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NowPlusMinutesValidator implements ConstraintValidator<NowPlusMinutes, LocalDateTime> {

    private int numberOfMinutes;

    @Override
    public void initialize(NowPlusMinutes constraintAnnotation) {
        numberOfMinutes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
        LocalDateTime lastNanoOfMinute = LocalDateTime.now()
                .plus(numberOfMinutes, ChronoUnit.MINUTES)
                .truncatedTo(ChronoUnit.MINUTES)
                .minusNanos(1);
        return dateTime == null || lastNanoOfMinute.isBefore(dateTime);
    }
}
