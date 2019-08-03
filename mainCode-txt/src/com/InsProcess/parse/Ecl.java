package com.InsProcess.parse;

import com.InsProcess.helper.EclVar;

import java.util.*;

import com.meng.TaiHunDanmaku.ui.*;

public class Ecl {
    public static HashSet<Sub> runningSubs = new HashSet<>();
    public static HashSet<Sub> toAddSubs = new HashSet<>();
    public static HashSet<Sub> toDeleteSubs = new HashSet<>();

    private ArrayList<Sub> subs = new ArrayList<>();
    public String nextPartName;


    public Sub sub() {
        Sub s = new Sub(this);
        subs.add(s);
        return s;
    }

    public static void update() {
        if (FightScreen.instence.boss == null) {
            runningSubs.clear();
            return;
        }
        for (Sub runningSub : runningSubs) {
            runningSub.update();
        }
        runningSubs.addAll(toAddSubs);
        runningSubs.removeAll(toDeleteSubs);
        toAddSubs.clear();
        toDeleteSubs.clear();
    }

    public void next() {
        toAddSubs.add(getSub(nextPartName));
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
