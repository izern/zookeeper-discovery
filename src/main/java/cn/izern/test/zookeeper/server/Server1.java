package cn.izern.test.zookeeper.server;

import java.util.Random;

/**
 * 服务1,  启动后注册在zookeeper中
 * @author root
 * 2018年3月21日 下午9:49:13
 */
public class Server1 extends AbstractServer  {
	
	private Long live = Long.valueOf((new Random().nextInt(50)+1));

	@Override
	public String service() {
		return "This is "+toString()+", current time is " + System.currentTimeMillis();
	}

	@Override
	public Long keepSecond() {
		return live;
	}

	@Override
	public String getName() {
		return "server1";
	}

}
