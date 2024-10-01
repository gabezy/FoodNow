package com.gabezy.foodnow.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Optional;

public class FreeDeliveryIncludeDescriptionValidator implements
        ConstraintValidator<FreeDeliveryIncludeDescription, Object> {

    private String valueField;
    private String validationField;
    private String requiredValue;

    @Override
    public void initialize(FreeDeliveryIncludeDescription constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.valueField = constraintAnnotation.valueField();
        this.validationField = constraintAnnotation.checkedField();
        this.requiredValue = constraintAnnotation.requiredValue();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid = true;

        try {
            BigDecimal valueBigDecimal = getProperty(BigDecimal.class, value, valueField);
            String description = getProperty(String.class, value, validationField);

            if (valueBigDecimal != null && BigDecimal.ZERO.compareTo(valueBigDecimal) == 0 &&
                    StringUtils.hasText(description))
            {
                isValid = description.toLowerCase().contains(requiredValue.toLowerCase());
            }
            return isValid;
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }


    private <T> T getProperty(@NonNull Class<T> type, Object object, String propertyName)
            throws InvocationTargetException, IllegalAccessException
    {
            PropertyDescriptor propertyDescriptor = Optional.ofNullable(BeanUtils.getPropertyDescriptor(object.getClass(), propertyName))
                    .orElseThrow(RuntimeException::new);
            return type.cast(propertyDescriptor.getReadMethod().invoke(object));
    }
}
