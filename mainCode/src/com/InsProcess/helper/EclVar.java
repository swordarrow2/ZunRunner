package com.InsProcess.helper;

public class EclVar {
    public static final int typeInt = 0;
    public static final int typeFloat = 1;
    public static final int typeString = 2;

    public int type;
    public int intValve;
    public float floatValue;
    public String stringValue;

    public EclVar(int value) {
        type = typeInt;
        intValve = value;
    }

    public EclVar(float value) {
        type = typeFloat;
        floatValue = value;
		intValve=(int)value;
    }

    public EclVar(String value) {
        type = typeString;
        stringValue = value;
		floatValue=Float.parseFloat(value);
		intValve=(int)floatValue;
    }
}
