package com.metro.shuttle.advisory.dto;

public class NextShuttleRequest {

	private String route;

	private String direction;

	private String stopName;

	boolean isCircuitOpen;

	public boolean isCircuitOpen() {
		return isCircuitOpen;
	}

	public void setCircuitOpen(boolean isCircuitOpen) {
		this.isCircuitOpen = isCircuitOpen;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

}
