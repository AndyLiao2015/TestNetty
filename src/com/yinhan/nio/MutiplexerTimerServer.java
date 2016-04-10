package com.yinhan.nio;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.yinhan.util.StringUtil;

public class MutiplexerTimerServer implements Runnable {
	
	private Selector selector;
	
	private ServerSocketChannel serverSocketChannel;
	
	private volatile boolean stop;
	
	public MutiplexerTimerServer(int port)
	{
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", port));
			
			selector = Selector.open();
			
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			System.out.println("The NIO timer server is start port:" + port);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop()
	{
		this.stop = true;
	}
	
	public void run() {
		while (!stop)
		{
			try {
				selector.select(1000);
				
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iter = keys.iterator();
				SelectionKey key = null;
				while(iter.hasNext())
				{
					key = iter.next();
					iter.remove();
					
					try {
						handlInupt(key);
					} catch (Exception e) {
						if (key != null)
						{
							key.cancel();
							
							if (key.channel() !=null)
							{
								key.channel().close();
							}
							key = null;
						}
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (selector != null)
		{
			try {
				selector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handlInupt(SelectionKey key) throws IOException
	{
		if (key.isValid())
		{
			if(key.isAcceptable())
			{
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			
			if(key.isReadable())
			{
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer buff = ByteBuffer.allocate(1024); // 1024×Ö½Ú  8Î»=1Byte 1024Byte=1KB 1024KB=1MB
				int readBytes = sc.read(buff);
				if(readBytes > 0)
				{
					buff.flip();
					byte[] bytes = new byte[buff.remaining()];
					buff.get(bytes);
					String req = new String(bytes, "UTF-8");
					System.out.println("The timer receive order:" + req);
					String currentTime = "QUERY TIME ORDER".equals(req) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
					doWrite(sc, currentTime);
				}
				else if(readBytes < 0)
				{
					key.cancel();
					sc.close();
				}
				else 
				{
					;
				}
			}
		}
	}
	
	public void doWrite(SocketChannel sc, String resp) throws IOException
	{
		if (StringUtil.isNotEmpty(resp))
		{
			byte[] bytes = resp.getBytes();
			ByteBuffer buff = ByteBuffer.allocate(bytes.length);
			buff.put(bytes);
			buff.flip();
			sc.write(buff);
		}
	}
}
