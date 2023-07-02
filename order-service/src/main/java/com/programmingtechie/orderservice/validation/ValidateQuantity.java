package com.programmingtechie.orderservice.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidateQuantityValidator.class)
public @interface ValidateQuantity {
    public String message() default "Invalid quantity provided: Quantity should not go beyond 10";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
