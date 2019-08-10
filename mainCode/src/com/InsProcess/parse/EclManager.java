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

    public HashSet<EclSubPack> subPacks = new HashSet<>();

    public EclManager(String baseFileName) {
        new EclFile(this, baseFileName);
    }

    public void start() {
       // toAddSubs.add(getSubPack("BossCard1").setManager(FightScreen.instence.boss));

		FightScreen.instence.boss = new Enemy();
		FightScreen.instence.boss.init(new Vector2(275, 450), 10, 700, new Task[] { new TaskMoveTo(193, 250) });
        toAddSubs.add(getSubPack("BossCard7").setManager(FightScreen.instence.boss));
    }

    public static void updateAll() {
        for (EclSub runningSub : runningSubs) {
            runningSub.update();
        }
        runningSubs.addAll(toAddSubs);
        runningSubs.removeAll(toDeleteSubs);
        runningSubs.removeAll(onPauseSubs);
        toAddSubs.clear();
        toDeleteSubs.clear();
    }

    public EclSub getSubPack(String name) {
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
