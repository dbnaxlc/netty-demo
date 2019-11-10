package com.xzq.Protocol.stack.codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.xzq.Protocol.stack.dto.Header;
import com.xzq.Protocol.stack.dto.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	MarshallingDecoder marshallingDecoder;
	

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		marshallingDecoder = new MarshallingDecoder();
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf)super.decode(ctx, in);
		if (frame == null) {
		    return null;
		}
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionID(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());
		int asize = frame.readInt();
		if(asize > 0 ) {
			Map<String, Object> att = new HashMap<String, Object>();
			byte[] key = new byte[frame.readInt()];
			frame.readBytes(key);
			att.put(new  String(key, "utf-8"), marshallingDecoder.decode(frame));
			header.setAttachment(att);
		}
		Object body = null;
		if(frame.readableBytes() > 4) {
			body = marshallingDecoder.decode(frame);
		}
		message.setHeader(header);
		message.setBody(body);
		return message;
	}

	
}
