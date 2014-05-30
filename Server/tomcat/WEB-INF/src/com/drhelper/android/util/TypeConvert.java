package com.drhelper.android.util;

import java.nio.ByteBuffer;

public class TypeConvert {
	public static ByteBuffer getByteBufferFromString(String str) {
		return ByteBuffer.wrap(str.getBytes());
	}
	
	public static String getStringFromByteBuffer(ByteBuffer buf) {
		byte[] data = buf.array();  
		String msg = new String(data).trim();
		return msg;
	}
}
