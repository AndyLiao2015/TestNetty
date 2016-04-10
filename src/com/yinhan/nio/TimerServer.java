package com.yinhan.nio;

public class TimerServer {
	
	public static void main(String[] args) {
		int port = 8080;
		
		new Thread(new MutiplexerTimerServer(port), "ThreadName:MutiplexerTimerServer-001").start();
	}
}
