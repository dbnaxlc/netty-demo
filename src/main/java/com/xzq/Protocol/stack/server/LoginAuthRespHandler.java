package com.xzq.Protocol.stack.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzq.Protocol.stack.constant.MessageType;
import com.xzq.Protocol.stack.dto.Header;
import com.xzq.Protocol.stack.dto.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(LoginAuthRespHandler.class);

	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		LOG.info("receive message : {}", message);
		if (message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
			String remoteIp = ctx.channel().remoteAddress().toString();
			if (nodeCheck.get(remoteIp) == null) {
				LOG.info("The login response body is [ 0]");
				ctx.writeAndFlush(buildResp((byte)0));
			} else {
				LOG.info("The login response body is [ -1]");
				ctx.writeAndFlush(buildResp((byte)-1));
			}
		}
	}

	private NettyMessage buildResp(Object body) {
		NettyMessage resp = new NettyMessage();
		Header header = new Header();
		header.setSessionID(111l);
		header.setType(MessageType.LOGIN_RESP.value());
		Map<String, Object> att = new HashMap<String, Object>();
		att.put("name", "xzq");
		header.setAttachment(att);
		resp.setHeader(header);
		resp.setBody(body);
		return resp;
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}

}
