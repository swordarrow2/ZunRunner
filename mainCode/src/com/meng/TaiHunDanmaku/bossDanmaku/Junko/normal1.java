package com.meng.TaiHunDanmaku.bossDanmaku.Junko;

import com.InsProcess.parse.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.*;
import com.meng.TaiHunDanmaku.bossDanmaku.*;
import com.meng.TaiHunDanmaku.helpers.*;
import com.meng.TaiHunDanmaku.task.*;
import com.meng.TaiHunDanmaku.ui.*;

import java.util.*;
import java.io.*;
import android.os.*;

public class normal1 extends BaseNormalDanmaku {
    private TaskManagerEnemyPlane taskManager;
    private Laser laser;

    @Override
    public void init(Junko baseBossPlane) {
        boss = baseBossPlane;
        laser = new Laser(
                new Sprite(((TextureRegionDrawable) ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()),
                new Sprite(((TextureRegionDrawable) ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()));
        //  laser.color = Color.RED;
        laser.position.set(new Vector2(183, 225));
        laser.degrees = 180;
        laser.distance = 60;

        Laser laser2 = new Laser(new Sprite(new Texture(Gdx.files.internal("textures/beamstart2.png"))),
                new Sprite(new Texture(Gdx.files.internal("textures/beammid2.png"))));
        //    laser2.color = Color.RED;
        laser2.position.set(boss.objectCenter.x - 10, boss.objectCenter.y - 10);
        laser2.degrees = 160;
        laser2.distance = 190;

      /*  ArrayList<Task> arrayList = new ArrayList<Task>();
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
        };*/
        Ecl ecl = new Ecl();
		Sub card7 = ecl.sub();
		
        card7.parse(read("/storage/emulated/0/AppProjects/ZunRunner/subed/BossCard7.txt"));
		
		Sub sub1   =ecl.sub();
		sub1.parse(read("/storage/emulated/0/AppProjects/ZunRunner/subed/BossCard7_at.txt"));
		
		Sub  sub2  =ecl.sub();
		sub2.parse(read("/storage/emulated/0/AppProjects/ZunRunner/subed/BossCard7_at2.txt"));
		
		Sub sub2b   =ecl.sub();
		sub2b.parse(read("/storage/emulated/0/AppProjects/ZunRunner/subed/BossCard7_at2b.txt"));
		
		Sub  sub3  =ecl.sub();
		sub3.parse(read("/storage/emulated/0/AppProjects/ZunRunner/subed/BossCard7_at3.txt"));
		
		Sub sub3b   =ecl.sub();
	    sub3b.parse(read("/storage/emulated/0/AppProjects/ZunRunner/subed/BossCard7_at3b.txt"));
		
			
        Sub sub4 = ecl.sub();
		sub4.parse(read("/storage/emulated/0/AppProjects/ZunRunner/subed/BossCard7_at4.txt"));

		
      
    //    shooters = testSub.bulletShooters;
        taskManager = new TaskManagerEnemyPlane(baseBossPlane, TaskRepeatMode.repeatAll);
        //   taskManager.addTask(new TaskShoot(shooters));
        card7.start();
        taskManager.addTask(new TaskRunnable(new Runnable() {

            @Override
            public void run() {
                Laser laser = new Laser(
                        new Sprite(((TextureRegionDrawable) ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()),
                        new Sprite(((TextureRegionDrawable) ResourcesManager.textures.get("bullet" + (0 * 16 + 8))).getRegion()));
                laser.position.set(new Vector2(FightScreen.instence.boss.getLocation().x, FightScreen.instence.boss.getLocation().y));
                laser.degrees = 0;
                laser.distance = 150;
            }
        }));
        taskManager.addTask(new TaskWait(60));
        taskManager.addTask(new TaskMoveTo(10000, 10000));
        taskManager.addTask(new TaskWait(60));
    }
	
	public String read(String path) {
        String s = "";
		File eclFile=new File(path);
        try {
            if (!eclFile.exists()) {
                throw new NullPointerException("file not found:"+eclFile.getAbsolutePath());
			  }
            long filelength = eclFile.length();
            byte[] filecontent = new byte[(int) filelength];
            FileInputStream in = new FileInputStream(eclFile);
            in.read(filecontent);
            in.close();
            s = new String(filecontent, "Shift_JIS");
		  } catch (Exception e) {
            e.printStackTrace();
		  }
        return s;
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
