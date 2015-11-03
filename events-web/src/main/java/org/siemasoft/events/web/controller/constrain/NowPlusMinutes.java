package org.siemasoft.events.web.controller.constrain;

import org.siemasoft.events.web.controller.validator.NowPlusMinutesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NowPlusMinutesValidator.class)
@Documented
public @interface NowPlusMinutes {

    String message() default "Date must be greater than or equal to now plus number of minutes: {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();
}

