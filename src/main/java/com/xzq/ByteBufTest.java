package com.xzq;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class ByteBufTest {

	public static void main(String[] args) {
		PooledByteBufAllocator allocator = new PooledByteBufAllocator();
		ByteBuf buf = allocator.heapBuffer(10240);
		buf.release();
		
		buf = Unpooled.buffer(10240);
		buf.release();
	}

}
