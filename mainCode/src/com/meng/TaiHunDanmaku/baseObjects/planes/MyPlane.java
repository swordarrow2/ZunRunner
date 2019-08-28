package com.meng.TaiHunDanmaku.baseObjects.planes;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.TaiHunDanmaku.BaseGameObject;
import com.meng.TaiHunDanmaku.helpers.Data;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.ui.FightScreen;
import com.meng.TaiHunDanmaku.ui.GameMain;
import com.meng.sht.Shooter;
import com.meng.sht.ShtFile;

public class MyPlane extends BaseGameObject {

    public static MyPlane instance;

    private JudgeCircleAnimation animation = null;
    private JudgeCircleAnimation2 animation2 = null;
    private float playerLastX = 270;
    public boolean slow = false;
    private AnimationManager animationManager;
    private SubPlaneReimu subPlane1, subPlane2, subPlane3, subPlane4;
    public ShtFile shtFile;
    private GameMain gameMain;
    
	private Shooter[] normalShooters;
	private Shooter[] slowShooters;
	
    public int power=4;

    public void init(GameMain gameMain) {
        super.init();
        shtFile=new ShtFile("pl00.sht");
        instance = this;
        this.gameMain = gameMain;
        animation = new JudgeCircleAnimation();
        animation.init();
        animation2 = new JudgeCircleAnimation2();
        animation2.init();
        existTime = 0;
        objectCenter.set(GameMain.width / 2, 80);
        image.setSize(30, 46);
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        powerInc();
        FightScreen.instence.groupNormal.addActor(image);
        image.setZIndex(Data.zIndexMyPlane);
        animationManager = new AnimationManager(this, 5);
        subPlane4 = new SubPlaneReimu().init(this, 4);
        subPlane3 = new SubPlaneReimu().init(this, 3);
        subPlane2 = new SubPlaneReimu().init(this, 2);
        subPlane1 = new SubPlaneReimu().init(this, 1);
    }

    @Override
    public void kill() {
        super.kill();
        ++gameMain.miss;
    }

    @Override
    public void update() {
        super.update();
        animFlag++;
        objectCenter = new Vector2(MathUtils.clamp(objectCenter.x, 10, GameMain.width - 10), MathUtils.clamp(objectCenter.y, 10, GameMain.height - 10));
        if (image.getRotation() != 0) {
            image.setRotation(0);
        }
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        shoot();
        if (objectCenter.x > playerLastX) {
            playerLastX = objectCenter.x;
            animationManager.setStatus(MoveStatus.moveRight);
        } else if (objectCenter.x < playerLastX) {
            playerLastX = objectCenter.x;
            animationManager.setStatus(MoveStatus.moveLeft);
        } else {
            animationManager.setStatus(MoveStatus.stay);
        }
        animationManager.update();
        image.toBack();
        animation2.update();
        animation.update();
        subPlane4.update();
        subPlane3.update();
        subPlane2.update();
        subPlane1.update();
    }

    private void powerInc() {
		int shooterInThis = 0;
		Shooter[] s = shtFile.shootersList.get( power+1);
		ArrayList<Shooter> shooters = new ArrayList<>();
		for (Shooter sh : s) {
			if (sh.option == 0) {
				++shooterInThis;
				shooters.add(sh);
			}
		}
		normalShooters = new Shooter[shooterInThis];
		for (int i = 0; i < normalShooters.length; ++i) {
			normalShooters[i] = shooters.get(i);
		}
		s = shtFile.shootersList.get(power +shtFile.playerArg.maxPower+1);
		shooters.clear();
		shooterInThis = 0;
		for (Shooter sh : s) {
			if (sh.option == 0) {
				++shooterInThis;
				shooters.add(sh);
			}
		}
		slowShooters = new Shooter[shooterInThis];
		for (int i = 0; i < slowShooters.length; ++i) {
			slowShooters[i] = shooters.get(i);
		} 
	}
    
    
    private void shoot() {

		if(slow){ 
			for (Shooter shooter : slowShooters) {
				if (existTime % shooter.rate == 0) {
					ObjectPools.reimuShootPool.obtain().init(
							new Vector2(objectCenter.x - shooter.pos.x, objectCenter.y - shooter.pos.y), shooter.power,
							new Vector2(-shooter.speed, 0).rotateRad(shooter.angle),shooter.ANM);
				}
			}
		}else { 
			for (Shooter shooter : normalShooters) {
				if (existTime % shooter.rate == 0) {
					ObjectPools.reimuShootPool.obtain().init(
							new Vector2(objectCenter.x - shooter.pos.x, objectCenter.y - shooter.pos.y), shooter.power,
							new Vector2(-shooter.speed, 0).rotateRad(shooter.angle),shooter.ANM);
				}
			}
		}
	}

}
