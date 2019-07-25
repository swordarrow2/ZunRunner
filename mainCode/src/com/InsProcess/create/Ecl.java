package com.InsProcess.create;

import java.util.ArrayList;

public class Ecl {
    private ArrayList<Sub> subs = new ArrayList<>();

    public Sub sub(String name, boolean... isInt) {
        Sub s = new Sub(this,name, isInt);
        subs.add(s);
        return s;
    }
	public Sub sub(String name, String unpackedEcl) {
        Sub s = new Sub(this,name, unpackedEcl);
        subs.add(s);
        return s;
		}
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Sub s : subs) {
            stringBuilder.append(s.toString());
        }
        return stringBuilder.toString();
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
