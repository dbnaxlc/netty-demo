package com.xzq.Protocol.stack.server;

import com.xzq.Protocol.stack.codec.NettyMessageDecoder;
import com.xzq.Protocol.stack.codec.NettyMessageEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

	public static void main(String[] args) {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bs = new ServerBootstrap();
			bs.group(boss, worker).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128)
			.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
					p.addLast(new NettyMessageEncoder());
					p.addLast(new LoginAuthRespHandler());
				}
			});
			ChannelFuture future = bs.bind(8080).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			worker.shutdownGracefully();
			boss.shutdownGracefully();
		}
		
	}

}
