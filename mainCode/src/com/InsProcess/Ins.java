package com.InsProcess;

import java.util.HashMap;

public class Ins {
    private EclException eclException = new EclException();
    private Sub sub;
    private final String lineStart = "    ";
    private final String lineEnd = ";\n";
    private int dan = -1;
    private HashMap<String, VarType> typeMap = new HashMap<>();

    private StringBuilder stringBuilder = new StringBuilder();

    Ins(Sub sub, boolean... isInt) {
        this.sub = sub;
        for (int i = 65; i < 65 + isInt.length; ++i) {
            typeMap.put(String.valueOf((char) i), isInt[i - 65] ? VarType.intVar : VarType.floatVar);
        }
    }

    enum VarType {
        intVar,
        floatVar
    }

    public Ins assign(String varName, float value) {
        VarType varType = typeMap.get(varName);
        if (varType == null) {
            throw eclException.varNotDefined();
        }
        if (typeMap.get(varName) == VarType.intVar) {
            stringBuilder.append(lineStart).append("$").append(varName).append(" = ").append((int) value).append(lineEnd);
            return this;
        }
        if (typeMap.get(varName) == VarType.floatVar) {
            stringBuilder.append(lineStart).append("%").append(varName).append(" = ").append(value).append("f").append(lineEnd);
            return this;
        }
        throw eclException.varNotDefined();
    }

    public String use(String varName) {
        VarType varType = typeMap.get(varName);
        if (varType == VarType.floatVar) {
            return "%" + varName;
        }
        if (varType == VarType.intVar) {
            return "$" + varName;
        }
        throw eclException.varNotDefined();
    }


    public Ins args(boolean... isInt) {
        if (isInt.length < 1) {
            return this;
        }
        stringBuilder.append(lineStart);
        stringBuilder.append("Var");
        for (int i = 65+sub.isInt.length; i < 65 + isInt.length+sub.isInt.length; ++i) {
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

    public String getStack(int i) {
        return "[" + i + "]";
    }

    public String transfer(String name) {
        VarType varType = typeMap.get(name);
        if (varType == VarType.floatVar) {
            return "_ff " + name;
        }
        if (varType == VarType.intVar) {
            return "_SS " + name;
        }
        throw eclException.varTypeUnknown();
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


    public Ins posAndImg(String x, String y, String color, String form) {
        _602(color, form)._603(x, y);
        return this;
    }

    public Ins posAndImg(float x, float y, int color, int form) {
        _602(color, form)._603(x, y);
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}
