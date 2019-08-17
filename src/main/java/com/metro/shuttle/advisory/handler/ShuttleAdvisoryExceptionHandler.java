package com.metro.shuttle.advisory.handler;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ShuttleAdvisoryExceptionHandler extends ResponseEntityExceptionHandler {
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handle(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : violations) {
			strBuilder.append(violation.getMessage() + "\n");
		}
		return strBuilder.toString();
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public void constraintViolationException(HttpServletResponse response) throws IOException {
		log.info("Exiting !!! Constraint Violation Found in UserInput");
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
