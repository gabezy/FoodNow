package com.gabezy.foodnow.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FreeDeliveryIncludeDescriptionValidator.class})
public @interface FreeDeliveryIncludeDescription {

    String message() default "Required description invalid";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};

    String valueField();

    String checkedField();

    String requiredValue();
}
