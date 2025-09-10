package com.example.housingmanagementsystem.UtilityClasses;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class NoticePeriodValidator implements ConstraintValidator<ValidNoticePeriod, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context){
        if(value==null){
            return true; // let @NotNull handle null cases
        }

        LocalDateTime now=LocalDateTime.now();
        LocalDateTime minNoticeDate=now.plusDays(30);

        return !value.isBefore(minNoticeDate);
    }
}
