package com.InsProcess.parse;

import com.badlogic.gdx.math.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.*;
import com.meng.TaiHunDanmaku.ui.*;
import java.util.*;

public class EclStack {

	private final int maxDepth = 64;
    private int depth = 0;
    private int[] stack = new int[maxDepth];
	private byte[] varArray;
	public HashMap<Integer,Integer> values=new HashMap<>();
	private int opCount=0;
	public void initVarSize(int i) {
		varArray = new byte[i];
	  }

	//public int read(int start) {
	//	return (varArray[start] & 0xff) | (varArray[start + 1] & 0xff) << 8 | (varArray[start + 2] & 0xff) << 16 | (varArray[start + 3] & 0xff) << 24;
	//  }

	public int getInt(int i) {
		if (i >= 0) {
			return (varArray[i] & 0xff) | (varArray[i + 1] & 0xff) << 8 | (varArray[i + 2] & 0xff) << 16 | (varArray[i + 3] & 0xff) << 24;
		  } else if (i == -1) {
			return popInt();
		  } else {	
			  switch (i) {
				  case -9947:
				  case -9948:
				  case -9949:
					return values.get(i);
				  case -9954:
					return (int)FightScreen.instence.boss.hp;
				  case -9978:
				  case -9979:
				  case -9980:
					return values.get(i);
				  case -9986:
					return 0;
				  case -9988:
					return FightScreen.instence.gameTimeFlag;
				  case -10000:
					return new RandomXS128().nextInt();
				  default:	
				  throw new NullPointerException("unexpect valie:"+i);
				  }	
		//	return values.get(i);
		  }
	  }

	public float getFloat(int i) {
		if (i >= 0) {
			return Float.intBitsToFloat((varArray[i] & 0xff) | (varArray[i + 1] & 0xff) << 8 | (varArray[i + 2] & 0xff) << 16 | (varArray[i + 3] & 0xff) << 24);
		  } else if (i == -1) {
			return popFloat();
		  } else {
			return Float.intBitsToFloat(values.get(i));
		  }
	  }

	public void putGlobal(int key, int value) {
		if (key >= 0) {
			varArray[key] = (byte) ((value >> 24) & 0xFF);
			varArray[key + 1] = (byte) ((value >> 16) & 0xFF);
			varArray[key + 2] = (byte) ((value >> 8) & 0xFF);
			varArray[key + 3] = (byte) (value & 0xFF);
		  } else {
			values.put(key, value);
		  }
	  }

	public void putGlobal(int key, float v) {
		int value=Float.floatToIntBits(v); 
		if (key >= 0) {
			varArray[key] = (byte) ((value >> 24) & 0xFF);
			varArray[key + 1] = (byte) ((value >> 16) & 0xFF);
			varArray[key + 2] = (byte) ((value >> 8) & 0xFF);
			varArray[key + 3] = (byte) (value & 0xFF);
		  } else {
			values.put(key, value);
		  }
	  }


    public void push(int n) {
	  ++opCount;
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
		  }
        stack[depth++] = n;
	  }

    public void push(float n) {
		++opCount;
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
		  }
        stack[depth++] = Float.floatToIntBits(n);
	  }

    public int popInt() {
		++opCount;
        if (depth == 0) {
            throw new RuntimeException("stack blank,count:"+opCount);
		  }
		return stack[depth--];
	  }

    public float popFloat() {
		++opCount;
        if (depth == 0) {
            throw new RuntimeException("stack blank");
		  }
        return Float.intBitsToFloat(stack[depth--]);
	  }

    public int peekInt() {
		++opCount;
        if (depth == 0) {
            throw new RuntimeException("stack blank");
		  }
        return stack[depth - 1];
	  }

	public float peekFloat() {
		++opCount;
        if (depth == 0) {
            throw new RuntimeException("stack blank");
		  }
        return Float.intBitsToFloat(stack[depth - 1]);
	  }
  }
