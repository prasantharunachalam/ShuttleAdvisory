package com.metro.shuttle.advisory.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableMap;
import com.metro.shuttle.advisory.dto.MetroRoutesResponse;
import com.metro.shuttle.advisory.dto.MetroTimePointDepartureResponse;
import com.metro.shuttle.advisory.dto.NextShuttleRequest;
import com.metro.shuttle.advisory.dto.TextValuePairResponse;
import com.metro.shuttle.advisory.service.MetroTransitService;
import com.metro.shuttle.advisory.service.ShuttleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShuttleServiceImpl implements ShuttleService {

	@Autowired
	private MetroTransitService metroTransitService;

	private final Map<String, String> directionLookUp = ImmutableMap.of("SOUTH", "1", "EAST", "2", "WEST", "3", "NORTH",
			"4");
	
	private final static String NOT_AVAILABLE = "NA";

	@Override
	public NextShuttleRequest getActualRouteNumberAndStopNameForUserInputsWithDirection(final String route,
			final String direction, final String stopName) {
		final NextShuttleRequest nextShuttleRequest = new NextShuttleRequest();
		
		final String actualRouteId = validateAndReturnRouteIdByRouteSearchString(route, nextShuttleRequest);
		if(NOT_AVAILABLE.equals(actualRouteId)) {
			setCircuitInOpenState(nextShuttleRequest);
			return nextShuttleRequest;
		}
		nextShuttleRequest.setRoute(actualRouteId);
//		nextShuttleRequest.setRoute("902");

		final String actualDirectionId = validateAndReturnDirectionIdByRouteAndDirectionSearchString(route, direction,
				nextShuttleRequest.getRoute(), nextShuttleRequest);
		if(NOT_AVAILABLE.equals(actualDirectionId)) {
			setCircuitInOpenState(nextShuttleRequest);
			return nextShuttleRequest;
		}
		nextShuttleRequest.setDirection(actualDirectionId);
//		nextShuttleRequest.setDirection("2");

		final String actualStopId = validateAndReturnStopIdByRouteSearchString(route, direction,
				nextShuttleRequest.getDirection(), nextShuttleRequest.getRoute(), stopName, nextShuttleRequest);
		if(NOT_AVAILABLE.equals(actualStopId)) {
			setCircuitInOpenState(nextShuttleRequest);
			return nextShuttleRequest;
		}
		nextShuttleRequest.setStopName(actualStopId);
		return nextShuttleRequest;
	}
	
	private void setCircuitInOpenState(final NextShuttleRequest nextShuttleRequest ) {
		nextShuttleRequest.setCircuitOpen(true);
		log.info("setCircuitInOpenState");
	}

	@Override
	public String validateAndReturnRouteIdByRouteSearchString(final String routeSearchString, final NextShuttleRequest nextShuttleRequest) {
		// get routes
		final ResponseEntity<List<MetroRoutesResponse>> metroRoutesResponseEntity = metroTransitService.getRouteIdByRouteSearchString();
		if(Objects.isNull(metroRoutesResponseEntity))
			return NOT_AVAILABLE;
		final List<MetroRoutesResponse> metroRoutesResponseList = metroRoutesResponseEntity.getBody();
		Optional<MetroRoutesResponse> obj = metroRoutesResponseList.stream().filter(routeItem -> {
			return routeItem.getDescription().contains(routeSearchString);
		}).findFirst();
		if (obj.isPresent()) {
			nextShuttleRequest.setRouteText(obj.get().getDescription());
			return obj.get().getRoute();
		}
		else
			throw new ConstraintViolationException(
					"Route Name [[" + routeSearchString + "]] is not valid. No matching RouteId exists.", null);
	}

	@Override
	public String validateAndReturnDirectionIdByRouteAndDirectionSearchString(final String routeSearchString,
			final String directionSearchString, final String actualRouteId, NextShuttleRequest nextShuttleRequest) {
		// get directions
		final ResponseEntity<List<TextValuePairResponse>> routeDirectionsResponseEntity = metroTransitService.getDirectionIdByRouteAndDirectionSearchString(actualRouteId);
		if(Objects.isNull(routeDirectionsResponseEntity))
			return NOT_AVAILABLE;
		List<TextValuePairResponse> routeDirectionsResponseList = routeDirectionsResponseEntity.getBody();
		if (!CollectionUtils.isEmpty(routeDirectionsResponseList))
			routeDirectionsResponseList = routeDirectionsResponseList.stream().filter(item -> {
				return item.getValue().equals(directionLookUp.get(directionSearchString.toUpperCase()));
			}).collect(Collectors.toList());
		else
			throw new ConstraintViolationException(
					"Direction is not applicable for the Route Name [[" + routeSearchString + "]].", null);

		if (CollectionUtils.isEmpty(routeDirectionsResponseList))
			throw new ConstraintViolationException(
					"Direction is not applicable for the Route Name [[" + routeSearchString + "]].", null);
		nextShuttleRequest.setDirectionText(directionSearchString.toUpperCase());
		return directionLookUp.get(directionSearchString.toUpperCase());
	}
	
	@Override
	public String validateAndReturnStopIdByRouteSearchString(final String routeSearchString,
			final String directionSearchString, final String actualDirectionId, final String actualRouteId,
			final String stopNameSearchString, final NextShuttleRequest nextShuttleRequest) {
		final ResponseEntity<List<TextValuePairResponse>> metroRouteStopResponseEntity = metroTransitService.getStopIdByRouteSearchString(actualDirectionId, actualRouteId);
		if(Objects.isNull(metroRouteStopResponseEntity))
			return NOT_AVAILABLE;
		final List<TextValuePairResponse> metroRouteStopResponseList = metroRouteStopResponseEntity.getBody();
		if (CollectionUtils.isEmpty(metroRouteStopResponseList))
			throw new ConstraintViolationException("StopName is not applicable for the Route Name [["
					+ routeSearchString + "]] and Direction [[" + directionSearchString + "]].", null);
		else {
			Optional<TextValuePairResponse> obj1 = metroRouteStopResponseList.stream().filter(routeStopItem -> {
				return routeStopItem.getText().contains(stopNameSearchString);
			}).findFirst();
			if (obj1.isPresent()) {
				nextShuttleRequest.setStopNameText(obj1.get().getText());
				return obj1.get().getValue();
			}
			else
				throw new ConstraintViolationException("StopName is not applicable for the Route Name [["
						+ routeSearchString + "]] and Direction [[" + directionSearchString + "]].", null);
		}
	}
	
	@Override
	public MetroTimePointDepartureResponse retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			final NextShuttleRequest nextShuttleRequest) {
		final ResponseEntity<List<MetroTimePointDepartureResponse>> response = metroTransitService.getTimeDurationForNextShuttleByRouteAndDirectionAndStop(nextShuttleRequest);
		if(Objects.isNull(response))
			retrieveDefaultDurationForNextShuttleByRouteAndDirectionAndStop(nextShuttleRequest);
		log.info("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
		final List<MetroTimePointDepartureResponse> list = response.getBody();
		Collections.sort(list);

		return list.get(0);
	}

	private MetroTimePointDepartureResponse retrieveDefaultDurationForNextShuttleByRouteAndDirectionAndStop(
			final NextShuttleRequest nextShuttleRequest) {
		final MetroTimePointDepartureResponse defaultResponse = new MetroTimePointDepartureResponse();
		defaultResponse.setCircuitOpen(true);
		log.info("retrieveFallBackTimeDurationForNextShuttleByRouteAndDirectionAndStop");
		return defaultResponse;
	}

}
