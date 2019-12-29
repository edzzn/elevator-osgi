package com.edzz.impl;

public class FloorRequest {
	private final int requestTime;
	private final int originFloor;
	private final int destinationFloor;
	private FloorRequestState state;
	
	public FloorRequest(int requestTime, int originFloor, int destinationFloor) {
		this.requestTime = requestTime;
		this.originFloor = originFloor;
		this.destinationFloor = destinationFloor;
		this.state = FloorRequestState.PENDING;
	}
	
	public String toString() { 
		String output = "";
		output += "Time: '" + this.requestTime + "'";
		output += ", State: '" + this.state + "'"; 
		output += ", (" + this.originFloor + "->" + this.destinationFloor+ ")";
	    return output;
	}

	public FloorRequestState getState() {
		return state;
	}

	public void setState(FloorRequestState state) {
		this.state = state;
	}

	public int getRequestTime() {
		return requestTime;
	}

	public int getOriginFloor() {
		return originFloor;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}
}
