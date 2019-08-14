package com.metro.shuttle.advisory.service;

import com.metro.shuttle.advisory.dto.MetroTimePointDepartureResponse;
import com.metro.shuttle.advisory.dto.NextShuttleRequest;

public interface NextShuttleService {

//	MetroTimePointDepartureResponse retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(final String route,
//			final String direction, final String stopName);

//	boolean checkIfShuttleExistsForGivenRoute(final String route, final String direction, final String stopName);

	NextShuttleRequest getActualRouteNumberAndStopNameForUserInputsWithDirection(String route, String direction,
			String stopName);

	MetroTimePointDepartureResponse retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			NextShuttleRequest nextShuttleRequest);
	
	String validateAndReturnRouteIdByRouteSearchString(final String routeSearchString);
	
	String validateAndReturnDirectionIdByRouteAndDirectionSearchString(final String routeSearchString, final String directionSearchString, final String actualRouteId);
	
	String validateAndReturnStopIdByRouteSearchString(final String routeSearchString, final String directionSearchString, final String actualDirectionId, final String actualRouteId, final String stopNameSearchString);
}