package com.metro.shuttle.advisory.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;



public class DirectionValidator implements ConstraintValidator<Direction, String>{
	
	final List<String> directions = Arrays.asList("NORTH", "SOUTH", "EAST", "WEST");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		 return StringUtils.isNotEmpty(value) && directions.contains(value.toUpperCase());
	}

}
