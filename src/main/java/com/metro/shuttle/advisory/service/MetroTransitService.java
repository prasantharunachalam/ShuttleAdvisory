package com.metro.shuttle.advisory.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.metro.shuttle.advisory.dto.MetroRoutesResponse;
import com.metro.shuttle.advisory.dto.MetroTimePointDepartureResponse;
import com.metro.shuttle.advisory.dto.NextShuttleRequest;
import com.metro.shuttle.advisory.dto.TextValuePairResponse;

public interface MetroTransitService {

	ResponseEntity<List<TextValuePairResponse>> getStopIdByRouteSearchString(final String actualDirectionId,
			final String actualRouteId);

	ResponseEntity<List<MetroRoutesResponse>> getRouteIdByRouteSearchString();

	ResponseEntity<List<TextValuePairResponse>> getDirectionIdByRouteAndDirectionSearchString(
			final String actualRouteId);

	ResponseEntity<List<MetroTimePointDepartureResponse>> getTimeDurationForNextShuttleByRouteAndDirectionAndStop(
			final NextShuttleRequest nextShuttleRequest);
}
