package com.example.housingmanagementsystem.UtilityClasses;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoticePeriodValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNoticePeriod {

    String message() default "Notice must be atleast 30 days from today";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
