package com.xzq.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	public static void main(String[] args) {
		EventLoopGroup worker = new NioEventLoopGroup();
		Bootstrap bs = new Bootstrap();
		bs.group(worker).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new EchoClientHandler());
					}
				});
		try {
			ChannelFuture future = bs.connect("127.0.0.1", 8989).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			worker.shutdownGracefully();
		}

	}

}
