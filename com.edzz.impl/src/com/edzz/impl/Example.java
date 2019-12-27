package com.edzz.impl;

import org.osgi.service.component.annotations.Component;
import com.edzzn.api.Greeting;;

@Component
public class Example implements Greeting{
	private int num = 0;
	
	public String sayHello() {
		// TODO Auto-generated method stub
		addOne();
		return "Hello " + num;
	}

	private void addOne() {
		num++;
	}

}
