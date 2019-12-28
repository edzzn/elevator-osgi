package com.edzz.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElevatorControllerTest {
	@Test
	public void test() throws Exception {
		ElevatorController elevatorController= new ElevatorController();
		elevatorController.start();	
		
		FloorRequest request1 = new FloorRequest(1, 1, 6);
		elevatorController.addRequest(request1);
		

		String debug = elevatorController.getPrettyQ();
		assertEquals("[ (1->6) ]", debug);

		waitFor(500);
		
		int time = elevatorController.getTime();
		assertEquals(1, time);

		waitFor(1000);
		
		time = elevatorController.getTime();
		assertEquals(2, time);

		FloorRequest request2 = new FloorRequest(time, 1, 6);
		elevatorController.addRequest(request2);
		
		waitFor(2000);
		
		elevatorController.addRequest(request2);
		debug = elevatorController.getPrettyQ();
		// There is only one element on the Queue after adding 2
		assertEquals("[ (1->6) ]", debug);
		

		waitFor(5000);
		debug = elevatorController.getPrettyDebug();
		assertEquals("[ (ROUTE E1 Time: '1', (1->6) {Name: E1,State: FLOORING,Dirrection: MOVING_UP}) (Duplicated Request) ]", debug);
	}
	
	
	public void waitFor(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
		}
	}
	

}
