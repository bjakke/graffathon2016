package com.github.bjakke;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class Util {

	static FloatBuffer allocateDirectFloatBuffer(int n){
		return ByteBuffer.allocateDirect(n * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}

	static IntBuffer allocateDirectIntBuffer(int n){
		return ByteBuffer.allocateDirect(n * Integer.BYTES).order(ByteOrder.nativeOrder()).asIntBuffer();
	}

}
