package com.epam.esm.service.exception.validation.annotation;

import com.epam.esm.service.exception.validation.NotBlankIfPresentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfPresent {

    String message() default "{javax.validation.constraints.NotBlank.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
