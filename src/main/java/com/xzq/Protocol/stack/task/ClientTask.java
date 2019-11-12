package com.xzq.Protocol.stack.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzq.Protocol.stack.client.Client;
import com.xzq.Protocol.stack.constant.MessageType;
import com.xzq.Protocol.stack.dto.Header;
import com.xzq.Protocol.stack.dto.NettyMessage;

import io.netty.channel.Channel;

public class ClientTask implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(Client.class);

	private Channel channel;

	public ClientTask(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(5000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			NettyMessage message = new NettyMessage();
			Header header = new Header();
			header.setSessionID(123456l);
			header.setPriority(Byte.valueOf(String.valueOf(i)));
			header.setType(MessageType.LOGIN_REQ.value());
			message.setHeader(header);
			LOG.info("currentThread : {} , send message : {}", Thread.currentThread().getName(), message);
			channel.writeAndFlush(message);
		}
	}

}
