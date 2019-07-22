package com.InsProcess;

import java.util.*;

public class Sub {

    public ArrayList<Ins> inses = new ArrayList<>();
    private String subName;
    private int argLength = 0;
    public boolean[] isInt;

    Sub(String name, boolean... isInt) {
        subName = name;
        this.argLength = isInt.length;
        this.isInt = isInt;
    }

    public Ins ins() {
        Ins ins000 = new Ins(this, isInt);
        inses.add(ins000);
        return ins000;
    }

    public String getSubName() {
        return subName;
    }

    public void addIns(Runnable runnable) {
        runnable.run();
    }

    @Override
    public String toString() {
        StringBuilder sb1 = new StringBuilder();
        for (Ins i : inses) {
            sb1.append(i.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        for (int i = 65; i < 65 + argLength; ++i) {
            sb2.append(" ");
            sb2.append((char) i);
        }
        return String.format("sub %s(%s) {\n%s}\n\n", subName, sb2.toString(), sb1.toString());
    }
}
