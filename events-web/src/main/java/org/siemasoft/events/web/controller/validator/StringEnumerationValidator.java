package org.siemasoft.events.web.controller.validator;

import org.siemasoft.events.web.controller.constrain.StringEnumeration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class StringEnumerationValidator implements ConstraintValidator<StringEnumeration, String> {

    private Set<String> AVAILABLE_ENUM_VALUES;

    @Override
    public void initialize(StringEnumeration stringEnumeration) {
        Class<? extends Enum<?>> enumClass = stringEnumeration.value();
        AVAILABLE_ENUM_VALUES = Arrays.stream(enumClass.getEnumConstants())
                .map(anEnum -> anEnum.name())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || AVAILABLE_ENUM_VALUES.contains(value);
    }
}
