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
		output += "\nElevator: " + elevatorController.getElevatorStatus();
		output += "\nQueue: " + elevatorController.getPrettyQ();
		return output;
	}
	
	@Override
	public String getStatus() {
		int time = elevatorController.getTime();
		return formatStatus(time);
	}

	@Override
	public void requestRide(int origin, int destination) {
		int time = elevatorController.getTime();
		FloorRequest request = new FloorRequest(time, origin, destination);
		elevatorController.addRequest(request);
	}

	@Override
	public String debug() {
		return elevatorController.getPrettyDebug();
	}

}
