package com.InsProcess.parse;

public class zunRunner {
    private Ecl ecl;

    public zunRunner() {
        ecl = new Ecl();
          Sub testSub = ecl.sub(" BossCard7_at4(A B)\n" +
                "{\n" +
                "    var C D;\n" +
                "    $C = 5;\n" +
                "    ins_600($C);\n" +
                "    ins_607($C, 5);\n" +
                "    ins_602($C, $A, $B);\n" +
                "    ins_606($C, 24, 1);\n" +
                "    ins_604($C, 1.5707964f, 0.0f);\n" +
                "    ins_605($C, 4.0f, 0.2f);\n" +
                "    ins_611($C, 0, 2, 1, -999999, -999999.0f, -999999.0f);\n" +
                "    ins_627($C, 64.0f);\n" +
                "    $D = 10000;\n" +
                "    goto BossCard7_at4_404 @ 0;\n" +
                "BossCard7_at4_364:\n" +
                "    ins_601($C);\n" +
                "    ins_23(10);\n" +
                "BossCard7_at4_404:\n" +
                "    if $D-- goto BossCard7_at4_364 @ 0;\n" +
                "    ins_10();\n" +
                "}\n" +
                "\n");

    }
	private String toStr(int i){
	  return String.valueOf(i);
	}
	
	private String toStr(float i){
		return i+"f";
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
