package com.InsProcess.parse;

import java.util.HashSet;

import com.InsProcess.parse.beans.EclSub;
import com.InsProcess.parse.beans.EclSubPack;
import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.planes.Enemy;
import com.meng.TaiHunDanmaku.task.Task;
import com.meng.TaiHunDanmaku.task.TaskMoveTo;
import com.meng.TaiHunDanmaku.ui.FightScreen;

public class EclManager {
    public static HashSet<EclSub> runningSubs = new HashSet<>();
    public static HashSet<EclSub> toAddSubs = new HashSet<>();
    public static HashSet<EclSub> toDeleteSubs = new HashSet<>();
    public static HashSet<EclSub> onPauseSubs = new HashSet<>();
    public static EclSub nextSub;

    public static HashSet<EclSubPack> subPacks = new HashSet<>();

    public EclManager(String baseFileName) {
        new EclFile(this, baseFileName);
    }

    public void start() {
        // toAddSubs.add(getSubPack("BossCard1").setManager(FightScreen.instence.boss));

        toAddSubs.add(getSubPack("Boss").setManager(FightScreen.instence.mapleEnemy));
    }

    public static void updateAll() {
        if(FightScreen.instence.onBoss){
        	if(FightScreen.instence.boss==null||FightScreen.instence.boss.hp<0){
        		runningSubs.clear();
        		FightScreen.instence.boss.hp=700;
        		runningSubs.add(nextSub);
        	}
        }
        for (EclSub runningSub : runningSubs) {
            runningSub.update();
        }
        runningSubs.addAll(toAddSubs);
        runningSubs.removeAll(toDeleteSubs);
        runningSubs.removeAll(onPauseSubs);
        toAddSubs.clear();
        toDeleteSubs.clear();
    }

    public static EclSub getSubPack(String name) {
        for (EclSubPack sp : subPacks) {
            if (sp.subName.equals(name)) {
                return sp.sub;
            }
        }
        throw new NullPointerException("sub not found:" + name);
    }

    @Override
    public String toString() {
        // for (EclSubPack f : subPacks) {
        //     sb.append(f.toString()).append("\n");
        //  }
        return getSubPack("MainSub00").toString();
    }

}
