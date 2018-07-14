package io.zbus.examples;


import java.util.HashMap;
import java.util.Map;

import io.zbus.mq.MqServer;
import io.zbus.rpc.RpcProcessor;
import io.zbus.rpc.RpcServer;
import io.zbus.rpc.annotation.RequestMapping;
import io.zbus.transport.Message;

public class Main {
	// default: /plus/{a}/{b}
	public int plus(int a, int b) {
		return a + b;
	}

	@RequestMapping(path = "/abc") // default path could be changed
	public Object json() {
		Map<String, Object> value = new HashMap<>();
		value.put("key", System.currentTimeMillis());
		return value;
	}

	/**
	 * Example of returning HTTP message type
	 * 
	 * @return
	 */
	public Message home() {
		Message res = new Message();
		res.setStatus(200);
		res.setHeader("content-type", "text/html; charset=utf8");
		res.setBody("<h1>java home page</h1>");

		return res;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		RpcProcessor p = new RpcProcessor();
		p.mount("/", Main.class);

		// Serve RPC embedded in MqServer
		MqServer mqServer = new MqServer(15555);
		RpcServer rpcServer = new RpcServer();
		rpcServer.setMqServer(mqServer); // embedded in MqServer
		rpcServer.setRpcProcessor(p);
		rpcServer.start();
	}
}
