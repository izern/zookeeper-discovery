package cn.izern.test.zookeeper.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

import cn.izern.test.zookeeper.server.AbstractServer;

/**
 * zookeeper 相关工具类
 * @author root
 * 2018年3月21日 下午9:55:29
 */
public class ZookeeperUtils {
	
	// ZNode 路径
	private static String basePaht = "/cn/izern/test/zookeeper";
	
	/**
	 * 获取默认client
	 * @return
	 */
	public static CuratorFramework getClient() {
		CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181",
				new ExponentialBackoffRetry(1000, 3));
		System.out.println("create client success.");
		client.start();
		return client;
	}
	
	/**
	 * 创建服务发现者
	 * @param client
	 * @return
	 */
	public static ServiceDiscovery<String> getDiscovery(CuratorFramework client){
		ServiceDiscovery<String> discovery = ServiceDiscoveryBuilder.builder(String.class)
					.client(client)
					.basePath(basePaht)
					.build();
		try {
			discovery.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("create ServiceDiscovery success.");
		return discovery;
	}
	
	/**
	 * 创建服务提供者信息
	 * @param server
	 * @return
	 */
	public static ServiceInstance<String> createService(AbstractServer server){
		try {
			ServiceInstance<String> service = ServiceInstance.<String>builder()
			        .id(server.toString())
			        .name(server.getName())
			        .payload(server.payload())
			        .uriSpec(new UriSpec("{address}:{port}"))
			        .build();
			System.out.println("create service "+server.toString() + " for service name " + server.getName());
			return service;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 服务注册
	 * @param service
	 * @param discovery
	 * @return
	 */
	public static ServiceDiscovery<String> regestService(ServiceInstance<String> service,
			ServiceDiscovery<String> discovery){
		try {
			discovery.registerService(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return discovery;
	}

}
