package com.yinhan.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimerServerHandler implements Runnable{
	private Socket socket = null;
	
	public TimerServerHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream());
			
			String currentTime = null;
			String body = null;
			while(true)
			{
				body = in.readLine();
				if(body == null)
					break;
				System.out.println("The timer server receive order:" + body);
				currentTime = "QUERY TIME ORDER".equals(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
				out.write(currentTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (in != null)
			{
				try {
					in.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				in = null;
			}
			if (out != null)
			{
				try {
					out.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				out = null;
			}
			
			if (this.socket != null)
			{
				try {
					this.socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				this.socket = null;
			}
		}
	}

}
