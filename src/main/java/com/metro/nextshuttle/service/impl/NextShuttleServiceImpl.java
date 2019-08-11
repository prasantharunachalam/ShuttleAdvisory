package com.metro.nextshuttle.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableMap;
import com.metro.nextshuttle.dto.MetroRoutesResponse;
import com.metro.nextshuttle.dto.MetroTimePointDepartureResponse;
import com.metro.nextshuttle.dto.NextShuttleRequest;
import com.metro.nextshuttle.dto.TextValuePairResponse;
import com.metro.nextshuttle.integration.IntegrationService;
import com.metro.nextshuttle.service.NextShuttleService;

@Service
public class NextShuttleServiceImpl implements NextShuttleService {

	@Autowired
	private IntegrationService integrationService;

	@Value(value = "${metrotransit.nexttrip.timepoint.departure.url}")
	private String metroTransitUrlForTimePointDeparture;
	
	@Value(value = "${metrotransit.nexttrip.routes.url}")
	private String metroTransitUrlForRoutes;
	
	@Value(value = "${metrotransit.nexttrip.routes.directions.url}")
	private String metroTransitUrlForRouteDirections;
	
	@Value(value = "${metrotransit.nexttrip.routes.stops.url}")
	private String metroTransitUrlForRouteStops;
	
	private final Map<String, String> directionLookUp = ImmutableMap.of( "SOUTH", "1", "EAST", "2", "WEST", "3", "NORTH", "4"); 

/*	@Override
	public MetroTimePointDepartureResponse retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			final String route, final String direction, final String stopName) {

		final ResponseEntity<List<MetroTimePointDepartureResponse>> response = integrationService.invokeExtenalService(
				replacePathParamsWithActuals(metroTransitUrlForTimePointDeparture, route, direction, stopName), HttpMethod.GET,
				integrationService.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
				new ParameterizedTypeReference<List<MetroTimePointDepartureResponse>>() {
				});

		System.out.println("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
		final List<MetroTimePointDepartureResponse> list = response.getBody();
		Collections.sort(list);

		return list.get(0);
	}*/

/*	@Override
	public boolean checkIfShuttleExistsForGivenRoute(final String route, final String direction,
			final String stopName) {
		return true;
	}*/

	private String replacePathParamsWithActuals(String url, final String route, final String direction,
			final String stopName) {
		if(url.contains("{ROUTE}"))
			url = url.replace("{ROUTE}", route);
		if(url.contains("{DIRECTION}"))
			url = url.replace("{DIRECTION}", direction);
		if(url.contains("{STOP}"))
			url = url.replace("{STOP}", stopName);
		return url;
	}
	

	@Override
	public NextShuttleRequest getActualRouteNumberAndStopNameForUserInputsWithDirection(String route, String direction,
			String stopName) {
		NextShuttleRequest nextShuttleRequest = new NextShuttleRequest();
		
		String actualRouteId = validateAndReturnRouteIdByRouteSearchString(route);
		nextShuttleRequest.setRoute(actualRouteId);
		
		String actualDirectionId = validateAndReturnDirectionIdByRouteAndDirectionSearchString(direction, nextShuttleRequest.getRoute());
		nextShuttleRequest.setDirection(actualDirectionId);
		
		String actualStopId = validateAndReturnStopIdByRouteSearchString(nextShuttleRequest.getDirection(), nextShuttleRequest.getRoute(), stopName);
		nextShuttleRequest.setStopName(actualStopId);
		return nextShuttleRequest;
	}
	
	@Override
	public String validateAndReturnRouteIdByRouteSearchString(String routeSearchString) {
		//get routes
		final ResponseEntity<List<MetroRoutesResponse>> metroRoutesResponseEntity = integrationService.invokeExtenalService(metroTransitUrlForRoutes, HttpMethod.GET, integrationService.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
				new ParameterizedTypeReference<List<MetroRoutesResponse>>() {
				});
		final List<MetroRoutesResponse> metroRoutesResponseList = metroRoutesResponseEntity.getBody();
		Optional<MetroRoutesResponse> obj = metroRoutesResponseList.stream().filter(routeItem->{return routeItem.getDescription().contains(routeSearchString);}).findFirst();
		if(obj.isPresent())
			return obj.get().getRoute();
		else
			throw new RuntimeException("");
	}
	
	@Override
	public String validateAndReturnDirectionIdByRouteAndDirectionSearchString(final String directionSearchString, final String actualRouteId) {
		//get directions
		final ResponseEntity<List<TextValuePairResponse>> routeDirectionsResponseEntity = integrationService.invokeExtenalService(
				replacePathParamsWithActuals(metroTransitUrlForRouteDirections, actualRouteId , null,null), HttpMethod.GET, integrationService.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
				new ParameterizedTypeReference<List<TextValuePairResponse>>() {
				});
		List<TextValuePairResponse> routeDirectionsResponseList = routeDirectionsResponseEntity.getBody();
		if(!CollectionUtils.isEmpty(routeDirectionsResponseList))
			routeDirectionsResponseList = routeDirectionsResponseList.stream().filter(item -> {return item.getValue().equals(directionLookUp.get(directionSearchString.toUpperCase()));}).collect(Collectors.toList());
		else
			throw new RuntimeException("");	
		
		if(CollectionUtils.isEmpty(routeDirectionsResponseList))
			throw new RuntimeException("");	
		return directionLookUp.get(directionSearchString.toUpperCase());
	}
	
	@Override
	public String validateAndReturnStopIdByRouteSearchString(final String actualDirectionId, final String actualRouteId, final String stopNameSearchString) {
		final ResponseEntity<List<TextValuePairResponse>> metroRouteStopResponseEntity = integrationService.invokeExtenalService(
				replacePathParamsWithActuals(metroTransitUrlForRouteStops, actualRouteId, actualDirectionId, null), HttpMethod.GET, integrationService.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
				new ParameterizedTypeReference<List<TextValuePairResponse>>() {
				});
		final List<TextValuePairResponse> metroRouteStopResponseList = metroRouteStopResponseEntity.getBody();
		if(CollectionUtils.isEmpty(metroRouteStopResponseList))
			throw new RuntimeException("");	
		else {
			Optional<TextValuePairResponse> obj1 = metroRouteStopResponseList.stream().filter(routeStopItem->{return routeStopItem.getText().contains(stopNameSearchString);}).findFirst();
			if(obj1.isPresent())
				return obj1.get().getValue();
			else
				throw new RuntimeException("");
		}
	}
	

	@Override
	public MetroTimePointDepartureResponse retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			final NextShuttleRequest nextShuttleRequest) {
		final ResponseEntity<List<MetroTimePointDepartureResponse>> response = integrationService.invokeExtenalService(
				replacePathParamsWithActuals(metroTransitUrlForTimePointDeparture, nextShuttleRequest.getRoute(), nextShuttleRequest.getDirection(), nextShuttleRequest.getStopName()), HttpMethod.GET,
				integrationService.createHttpEntityWithHeaders(Collections.singletonList(MediaType.APPLICATION_JSON)),
				new ParameterizedTypeReference<List<MetroTimePointDepartureResponse>>() {
				});

		System.out.println("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
		final List<MetroTimePointDepartureResponse> list = response.getBody();
		Collections.sort(list);

		return list.get(0);		
	}

}
