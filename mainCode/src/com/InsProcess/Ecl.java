package com.InsProcess;

import java.util.*;

public class Ecl {
    private ArrayList<Sub> subs = new ArrayList<>();

    public Sub sub(String name, boolean... isInt) {
        Sub s = new Sub(name, isInt);
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
