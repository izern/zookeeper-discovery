package cn.izern.test.zookeeper.client;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.izern.test.zookeeper.util.ZookeeperUtils;

/**
 * client
 * @author root
 * 2018年3月21日 下午11:47:15
 */
public class Client implements Runnable{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected CuratorFramework client = ZookeeperUtils.getClient();
	protected ServiceDiscovery<String> discovery = ZookeeperUtils.getDiscovery(client);

	@Override
	public void run() {
		try {
			while(true) {
				Collection<ServiceInstance<String>> instances = discovery.queryForInstances("test-server");
				logger.info("find test-server instance count " + instances.size());
				List<ServiceInstance<String>> instances2 = new ArrayList<>(instances);
				if(instances.size() > 0) {
					ServiceInstance<String> serviceInstance = instances2.get(
							new Random().nextInt(instances.size()));
					logger.info("random select a server " + serviceInstance.getId());
					String clazzName = serviceInstance.getPayload();
					Class<?> service = Class.forName(clazzName);
					Method method = service.getMethod("service");
					Object result = method.invoke(service.newInstance());
					logger.error("invoke server method resultis " + result);
					logger.error("-------------------------------");
				}
				TimeUnit.SECONDS.sleep(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
