package com.xzq.Protocol.stack.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzq.Protocol.stack.constant.MessageType;
import com.xzq.Protocol.stack.dto.Header;
import com.xzq.Protocol.stack.dto.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginAuthReqHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }
	
	private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setSessionID(123456l);
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        LOG.info("send message : {}", message);
        return message;
    }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if(message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                // 握手失败，关闭连接
                ctx.close();
            } else {
                LOG.info("Login is ok : " + message);
                ctx.fireChannelRead(msg);
            }
		} else {
			ctx.fireChannelRead(msg);
		}
		super.channelRead(ctx, msg);
	}

}
