package com.meng.gui.planes;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.gui.BaseGameObject;
import com.meng.gui.helpers.Data;
import com.meng.gui.helpers.ObjectPools;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.gui.helpers.TextureNameManager;
import com.meng.gui.ui.FightScreen;
import com.meng.zunRunner.sht.Shooter;

public class SubPlaneReimu extends BaseGameObject {

	public Vector2 nowPosition = new Vector2();
	public MyPlaneReimu myPlaneReimu;
	private int bianHao = 1;
	private int degree = 0;
	private Vector2 normalPos;
	private Vector2 slowPos;
	private Shooter[] normalShooters;
	private Shooter[] slowShooters;

	int p1n = 0;
	int p2n1 = 1;
	int p2n2 = 2;
	int p3n1 = 3;
	int p3n2 = 4;
	int p3n3 = 5;
	int p4n1 = 6;
	int p4n2 = 7;
	int p4n3 = 8;
	int p4n4 = 9;
	int p1s = 10;
	int p2s1 = 11;
	int p2s2 = 12;
	int p3s1 = 13;
	int p3s2 = 14;
	int p3s3 = 15;
	int p4s1 = 16;
	int p4s2 = 17;
	int p4s3 = 18;
	int p4s4 = 19;

	public SubPlaneReimu init(MyPlaneReimu myPlaneReimu, int num) {
		super.init();
		this.bianHao = num;
		this.myPlaneReimu = myPlaneReimu; 
		objectCenter = myPlaneReimu.objectCenter.cpy();
		image.setDrawable(ResourcesManager.textures.get("pl0033"));
		//image.setDrawable(ResourcesManager.playerDrawabls.get(53));
        image.setSize(image.getDrawable().getMinWidth(), image.getDrawable().getMinHeight());
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        image.setRotation(0);
		powerInc();
		FightScreen.instence.groupNormal.addActor(image);
		image.setZIndex(Data.zIndexMyPlane);
		return this;
	}

	private void powerInc() {
		if (myPlaneReimu.power < bianHao) {
			normalPos = new Vector2(-1000, -1000);
			slowPos = new Vector2(-1000, -1000);
		}
		int shooterInThis = 0;
		Shooter[] s = myPlaneReimu.shtFile.shootersList.get(myPlaneReimu.power);
		ArrayList<Shooter> shooters = new ArrayList<>();
		for (Shooter sh : s) {
			if (sh.option == bianHao) {
				++shooterInThis;
				shooters.add(sh);
			}
		}
		normalShooters = new Shooter[shooterInThis];
		for (int i = 0; i < normalShooters.length; ++i) {
			normalShooters[i] = shooters.get(i);
		}
		s = myPlaneReimu.shtFile.shootersList.get(myPlaneReimu.power+ myPlaneReimu.shtFile.playerArg.maxPower+1);
		shooters.clear();
		shooterInThis = 0;
		for (Shooter sh : s) {
			if (sh.option == bianHao) {
				++shooterInThis;
				shooters.add(sh);
			}
		}
		slowShooters = new Shooter[shooterInThis];
		for (int i = 0; i < slowShooters.length; ++i) {
			slowShooters[i] = shooters.get(i);
		}
		switch (myPlaneReimu.power) {
		case 4:
			switch (bianHao) {
			case 4:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p4n4);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p4s4);
				break;
			case 3:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p4n3);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p4s3);
				break;
			case 2:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p4n2);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p4s2);
				break;
			case 1:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p4n1);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p4s1);
				break;
			}
			break;
		case 3:
			switch (bianHao) {
			case 3:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p3n3);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p3s3);
				break;
			case 2:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p3n2);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p3s2);
				break;
			case 1:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p3n1);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p3s1);
				break;
			}
			break;
		case 2:
			switch (bianHao) { 
			case 2:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p2n2);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p2s2);
				break;
			case 1:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p2n1);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p2s1);
				break;
			} 
			case 1:
				normalPos = myPlaneReimu.shtFile.subPlanePositions.get(p1n);
				slowPos = myPlaneReimu.shtFile.subPlanePositions.get(p1s);
				break; 
		}
		
	}

	@Override
	public void kill() {
		super.kill();
		image.remove();
	}

	public float getRotationDegree() {
		degree += 5;
		return degree;
	}

	public Vector2 getSize() {
		return new Vector2(16, 16);
	}

	@Override
	public void update() {
		super.update();
		//myPlaneReimu.slow=true;
		if (myPlaneReimu.slow) {
			nowPosition.set(myPlaneReimu.objectCenter.x - slowPos.x, myPlaneReimu.objectCenter.y - slowPos.y);
		} else {
			nowPosition.set(myPlaneReimu.objectCenter.x - normalPos.x, myPlaneReimu.objectCenter.y - normalPos.y);
		}
		objectCenter.add(nowPosition.sub(objectCenter).scl(0.3f));
		// image.setDrawable(getDrawable());
		//image.setRotation(getRotationDegree());
		image.setPosition(objectCenter.x, objectCenter.y, Align.center);
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
		shoot();
	}

	public void shoot() {
		if(myPlaneReimu.slow){
			for (Shooter shooter : slowShooters) {
				if (existTime % shooter.rate == 0) {
					ObjectPools.reimuSubPlaneBulletStraightPool.obtain().init(
							new Vector2(objectCenter.x - shooter.pos.x, objectCenter.y - shooter.pos.y), shooter.power,
							new Vector2(-shooter.speed, 0).rotateRad(shooter.angle),shooter.ANM);
				}
			}
		}else { 
			for (Shooter shooter : normalShooters) {
				if (existTime % shooter.rate == 0) {
					ObjectPools.reimuSubPlaneBulletStraightPool.obtain().init(
							new Vector2(objectCenter.x - shooter.pos.x, objectCenter.y - shooter.pos.y), shooter.power,
							new Vector2(-shooter.speed, 0).rotateRad(shooter.angle),shooter.ANM);
				}
			}
		}
	}
}
