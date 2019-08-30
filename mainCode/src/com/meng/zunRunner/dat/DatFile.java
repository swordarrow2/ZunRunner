package com.meng.zunRunner.dat;

public class DatFile {

    private byte[] fileByte;
    private int position = 0;
    
    
    
    
    
    
    
    private byte readByte() {
        return fileByte[position++];
    }

    private short readShort() {
        return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
    }

    private int readInt() {
        return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16 | (fileByte[position++] & 0xff) << 24;
    }

    private float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    private void readIntArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = readInt();
        }
    }
}
