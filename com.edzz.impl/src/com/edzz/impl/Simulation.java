package com.edzz.impl;

import org.osgi.service.component.annotations.Component;
import com.edzzn.api.ISimulation;;

@Component
public class Simulation implements ISimulation {
	private ElevatorController elevatorController = new ElevatorController();
	
	public  Simulation() {
		elevatorController.start();
	}

	private String formatStatus(int time) {
		String output = "Status: Elapsed time: " + time;
		output += "\nQueue: " + elevatorController.getPrettyQ();
		return output;
	}
	
	@Override
	public String getStatus() {
		int time = elevatorController.getNum();
		return formatStatus(time);
	}

	@Override
	public void requestRide(int origin, int destination) {
		FloorRequest request = new FloorRequest(origin, destination);
		elevatorController.addRequest(request);
	}

}
