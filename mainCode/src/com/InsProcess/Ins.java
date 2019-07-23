package com.InsProcess;

import java.util.HashMap;

public class Ins {
    private Sub sub;
    private final String lineStart = "    ";
    private final String lineEnd = ";\n";
    private int dan = -1;
    private HashMap<String, VarType> typeMap = new HashMap<>();
    private final String varNotDefine = "var not defined";
    private final String varTypeUnkonwn = "var type unknown";

    private StringBuilder stringBuilder = new StringBuilder();

    Ins(Sub sub, boolean... isInt) {
        this.sub = sub;
        if (isInt.length > 0) {
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
        stringBuilder.append("if ").append(judge).append(" goto ").append(loopFlag.getName()).append(" @ 0;\n");
        return this;
    }

    public Ins ifxGotoAt(String judge, LoopFlag loopFlag, int at) {
        stringBuilder.append("if ").append(judge).append(" goto ").append(loopFlag.getName()).append(" @ ").append(at).append(";\n");
        return this;
    }

    public Ins unlessxGoto(String judge, LoopFlag loopFlag) {
        stringBuilder.append("unless ").append(judge).append(" goto ").append(loopFlag.getName()).append(" @ 0;\n");
        return this;
    }

    public Ins unlessxGotoAt(String judge, LoopFlag loopFlag, int at) {
        stringBuilder.append("unless ").append(judge).append(" goto ").append(loopFlag.getName()).append(" @ ").append(at).append(";\n");
        return this;
    }

    public Ins numberIns(String text) {
        stringBuilder.append(text);
        return this;
    }

    public Ins args(boolean... isInt) {
        if (isInt.length < 1) {
			stringBuilder.append("Var;");
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
            return "_ff %" + name;
        }
        if (varType == VarType.intVar) {
            return "_SS $" + name;
        }
        throw new EclException(varTypeUnkonwn);
    }


    public String transferIntToFloat(String name) {
        return "_fS $" + name;
    }

    public String transferFloatToInt(String name) {
        return "_Sf %" + name;
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

    public Ins _602(String form, String color) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_602(%d, %s, %s)", dan, form, color));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _603(String offsetX, String offsetY) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_603(%d, %s, %s)", dan, offsetX, offsetY));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _604(String direct, String r) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_604(%d, %s, %s)", dan, direct, r));
        stringBuilder.append(lineEnd);
        return this;
    }

    public Ins _605(String speed, String slowlestSpeed) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_605(%d, %s, %s)", dan, speed, slowlestSpeed));
        stringBuilder.append(lineEnd);
        return this;
    }

	public Ins _606(String way, String ceng) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_606(%d, %s, %s)", dan, way, ceng));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _607(String style) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_607(%d, %s)", dan, style));
        stringBuilder.append(lineEnd);
        return this;
	  }
	
	public Ins _608(String voiceOnShoot, String voiceOnChange) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_608(%d, %s, %s)", dan, voiceOnShoot, voiceOnChange));
        stringBuilder.append(lineEnd);
        return this;
	  }
	
	public Ins _609(String num, String way, String inta, String intb, String floatr,String floats) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_609(%d, %s, %s, %s, %s, %s, %s)", dan, num, way, inta, intb, floatr, floats));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _610(String num, String way, String inta, String intb, String intc, String intd, String floatr, String floats, String floatm, String floatn) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_610(%d, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", dan, num, way, inta, intb, intc, intd , floatr, floats, floatm, floatn));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _611(String way, String inta, String intb, String floatr,String floats) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_609(%d, %s, %s, %s, %s, %s)", dan, way, inta, intb, floatr, floats));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _612(String way, String inta, String intb, String intc, String intd, String floatr, String floats, String floatm, String floatn) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_610(%d, %s, %s, %s, %s, %s, %s, %s, %s, %s)", dan, way, inta, intb, intc, intd , floatr, floats, floatm, floatn));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _613() {
        stringBuilder.append(lineStart);
        stringBuilder.append("ins_613()");
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _614(String danmakuA, String danmakuB) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_614(%s, %s)", danmakuA, danmakuB));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _615(String floatR) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_615(%s)", floatR));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _616(String floatR) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_616(%s)", floatR));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _623(String floatVarName, String floatX, String floatY) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_623(%s, %s, %s)", floatVarName, floatX, floatY));
        stringBuilder.append(lineEnd);
        return this;
	  }
	
	public Ins _624(String floatA, String b, String c, String d, String e, String f, String g, String h) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_624(%d, %s, %s, %s, %s, %s, %s, %s, %s)", dan, floatA, b, c, d, e, f, g, h));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _625(String intA, String b, String c, String d, String e, String f, String g, String h) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_625(%d, %s, %s, %s, %s, %s, %s, %s, %s)", dan, intA, b, c, d, e, f, g, h));
        stringBuilder.append(lineEnd);
        return this;
	  }
	
	public Ins _626(String floatAngel, String r) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_626(%d, %s, %s)", dan, floatAngel, r));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _627(String r) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_627(%d, %s)", dan, r));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _628(String floatX, String y) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_628(%d, %s, %s)", dan, floatX, y));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _629(String floatR, String intRgb) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_629(%s, %s)", floatR, intRgb));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  //int
	public Ins _630(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_630(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _631(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_631(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _632(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_632(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _633(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_633(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _634(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_634(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  //float
	public Ins _635(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_635(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _636(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_636(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  //int
	public Ins _637(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_637(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _638(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_638(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _639(String a) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_639(%s)", a));
        stringBuilder.append(lineEnd);
        return this;
	  }
	//mode=16777216 dan=2
	public Ins _640(String intMode, Sub sub) {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_640(%d, %s, %s)", dan, intMode, sub.getSubName()));
        stringBuilder.append(lineEnd);
        return this;
	  }
	  
	public Ins _641() {
        stringBuilder.append(lineStart);
        stringBuilder.append(String.format("ins_642(%d)", dan));
        stringBuilder.append(lineEnd);
        return this;
	  }
	    
    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}
