package com.metro.shuttle.advisory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metro.shuttle.advisory.dto.MetroTimePointDepartureResponse;
import com.metro.shuttle.advisory.dto.NextShuttleRequest;
import com.metro.shuttle.advisory.dto.NextShuttleResponse;
import com.metro.shuttle.advisory.service.ShuttleService;
import com.metro.shuttle.advisory.validator.Direction;
import com.metro.shuttle.advisory.validator.Route;
import com.metro.shuttle.advisory.validator.Stop;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/shuttles")
@Validated
@Slf4j
public class ShuttleAdvisoryResource {

	@Autowired
	private ShuttleService nextShuttleService;

	@Value(value = "${minneapolis.timezone.id}")
	private String zoneId;

	@GetMapping(path = "/{route}/{direction}/{stopName}/duration", produces = { MediaType.APPLICATION_JSON_VALUE })
	public NextShuttleResponse getTimeDurationForNextShuttle(@Route @PathVariable final String route,
			@Direction @PathVariable final String direction, @Stop @PathVariable final String stopName) {
		log.info("Begin getTimeDurationForNextShuttle :: {} {} {} ", route, direction, stopName);
		NextShuttleResponse nextShuttleResponse = null;

		// get actual route number, stop name and direction for user inputs
		NextShuttleRequest nextShuttleRequest = nextShuttleService
				.getActualRouteNumberAndStopNameForUserInputsWithDirection(route, direction, stopName);
		if(nextShuttleRequest.isCircuitOpen())
			return getFallbackTimeDurationForNextShuttle();
		MetroTimePointDepartureResponse metroTimePointDepartureResponse = nextShuttleService
				.retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(nextShuttleRequest);
		if(metroTimePointDepartureResponse.isCircuitOpen())
			return getFallbackTimeDurationForNextShuttle();		

		nextShuttleResponse = new NextShuttleResponse();
		nextShuttleResponse.setDuration(metroTimePointDepartureResponse.getDepartureText(), zoneId,
				metroTimePointDepartureResponse.getDepartureLocalDateTime());
		log.info("End getTimeDurationForNextShuttle :: {} ", nextShuttleResponse);
		return nextShuttleResponse;
	}
	
	private NextShuttleResponse getFallbackTimeDurationForNextShuttle() {	
		final NextShuttleResponse defaultResponse = new NextShuttleResponse();
		defaultResponse.setDurationInMinutesForArrival("");
		defaultResponse.setDurationInSecondsForArrival("");
		defaultResponse.setAdvisoryMessage("Next Shuttle Info will be available sooner!!!");
		return defaultResponse;
	}
}
