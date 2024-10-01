package com.gabezy.foodnow.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class MultipleValidator implements ConstraintValidator<Multiple, Number> {

    private int multipleNumber;


    @Override
    public void initialize(Multiple constraintAnnotation) {
        this.multipleNumber = constraintAnnotation.by();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        boolean isMultiple = true;

        if(value != null) {
            var decimalValue = BigDecimal.valueOf(value.doubleValue());
            var decimalmultiple = BigDecimal.valueOf(multipleNumber);
            var remainder = decimalValue.remainder(decimalmultiple);
            isMultiple = BigDecimal.ZERO.compareTo(remainder) == 0;
        }

        return isMultiple;
    }
}
