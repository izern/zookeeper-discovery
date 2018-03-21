package cn.izern.test.zookeeper;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;

import cn.izern.test.zookeeper.server.Server1;
import cn.izern.test.zookeeper.util.ZookeeperUtils;

public class Main {

	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			Runnable run = new Server1();
			Thread thread = new Thread(run);
			thread.setName("thread-"+ i);
			thread.start();
		}
		int i = 0;
		CuratorFramework client = ZookeeperUtils.getClient();
		ServiceDiscovery<String> discovery = ZookeeperUtils.getDiscovery(client);
		while(i < 20) {
			Collection<ServiceInstance<String>> instances = discovery.queryForInstances("server1");
			System.out.println("--------- find instance --------");
			instances.forEach((instance)->{
				System.out.println(instance.getId());
			});
			System.out.println("---------      end      --------");
			i ++;
			TimeUnit.SECONDS.sleep(1L);
		}
	}
}
