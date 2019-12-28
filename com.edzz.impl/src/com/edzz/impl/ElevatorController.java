package com.edzz.impl;

import java.util.Queue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ElevatorController implements Runnable {
	private int time = 0;
	private volatile Thread runnableThread = null;
	private LinkedList<FloorRequest> requestQueue = new LinkedList<FloorRequest>();
	private Elevator elevator = new Elevator("E1", 0, 8);
	private List<String> debug = new ArrayList<>();

	public void start() {
		if (runnableThread == null) {
			runnableThread = new Thread(this, "Clock");
			runnableThread.start();
		}
	}

	public int getTime() {
		return time;
	}

	public void incrementNum() {
		this.time = time += 1;
	}

	public void addRequest(FloorRequest request) {
		FloorRequest tailRequest = this.requestQueue.peekLast();
		if (tailRequest != null) {
			int timeSinceLastRequest = Math.abs(tailRequest.requestTime - time);
			Boolean isSimilarRequest = request.originFloor == tailRequest.originFloor
					&& request.destinationFloor == tailRequest.destinationFloor;

			if (timeSinceLastRequest < 3 && isSimilarRequest) {
				// Activate the buzzer, Don't add duplicated request 
				debug.add("Duplicated Request");
			} else {
				requestQueue.add(request);
			}
		} else {
			requestQueue.add(request);
		}
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
		FloorRequest request = this.requestQueue.peek();
		this.requestQueue.peek();
		if (request != null && isElevatorAvailable) {
			request = this.requestQueue.poll();
			elevator.setCurrentState(ElevatorState.FLOORING);
			String debugOutput = "ROUTE " + elevator.getName() + " " + request + " {" + this.getElevatorStatus() + "}";
			debug.add(debugOutput);
		}
	}

	@Override
	public void run() {
		Thread myThread = Thread.currentThread();
		while (runnableThread == myThread) {
			// Check the elevator State before Moving
			this.updateElevatorState();

			this.incrementNum();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
