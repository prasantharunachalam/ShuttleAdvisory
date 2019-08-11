package com.metro.nextshuttle.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IntegrationServiceImpl implements IntegrationService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public HttpEntity<String> createHttpEntityWithHeaders(List<MediaType> mediaTypes) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(mediaTypes);
		final HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		return entity;
	}

	@Override
	public <T> ResponseEntity<T> invokeExtenalService(final String url, final HttpMethod method,
			final HttpEntity<String> entity, ParameterizedTypeReference<T> responseType) {
		return restTemplate.exchange(url, method, entity, responseType);
	}
}
