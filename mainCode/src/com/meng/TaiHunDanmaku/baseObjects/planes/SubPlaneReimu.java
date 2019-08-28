package com.meng.TaiHunDanmaku.baseObjects.planes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.TaiHunDanmaku.BaseGameObject;
import com.meng.TaiHunDanmaku.helpers.Data;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.helpers.TextureNameManager;
import com.meng.TaiHunDanmaku.ui.FightScreen;

public class SubPlaneReimu extends BaseGameObject {

    private Vector2 nowPosition = new Vector2();
    public MyPlane myPlane;
    private int bianHao = 1;
    private int degree = 0;
    private Vector2 vel = new Vector2(0, 37);
    private Vector2 normalPos;
    private  Vector2 slowPos;

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
	private int value;

    public SubPlaneReimu init(MyPlane myPlane,int num) {
        super.init();
        this.bianHao = num;
        this.normalPos=normalPos;
        this.slowPos=slowPos;
        this.myPlane = myPlane;
        size = getSize();
        objectCenter = myPlane.objectCenter.cpy();
        image.setDrawable(ResourcesManager.textures.get(TextureNameManager.ReimuSubPlane));
        image.setSize(size.x, size.y);
        image.setRotation(getRotationDegree());
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        FightScreen.instence.groupNormal.addActor(image);
        powerInc();
        image.setZIndex(Data.zIndexMyPlane);
        return this;
    }
    
    private void powerInc(){
		if(myPlane.power < bianHao){
			normalPos=new Vector2(-1000, -1000);
			slowPos=new Vector2(-1000, -1000);
		}
    	switch (myPlane.power) {
		case 4:
			switch (bianHao) {
			case 4: //subPlanePositions.get(p4n4)
				normalPos=myPlane.shtFile.subPlanePositions.get(p4n4);
				slowPos=myPlane.shtFile.subPlanePositions.get(p4s4);
				break;
			case 3:
				normalPos=myPlane.shtFile.subPlanePositions.get(p4n3);
				slowPos=myPlane.shtFile.subPlanePositions.get(p4s3);
				break;
			case 2:
				normalPos=myPlane.shtFile.subPlanePositions.get(p4n2);
				slowPos=myPlane.shtFile.subPlanePositions.get(p4s2);
				break;
			case 1:
				normalPos=myPlane.shtFile.subPlanePositions.get(p4n1);
				slowPos=myPlane.shtFile.subPlanePositions.get(p4s1);
				break;
			}
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
        if (myPlane.slow) {
               nowPosition.set(myPlane.objectCenter.x - slowPos.x, myPlane.objectCenter.y - slowPos.y);
        } else {
               nowPosition.set(myPlane.objectCenter.x - normalPos.x, myPlane.objectCenter.y - normalPos.y);
        }
        objectCenter.add(nowPosition.sub(objectCenter).scl(0.2f));
        //  image.setDrawable(getDrawable());
        image.setRotation(getRotationDegree());
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        shoot();
    }

    public void shoot() {
        if (myPlane.existTime % 4 == 1) {
            ObjectPools.reimuSubPlaneBulletStraightPool.obtain().init(new Vector2(objectCenter.x + 4, objectCenter.y + 16), vel);
            ObjectPools.reimuSubPlaneBulletStraightPool.obtain().init(new Vector2(objectCenter.x - 4, objectCenter.y + 16), vel);
        }
    }
}
