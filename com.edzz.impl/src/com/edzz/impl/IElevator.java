package com.edzz.impl;

public interface IElevator {
	public int getMaxFloor();
	public int getMinFloor();
	public ElevatorState getDireaction();
	
	public void setDirection(ElevatorState direction);
	public int getCurrentFloor();
	public void setCurrentFloor(int floor);
	public ElevatorState getCurrentState();
	public void setCurrentState(ElevatorState state);
}
