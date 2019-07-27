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
    public HashMap<String, EclVar> varsHashMap = new HashMap<>(8);
    public String subName;
    public ArrayList<BulletShooter> bulletShooters = new ArrayList<>();
    public EclNumberStack numberStack = new EclNumberStack();
    public Ecl ecl;
    public int nowIns = 0;

    Sub(Ecl ecl, String unpackedEcl) {
        this.ecl = ecl;
        //   parse(unpackedEcl);
        Ecl.runningSubs.add(this.insertArgs(new EclVar(20), new EclVar(3)));
    }

    public int getLoopPoint(String name) {
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

    public void parse(String unpackedEcl) {
        unpackedEcl = parseDiffSwitch(unpackedEcl);
        String[] strInses = unpackedEcl.split("\\n");
        subName = unpackedEcl.substring(0, unpackedEcl.indexOf("(")).replaceAll("\\s", "");
        for (String s : strInses) {
            String strInsLine = s.replaceAll("\\s", "");
            System.out.println("strInsLine:" + strInsLine);
            if (strInsLine.equals("")) {
                continue;
            }
            int index = strInsLine.indexOf("ins_");
            if (index != -1) {
                String insNum = strInsLine.substring(index + 4, strInsLine.indexOf("("));
                String[] strArgs = strInsLine.substring(strInsLine.indexOf("(") + 1, strInsLine.indexOf(")")).split(",");
                EclVar[] eclVarArgs = new EclVar[strArgs.length];
                System.out.println("eclVarArgs.length:" + eclVarArgs.length);
                for (int argLoopFlag = 0; argLoopFlag < eclVarArgs.length; ++argLoopFlag) {
                    if (strInsLine.contains("()")) {
                        break;
                    }
                    String argStr = strArgs[argLoopFlag];
                    if (argStr.startsWith("$")) {
                        eclVarArgs[argLoopFlag] = varsHashMap.get(argStr.substring(1));
                    } else if (argStr.startsWith("%")) {
                        eclVarArgs[argLoopFlag] = varsHashMap.get(argStr.substring(1));
                    } else if (argStr.startsWith("[") && argStr.endsWith("]")) {
                        eclVarArgs[argLoopFlag] = getSpecialValue((int) Float.parseFloat(argStr.substring(1, argStr.length() - 1)));
                    } else {
                        eclVarArgs[argLoopFlag] = new EclVar(strInsLine.contains("f") ? Float.parseFloat(argStr) : Integer.parseInt(argStr));
                    }
                }
                if (strInsLine.contains("()")) {
                    System.out.print("no arg");
                } else {
                    for (int i = 0; i < eclVarArgs.length; i++) {
                        EclVar eclVar = eclVarArgs[i];
                        System.out.print("var[" + i + "]:" + eclVar.toString() + "  ");
                    }
                }
                System.out.print("\n");
                inses.add(new Ins(Integer.parseInt(insNum), eclVarArgs));
            } else {
                if (strInsLine.startsWith("var")) {
                    String argsStr = strInsLine.substring(3);
                    int bound = argsStr.length() - 1;
                    for (int i = 0; i < bound; ++i) {
                        String strvar = String.valueOf(argsStr.charAt(i));
                        varsHashMap.put(String.valueOf(argsStr.charAt(i)), null);
                    }
                } else if (strInsLine.startsWith("$")) {
                    String strvar = strInsLine.substring(1, 2);
                    //computeIfAbsent
                    EclVar eclVar = varsHashMap.get(strvar);
                    if (eclVar == null) {
                        eclVar = new EclVar(Integer.parseInt(strInsLine.substring(strInsLine.indexOf("=") + 1, strInsLine.indexOf(";"))));
                        varsHashMap.put(strvar, eclVar);
                    }
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
                } else if (strInsLine.startsWith("if")) {
                    inses.add(new Ins(13, new EclVar(strInsLine.substring(0, strInsLine.indexOf("@")))));
                } else if (strInsLine.endsWith(":")) {
                    //unknown
                    String num = strInsLine.substring(0, strInsLine.length() - 1);
                    inses.add(new Ins(-1, new EclVar(Integer.parseInt(num))));
                } else if (strInsLine.endsWith(";")) {
                    String num = strInsLine.substring(0, strInsLine.length() - 1);
                    numberStack.push(strInsLine.contains("f") ? Float.parseFloat(num) : Integer.parseInt(num));
                }
            }
        }
    }

    private String parseDiffSwitch(String unpackedEcl) {
        StringBuilder stringBuilder = new StringBuilder(unpackedEcl);
        int start = unpackedEcl.indexOf("!E");
        int end = unpackedEcl.indexOf("!*") + 2;
        while (start != -1) {
            String switchStr = unpackedEcl.substring(start, end);
            int indexe = switchStr.indexOf("\n", switchStr.indexOf(FightScreen.instence.difficulty));
            int indexeend = switchStr.indexOf(";", indexe);
            stringBuilder.replace(start, end, switchStr.substring(indexe, indexeend));
            start = unpackedEcl.indexOf("!E", end);
            end = unpackedEcl.indexOf("!*") + 2;
        }
        return stringBuilder.toString();
    }


    public void update() {
        if (nowIns < inses.size() - 1) {
            Ins ins = inses.get(nowIns);
            if (ins.insNum == -1 && FightScreen.instence.gameTimeFlag - ins.args[0].i == 0) {
                return;
            }
            if (ins.insNum == 23 && ins.args[0].i > 0) {
                --ins.insNum;
            } else {
                invoke(ins);
                ++nowIns;
                update();
            }
        }
    }

    public void invoke(Ins ins) {
        EclVar[] a = ins.args;
        switch (ins.insNum) {
            case 11:
                _11(a);
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
                _602(a[0].i, a[1].i, a[2].i);
                break;
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

    public Sub insertArgs(EclVar... args) {
        for (int i = 0; i < args.length; i++) {
            varsHashMap.put(String.valueOf((char) (i + 65)), args[i]);
        }
        return this;
    }

    public void gotoLoopFlag(String flagName) {
        nowIns = getLoopPoint(flagName);
    }

    public void ifxGoto(String judge, LoopFlag loopFlag) {

    }

    public void ifxGotoAt(String judge, LoopFlag loopFlag, int at) {

    }

    public void unlessxGoto(String judge, LoopFlag loopFlag) {

    }

    public void unlessxGotoAt(String judge, LoopFlag loopFlag, int at) {

    }

    public void numberIns(String text) {

    }

    public void _11(EclVar... args) {
        ecl.getSub(args[0].s).insertArgs(args).update();
    }

    public void _15(EclVar... args) {
        Ecl.runningSubs.add(ecl.getSub(args[0].s).insertArgs(args));
    }

    public void putSpecialValue(int valueCase, EclVar value) {
        if (valueCase < 0) {
            valueCase = -valueCase;
        }
        if (valueCase == 1) {
            numberStack.push(value);
        } else {
            varsHashMap.put(String.valueOf(valueCase), value);
        }
    }

    public EclVar getSpecialValue(int i) {
        if (i < 0) {
            i = -i;
        }
        switch (i) {
            case 1:
                return numberStack.pop();
            case 9964:
                return new EclVar(MyPlaneReimu.instance.objectCenter.x);
            case 9965:
                return new EclVar(MyPlaneReimu.instance.objectCenter.y);
            case 9978:
            case 9979:
                return varsHashMap.get(String.valueOf(i));
            case 10000:
                return new EclVar(new RandomXS128().nextInt());
            default:
                return null;
        }
    }

    public void push(int i) {
        putSpecialValue(1, new EclVar(i));
    }

    public void push(float f) {
        putSpecialValue(1, new EclVar(f));
    }

    public int popInt() {
        return getSpecialValue(1).i;
    }

    public float popFloat() {
        return getSpecialValue(1).f;
    }

    public void _600(int danmakuNum) {
        BulletShooter bShooter = new BulletShooter();
        bulletShooters.set(danmakuNum, bShooter);
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
        bulletShooters.set(danmakuB, bulletShooters.get(danmakuA).clone());
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
