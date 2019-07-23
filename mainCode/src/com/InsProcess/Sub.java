package com.InsProcess;

import java.util.*;

public class Sub {

    public ArrayList<Ins> inses = new ArrayList<>();
    private String subName;
    private int argLength = 0;
    public boolean[] isInt;

    Sub(String name, boolean... isInt) {
        subName = name;
        this.argLength = isInt.length;
        this.isInt = isInt;
	  }

	Sub(String name, String unpackedEcl) {
		subName = name;
		parse(unpackedEcl);
	  }

	private void parse(String unpackedEcl) {
		String[] strInses=unpackedEcl.split("\\n");
		Ins ins=ins();
		for (int i=0;i < strInses.length;++i) {
			String strIns=strInses[i].replace("\\s", "");
			if (strIns.trim() == "") {
				continue;
			  }
			int index=strIns.indexOf("ins_");
			if (index != -1) {
				String insNum=strIns.substring(index + 4, strIns.indexOf("("));
				int argCount=getArgCount(strIns);
				String argStr=strIns.substring(strIns.indexOf("(") + 1, strIns.indexOf(")"));
				String[] arg=argStr.split(",");
				switch (Integer.parseInt(insNum)) {
					case 600:
					  ins._600();
					  break;
					case 601:
					  ins._601(Integer.parseInt(arg[0]));
					  break;
					case 602:
					  ins._602(arg[0], arg[1]);
					  break;
					case 603:
					  ins._603(arg[0], arg[1]);
					  break;
					case 604:
					  ins._604(arg[0], arg[1]);
					  break;
					case 605:
					  ins._605(arg[0], arg[1]);
					  break;
					case 606:
					  ins._606(arg[0], arg[1]);
					  break;
					case 607:

					  break;
					case 608:

					  break;
					case 609:

					  break;
					case 610:

					  break;
					case 611:

					  break;
					case 612:

					  break;
					case 613:

					  break;
					case 614:

					  break;
					case 615:

					  break;
					case 616:

					  break;
					case 623:

					  break;
					case 624:

					  break;
					case 625:

					  break;
					case 626:

					  break;
					case 627:

					  break;
					case 628:

					  break;
					case 629:

					  break;
					case 630:

					  break;
					case 631:

					  break;
					case 632:

					  break;
					case 633:

					  break;
					case 634:

					  break;
					case 635:

					  break;
					case 636:

					  break;
					case 637:

					  break;
					case 638:

					  break;

					case 639:

					  break;
					case 640:

					  break;
					case 641:

					  break;
				  } 
			  } else {
				if (strIns.indexOf("Var") != -1) {

				  } else if (strIns.indexOf(":") != -1) {

				  }
			  } 
		  }
	  }

	private int getArgCount(String s) {
		int count=0;
		for (int i=0;i < s.length();++i) {
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
