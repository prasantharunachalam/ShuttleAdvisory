package com.metro.nextshuttle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metro.nextshuttle.dto.MetroTimePointDepartureResponse;
import com.metro.nextshuttle.dto.NextShuttleRequest;
import com.metro.nextshuttle.dto.NextShuttleResponse;
import com.metro.nextshuttle.service.NextShuttleService;
import com.metro.nextshuttle.validator.Direction;
import com.metro.nextshuttle.validator.Route;
import com.metro.nextshuttle.validator.Stop;

@RestController
@RequestMapping(path = "/shuttles")
@Validated
public class NextShuttleController {

	@Autowired
	private NextShuttleService nextShuttleService;

	@Value(value = "${minneapolis.timezone.id}")
	private String zoneId;

	@GetMapping(path = "/{route}/{direction}/{stopName}/duration", produces = { MediaType.APPLICATION_JSON_VALUE })
	public NextShuttleResponse getTimeDurationForNextShuttle(@Route @PathVariable final String route,
			@Direction @PathVariable final String direction, @Stop @PathVariable final String stopName) {
		NextShuttleResponse nextShuttleResponse = null;

		// get actual route number, stop name and direction for user inputs
		NextShuttleRequest nextShuttleRequest = nextShuttleService
				.getActualRouteNumberAndStopNameForUserInputsWithDirection(route, direction, stopName);
//		if (nextShuttleService.checkIfShuttleExistsForGivenRoute(route, direction, stopName)) {
		MetroTimePointDepartureResponse metroTimePointDepartureResponse = nextShuttleService
				.retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(nextShuttleRequest);
//					.retrieveTimeDurationForNextShuttleByRouteAndDirectionAndStop(route, direction, stopName);
		nextShuttleResponse = new NextShuttleResponse();
		nextShuttleResponse.setDuration(metroTimePointDepartureResponse.getDepartureText(), zoneId,
				metroTimePointDepartureResponse.getDepartureLocalDateTime());
//		}

		return nextShuttleResponse;
	}

}
