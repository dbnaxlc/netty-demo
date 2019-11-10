package com.xzq.Protocol.stack.codec;

import java.io.IOException;
import java.util.Map;

import com.xzq.Protocol.stack.dto.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {
	
	private MarshallingEncoder encoder;
	
	public NettyMessageEncoder() throws IOException {
		this.encoder = new MarshallingEncoder();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
		out.writeInt(msg.getHeader().getCrcCode());
		out.writeInt(msg.getHeader().getLength());
		out.writeLong(msg.getHeader().getSessionID());
		out.writeByte(msg.getHeader().getType());
		out.writeByte(msg.getHeader().getPriority());
		if(msg.getHeader().getAttachment() != null) {
			out.writeInt(msg.getHeader().getAttachment().size());
			for(Map.Entry<String, Object> ent : msg.getHeader().getAttachment().entrySet()) {
				byte[] keyByteArray = ent.getKey().getBytes("utf-8");
				out.writeInt(keyByteArray.length);
				out.writeBytes(keyByteArray);
				encoder.encode(ent.getValue(), out);
			}
		}
		if(msg.getBody() == null) {
			out.writeInt(0);
		} else {
			encoder.encode(msg.getBody(), out);
		}
		out.setInt(4, out.readableBytes() - 8);
	}

}
