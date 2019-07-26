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
    private String subName;
    public ArrayList<BulletShooter> bulletShooters = new ArrayList<>();
    public EclNumberStack numberStack = new EclNumberStack();
    public Ecl ecl;
    public int nowIns = 0;

    Sub(Ecl ecl, String name, String unpackedEcl) {
        subName = name;
        this.ecl = ecl;
        parse(unpackedEcl);
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

    private void parse(String unpackedEcl) {
        String[] strInses = unpackedEcl.split("\\n");
        for (int i = 0, strInsesLength = strInses.length; i < strInsesLength; i++) {
            String s = strInses[i];//now ins
            String strIns = s.replace("\\s", "");
            if (strIns.trim().equals("")) {
                continue;
            }
            int index = strIns.indexOf("ins_");
            if (index != -1) {
                String insNum = strIns.substring(index + 4, strIns.indexOf("("));
                String argStr = strIns.substring(strIns.indexOf("(") + 1, strIns.indexOf(")"));
                String[] a = argStr.split(",");
                 EclVar[] eclvars=new EclVar[a.length];
		         for(int argfl=0;argfl<eclvars.length;++argfl){
			         eclvars[argfl]=new EclVar(a[argfl]);
			       }
		    	 inses.add(new Ins(Integer.parseInt(insNum),eclvars));   
            } else {
                if (strIns.contains("Var")) {

                } else if (strIns.contains(":")) {

                }
            }
        }
    }
	
	public void update(){
	  if(nowIns<inses.size()-1){
		Ins ins=inses.get(nowIns);
	  if(ins.insNum==23&&ins.args[0].intValve>0){
		--ins.insNum;
	  }else{
		invoke(ins);
		++nowIns;
		update();
	  }}
	}

    public void invoke(Ins ins) {
	  EclVar[] a=ins.args;
		  switch (ins.insNum) {
			  case 11:
				_11(a);
				break;
			  case 15:
				_11(a);
				break;
			  case 600:
				_600();
				break;
			  case 601:
				_601(Integer.parseInt(a[0]));
				break;
			  case 602:
				_602(a[0], a[1]);
				break;
			  case 603:
				_603(a[0], a[1]);
				break;
			  case 604:
				_604(a[0], a[1]);
				break;
			  case 605:
				_605(a[0], a[1]);
				break;
			  case 606:
				_606(a[0], a[1]);
				break;
			  case 607:
				_607(a[0]);
				break;
			  case 608:
				_608(a[0], a[1]);
				break;
			  case 609:
				_609(a[0], a[1], a[2], a[3], a[4], a[5]);
				break;
			  case 610:
				_610(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9]);
				break;
			  case 611:
				_611(a[0], a[1], a[2], a[3], a[4]);
				break;
			  case 612:
				_612(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);
				break;
			  case 613:
				_613();
				break;
			  case 614:
				_614(a[0], a[1]);
				break;
			  case 615:
				_615(a[0]);
				break;
			  case 616:
				_616(a[0]);
				break;
			  case 623:
				_623(a[0], a[1], a[2]);
				break;
			  case 624:
				_624(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
				break;
			  case 625:
				_625(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
				break;
			  case 626:
				_626(a[0], a[1]);
				break;
			  case 627:
				_627(a[0]);
				break;
			  case 628:
				_628(a[0], a[1]);
				break;
			  case 629:
				_629(a[0], a[1]);
				break;
			  case 630:
				_630(a[0]);
				break;
			  case 631:
				_631(a[0]);
				break;
			  case 632:
				_632(a[0]);
				break;
			  case 633:
				_633(a[0]);
				break;
			  case 634:
				_634(a[0]);
				break;
			  case 635:
				_635(a[0]);
				break;
			  case 636:
				_636(a[0]);
				break;
			  case 637:
				_637(a[0]);
				break;
			  case 638:
				_638(a[0]);
				break;
			  case 639:
				_639(a[0]);
				break;
			  case 640:
				_640(a[0], a[1]);
				break;
			  case 641:
				_641();
				break;
			  default:
				
				break;
			}

         }

	public Sub insertArgs(EclVar... args){
	  for(EclVar e:args){
		varsHashMap.put(e.stringValue,e);
	  }
	  return this;
	}
	
    public void ins(int number,EclVar... args) {
        inses.add(new Ins(number, args));
    }

    public String getSubName() {
        return subName;
    }

    public void addIns(Runnable runnable) {
        runnable.run();
    }

    public void assign(String varName, int value) {
        EclVar eclVar = varsHashMap.get(varName);
        if (eclVar == null) {
            eclVar = new EclVar(value);
            varsHashMap.put(varName, eclVar);
            return;
        }
        eclVar.intValve = value;
    }

    public void assign(String varName, float value) {
        EclVar eclVar = varsHashMap.get(varName);
        if (eclVar == null) {
            eclVar = new EclVar(value);
            varsHashMap.put(varName, eclVar);
            return;
        }
        eclVar.floatValue = value;
    }

    public EclVar use(String varName) {
        return varsHashMap.get(varName);
    }

    public LoopFlag loop(int flagName) {
        LoopFlag flag = new LoopFlag(this, String.valueOf(flagName));
        inses.add(flag);
        return flag;
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

    public void args(boolean... isInt) {
        if (isInt.length < 1) {
            return;
        }
        for (int i = 65 + isInt.length; i < 65 + isInt.length + isInt.length; ++i) {
            varsHashMap.put(String.valueOf((char) i), null);
        }
    }
    public void _11(Sub sub, EclVar... args) {
        ecl.getSub(sub.getSubName()).insertArgs(args).update();
    }

    public void _15(final Sub sub, EclVar... args) {
        ecl.runningSubs.add(ecl.getSub(sub.getSubName()).insertArgs(args));
    }


    public void diffSwitch(int e, int n, int h, int l, int o) {
        switch (FightScreen.instence.difficulty) {
            case "easy":
                numberStack.push(e);
                break;
            case "normal":
                numberStack.push(n);
                break;
            case "hard":
                numberStack.push(h);
                break;
            case "lunatic":
                numberStack.push(l);
                break;
            case "overdrive":
                numberStack.push(o);
                break;
            default:
                numberStack.push(n);
                break;
        }
    }

    public void diffSwitch(float e, float n, float h, float l, float o) {
        switch (FightScreen.instence.difficulty) {
            case "easy":
                numberStack.push(e);
                break;
            case "normal":
                numberStack.push(n);
                break;
            case "hard":
                numberStack.push(h);
                break;
            case "lunatic":
                numberStack.push(l);
                break;
            case "overdrive":
                numberStack.push(o);
                break;
            default:
                numberStack.push(n);
                break;
        }
    }
	
	public void putSpecialValue(int valueCase,EclVar value){
	  if(valueCase<0){
		valueCase=-valueCase;
	  }
	  if(valueCase==1){
	    numberStack.push(value);	
	  }else{
	    varsHashMap.put(String.valueOf(valueCase),value);
	  }
	}
	
	public EclVar getSpecialValue(int i){
	  if(i<0){
		i=-i;
	  }
	  switch (i){
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
		default :
			return null;
	  }
	}

    public void push(int i) {
        numberStack.push(i);
    }

    public void push(float f) {
        numberStack.push(f);
    }

    public void push(double d) {
        numberStack.push((float) d);
    }

    public int popInt() {
        return numberStack.popInt();
    }

    public float popFloat() {
        return numberStack.popFloat();
    }

    public String transferIntToFloat(String name) {
        return "_fS $" + name;
    }

    public String transferFloatToInt(String name) {
        return "_Sf %" + name;
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
        bulletShooters.set(danmakuB,  bulletShooters.get(danmakuA).clone());
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
    public void _640(int danmakuNum, int intMode, Sub sub) {
        return _640(danmakuNum, intMode, getSubName());
    }

    public void _640(int danmakuNum, int intMode, String sub) {
    }

    public void _641(int danmakuNum) {
    }

}
