package com.meng.TaiHunDanmaku.bossDanmaku.Junko;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.*;
import com.meng.TaiHunDanmaku.bossDanmaku.*;
import com.meng.TaiHunDanmaku.helpers.*;
import com.meng.TaiHunDanmaku.task.*;
import java.util.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.meng.TaiHunDanmaku.ui.*;

public class normal1 extends BaseNormalDanmaku {
    private TaskManagerEnemyPlane taskManager;
    private Laser laser;
    @Override
    public void init(Junko baseBossPlane) {
        boss = baseBossPlane;
        laser = new Laser(
		  new Sprite(((TextureRegionDrawable)ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()),
		  new Sprite(((TextureRegionDrawable)ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()));		  
		//  laser.color = Color.RED;
        laser.position.set(new Vector2(183,225));
        laser.degrees = 180;
        laser.distance = 60;

        Laser laser2 = new Laser(new Sprite(new Texture(Gdx.files.internal("textures/beamstart2.png"))),
                new Sprite(new Texture(Gdx.files.internal("textures/beammid2.png"))));
    //    laser2.color = Color.RED;
        laser2.position.set(boss.objectCenter.x - 10, boss.objectCenter.y - 10);
        laser2.degrees = 160;
        laser2.distance = 190;

        ArrayList<Task> arrayList = new ArrayList<Task>();
        shooters = new BulletShooter[]{
                new BulletShooter().init()
                        .setEnemyPlane(boss)
                        .setShooterCenter(boss.objectCenter)
                        .setBulletColor(6)
                        .setBulletForm(6)
                        .setBulletWays(112)
                        .setBulletVelocity(new Vector2(0, -1))
                        .setBulletStyle(2)
                        .setBulletHighLight(true)
                        .setBulletAcceleration(new Vector2(0, -0.075f))
                        .setBulletTasks(arrayList)
        };
        taskManager = new TaskManagerEnemyPlane(baseBossPlane, TaskRepeatMode.repeatAll);
        taskManager.addTask(new TaskShoot(shooters));
		taskManager.addTask(new TaskRunnable(new Runnable(){

								  @Override
								  public void run() {
									  Laser laser = new Laser(
										new Sprite(((TextureRegionDrawable)ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()),
										new Sprite(((TextureRegionDrawable)ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()));
										laser.position.set(new Vector2(FightScreen.instence.boss.getLocation().x,FightScreen.instence.boss.getLocation().y));
									  laser.degrees = 0;
									  laser.distance = 150; 
									}
								}));
        taskManager.addTask(new TaskWait(60));
        taskManager.addTask(new TaskMoveTo(10000, 10000));
        taskManager.addTask(new TaskWait(60));
    }

    @Override
    public void update() {
        super.update();
        taskManager.update();
        frame++;
         // laser.degrees = frame * 0.3f;
		// laser.position.y+=5; 
    }

}
