package com.xzq;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerUnexpectedExit {

	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap boot = new ServerBootstrap().group(group)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO) )
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
						
					}
				});
		try {
			ChannelFuture future = boot.bind(8081).sync();
			future.channel().closeFuture().addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					System.out.println("链路关闭");
					
				}
			});
			System.out.println("xxxxxxxx");
		} catch (InterruptedException e) {
			group.shutdownGracefully();
		}
	}

}
