package com.InsProcess.parse;

import java.util.*;

public class Ecl {
  public static HashSet<Sub> runningSubs=new HashSet<>();
    private ArrayList<Sub> subs = new ArrayList<>();

	public Sub sub(String name, String unpackedEcl) {
        Sub s = new Sub(this,name, unpackedEcl);
        subs.add(s);
        return s;
		}
		
    public static void update(){
	  for(Sub sub:runningSubs){
		sub.update();
	  }
	}
		
		
	public Sub getSub(String subName){
	  for(Sub s:subs){
		if(s.getSubName().equals(subName)){
		  return s;
		}
	  }
	  return null;
	}
}
