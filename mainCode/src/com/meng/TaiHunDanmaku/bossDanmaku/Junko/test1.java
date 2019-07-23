package com.meng.TaiHunDanmaku.bossDanmaku.Junko;

import com.badlogic.gdx.math.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.Junko;
import com.meng.TaiHunDanmaku.bossDanmaku.BaseNormalDanmaku;
import com.meng.TaiHunDanmaku.task.*;

public class test1 extends BaseNormalDanmaku {
    private TaskManagerEnemyPlane tm;

    @Override
    public void init(Junko b){
        boss=b;
        tm=new TaskManagerEnemyPlane(b,TaskRepeatMode.repeatAll) ;
        shooters=new BulletShooter[]{
                new BulletShooter().init()
                        .setEnemyPlane(boss)
                        .setShooterCenter(boss.objectCenter)
                        .setBulletColor(BulletColor.red)
                        .setBulletForm(BulletForm.xiaoyu)
                        .setBulletWays(1)
                        .setBulletStyle(BulletStyle.snipe)
                        .setBulletWaysDegree(3.75f)
                        .setBulletVelocity(new Vector2(0,-5))
        };
        tm.addTask(new TaskShoot(shooters));
        tm.addTask(new TaskWait(60));
        tm.addTask(new TaskMoveTo(10000,10000));
        tm.addTask(new TaskWait(60));
    }

    @Override
    public void update(){
        super.update();
        tm.update();
        frame++;
    }

}
