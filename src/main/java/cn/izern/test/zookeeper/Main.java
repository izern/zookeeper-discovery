package cn.izern.test.zookeeper;

import cn.izern.test.zookeeper.client.Client;
import cn.izern.test.zookeeper.server.Server1;
import cn.izern.test.zookeeper.server.Server2;
import cn.izern.test.zookeeper.server.Server3;

public class Main {

	
	public static void main(String[] args) throws Exception {
		
		// start server 
		Runnable run = new Server1();
		Thread thread = new Thread(run);
		thread.start();
		// server 2
		Runnable run2 = new Server2();
		Thread thread2 = new Thread(run2);
		thread2.start();
		// server 3
		Runnable run3 = new Server3();
		Thread thread3 = new Thread(run3);
		thread3.start();
		
		Runnable client = new Client();
		Thread clientThread = new Thread(client);
		clientThread.setDaemon(true);
		clientThread.start();
		
	}
}
