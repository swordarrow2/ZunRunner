package com.InsProcess.helper;

public class EclVar {
    public static final int typeInt = 0;
    public static final int typeFloat = 1;
    public static final int typeString = 2;

    public int type;
    public int i;
    public float f;
    public String s;

    public EclVar(){

    }

    public EclVar(int value) {
        type = typeInt;
        i = value;
    }

    public EclVar(float value) {
        type = typeFloat;
        f = value;
        i = (int) value;
    }

    public EclVar(String value) {
        type = typeString;
        s = value;
    }

    @Override
    public String toString() {
        switch (type) {
            case typeInt:
                return String.valueOf(i);
            case typeFloat:
                return String.valueOf(f);
            case typeString:
                return s;
            default:
                return null;
        }
    }
}
