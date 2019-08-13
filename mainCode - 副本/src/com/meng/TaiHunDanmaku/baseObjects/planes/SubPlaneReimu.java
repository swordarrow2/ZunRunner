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

    private Vector2 nowPosition = Vector2.Zero;
    public MyPlane myPlane;
    private int bianHao = 1;
    private int degree = 0;
    private Vector2 vel = new Vector2(0, 37);
    private int[] subPlanePosition = new int[]{
            0, 20,
            0, 32,
            -16, 20, 16, 20,
            -32, 0, 32, 0,
            -16, 20, 0, 30, 16, 20,
            -32, 0, 0, 32, 32, 0,
            -16, 20, -8, 30, 8, 30, 16, 20,
            -32, 0, -16, 32, 16, 32, 32, 0
    };

    public SubPlaneReimu init(MyPlane myPlane, int subPlaneNumber) {
        super.init();
        this.bianHao = subPlaneNumber;
        subPlanePosition = getSubPlanePosition();
        this.myPlane = myPlane;
        size = getSize();
        objectCenter = myPlane.objectCenter.cpy();
        image.setDrawable(ResourcesManager.textures.get(TextureNameManager.ReimuSubPlane));
        image.setSize(size.x, size.y);
        image.setRotation(getRotationDegree());
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        FightScreen.instence.groupNormal.addActor(image);
        image.setZIndex(Data.zIndexMyPlane);
        return this;
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

    public int[] getSubPlanePosition() {
        return subPlanePosition;
    }

    public Vector2 getSize() {
        return new Vector2(16, 16);
    }

    @Override
    public void update() {
        super.update();
        if (myPlane.slow) {
            switch (bianHao) {
                case 1:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[24], myPlane.objectCenter.y + subPlanePosition[25]);
                    break;
                case 2:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[26], myPlane.objectCenter.y + subPlanePosition[27]);
                    break;
                case 3:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[28], myPlane.objectCenter.y + subPlanePosition[29]);
                    break;
                case 4:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[30], myPlane.objectCenter.y + subPlanePosition[31]);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + bianHao);
            }
        } else {
            switch (bianHao) {
                case 1:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[32], myPlane.objectCenter.y + subPlanePosition[33]);
                    break;
                case 2:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[34], myPlane.objectCenter.y + subPlanePosition[35]);
                    break;
                case 3:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[36], myPlane.objectCenter.y + subPlanePosition[37]);
                    break;
                case 4:
                    nowPosition.set(myPlane.objectCenter.x + subPlanePosition[38], myPlane.objectCenter.y + subPlanePosition[39]);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + bianHao);
            }
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
