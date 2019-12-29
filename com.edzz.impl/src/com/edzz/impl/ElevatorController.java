package com.edzz.impl;

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
//			runnableThread = new Thread(this, "Clock");
			runnableThread = new Thread(this);
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
			int timeSinceLastRequest = Math.abs(tailRequest.getRequestTime() - time);
			Boolean isSimilarRequest = request.getOriginFloor() == tailRequest.getOriginFloor()
					&& request.getDestinationFloor() == tailRequest.getDestinationFloor();

			if (timeSinceLastRequest < 20 && isSimilarRequest) {
				// Activate the buzzer, Don't add duplicated request
				debug.add("Duplicated Request");
			} else {
				if (request.getDestinationFloor() < elevator.getMaxFloor()) {
					requestQueue.add(request);
				}
			}
		} else {
			if (request.getDestinationFloor() < elevator.getMaxFloor()) {
				requestQueue.add(request);
			}
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
			prettyOutPut += "(" + currentRequest.getOriginFloor() + "->" + currentRequest.getDestinationFloor() + ")";
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
		NextState nextState = this.calculateNextState();
		final ElevatorState state = this.elevator.getCurrentState();
		ElevatorState nextElevatorState = nextState.elevatorState;
		FloorRequestState nextRequestState = nextState.requestState;

		
		this.elevator.setCurrentState(nextElevatorState);
		if (this.elevator.getRequest() !=  null) {

			debug.add(state + " - " + nextState);
			this.elevator.setRequestState(nextRequestState);	
		}
		
		switch (nextElevatorState) {
		case MOVING_DOWN:
		case MOVING_DOWN_DEFAULT:
			elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
			break;
		case MOVING_UP:
		case MOVING_UP_DEFAULT:
			elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
			break;
		case STOPPED:
			break;
		}
	}

	private static class NextState {
		ElevatorState elevatorState;
		FloorRequestState requestState;

		public NextState() {
			this.elevatorState = ElevatorState.STOPPED;
			this.requestState = FloorRequestState.DROPPED;
		}

		@Override
		public String toString() {
			String output = "";
			output += "ElevatorState: '" + elevatorState + "'";
			output += ", FloorRequestState: '" + requestState + "'";
			return output;
		}

	}

	private NextState calculateNextState() {
		int currentFloor = this.elevator.getCurrentFloor();
		FloorRequest currentRequest = this.elevator.getRequest();
		
		NextState nextState = new NextState();
		if (currentRequest != null) {
			FloorRequestState currentRequestState = this.elevator.getRequestState();
			// If elevator has a request
			switch (currentRequestState) {
			case PENDING:
				// Here the request is managed and the direction is set to pickup user
				nextState.requestState = FloorRequestState.PICKING_UP;
				nextState.elevatorState = ElevatorState.STOPPED;
				break;
			case DROPPED:
				this.elevator.setRequest(null);
				nextState.elevatorState = ElevatorState.STOPPED;
				nextState.requestState = FloorRequestState.DROPPED;
				break;
			case DROPING:

				String debugOutputDroping = "";
				debugOutputDroping += "Droping";
				debugOutputDroping += ", currentFloor: " + currentFloor;
				debugOutputDroping += ", detinationFloor: " + currentRequest.getDestinationFloor();
				debug.add(debugOutputDroping);
				if (currentFloor == currentRequest.getDestinationFloor()) {
					// Arrived
					nextState.requestState = FloorRequestState.DROPPED;
				} else {
					// Keep moving
					if (currentFloor > currentRequest.getDestinationFloor()) {
						// Move DOWN
						nextState.elevatorState = ElevatorState.MOVING_DOWN;
						nextState.requestState = FloorRequestState.DROPING;
					} else {
						// Current floor is lower than destination, move UP
						nextState.elevatorState = ElevatorState.MOVING_UP;
						nextState.requestState = FloorRequestState.DROPING;
					}
				}
				break;
			case PICKING_UP:
				String debugOutput = "";
				debugOutput += "Picking Up";
				debugOutput += ", currentFloor: " + currentFloor;
				debugOutput += ", originFloor: " + currentRequest.getOriginFloor();
				debug.add(debugOutput);
				if (currentFloor == currentRequest.getOriginFloor()) {
					// Arrived
					nextState.requestState = FloorRequestState.DROPING;
				} else {
					// Keep moving
					if (currentFloor > currentRequest.getOriginFloor()) {
						// Move DOWN
						nextState.elevatorState = ElevatorState.MOVING_DOWN;
						nextState.requestState = FloorRequestState.PICKING_UP;
					} else {
						// Current floor is lower than destination, move UP
						nextState.elevatorState = ElevatorState.MOVING_UP;
						nextState.requestState = FloorRequestState.PICKING_UP;
					}
				}
				break;
			}

		} else {
			// If there is no current request
			// Go to default Floor or attend another request
			FloorRequest request = this.requestQueue.peek();
			if (request != null) {
				// But there is a Queued request
				request = this.requestQueue.poll();
				this.elevator.setRequest(request);
				nextState.elevatorState = ElevatorState.STOPPED;
				nextState.requestState = FloorRequestState.PENDING;
			} else {
				// go to the default floor
				if (currentFloor == elevator.getDefaultFloor()) {
					// Arrived
					nextState.elevatorState = ElevatorState.STOPPED;
				} else {

					String debugOutput = "";
					debugOutput += "Going to Default Floor";
					debugOutput += ", currentFloor: " + currentFloor;
					debugOutput += ", defaultFloor: " + elevator.getDefaultFloor();
					debug.add(debugOutput);
					// Keep moving
					if (currentFloor > elevator.getDefaultFloor()) {
						// Move DOWN
						nextState.elevatorState = ElevatorState.MOVING_DOWN_DEFAULT;
					} else {
						// Current floor is lower than destination, move UP
						nextState.elevatorState = ElevatorState.MOVING_UP_DEFAULT;
					}
				}
			}
		}
		return nextState;
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
