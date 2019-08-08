package com.InsProcess.parse.beans;

import com.InsProcess.parse.*;

public class EclSubPack {
    public String subName;
    public int position;
    public EclSub sub;

    @Override
    public String toString() {
        return sub.toString();
    }
}
