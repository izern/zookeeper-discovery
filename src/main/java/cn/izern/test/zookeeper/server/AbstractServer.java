package cn.izern.test.zookeeper.server;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.izern.test.zookeeper.util.ZookeeperUtils;

/**
 * 抽象服务
 * @author root
 * 2018年3月21日 下午9:44:38
 */
public abstract class AbstractServer implements Runnable {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 获取服务名称
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * 业务代码
	 * @return
	 */
	public abstract String service();
	
	/**
	 * 决定此服务提供多久服务
	 * @return
	 */
	public abstract Long keepSecond();
	
	/**
	 * 获取类信息，  模拟远程调用， 将类名存储在zookeeper，
	 * 利用反射调用service方法
	 * @return
	 */
	public String payload() {
		return this.getClass().getName();
	}
	
	@Override
	public void run() {
		CuratorFramework client = ZookeeperUtils.getClient();
		ServiceInstance<String> service = ZookeeperUtils.createService(this);
		ServiceDiscovery<String> discovery = ZookeeperUtils.getDiscovery(client);
		ZookeeperUtils.regestService(service, discovery);
		try {
			long live = keepSecond();
			TimeUnit.SECONDS.sleep(live);
			logger.info(service.getId() + " alive " + live + " S");
			discovery.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(this.toString() + " death");
	}

}
