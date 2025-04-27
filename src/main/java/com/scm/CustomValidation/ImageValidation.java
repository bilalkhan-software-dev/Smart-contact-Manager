package com.scm.CustomValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Target({ElementType.ANNOTATION_TYPE
        ,ElementType.FIELD,ElementType
        .CONSTRUCTOR,ElementType.METHOD
        })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface ImageValidation {

    String message() default "invalid file" ;

    Class<?>[] groups() default {} ;

    Class<? extends Payload>[] payload() default {};

}
