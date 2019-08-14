package com.metro.shuttle.advisory.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetroTimePointDepartureResponse implements Comparable<MetroTimePointDepartureResponse> {

	private final static String US_ZONEID = "GMT-05:00";
	private static final Pattern pattern = Pattern.compile("(?<=Date\\()(.*)(?=\\))");
	private static final String SEPARATOR = "-";

	@JsonProperty("Actual")
	private Boolean actual;

	@JsonProperty("BlockNumber")
	private Integer blockNumber;

	@JsonProperty("DepartureText")
	private String departureText;

	@JsonProperty("DepartureTime")
	private String departureTime;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("Gate")
	private String gate;

	@JsonProperty("Route")
	private String route;

	@JsonProperty("RouteDirection")
	private String routeDirection;

	@JsonProperty("Terminal")
	private String terminal;

	@JsonProperty("VehicleHeading")
	private String vehicleHeading;

	@JsonProperty("VehicleLatitude")
	private String vehicleLatitude;

	@JsonProperty("VehicleLongitude")
	private String vehicleLongitude;

	private LocalDateTime departureLocalDateTime;

	boolean isCircuitOpen;

	public boolean isCircuitOpen() {
		return isCircuitOpen;
	}

	public void setCircuitOpen(boolean isCircuitOpen) {
		this.isCircuitOpen = isCircuitOpen;
	}

	public LocalDateTime getDepartureLocalDateTime() {
		return departureLocalDateTime;
	}

	public void setDepartureLocalDateTime(LocalDateTime departureLocalDateTime) {
		this.departureLocalDateTime = departureLocalDateTime;
	}

	public Integer getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Integer blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getDepartureText() {
		return departureText;
	}

	public void setDepartureText(String departureText) {
		this.departureText = departureText;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
		this.setDepartureLocalDateTime(deserialize(departureTime));
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGate() {
		return gate;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getRouteDirection() {
		return routeDirection;
	}

	public void setRouteDirection(String routeDirection) {
		this.routeDirection = routeDirection;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getVehicleHeading() {
		return vehicleHeading;
	}

	public void setVehicleHeading(String vehicleHeading) {
		this.vehicleHeading = vehicleHeading;
	}

	public String getVehicleLatitude() {
		return vehicleLatitude;
	}

	public void setVehicleLatitude(String vehicleLatitude) {
		this.vehicleLatitude = vehicleLatitude;
	}

	public String getVehicleLongitude() {
		return vehicleLongitude;
	}

	public void setVehicleLongitude(String vehicleLongitude) {
		this.vehicleLongitude = vehicleLongitude;
	}

	@Override
	public int compareTo(MetroTimePointDepartureResponse obj2) {
		LocalDateTime currentObjDepartureTime = this.departureLocalDateTime;
		LocalDateTime nextObjDepartureTime = obj2.departureLocalDateTime;
		return currentObjDepartureTime.compareTo(nextObjDepartureTime);
	}

	private LocalDateTime deserialize(String departureTime) {
		Date result = null;
		if (StringUtils.isNotEmpty(departureTime)) {
			final Matcher matcher = pattern.matcher(departureTime);
			if (matcher.find()) {
				final String[] values = matcher.group(1).split(SEPARATOR);
				Calendar calendar = Calendar.getInstance();
				if (values.length == 2) {
					calendar.setTimeInMillis(Long.parseLong(values[0]));
					calendar.getTimeZone().setRawOffset(Integer.parseInt(values[1]));
					result = new Date(calendar.getTime().getTime());
				} else if (values.length == 3) {
					calendar.setTimeInMillis(Long.parseLong(SEPARATOR + values[1]));
					calendar.getTimeZone().setRawOffset(Integer.parseInt(values[2]));
					result = new Date(calendar.getTime().getTime());
				}
			}
		}
		return Objects.nonNull(result) ? LocalDateTime.ofInstant(result.toInstant(), ZoneId.of(US_ZONEID)) : null;
	}

	public Boolean getActual() {
		return actual;
	}

	public void setActual(Boolean actual) {
		this.actual = actual;
	}
}
