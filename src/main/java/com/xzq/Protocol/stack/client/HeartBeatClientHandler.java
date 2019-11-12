package com.xzq.Protocol.stack.client;

import java.util.Date;

import com.xzq.Protocol.stack.constant.MessageType;
import com.xzq.Protocol.stack.dto.Header;
import com.xzq.Protocol.stack.dto.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("客户端循环心跳监测发送: " + new Date());
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.WRITER_IDLE) {
				NettyMessage message = new NettyMessage();
				Header header = new Header();
				header.setSessionID(123456l);
				header.setType(MessageType.HEARTBEAT_REQ.value());
				message.setHeader(header);
				ctx.writeAndFlush(message);
			}
		}
	}

}
