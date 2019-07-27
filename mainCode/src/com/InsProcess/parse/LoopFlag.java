package com.InsProcess.parse;

public class LoopFlag extends Ins {
    private String name;

    public LoopFlag(String flagName) {
        super(0);
        name = flagName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ":\n";
    }
}
