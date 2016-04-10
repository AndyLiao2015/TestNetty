package com.yinhan.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimerClient {
	public static void main(String[] args) {
		int port = 8080;
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println("QUERY TIME ORDER");
			System.out.println("Send order 2 server succeed!");
			String resp = in.readLine();
			System.err.println();
			System.out.print("Now is:" + resp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
			
			if (socket != null)
			{
				try {
					socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				socket = null;
			}
		}
	}
}
