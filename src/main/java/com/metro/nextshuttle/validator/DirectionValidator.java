package com.metro.nextshuttle.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.micrometer.core.instrument.util.StringUtils;

public class DirectionValidator implements ConstraintValidator<Direction, String>{
	
	final List<String> directions = Arrays.asList("NORTH", "SOUTH", "EAST", "WEST");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		 return StringUtils.isNotEmpty(value) && directions.contains(value.toUpperCase());
	}

}
