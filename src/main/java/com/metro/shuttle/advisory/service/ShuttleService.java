package com.metro.shuttle.advisory.service;

import com.metro.shuttle.advisory.dto.MetroTimePointDepartureResponse;
import com.metro.shuttle.advisory.dto.NextShuttleRequest;

public interface ShuttleService {

	NextShuttleRequest getActualRouteNumberAndStopNameForUserInputsWithDirection(String route, String direction,
			String stopName);

	MetroTimePointDepartureResponse retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			NextShuttleRequest nextShuttleRequest);

	String validateAndReturnRouteIdByRouteSearchString(final String routeSearchString,
			final NextShuttleRequest nextShuttleRequest);

	String validateAndReturnDirectionIdByRouteAndDirectionSearchString(final String routeSearchString,
			final String directionSearchString, final String actualRouteId,
			final NextShuttleRequest nextShuttleRequest);

	String validateAndReturnStopIdByRouteSearchString(final String routeSearchString,
			final String directionSearchString, final String actualDirectionId, final String actualRouteId,
			final String stopNameSearchString, final NextShuttleRequest nextShuttleRequest);

}
