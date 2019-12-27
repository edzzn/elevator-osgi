package com.edzz.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExampleTest {
	@Test
	public void test() throws Exception {
		String result = new Simulation().getStatus();
		assertEquals("Hello Edisson", result);
	}

}
