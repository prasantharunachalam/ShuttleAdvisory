package com.metro.nextshuttle.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.micrometer.core.instrument.util.StringUtils;

public class StopValidator implements ConstraintValidator<Stop, String>{
	
	private final static String pattern = "^[a-zA-Z0-9-\\s//]*$";
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		 return StringUtils.isNotEmpty(value) && value.matches(pattern);
	}

}
