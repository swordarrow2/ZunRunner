package com.meng.zunRunner.ecl.beans;

public class EclSubPack {
    public String subName;
    public int position;
    public EclSub sub;

    @Override
    public String toString() {
        return sub.toString();
    }
}
