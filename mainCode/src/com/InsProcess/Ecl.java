package com.InsProcess;

import java.util.*;

public class Ecl {
    private ArrayList<Sub> subs = new ArrayList<>();

    public Sub sub(String name) {
        return sub(name, 0);
    }

    public Sub sub(String name, int argLength, boolean... isInt) {
        Sub s = new Sub(name, argLength, isInt);
        subs.add(s);
        return s;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Sub s : subs) {
            stringBuilder.append(s.toString());
        }
        return stringBuilder.toString();
    }
}
