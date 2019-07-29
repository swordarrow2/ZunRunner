package com.InsProcess.parse;

import com.InsProcess.helper.*;
import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.ui.FightScreen;

import java.util.*;

import com.badlogic.gdx.math.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.*;

public class Sub {

    public ArrayList<Ins> inses = new ArrayList<>();
    public HashMap<String, EclVar> varsHashMap = new HashMap<>(16);
    public static HashMap<String, EclVar> globleVarHashMap = new HashMap<>(16);
    public String subName;
    public HashMap<Integer, BulletShooter> bulletShooters = new HashMap<>();
    public EclNumberStack numberStack = new EclNumberStack();
    public Ecl ecl;
    public int nowIns = 0;

    Sub(Ecl ecl) {
        this.ecl = ecl;
		for (int i = 0; i < 26; i++) {
              varsHashMap.put(String.valueOf((char) (i + 65)), new EclVar());
		  }
    }

    public int getLable(String name) {
        for (int i = 0, insesSize = inses.size(); i < insesSize; i++) {
            Ins ins = inses.get(i);
            if (ins instanceof LoopFlag) {
                if (((LoopFlag) ins).getName().equals(name)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Sub parse(String unpackedEcl) {
		subName = unpackedEcl.substring(0, unpackedEcl.indexOf("(")).replaceAll("\\s", "");
		unpackedEcl = parseDiffSwitch(unpackedEcl);
        String[] strInses = unpackedEcl.split("\\n");
             for (String s : strInses) {
            String strInsLine = s.replaceAll("\\s", "");
            if (strInsLine.equals("")) {
                continue;
            }
            int index = strInsLine.indexOf("ins_");
            if (index != -1) {
                String insNum = strInsLine.substring(index + 4, strInsLine.indexOf("("));
                String[] strArgs = strInsLine.substring(strInsLine.indexOf("(") + 1, strInsLine.indexOf(")")).split(",");
                EclVar[] eclVarArgs = new EclVar[strArgs.length];
                if (!strInsLine.contains("()")) {
                    for (int argFlag = 0; argFlag < eclVarArgs.length; ++argFlag) {
                        String argStr = strArgs[argFlag];
                        if (argStr.startsWith("$")) {
                            eclVarArgs[argFlag] = varsHashMap.get(argStr.substring(1));
					//			 if(!argStr.substring(1).equals("C")){
						//throw new RuntimeException(argStr.substring(1));
				//		}
                        } else if (argStr.startsWith("%")) {
                            eclVarArgs[argFlag] = varsHashMap.get(argStr.substring(1));
                        } else if (argStr.startsWith("[") && argStr.endsWith("]")) {
                          eclVarArgs[argFlag] = getSpecialValue((int) Float.parseFloat(argStr.substring(1, argStr.length() - 1)));
		                } else if (argStr.startsWith("\"") && argStr.endsWith("\"")) {
                            eclVarArgs[argFlag] = new EclVar(argStr.substring(1, argStr.length() - 1));
                        } else if (argStr.startsWith("_SS")) {
                            eclVarArgs[argFlag] = new EclVar(Integer.parseInt(argStr.substring(3)));
                        } else if (argStr.startsWith("_ff")) {
                            eclVarArgs[argFlag] = new EclVar(Float.parseFloat(argStr.substring(3)));
                        } else if (argStr.startsWith("_fS")) {
                            eclVarArgs[argFlag] = new EclVar(Float.parseFloat(argStr.substring(3)));
                        } else if (argStr.startsWith("_Sf")) {
                            eclVarArgs[argFlag] = new EclVar((int) Float.parseFloat(argStr.substring(3)));
                        } else {
                            if (argStr.contains("f")) {
                                eclVarArgs[argFlag] = new EclVar(Float.parseFloat(argStr));
                            } else {
                                eclVarArgs[argFlag] = new EclVar(Integer.parseInt(argStr));
                            }
                        }
                    }
                    if (Integer.parseInt(insNum) == 23) {
                        eclVarArgs[0].f = eclVarArgs[0].i;
                    }
                }
                inses.add(new Ins(Integer.parseInt(insNum), eclVarArgs));
            } else {
                if (strInsLine.startsWith("var")) {
                    String argsStr = strInsLine.substring(3);
                    int bound = argsStr.length() - 1;
                    for (int i = 0; i < bound; ++i) {
                        varsHashMap.put(String.valueOf(argsStr.charAt(i)), new EclVar());
                    }
                } else if (strInsLine.startsWith("$")) {
                    String strvar = strInsLine.substring(1, 2);
                    if (varsHashMap.get(strvar) == null) {
                        throw new RuntimeException("value null");
                    }
                    varsHashMap.put(strvar, new EclVar(Integer.parseInt(strInsLine.substring(strInsLine.indexOf("=") + 1, strInsLine.indexOf(";")))));
                } else if (strInsLine.startsWith("%")) {
                    String strvar = strInsLine.substring(1, 2);
                    EclVar eclVar = varsHashMap.get(strvar);
                    if (eclVar == null) {
                        eclVar = new EclVar(Float.parseFloat(strInsLine.substring(strInsLine.indexOf("=") + 1, strInsLine.indexOf(";"))));
                        varsHashMap.put(strvar, eclVar);
                    }
                } else if (strInsLine.startsWith(subName)) {
                    inses.add(new LoopFlag(strInsLine.substring(0, strInsLine.length() - 1)));
                } else if (strInsLine.startsWith("goto")) {
                    inses.add(new Ins(12, new EclVar(strInsLine.substring(4, strInsLine.indexOf("@")))));
                } else if (strInsLine.startsWith("unless")) {
                    inses.add(new Ins(13, new EclVar(strInsLine.substring(0, strInsLine.indexOf("@")))));
                } else if (strInsLine.startsWith("if")) {
                    inses.add(new Ins(14, new EclVar(strInsLine.substring(0, strInsLine.indexOf("@")))));
                } else if (strInsLine.endsWith(":")) {
                    //unknown
                    String num = strInsLine.substring(0, strInsLine.length() - 1);
                    inses.add(new Ins(-1, new EclVar(Integer.parseInt(num))));
                } else if (strInsLine.startsWith("[-") && strInsLine.contains("]=")) {
                    String number = strInsLine.substring(strInsLine.indexOf("=") + 1, strInsLine.length() - 1);
                    int k = (int) Float.parseFloat(strInsLine.substring(1, strInsLine.indexOf("]")));
                    if (strInsLine.contains("f")) {
                        putSpecialValue(k, new EclVar(Float.parseFloat(number)));
                    } else {
                        putSpecialValue(k, new EclVar(Integer.parseInt(number)));
                    }
                } else if (strInsLine.endsWith(";")) {
                    String num = strInsLine.substring(0, strInsLine.length() - 1);
                    if (strInsLine.contains("f")) {
                        numberStack.push(new EclVar(Float.parseFloat(num)));
                    } else {
                        numberStack.push(new EclVar(Integer.parseInt(num)));
                    }
                }
            }
        }
        return this;
    }

    public void start() {
        Ecl.toAddSubs.add(this);
    }

    private String parseDiffSwitch(String unpackedEcl) {
	 
        StringBuilder stringBuilder = new StringBuilder(unpackedEcl);
        int start = unpackedEcl.indexOf("!E");
        int end = unpackedEcl.indexOf("!*") + 2;
     while (start != -1) {
            String switchStr = unpackedEcl.substring(start, end);
            int indexe = switchStr.indexOf("\n", switchStr.indexOf(FightScreen.instence.difficulty));
            int indexeend = switchStr.indexOf(";", indexe)+1;
            stringBuilder.replace(start, end, switchStr.substring(indexe, indexeend));
			unpackedEcl=stringBuilder.toString();
            start = unpackedEcl.indexOf("!E", end);
            end = unpackedEcl.indexOf("!*",start) + 2;
        }
        return stringBuilder.toString();
    }


    public void update() {
        if (nowIns < inses.size()) {
            Ins ins = inses.get(nowIns);
            if (ins.insNum == -1 && FightScreen.instence.gameTimeFlag - ins.args[0].i == 0) {
                return;
            }
            if (ins.insNum == 23) {
                if (ins.args[0].i > 0) {
                    --ins.args[0].i;
                    return;
                }
                ins.args[0].i = (int) ins.args[0].f;
            }
            invoke(ins);
            ++nowIns;
            update();
        }
    }
	
    public void invoke(Ins ins) {
        EclVar[] a = ins.args;
        switch (ins.insNum) {
            case 10:
                System.out.println("sub exit");
			    nowIns=999;
				Ecl.toDeleteSubs.add(this);
                break;
            case 11:
                _11(a);
                break;
            case 12:
                nowIns = getLable(a[0].s);
                break;
            case 13:
                EclVar eclVar13 = a[0];
                String gotoFlag13 = eclVar13.s.substring(eclVar13.s.indexOf("goto") + 4);
                String expression13 = eclVar13.s.substring(eclVar13.s.indexOf("unless") + 6, eclVar13.s.indexOf("goto"));
                if (!invokeExpression(expression13)) {
                    nowIns = getLable(gotoFlag13);
                }
                break;
            case 14:
                EclVar eclVar14 = a[0];
                String gotoFlag14 = eclVar14.s.substring(eclVar14.s.indexOf("goto") + 4);
                String expression14 = eclVar14.s.substring(eclVar14.s.indexOf("if") + 2, eclVar14.s.indexOf("goto"));
                if (invokeExpression(expression14)) {
                    nowIns = getLable(gotoFlag14);
                }
                break;
            case 15:
                _15(a);
                break;
            case 600:
                _600(a[0].i);
                break;
            case 601:
                _601(a[0].i);
                break;
            case 602:
			  try{
                _602(a[0].i, a[1].i, a[2].i);
       }catch(Exception e){
		   throw new RuntimeException(subName+" "+a[0]+" "+a[1]+" "+a[2]);
	   }        break;
            case 603:
                _603(a[0].i, a[1].f, a[2].f);
                break;
            case 604:
                _604(a[0].i, a[1].f, a[2].f);
                break;
            case 605:
                _605(a[0].i, a[1].f, a[2].f);
                break;
            case 606:
                _606(a[0].i, a[1].i, a[2].i);
                break;
            case 607:
                _607(a[0].i, a[1].i);
                break;
            case 608:
                _608(a[0].i, a[1].i, a[2].i);
                break;
            case 609:
                _609(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].i, a[6].f, a[7].f);
                break;
            case 610:
                _610(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].i, a[6].i, a[7].i, a[8].f, a[9].f, a[10].f, a[11].f);
                break;
            case 611:
                _611(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].f, a[6].f);
                break;
            case 612:
                _612(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].f, a[6].f, a[7].f, a[8].f, a[9].f, a[10].f);
                break;
            case 613:
                _613();
                break;
            case 614:
                _614(a[0].i, a[1].i);
                break;
            case 615:
                _615(a[0].f);
                break;
            case 616:
                _616(a[0].f);
                break;
            case 623:
                _623(a[0].s, a[1].f, a[2].f);
                break;
            case 624:
                _624(a[0].i, a[1].f, a[2].f, a[3].f, a[4].f, a[5].f, a[6].f, a[7].f, a[8].f);
                break;
            case 625:
                _625(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].i, a[6].i, a[7].i, a[8].i);
                break;
            case 626:
                _626(a[0].i, a[1].f, a[2].f);
                break;
            case 627:
                _627(a[0].i, a[1].f);
                break;
            case 628:
                _628(a[0].i, a[1].f, a[2].f);
                break;
            case 629:
                _629(a[0].f, a[1].i);
                break;
            case 630:
                _630(a[0].i);
                break;
            case 631:
                _631(a[0].i);
                break;
            case 632:
                _632(a[0].i);
                break;
            case 633:
                _633(a[0].i);
                break;
            case 634:
                _634(a[0].i);
                break;
            case 635:
                _635(a[0].f);
                break;
            case 636:
                _636(a[0].f);
                break;
            case 637:
                _637(a[0].i);
                break;
            case 638:
                _638(a[0].i);
                break;
            case 639:
                _639(a[0].i);
                break;
            case 640:
                _640(a[0].i, a[1].i, a[2].s);
                break;
            case 641:
                _641(a[0].i);
                break;
            default:

                break;
        }
    }

    //0:false else true
    private boolean invokeExpression(String expression) {
        if (expression.equals("1")) {
            return true;
        }
        if (expression.startsWith("$")) {
            String varName = expression.substring(1, 2);
            EclVar eclVar = varsHashMap.get(varName);
            --eclVar.i;
            return eclVar.i != 0;
        } else if (expression.contains("[-") && !expression.contains("&&")) {
            //([-9986] == (60*60))
            String sim;
            switch (expression.substring(expression.indexOf("]") + 1, expression.indexOf("]") + 3)) {
                case ">=":
                    sim = ">=";
                    break;
                case "<=":
                    sim = "<=";
                    break;
                case "==":
                    sim = "==";
                    break;
                default:
				  sim = expression.substring(expression.indexOf("]") + 1,expression.indexOf("]") +2);
                    break;
            }
            float numLeft = Float.parseFloat(expression.substring(expression.indexOf("[-") + 2, expression.indexOf("]")));
            String numberRightStr = expression.replace(")", "").replace("(", "");
            numberRightStr = numberRightStr.substring(numberRightStr.indexOf(sim) + sim.length());
            float numRight = 1;
            if (numberRightStr.contains("*")) {
                String[] nums = numberRightStr.split("\\*");
                for (String nu : nums) {
                    numRight *= Float.parseFloat(nu);
                }
            }else{
				numRight=Float.parseFloat(numberRightStr);
			}
            boolean b;
            switch (sim) {
                case ">=":
                    b = getSpecialValue((int) numLeft).i >= numRight;
                    break;
                case "<=":
                    EclVar eclVar = getSpecialValue((int) numLeft);
                    b = eclVar.i <= numRight;
                    break;
                case "==":
                    b = getSpecialValue((int) numLeft).i == numRight;
                    break;
                case ">":
                    b = getSpecialValue((int) numLeft).i > numRight;
                    break;
                case "<":
                    b = getSpecialValue((int) numLeft).i < numRight;
                    break;
                default:
				  throw new RuntimeException("unknown expression:"+expression);
            }
            return b;
        } else if (expression.contains("&&")) {
            expression = expression.replace(")", "").replace("(", "");
            String[] exps = expression.split("&&");
            for (String exp : exps) {
                if (!invokeExpression(exp)) {
                    return false;
                }
            }
            return true;
        } else {
            throw new RuntimeException("unknown expression:"+expression);
        }
    }

    public Sub insertArgs(EclVar... args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println("key:" + String.valueOf((char) (i + 65)) + "  value" + args[i]);
			EclVar ev=varsHashMap.get(String.valueOf((char) (i + 65)));
		    ev.f=args[i].f;
			ev.i=args[i].i;
			ev.s=args[i].s;
			ev.type=args[i].type;
        }
        return this;
    }

    public void _11(EclVar... args) {
		if(args[0].s.equals("BossItemCard")){
		  return;
		}
		EclVar[] as=new EclVar[args.length-1];
		for(int i=0;i<as.length;++i){
		  as[i]=args[i+1];
		}
        ecl.getSub(args[0].s).insertArgs(as).update();
    }

    public void _15(EclVar... args) {
		EclVar[] as=new EclVar[args.length-1];
		for(int i=0;i<as.length;++i){
			as[i]=args[i+1];
		  }
        Ecl.toAddSubs.add(ecl.getSub(args[0].s).insertArgs(as));
		//if(!args[0].s.equals("BossCard7_at")){
		//	throw new RuntimeException(args[0].s);
		//}
    }

    public void putSpecialValue(int valueCase, EclVar value) {
        if (valueCase < 0) {
            valueCase = -valueCase;
        }
        switch (valueCase) {
            case 1:
                numberStack.push(value);
                break;
            case 9915:
            case 9916:
            case 9917:
            case 9918:
            case 9919:
            case 9920:
            case 9921:
            case 9922:
            case 9923:
            case 9924:
            case 9925:
            case 9926:
            case 9947:
            case 9948:
            case 9949:
                globleVarHashMap.put(String.valueOf(valueCase), value);
                break;
            case 9978:
            case 9979:
			case 9981:
                varsHashMap.put(String.valueOf(valueCase), value);
                break;
            default:
                throw new RuntimeException("illegal var:"+valueCase);
        }
    }

    public EclVar getSpecialValue(int i) {
        if (i < 0) {
            i = -i;
        }
        switch (i) {
            case 1:
                return numberStack.pop();
            case 9947:
            case 9948:
            case 9949:
                return globleVarHashMap.get(String.valueOf(i));
            case 9954:
                return new EclVar(FightScreen.instence.boss.hp);
            case 9964:
                return new EclVar(MyPlaneReimu.instance.objectCenter.x);
            case 9965:
                return new EclVar(MyPlaneReimu.instance.objectCenter.y);
            case 9978:
            case 9979:
                return varsHashMap.get(String.valueOf(i));
			case 9986:
			  return new EclVar(0);
            case 9988:
                return new EclVar(FightScreen.instence.gameTimeFlag);
            case 9998:
			  return new EclVar((float)(new RandomXS128().nextFloat()*Math.PI*(new RandomXS128().nextInt(1)*2-1)));
			case 10000:
                return new EclVar(new RandomXS128().nextInt());
            default:
                return null;
        }
    }

    public void _600(int danmakuNum) {
        BulletShooter bShooter = new BulletShooter().init();
        bShooter.shooterCenter = FightScreen.instence.boss.objectCenter;
        bulletShooters.put(danmakuNum, bShooter);
    }

    public void _601(int danmakuNum) {
        bulletShooters.get(danmakuNum).shoot();
    }

    public void _602(int danmakuNum, int form, int color) {
        bulletShooters.get(danmakuNum).setBulletColor(color).setBulletForm(form);
    }

    public void _603(int danmakuNum, float offsetX, float offsetY) {
        bulletShooters.get(danmakuNum).setShootCenterOffset(new Vector2(offsetX, offsetY));
    }

    public void _604(int danmakuNum, float direct, float r) {
        BulletShooter bs = bulletShooters.get(danmakuNum);
        if (bs == null) {
            throw new NullPointerException("is null:" + danmakuNum);
        }
        bulletShooters.get(danmakuNum).setBulletWaysDegree((float) Math.toDegrees(direct));
    }

    public void _605(int danmakuNum, float speed, float slowlestSpeed) {

    }

    public void _606(int danmakuNum, int way, int ceng) {
        bulletShooters.get(danmakuNum).setBulletWays(way).setBulletCengShu(ceng);
    }

    public void _607(int danmakuNum, int style) {
        bulletShooters.get(danmakuNum).setBulletStyle(style);
    }

    public void _608(int danmakuNum, int voiceOnShoot, int voiceOnChange) {
    }

    public void _609(int danmakuNum, int num, int way, int mode, int inta, int intb, float floatr, float floats) {
    }

    public void _610(int danmakuNum, int num, int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
    }

    public void _611(int danmakuNum, int way, int mode, int inta, int intb, float floatr, float floats) {
    }

    public void _612(int danmakuNum, int way, int mode, int inta, int intb, float intc, float intd, float floatr, float floats, float floatm, float floatn) {
    }

    public void _613() {
    }

    public void _614(int danmakuA, int danmakuB) {
        bulletShooters.put(danmakuB, bulletShooters.get(danmakuA).clone());
    }

    public void _615(float floatR) {
    }

    public void _616(float floatR) {
    }

    public void _623(String floatVarName, float floatX, float floatY) {
    }

    public void _624(int danmakuNum, float floatA, float b, float c, float d, float e, float f, float g, float h) {
    }

    public void _625(int danmakuNum, int intA, int b, int c, int d, int e, int f, int g, int h) {
    }

    public void _626(int danmakuNum, float floatAngel, float r) {
    }

    public void _627(int danmakuNum, float r) {
    }

    public void _628(int danmakuNum, float floatX, float y) {
    }

    public void _629(float floatR, int intRgb) {
    }

    //int
    public void _630(int a) {
    }

    public void _631(int a) {
    }

    public void _632(int a) {
    }

    public void _633(int a) {
    }

    public void _634(int a) {
    }

    //float
    public void _635(float a) {
    }

    public void _636(float a) {
    }

    //int
    public void _637(int a) {
    }

    public void _638(int a) {
    }

    public void _639(int a) {
    }

    //mode=16777216 danmakuNum=2
    public void _640(int danmakuNum, int intMode, String sub) {
    }

    public void _641(int danmakuNum) {
    }

}
