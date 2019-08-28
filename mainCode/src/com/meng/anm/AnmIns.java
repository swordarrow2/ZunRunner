package com.meng.anm;

public class AnmIns {
	public short type;
	public short length;
	public short time;
	/* TODO: Implement this, it works similarly to that one in ECL files. */
	public short param_mask;
	public byte data[];

	@Override
	public String toString() {
		return "type:" + type + " len:" + length + " time:" + time + " param:" + param_mask + " dataLen:" + data.length;
	}
}
