package com.InsProcess.parse;

import com.InsProcess.helper.EclVar;

import java.util.*;

import com.meng.TaiHunDanmaku.ui.*;
import com.InsProcess.parse.beans.*;

public class Ecl {
    public static HashSet<EclSub> runningSubs = new HashSet<>();
    public static HashSet<EclSub> toAddSubs = new HashSet<>();
    public static HashSet<EclSub> toDeleteSubs = new HashSet<>();
	public static HashSet<EclSub> onPauseSubs=new HashSet<>();
	
	public HashSet<EclSubPack> subPacks=new HashSet<>();
	
	private String base;
	public Ecl(String baseFileName){
		base=baseFileName;
		new EclFile(this,base);
	}
	
	public void start(){
	  toAddSubs.add(getSubPack("main"));
	}

    public static void update() {
   /*     if (FightScreen.instence.boss == null) {
         //   runningSubs.clear();
            return;
        }*/
        for (EclSub runningSub : runningSubs) {
            runningSub.update();
        }
        runningSubs.addAll(toAddSubs);
        runningSubs.removeAll(toDeleteSubs);
		runningSubs.removeAll(onPauseSubs);
        toAddSubs.clear();
        toDeleteSubs.clear();
	  }

	public EclSub getSubPack(String name){
		for(EclSubPack sp:subPacks){
			if(sp.subName.equals(name)){
				return sp.sub;
			  }
		  }
		throw new NullPointerException("sub not found:"+name);
	  }
	  
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(subPacks.size()).append("\n");
		for(EclSubPack f:subPacks){
		  sb.append(f.subName).append("\n");
		}
		return sb.toString();
	  }

	
}
