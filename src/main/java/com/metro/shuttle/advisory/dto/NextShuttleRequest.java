package com.metro.shuttle.advisory.dto;

public class NextShuttleRequest {

	private String route;

	private String direction;

	private String stopName;

	boolean isCircuitOpen;
	
	private String routeText;

	public String getRouteText() {
		return routeText;
	}

	public void setRouteText(String routeText) {
		this.routeText = routeText;
	}

	public String getDirectionText() {
		return directionText;
	}

	public void setDirectionText(String directionText) {
		this.directionText = directionText;
	}

	public String getStopNameText() {
		return stopNameText;
	}

	public void setStopNameText(String stopNameText) {
		this.stopNameText = stopNameText;
	}

	private String directionText;

	private String stopNameText;

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
