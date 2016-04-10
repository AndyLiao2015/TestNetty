package com.yinhan.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimerServer {
	public static void main(String[] args) throws IOException {
		int port = 8080;
		
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The timer server bind port:" + port);
			Socket socket = null;
			while (true)
			{
				socket = server.accept();
				new Thread(new TimerServerHandler(socket)).start();
			}
		} finally {
			if (server != null)
			{
				System.out.println("Thie timer server close!!!");
				server.close();
				server = null;
			}
		}
	}
}
