package com.edzz.impl;

public class Elevator implements IElevator {
	private final String name;
	private final int maxFloor;
	private final int minFloor = 0;
	private ElevatorState direction = ElevatorState.MOVING_UP;
	private final int defaultFloor;
	private FloorRequest request;

	private int currentFloor;
	private ElevatorState currentState = ElevatorState.STOPPED;

	public Elevator(String name, int defaultFloor, int maxFloor) {
		this.name = name;
		this.defaultFloor = defaultFloor;
		this.currentFloor = defaultFloor;
		this.maxFloor = maxFloor;
	}

	@Override
	public int getMaxFloor() {
		return maxFloor;
	}

	@Override
	public int getMinFloor() {
		return minFloor;
	}

	@Override
	public ElevatorState getDireaction() {
		return direction;
	}

	@Override
	public void setDirection(ElevatorState direction) {
		this.direction = direction;
	}

	@Override
	public int getCurrentFloor() {
		return currentFloor;
	}

	@Override
	public ElevatorState getCurrentState() {
		return currentState;
	}

	@Override
	public void setCurrentState(ElevatorState state) {
		this.currentState = state;
	}

	@Override
	public void setCurrentFloor(int floor) {
		this.currentFloor = floor;
	}

	@Override
	public String getStatus() {
		String output = "[";
		output += "Elevator: "  + name;
		output += ", Floor: " + currentFloor;
		output += ", State: " + currentState;
		if (request != null) {
			output += ", Request: " + request.toString();			
		} else {
			output += ", Request: None";
		}
		output += "]";

		return output;
	}

	@Override
	public String getName() {
		return name;
	}

	public FloorRequest getRequest() {
		return request;
	}

	public void setRequest(FloorRequest request) {
		this.request = request;
	}

	public void setRequestState(FloorRequestState state) {
		this.request.setState(state);
	}
	
	public FloorRequestState getRequestState() {
		return request.getState();
	}

	@Override
	public int getDefaultFloor() {
		return this.defaultFloor;
	}
}
