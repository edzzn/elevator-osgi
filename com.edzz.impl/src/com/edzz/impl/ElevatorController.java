package com.edzz.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ElevatorController implements Runnable {
	private int time = 0;
	private volatile Thread runnableThread = null;
	private LinkedList<FloorRequest> requestQueue = new LinkedList<FloorRequest>();
	private List<String> debug = new ArrayList<>();
	private List<Elevator> elevators = new ArrayList<>();

	public ElevatorController() {
		Elevator elevator1 = new Elevator("E1", 0, 8);
		Elevator elevator2 = new Elevator("E2", 7, 8);
		elevators.add(elevator1);
		elevators.add(elevator2);
	}

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

		int maxFloor = 0;
		for (Elevator elevator : this.elevators) {
			if (elevator.getMaxFloor() > maxFloor) {
				maxFloor = elevator.getMaxFloor();
			}
		}

		if (tailRequest != null) {
			int timeSinceLastRequest = Math.abs(tailRequest.getRequestTime() - time);
			Boolean isSimilarRequest = request.getOriginFloor() == tailRequest.getOriginFloor()
					&& request.getDestinationFloor() == tailRequest.getDestinationFloor();

			if (timeSinceLastRequest < 20 && isSimilarRequest) {
				// Activate the buzzer, Don't add duplicated request
				debug.add("Duplicated Request");
			} else {
				if (request.getDestinationFloor() < maxFloor) {
					requestQueue.add(request);
				}
			}
		} else {
			if (request.getDestinationFloor() < maxFloor) {
				requestQueue.add(request);
			}
		}
	}

	public String getElevatorsStatus() {
		String status = "";
		for (Elevator elevator : elevators) {
			status += "" + elevator.getStatus() + "\n";
		}
		return status;
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

		for (Elevator elevator : this.elevators) {
			NextState nextState = this.calculateNextState(elevator);
			
			// Is a null object is received, then skip current
			if (nextState ==  null) {
				continue;
			}
			
			final ElevatorState state = elevator.getCurrentState();
			ElevatorState nextElevatorState = nextState.elevatorState;
			FloorRequestState nextRequestState = nextState.requestState;

			elevator.setCurrentState(nextElevatorState);
			if (elevator.getRequest() != null) {

				debug.add(elevator.getStatus() + ", " + state + " - " + nextState);
				elevator.setRequestState(nextRequestState);
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

	private NextState calculateNextState(Elevator elevator) {
		int currentFloor = elevator.getCurrentFloor();
		FloorRequest currentRequest = elevator.getRequest();

		NextState nextState = new NextState();
		if (currentRequest != null) {
			FloorRequestState currentRequestState = elevator.getRequestState();
			// If elevator has a request
			switch (currentRequestState) {
			case PENDING:
				// Here the request is managed and the direction is set to pickup user
				nextState.requestState = FloorRequestState.PICKING_UP;
				nextState.elevatorState = ElevatorState.STOPPED;
				break;
			case DROPPED:
				elevator.setRequest(null);
				nextState.elevatorState = ElevatorState.STOPPED;
				nextState.requestState = FloorRequestState.DROPPED;
				break;
			case DROPING:

				String debugOutputDroping = "";
				debugOutputDroping += elevator.getStatus();
				debugOutputDroping += ", Droping";
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
				debugOutput += elevator.getStatus();
				debugOutput += ", Picking Up";
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
				
				Boolean areBothAvailable = this.areBothAvailable();
				
				if (areBothAvailable) {
					// if 2 elevators are free, decide which one to use
					// Here we get the distance between the currentFloors and the ride origin
					Elevator elevatorChosen = elevator;
					
					int minDistance = Integer.MAX_VALUE;
					
					for (Elevator e : this.elevators) {
						int distanceFromCurrToOrigin = Math.abs(request.getOriginFloor() - e.getCurrentFloor());
						if (distanceFromCurrToOrigin < minDistance) {
							elevatorChosen = e;
						}
					}
					request = this.requestQueue.poll();
					request.setState(FloorRequestState.PENDING);
					elevatorChosen.setRequest(request);
					elevatorChosen.setCurrentState(ElevatorState.STOPPED);
					// Return Null and don't alter current elevator state.
					return null;
				} else {
					// if not use the other elevator 	
					request = this.requestQueue.poll();
					elevator.setRequest(request);
					nextState.elevatorState = ElevatorState.STOPPED;
					nextState.requestState = FloorRequestState.PENDING;
				}
			} else {
				// go to the default floor
				if (currentFloor == elevator.getDefaultFloor()) {
					// Arrived
					nextState.elevatorState = ElevatorState.STOPPED;
				} else {

					String debugOutput = "";
					debugOutput += elevator.getStatus();
					debugOutput += ", Going to Default Floor";
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

	private Boolean areBothAvailable() {
		Boolean isAElevatorBusy = false;
		for (Elevator elevator : this.elevators) {
			FloorRequest request = elevator.getRequest();
			if (request != null) {
				isAElevatorBusy = true;
			}
		}
		return !isAElevatorBusy;
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
