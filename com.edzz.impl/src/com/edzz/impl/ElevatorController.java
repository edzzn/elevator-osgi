package com.edzz.impl;

import java.util.Queue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ElevatorController implements Runnable {
	private int num = 0;
	private volatile Thread runnableThread = null;
	private Queue<FloorRequest> requestQueue = new LinkedList<FloorRequest>();
	private Elevator elevator = new Elevator("E1", 0, 8);
	private List<String> debug = new ArrayList<>();

	public void start() {
		if (runnableThread == null) {
			runnableThread = new Thread(this, "Clock");
			runnableThread.start();
		}
	}

	public int getNum() {
		return num;
	}

	public void incrementNum() {
		this.num = num += 1;
	}

	public void addRequest(FloorRequest request) {
		requestQueue.add(request);
	}

	public String getElevatorStatus() {
		return elevator.getStatus();
	}

	public String getPrettyQ() {
		Iterator<FloorRequest> value = requestQueue.iterator();

		// Displaying the values after iterating through the queue
		String prettyOutPut = "[";
		while (value.hasNext()) {
			FloorRequest currentRequest = value.next();
			prettyOutPut += " ";
			prettyOutPut += "(" + currentRequest.originFloor + "->" + currentRequest.destinationFloor + ")";
		}
		prettyOutPut += " ]";
		return prettyOutPut;
	}
	
	public String getPrettyDebug() {
		Iterator<String> value = debug.iterator();

		// Displaying the values after iterating through the queue
		String prettyOutput = "[";
		while (value.hasNext()) {
			String debugOutput = value.next();
			prettyOutput += " ";
			prettyOutput += "(" + debugOutput + ")";
		}
		prettyOutput += " ]";
		return prettyOutput;
	}
	
	public void updateElevatorState() {
		// Is there an available Elevator
		Boolean isElevatorAvailable = elevator.getCurrentState() == ElevatorState.STOPPED;
		if (!requestQueue.isEmpty() && isElevatorAvailable) {
			String debugOutput = "ROUTE elevator" + this.getNum() + " " + this.getPrettyQ() + " " + this.getElevatorStatus();
			debug.add(debugOutput);
		}
	}

	@Override
	public void run() {
		Thread myThread = Thread.currentThread();
		while (runnableThread == myThread) {
			this.incrementNum();
			// Check the elevator State before Moving
			this.updateElevatorState();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
