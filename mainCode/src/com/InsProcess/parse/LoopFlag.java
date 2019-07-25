package com.InsProcess.parse;

public class LoopFlag extends Ins {
    private String name;

    public LoopFlag(Sub sub, String flagName) {
        super(sub);
        name = sub.getSubName() + "_" + flagName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ":\n";
    }
}
