package com.acgist.main;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.acgist.snail.net.dht.DhtClient;
import com.acgist.snail.system.bcode.BCodeEncoder;
import com.acgist.snail.system.config.SystemConfig;
import com.acgist.snail.system.exception.NetException;
import com.acgist.snail.utils.ThreadUtils;

public class DhtClientTest {

	@Test
	public void client() {
		InetSocketAddress address = new InetSocketAddress("127.0.0.1", SystemConfig.getDhtPort());
		DhtClient client = DhtClient.newInstance(address);
		while (true) {
			try {
				final Map<String, String> data = new HashMap<String, String>();
				data.put("v", "1.0.0");
				System.out.println("发送消息：" + data);
				client.send(ByteBuffer.wrap(BCodeEncoder.mapToBytes(data)), address);
			} catch (NetException e) {
				e.printStackTrace();
			}
			ThreadUtils.sleep(1000);
		}
	}
	
	private static final String host = "127.0.0.1";
	private static final int port = 49160; // FDM测试端口
	
	@Test
	public void ping() {
		DhtClient client = DhtClient.newInstance(host, port);
		boolean ping = client.ping();
		System.out.println(ping);
		ThreadUtils.sleep(Long.MAX_VALUE);
	}
	
	@Test
	public void findNode() {
		DhtClient client = DhtClient.newInstance(host, port);
		final String target = "5E5324691812CAA0032EA76E813CCFC4D04E7E9E";
		client.findNode(target);
		ThreadUtils.sleep(Long.MAX_VALUE);
	}
	
}