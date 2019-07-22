package com.InsProcess;

import java.util.HashMap;

public class Ins {
    private Sub sub;
    private final String lineStart = "    ";
    private final String lineEnd = ";\n";
    private int dan = -1;
    private HashMap<String, VarType> typeMap;
    private final String varNotDefine = "var not defined";
    private final String varTypeUnkonwn = "var type unknown";

    private StringBuilder stringBuilder = new StringBuilder();

    Ins(Sub sub, boolean... isInt) {
        this.sub = sub;
        if (isInt.length > 0) {
            typeMap = new HashMap<>();
            for (int i = 65; i < 65 + isInt.length; ++i) {
                typeMap.put(String.valueOf((char) i), isInt[i - 65] ? VarType.intVar : VarType.floatVar);
            }
        }
    }

    enum VarType {
        intVar,
        floatVar
    }

    public Ins assign(String varName, float value) {
        VarType varType = typeMap.get(varName);
        if (varType == null) {
            throw new EclException(varNotDefine);
        }
        if (typeMap.get(varName) == VarType.intVar) {
            stringBuilder.append(lineStart).append("$").append(varName).append(" = ").append((int) value).append(lineEnd);
            return this;
        }
        if (typeMap.get(varName) == VarType.floatVar) {
            stringBuilder.append(lineStart).append("%").append(varName).append(" = ").append(value).append("f").append(lineEnd);
            return this;
        }
        throw new EclException(varNotDefine);
    }

    public String use(String varName) {
        VarType varType = typeMap.get(varName);
        if (varType == VarType.floatVar) {
            return "%" + varName;
        }
        if (varType == VarType.intVar) {
            return "$" + varName;
        }
        throw new EclException(varNotDefine);
    }

    public LoopFlag loop(int flagName) {
        LoopFlag flag = new LoopFlag(sub, String.valueOf(flagName));
        stringBuilder.append(flag.toString());
        return flag;
    }

    public Ins gotoLoopFlag(LoopFlag loopFlag) {
        stringBuilder.append("goto " + loopFlag.getName() + " @ 0;\n");
        return this;
    }

    public Ins ifxGoto(String judge, LoopFlag loopFlag) {
        stringBuilder.append("if ").append(judge).append(" ").append(loopFlag.getName()).append(" @ 0;\n");
        return this;
    }

    public Ins ifxGotoAt(String judge, LoopFlag loopFlag, int at) {
        stringBuilder.append("if ").append(judge).append(" ").append(loopFlag.getName()).append(" @ ").append(at).append(";\n");
        return this;
    }

    public Ins unlessxGoto(String judge, LoopFlag loopFlag) {
        stringBuilder.append("unless ").append(judge).append(" ").append(loopFlag.getName()).append(" @ 0;\n");
        return this;
    }

    public Ins unlessxGotoAt(String judge, LoopFlag loopFlag, int at) {
        stringBuilder.append("unless ").append(judge).append(" ").append(loopFlag.getName()).append(" @ ").append(at).append(";\n");
        return this;
    }

    public Ins numberIns(String text) {
        stringBuilder.append(text);
        return this;
    }

    public Ins args(boolean... isInt) {
        if (isInt.length < 1) {
            return this;
        }
        stringBuilder.append(lineStart);
        stringBuilder.append("Var");
        for (int i = 65 + sub.isInt.length; i < 65 + isInt.length + sub.isInt.length; ++i) {
            stringBuilder.append(" ");
            stringBuilder.append((char) i);
            typeMap.put(String.valueOf((char) i), isInt[i - 65 - sub.isInt.length] ? VarType.intVar : VarType.floatVar);
        }
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _11(Sub sub, String... args) {

        stringBuilder.append(lineStart).append("ins_11(\"").append(sub.getSubName()).append("\"");
        for (String s : args) {
            stringBuilder.append(", ").append(s);
        }
        stringBuilder.append(")").append(lineEnd);
        return this;
    }

    public Ins _15(Sub sub, String... args) {
        stringBuilder.append(lineStart).append("ins_15(\"").append(sub.getSubName()).append("\"");
        for (String s : args) {
            stringBuilder.append(", ").append(s);
        }
        stringBuilder.append(")").append(lineEnd);
        return this;
    }


    public Ins diffSwitch(int e, int n, int h, int l, int o) {
        stringBuilder.append("!E").append("\n  ").append(e).append(";\n!N")
                .append("\n  ").append(n).append(";\n!H")
                .append("\n  ").append(h).append(";\n!L")
                .append("\n  ").append(l).append(";\n!O")
                .append("\n  ").append(o).append(";\n!*\n");
        return this;
    }

    public Ins push(int i) {
        stringBuilder.append(i).append(";\n");
        return this;
    }

    public Ins push(float f) {
        stringBuilder.append(f).append("f;\n");
        return this;
    }

    public Ins push(double d) {
        stringBuilder.append(d).append("f;\n");
        return this;
    }

    public String pop() {
        return pop(1);
    }

    public String pop(int i) {
        return "[-" + Math.abs(i) + "]";
    }

    public String transfer(String name) {
        VarType varType = typeMap.get(name);
        if (varType == VarType.floatVar) {
            return "_ff " + name;
        }
        if (varType == VarType.intVar) {
            return "_SS " + name;
        }
        throw new EclException(varTypeUnkonwn);
    }


    public String transferIntToFloat(String name) {
        return "_fS " + name;
    }

    public String transferFloatToInt(String name) {
        return "_Sf " + name;
    }

    public Ins _600() {
        ++dan;
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_600(%d)", dan));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _601() {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_601(%d)", dan));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _601(int i) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_601(%d)", i));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _602(int form, int color) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_602(%d, %d, %d)", dan, form, color));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _602(String form, String color) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_602(%d, %s, %s)", dan, form, color));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _603(float offsetX, float offsetY) {
        return _603(offsetX + "f", offsetY + "f");
    }

    public Ins _603(String offsetX, String offsetY) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_603(%d, %s, %s)", dan, offsetX, offsetY));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _604(float dir, float r) {
        return _604(dir + "f", r + "f");
    }

    public Ins _604(String dir, String r) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_604(%d, %s, %s)", dan, dir, r));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _605(String speed, String s) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_605(%d, %s, %s)", dan, speed, s));
        stringBuilder.append(lineEnd);
        return this;
    }


    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}
