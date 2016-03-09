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

    String message() default "Minimum value for this date is now plus {value} minute(s)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();
}

