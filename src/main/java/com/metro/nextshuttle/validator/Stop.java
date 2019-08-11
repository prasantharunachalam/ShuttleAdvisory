package com.metro.nextshuttle.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = StopValidator.class)
@Documented
public @interface Stop {

    String message() default "Stop Name is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
