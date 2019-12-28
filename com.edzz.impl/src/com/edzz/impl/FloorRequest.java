package com.edzz.impl;

public class FloorRequest {
	final int requestTime;
	final int originFloor;
	final int destinationFloor;
	
	public FloorRequest(int requestTime, int originFloor, int destinationFloor) {
		this.requestTime = requestTime;
		this.originFloor = originFloor;
		this.destinationFloor = destinationFloor;
	}
	
	public String toString() { 
	    return "Time: '" + this.requestTime + "', (" + this.originFloor + "->" + this.destinationFloor+ ")";
	} 
}
