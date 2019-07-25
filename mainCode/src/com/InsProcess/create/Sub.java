package com.InsProcess.create;

import com.InsProcess.helper.EclFloatStack;
import com.InsProcess.helper.EclIntStack;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.BulletShooter;

import java.util.ArrayList;

public class Sub {

    public ArrayList<Ins> inses = new ArrayList<>();
    private String subName;
    private int argLength = 0;
    public boolean[] isInt=new boolean[1];
	public ArrayList<BulletShooter> bulletShooters=new ArrayList<>();
	public EclIntStack intStack=new EclIntStack();
	public EclFloatStack floatStack=new EclFloatStack();
	public Ecl ecl;

    Sub(Ecl ecl, String name, boolean... isInt) {
        subName = name;
		this.ecl=ecl;
        this.argLength = isInt.length;
        this.isInt = isInt;
    }

    Sub(Ecl ecl, String name, String unpackedEcl) {
        subName = name;
		this.ecl=ecl;
        parse(unpackedEcl);
    }

    private void parse(String unpackedEcl) {
        String[] strInses = unpackedEcl.split("\\n");
        Ins ins = ins();
        for (int i = 0, strInsesLength = strInses.length; i < strInsesLength; i++) {
            String s = strInses[i];
            String strIns = s.replace("\\s", "");
            if (strIns.trim().equals("")) {
                continue;
            }
            int index = strIns.indexOf("ins_");
            if (index != -1) {
                String insNum = strIns.substring(index + 4, strIns.indexOf("("));
                int argCount = getArgCount(strIns);
                String argStr = strIns.substring(strIns.indexOf("(") + 1, strIns.indexOf(")"));
                String[] a = argStr.split(",");
                switch (Integer.parseInt(insNum)) {
                    case 11:
                        String[] tmp = new String[a.length - 1];
                        System.arraycopy(a, 1, tmp, 0, a.length);
                        ins._11(a[0], tmp);
						break;
					case 15:
					  String[] tmp2 = new String[a.length - 1];
					  System.arraycopy(a, 1, tmp2, 0, a.length);
					  ins._11(a[0], tmp2);
					  break;
                    case 600:
                        ins._600();
                        break;
                    case 601:
                        ins._601(Integer.parseInt(a[0]));
                        break;
                    case 602:
                        ins._602(a[0], a[1]);
                        break;
                    case 603:
                        ins._603(a[0], a[1]);
                        break;
                    case 604:
                        ins._604(a[0], a[1]);
                        break;
                    case 605:
                        ins._605(a[0], a[1]);
                        break;
                    case 606:
                        ins._606(a[0], a[1]);
                        break;
                    case 607:
                        ins._607(a[0]);
                        break;
                    case 608:
                        ins._608(a[0], a[1]);
                        break;
                    case 609:
                        ins._609(a[0], a[1], a[2], a[3], a[4], a[5]);
                        break;
                    case 610:
                        ins._610(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9]);
                        break;
                    case 611:
                        ins._611(a[0], a[1], a[2], a[3], a[4]);
                        break;
                    case 612:
                        ins._612(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);
                        break;
                    case 613:
                        ins._613();
                        break;
                    case 614:
                        ins._614(a[0], a[1]);
                        break;
                    case 615:
                        ins._615(a[0]);
                        break;
                    case 616:
                        ins._616(a[0]);
                        break;
                    case 623:
                        ins._623(a[0], a[1], a[2]);
                        break;
                    case 624:
                        ins._624(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
                        break;
                    case 625:
                        ins._625(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
                        break;
                    case 626:
                        ins._626(a[0], a[1]);
                        break;
                    case 627:
                        ins._627(a[0]);
                        break;
                    case 628:
                        ins._628(a[0], a[1]);
                        break;
                    case 629:
                        ins._629(a[0], a[1]);
                        break;
                    case 630:
                        ins._630(a[0]);
                        break;
                    case 631:
                        ins._631(a[0]);
                        break;
                    case 632:
                        ins._632(a[0]);
                        break;
                    case 633:
                        ins._633(a[0]);
                        break;
                    case 634:
                        ins._634(a[0]);
                        break;
                    case 635:
                        ins._635(a[0]);
                        break;
                    case 636:
                        ins._636(a[0]);
                        break;
                    case 637:
                        ins._637(a[0]);
                        break;
                    case 638:
                        ins._638(a[0]);
                        break;
                    case 639:
                        ins._639(a[0]);
                        break;
                    case 640:
                        ins._640(a[0], a[1]);
                        break;
                    case 641:
                        ins._641();
                        break;
                    default:
                        break;
                }
            } else {
                if (strIns.contains("Var")) {

                } else if (strIns.contains(":")) {

                }
            }
        }
    }
	
	public void invoke(String... args){
	  
	}

    private int getArgCount(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == ',') {
                ++count;
            }
        }
        ++count;
        return count;
    }

    public Ins ins() {
        Ins ins000 = new Ins(this, isInt);
        inses.add(ins000);
        return ins000;
    }

    public String getSubName() {
        return subName;
    }

    public void addIns(Runnable runnable) {
        runnable.run();
    }

    @Override
    public String toString() {
        StringBuilder sb1 = new StringBuilder();
        for (Ins i : inses) {
            sb1.append(i.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        for (int i = 65; i < 65 + argLength; ++i) {
            sb2.append(" ");
            sb2.append((char) i);
        }
        return String.format("sub %s(%s) {\n%s}\n\n", subName, sb2.toString(), sb1.toString());
    }
}
