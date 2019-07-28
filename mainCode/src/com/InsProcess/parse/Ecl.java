package com.InsProcess.parse;

import com.InsProcess.helper.EclVar;

import java.util.*;

public class Ecl {
    public static HashSet<Sub> runningSubs = new HashSet<>();
    private ArrayList<Sub> subs = new ArrayList<>();

    public Sub sub() {
        Sub s = new Sub(this);
        subs.add(s);
        return s;
    }

    public static void update() {
        for (Sub runningSub : runningSubs) {
            runningSub.update();
        }
    }

    public Sub getSub(String subName) {
        for (Sub s : subs) {
            if (s.subName.equals(subName)) {
                return s;
            }
        }
        return null;
    }
}
