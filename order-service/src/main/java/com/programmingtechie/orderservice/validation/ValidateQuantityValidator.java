package com.programmingtechie.orderservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateQuantityValidator implements ConstraintValidator<ValidateQuantity, Integer> {
    private final Integer constant = 10;
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value > constant){
            return false;
        }
        return true;
    }
}
