package com.gabezy.foodnow.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.PositiveOrZero;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@PositiveOrZero
public @interface DeliveryFee {

    @OverridesAttribute(constraint = PositiveOrZero.class, name = "message")
    String message() default "{DeliveryFee.invalid}";

    Class<?> [] groups() default {};

    Class<? extends Payload[]> [] payload() default {};

}
