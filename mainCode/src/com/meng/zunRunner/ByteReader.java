package com.meng.zunRunner;

public class ByteReader {
	public byte[] fileByte;
	public int position = 0;

	public ByteReader(byte[] bs) {
		fileByte = bs;
	}

	public byte readByte() {
        return fileByte[position++];
    }

	public byte readByte(int pos) {
		return fileByte[pos];
	}

	public short readShort() {
        return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
    }

	public int readInt() {
        return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16 | (fileByte[position++] & 0xff) << 24;
    }

	public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

	public void readArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = readInt();
        }
    }

	public void readArray(byte[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = readByte();
        }
    }

	public void moveToNextInt() {
        if ((position & 0b11) == 0) {
            return;
        }
        position |= 0b11;
        ++position;
    }
}
