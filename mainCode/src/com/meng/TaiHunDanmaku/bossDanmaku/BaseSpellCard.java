package com.meng.TaiHunDanmaku.bossDanmaku;

import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.Enemy;
import com.meng.TaiHunDanmaku.task.*;

public abstract class BaseSpellCard{

    public Enemy boss;
    public int spellTime=1000;
    public String spellName="";
    public BulletShooter[] shooters;
    public int frame=0;
    public TaskManagerEnemyPlane taskManagerEnemyPlane;

    public int waitFrameSpell=60;

    public abstract void init(Enemy b);

    public void update(){
        if(waitFrameSpell-->0)
            return;
        frame++;
    }
}
