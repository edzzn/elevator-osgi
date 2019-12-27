package com.edzzn.api;

public interface ISimulation {
	public String getStatus();
	public void requestRide(int origin, int destination);
}
