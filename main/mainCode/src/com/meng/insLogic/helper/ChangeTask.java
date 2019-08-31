package com.meng.insLogic.helper;

public class ChangeTask implements Cloneable {
    public boolean singleMode;
    public int mode;
    public int a;
    public int b;
    public int c;
    public int d;
    public float m;
    public float n;
    public float r;
    public float s;

    public ChangeTask(boolean singleMode, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
        this.singleMode = singleMode;
        this.mode = mode;
        a = inta;
        b = intb;
        c = intc;
        d = intd;
        m = floatm;
        n = floatn;
        r = floatr;
        s = floats;
    }

    @Override
    public ChangeTask clone() {
        try {
            return (ChangeTask) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
