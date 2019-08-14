package com.metro.shuttle.advisory.integration;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface IntegrationService {

	HttpEntity<String> createHttpEntityWithHeaders(final List<MediaType> mediaTypes);

	<T> ResponseEntity<T> invokeExtenalService(final String url, final HttpMethod method,
			final HttpEntity<String> entity, ParameterizedTypeReference<T> responseType);

}
