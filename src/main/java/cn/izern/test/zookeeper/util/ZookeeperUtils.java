package cn.izern.test.zookeeper.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.izern.test.zookeeper.server.AbstractServer;

/**
 * zookeeper 相关工具类
 * @author root
 * 2018年3月21日 下午9:55:29
 */
public class ZookeeperUtils {
	
	// ZNode 路径
	private static String basePaht = "/cn/izern/test/zookeeper";
	protected static Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);
	
	
	/**
	 * 获取默认client
	 * @return
	 */
	public static CuratorFramework getClient() {
		CuratorFramework client = CuratorFrameworkFactory.builder()
					.connectString("localhost:2181,localhost:2182,localhost:2183")
					.retryPolicy(new ExponentialBackoffRetry(1000, 3))
					.connectionTimeoutMs(3000)
					.sessionTimeoutMs(5000)
					.build();
//				newClient("localhost:2181",
//				new ExponentialBackoffRetry(1000, 3));
		logger.info("create client success.");
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
		logger.info("create ServiceDiscovery success.");
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
			        .name("test-server")
			        .payload(server.payload())
			        .uriSpec(new UriSpec("{address}:{port}"))
			        .build();
			logger.info("create service {} for service name ", server.toString(), "test-server");
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
