package com.InsProcess;

public class zunRunner {
    private Ecl ecl;

    public zunRunner() {
        ecl = new Ecl();
        final Sub testSub = ecl.sub("TestSub", true, false);
        testSub.addIns(new Runnable() {

            @Override
            public void run() {
                Ins ins = testSub.ins();
                ins._600();
                ins._602(2, 5);
                ins.assign(A, 43).assign(B, 64);
                ins._603(ins.use(A), ins.use(B));
                ins._601();
                LoopFlag loop1 = ins.loop(9961);
                ins._600()
                        ._602(4, 2)
                        ._603(85, 12)
                        ._601()
                        .gotoLoopFlag(loop1)
                        ._601(0);
            }
        });

        final Sub mainFront = ecl.sub("MainFront", false, false);
        mainFront.addIns(new Runnable() {

            @Override
            public void run() {
                Ins ins = mainFront.ins();
                ins.args(true, true, false, false)
                        ._600()
                        ._602(64, 64)
                        ._603(5, 2)
                        ._601()
                        ._600()
                        .assign(A, 64).assign(B, 54)
                        .assign(C, 3).assign(D, 7)
                        .assign(E, 99).assign(F, 61)
                        .diffSwitch(3, 6, 9, 12, 15);
                LoopFlag flag1 = ins.loop(9961);
                ins._11(testSub, ins.transfer(A), ins.transfer(B))
                        .push(4).push(5.0)
                        ._602(ins.pop(1), ins.pop(1))
                        ._601()
                        ._603(ins.use(E), ins.use(F))
                        .ifxGoto("1", flag1)
                        ._601(0);
            }
        });
    }

    @Override
    public String toString() {
        return ecl.toString();
    }

    public static final String A = "A";
    public static final String B = "B";
    public static final String C = "C";
    public static final String D = "D";
    public static final String E = "E";
    public static final String F = "F";
    public static final String G = "G";
    public static final String H = "H";
    public static final String I = "I";
    public static final String J = "J";
}
