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
@Constraint(validatedBy = RouteValidator.class)
@Documented
public @interface Route {

    String message() default "Route Name is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
