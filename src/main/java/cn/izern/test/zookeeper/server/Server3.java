package cn.izern.test.zookeeper.server;

import java.util.Random;

/**
 * 服务3,  启动后注册在zookeeper中
 * @author root
 * 2018年3月21日 下午9:49:13
 */
public class Server3 extends AbstractServer  {
	
	private Long live = Long.valueOf((new Random().nextInt(80)+10));

	@Override
	public String service() {
		return "This is "+getName()+", current time is " + System.currentTimeMillis();
	}

	@Override
	public Long keepSecond() {
		return live;
	}

	@Override
	public String getName() {
		return "server3";
	}

}
