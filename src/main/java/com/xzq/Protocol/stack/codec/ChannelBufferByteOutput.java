package com.xzq.Protocol.stack.codec;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

import io.netty.buffer.ByteBuf;

public class ChannelBufferByteOutput implements ByteOutput {

	private final ByteBuf buffer;

	public ChannelBufferByteOutput(ByteBuf buffer) {
		this.buffer = buffer;
	}

	@Override
	public void close() throws IOException {
		// Nothing to do
	}

	@Override
	public void flush() throws IOException {
		// nothing to do
	}

	@Override
	public void write(int b) throws IOException {
		buffer.writeByte(b);
	}

	@Override
	public void write(byte[] bytes) throws IOException {
		buffer.writeBytes(bytes);
	}

	@Override
	public void write(byte[] bytes, int srcIndex, int length) throws IOException {
		buffer.writeBytes(bytes, srcIndex, length);
	}

	ByteBuf getBuffer() {
		return buffer;
	}

}
