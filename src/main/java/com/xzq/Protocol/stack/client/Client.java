package com.xzq.Protocol.stack.client;

import com.xzq.Protocol.stack.codec.NettyMessageDecoder;
import com.xzq.Protocol.stack.codec.NettyMessageEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	public static void main(String[] args) {
		EventLoopGroup worker = new NioEventLoopGroup();
		Bootstrap bs = new Bootstrap();
		bs.group(worker).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
						p.addLast(new NettyMessageEncoder());
						p.addLast(new LoginAuthReqHandler());
					}
				});
		try {
			ChannelFuture future = bs.connect("127.0.0.1", 8080).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			worker.shutdownGracefully();
		}
	}

}
