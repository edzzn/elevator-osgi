package com.edzz.impl;

import org.osgi.service.component.annotations.Component;
import com.edzzn.api.ISimulation;;

@Component
public class Simulation implements ISimulation {
	private ElevatorController runnable = new ElevatorController();
	
	public  Simulation() {
		runnable.start();
	}

	private String formatStatus(int time) {
		String output = "Status: Elapsed time: " + time;
		return output;
	}
	
	@Override
	public String getStatus() {
		int time = runnable.getNum();
		return formatStatus(time);
	}

}
