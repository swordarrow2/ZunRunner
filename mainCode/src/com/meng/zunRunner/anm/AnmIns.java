package com.meng.zunRunner.anm;

public class AnmIns {
	public short id;
	public short length;
	public short time;
	/* TODO: Implement this, it works similarly to that one in ECL files. */
	public short param_mask;
	public byte data[];

	@Override
	public String toString() {
		return "type:" + id + " len:" + length + " time:" + time + " param:" + param_mask + " dataLen:" + data.length;
	}
}
