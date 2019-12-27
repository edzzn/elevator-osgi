package com.edzz.impl;

public class RunnableExample implements Runnable {
	private int num = 0;
	private volatile Thread runnableThread = null;

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

	@Override
	public void run() {

		Thread myThread = Thread.currentThread();
		while (runnableThread == myThread) {

			this.incrementNum();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("Inside : " + Thread.currentThread().getName() + " - num: " + num);

	}
}
