package com.edzz.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExampleTest {

	@Test
	public void test() throws Exception {
		String result = new Example().sayHello("Edisson");
		assertEquals("Hello Edisson", result);
	}

}
