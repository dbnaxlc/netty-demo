package com.xzq.protocol.stack.codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.xzq.Protocol.stack.codec.MarshallingDecoder;
import com.xzq.Protocol.stack.codec.MarshallingEncoder;
import com.xzq.Protocol.stack.dto.Header;
import com.xzq.Protocol.stack.dto.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TestCodec {

	MarshallingEncoder marshallingEncoder;
	MarshallingDecoder marshallingDecoder;

	public TestCodec() throws IOException {
		marshallingDecoder = new MarshallingDecoder();
		marshallingEncoder = new MarshallingEncoder();
	}

	public ByteBuf encode(NettyMessage msg) throws Exception {
		ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeInt((msg.getHeader().getCrcCode()));
		sendBuf.writeInt((msg.getHeader().getLength()));
		sendBuf.writeLong((msg.getHeader().getSessionID()));
		sendBuf.writeByte((msg.getHeader().getType()));
		sendBuf.writeByte((msg.getHeader().getPriority()));
		sendBuf.writeInt((msg.getHeader().getAttachment().size()));
		String key = null;
		byte[] keyArray = null;
		Object value = null;

		for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue();
			marshallingEncoder.encode(value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		if (msg.getBody() != null) {
			marshallingEncoder.encode(msg.getBody(), sendBuf);
		} else
			sendBuf.writeInt(0);
		sendBuf.setInt(4, sendBuf.readableBytes());
		return sendBuf;
	}

	public NettyMessage decode(ByteBuf in) throws Exception {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(in.readInt());
		header.setLength(in.readInt());
		header.setSessionID(in.readLong());
		header.setType(in.readByte());
		header.setPriority(in.readByte());

		int size = in.readInt();
		if (size > 0) {
			Map<String, Object> attch = new HashMap<String, Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for (int i = 0; i < size; i++) {
				keySize = in.readInt();
				keyArray = new byte[keySize];
				in.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attch.put(key, marshallingDecoder.decode(in));
			}
			keyArray = null;
			key = null;
			header.setAttachment(attch);
		}
		if (in.readableBytes() > 4) {
			message.setBody(marshallingDecoder.decode(in));
		}
		message.setHeader(header);
		return message;
	}

	public static void main(String[] args) throws Exception {
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setLength(0);
		header.setSessionID(99999);
		header.setType((byte) 1);
		header.setPriority((byte) 7);
		Map<String, Object> attachment = new HashMap<String, Object>();
		for (int i = 0; i < 10; i++) {
			attachment.put("ciyt --> " + i, "xzq " + i);
		}
		header.setAttachment(attachment);
		nettyMessage.setHeader(header);
		nettyMessage.setBody("abcdefg-----------------------AAAAAA");
		TestCodec tt = new TestCodec();
		ByteBuf buf = tt.encode(nettyMessage);
		System.out.println(tt.decode(buf));
	}
}
