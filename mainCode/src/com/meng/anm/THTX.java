package com.meng.anm;

public class THTX {
	public byte[] magic = new byte[4];
	public short zero;
	public short format;
	/* These may be different from the parent entry. */
	public short w;
	public short h;
	public int size;
	public byte[] data;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "format:" + format + " w:" + w + " h:" + h + " size:" + size + " dataLength:" + data.length;
	}
}
