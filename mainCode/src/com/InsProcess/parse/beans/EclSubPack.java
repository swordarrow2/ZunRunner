package com.InsProcess.parse.beans;

public class EclSubPack {
    public String subName;
    public int position;
    public EclSub sub;

    @Override
    public String toString() {
        sub.insList();
        return sub.toString();
    }
}
