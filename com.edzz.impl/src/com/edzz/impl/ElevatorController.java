package com.edzz.impl;

import java.util.Queue;
import java.util.Iterator;
import java.util.LinkedList;

public class ElevatorController implements Runnable {
	private int num = 0;
	private volatile Thread runnableThread = null;
	private Queue<FloorRequest> requestQueue = new LinkedList<FloorRequest>();

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

	@Override
	public void run() {
		Thread myThread = Thread.currentThread();
		while (runnableThread == myThread) {
			// Check the elevator State before Moving
			this.incrementNum();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
