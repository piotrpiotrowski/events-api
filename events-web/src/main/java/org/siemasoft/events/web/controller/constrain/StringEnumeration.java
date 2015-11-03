package org.siemasoft.events.web.controller.constrain;

import org.siemasoft.events.web.controller.validator.StringEnumerationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringEnumerationValidator.class)
@Documented
public @interface StringEnumeration {

    String message() default "Enum {value} does not contain value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> value();
}

