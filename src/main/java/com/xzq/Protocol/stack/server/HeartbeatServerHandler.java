package com.xzq.Protocol.stack.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
	
	private int connectLossCount = 0;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.READER_IDLE){
            	connectLossCount++;
                if (connectLossCount>2){
                    System.out.println("关闭这个不活跃通道！");
                    ctx.channel().close();
                }
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		connectLossCount = 0;
		System.out.println("心跳检测线程接收数据");
	}

	
	
}
