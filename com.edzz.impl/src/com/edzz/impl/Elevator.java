package com.edzz.impl;

public class Elevator implements IElevator {
	private final int maxFloor;
	private final int minFloor = 0;
	private ElevatorState direction = ElevatorState.MOVING_UP;

	private int currentFloor;
	private ElevatorState currentState = ElevatorState.STOPPED;

	public Elevator(int currentFloor, int maxFloor) {
		this.currentFloor = currentFloor;
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
}
