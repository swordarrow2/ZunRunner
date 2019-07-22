package com.InsProcess;

public class zunRunner {
    private Ecl ecl;

    public zunRunner() {
        ecl = new Ecl();
        final Sub testSub = ecl.sub("testSub", true, false);
        testSub.addIns(new Runnable() {

            @Override
            public void run() {
                Ins ins = testSub.ins();
                ins._600();
                ins._602(2, 5);
                ins.assign(A, 43).assign(B, 64);
                ins._603(ins.use(A), ins.use(B));
                ins._601();
                ins._600();
                ins._602(4, 2);
                ins._603(85, 12);
                ins._601();
                ins._601(0);
            }
        });

        final Sub mainFront = ecl.sub("MainFront",false,false);
        mainFront.addIns(new Runnable() {

            @Override
            public void run() {
                Ins v = mainFront.ins();
                v.args( true, true, false, false)
                        ._600()
                        .posAndImg(64, 64, 2, 5)
                        ._601()
                        ._600()
                        .assign(A, 64).assign(B, 54).assign(C, 3).assign(D, 7)
                        .diffSwitch(3, 6, 9, 12, 15)
                        .posAndImg(v.getStack(-1), v.getStack(-1), "2", "5")
                        ._11(testSub, v.transfer(A), v.transfer(B))
                        ._602(4, 2)
                        ._603(v.use(E), v.use(F))
                        ._601()
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
