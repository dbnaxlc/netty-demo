package com.xzq.protocol.stack.codec;

import java.io.IOException;

import com.xzq.Protocol.stack.codec.MarshallingDecoder;
import com.xzq.Protocol.stack.codec.MarshallingEncoder;


public class TestCodec {

	MarshallingEncoder marshallingEncoder;
	MarshallingDecoder marshallingDecoder;

	public TestCodec() throws IOException {
		marshallingDecoder = new MarshallingDecoder();
		marshallingEncoder = new MarshallingEncoder();
	}
}
