package com.metro.shuttle.advisory.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.metro.shuttle.advisory.dto.MetroRoutesResponse;
import com.metro.shuttle.advisory.dto.MetroTimePointDepartureResponse;
import com.metro.shuttle.advisory.dto.NextShuttleRequest;
import com.metro.shuttle.advisory.dto.TextValuePairResponse;
import com.metro.shuttle.advisory.integration.IntegrationService;
import com.metro.shuttle.advisory.service.MetroTransitService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetroTransitServiceImpl implements MetroTransitService{
	
	@Value(value = "${metrotransit.nexttrip.routes.url}")
	private String metroTransitUrlForRoutes;

	@Value(value = "${metrotransit.nexttrip.routes.directions.url}")
	private String metroTransitUrlForRouteDirections;

	@Value(value = "${metrotransit.nexttrip.routes.stops.url}")
	private String metroTransitUrlForRouteStops;
	
	@Value(value = "${metrotransit.nexttrip.timepoint.departure.url}")
	private String metroTransitUrlForTimePointDeparture;	
	
	@Autowired
	private IntegrationService integrationService;
	
	private String replacePathParamsWithActuals(String url, final String route, final String direction,
			final String stopName) {
		if (StringUtils.isNotEmpty(url)) {
			if (url.contains("{ROUTE}"))
				url = url.replace("{ROUTE}", route);
			if (url.contains("{DIRECTION}"))
				url = url.replace("{DIRECTION}", direction);
			if (url.contains("{STOP}"))
				url = url.replace("{STOP}", stopName);
		}
		return url;
	}
	
	@HystrixCommand(commandKey = "actual-routeid-by-search-string", fallbackMethod = "getDefaultRouteIdByRouteSearchString")
	@Override
	public ResponseEntity<List<MetroRoutesResponse>> getRouteIdByRouteSearchString(){
		return integrationService
		.invokeExtenalService(metroTransitUrlForRoutes, HttpMethod.GET,
				integrationService
						.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
				new ParameterizedTypeReference<List<MetroRoutesResponse>>() {
				});
	}
	
	public ResponseEntity<List<MetroRoutesResponse>> getDefaultRouteIdByRouteSearchString() {
		return null;
	}

	@HystrixCommand(commandKey = "actual-directionid-by-search-string", fallbackMethod = "getDefaultDirectionIdByRouteAndDirectionSearchString")
	@Override
	public ResponseEntity<List<TextValuePairResponse>> getDirectionIdByRouteAndDirectionSearchString(final String actualRouteId){
		return integrationService
				.invokeExtenalService(
						replacePathParamsWithActuals(metroTransitUrlForRouteDirections, actualRouteId, null, null),
						HttpMethod.GET,
						integrationService
								.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
						new ParameterizedTypeReference<List<TextValuePairResponse>>() {
						});
	}
	
	public ResponseEntity<List<TextValuePairResponse>> getDefaultDirectionIdByRouteAndDirectionSearchString(final String actualRouteId) {
		return null;
	}	
	
	@HystrixCommand(commandKey = "actual-stopid-by-search-string", fallbackMethod = "getDefaultStopIdByRouteSearchString")
	@Override
	public ResponseEntity<List<TextValuePairResponse>> getStopIdByRouteSearchString(final String actualDirectionId, final String actualRouteId){
		return integrationService
				.invokeExtenalService(
						replacePathParamsWithActuals(metroTransitUrlForRouteStops, actualRouteId, actualDirectionId,
								null),
						HttpMethod.GET,
						integrationService
								.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
						new ParameterizedTypeReference<List<TextValuePairResponse>>() {
						});
	}
	
	public ResponseEntity<List<TextValuePairResponse>> getDefaultStopIdByRouteSearchString(final String actualDirectionId, final String actualRouteId) {
		return null;
	}
	
	@Override
	@HystrixCommand(commandKey = "nextshuttle-duration-by-route-direction-stop", fallbackMethod = "getDefaultTimeDurationForNextShuttleByRouteAndDirectionAndStop")
	public ResponseEntity<List<MetroTimePointDepartureResponse>> getTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			final NextShuttleRequest nextShuttleRequest) {
		return integrationService.invokeExtenalService(
				replacePathParamsWithActuals(metroTransitUrlForTimePointDeparture, nextShuttleRequest.getRoute(),
						nextShuttleRequest.getDirection(), nextShuttleRequest.getStopName()),
				HttpMethod.GET,
				integrationService.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
				new ParameterizedTypeReference<List<MetroTimePointDepartureResponse>>() {
				});
	}	
	
	public ResponseEntity<List<MetroTimePointDepartureResponse>> getDefaultTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			final NextShuttleRequest nextShuttleRequest) {
		log.info("getDefaultTimeDurationForNextShuttleByRouteAndDirectionAndStop");
		return null;
	}	
}
