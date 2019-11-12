package com.xzq.Protocol.stack.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xzq.Protocol.stack.codec.NettyMessageDecoder;
import com.xzq.Protocol.stack.codec.NettyMessageEncoder;
import com.xzq.Protocol.stack.task.ClientTask;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {
	
	private static final ExecutorService es = Executors.newFixedThreadPool(10);
	
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
						p.addLast(new IdleStateHandler(0, 2, 0));
						p.addLast(new LoginAuthReqHandler());
						p.addLast(new HeartBeatClientHandler());
					}
				});
		try {
			ChannelFuture future = bs.connect("127.0.0.1", 8080).sync();
			es.execute(new ClientTask(future.channel()));
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			worker.shutdownGracefully();
		}
	}

	
}
