package com.metro.shuttle.advisory.validator;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = DirectionValidator.class)
@Documented
public @interface Direction {

    String message() default "Direction is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
