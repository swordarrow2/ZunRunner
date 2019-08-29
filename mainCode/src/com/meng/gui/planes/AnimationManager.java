package com.meng.gui.planes;

import com.meng.gui.helpers.ResourcesManager;

public class AnimationManager{
    private MyPlaneReimu myPlaneReimu;
    private int animFrom=0;
    private int animTo=7;
    private int everyAnimFrameTime=0;
    private int time=0;
    private int curFrameNumber=0;
    private MoveStatus status=MoveStatus.stay;

    public AnimationManager(MyPlaneReimu obj, int everyAnimFrameTime){
        this.everyAnimFrameTime=everyAnimFrameTime;
        myPlaneReimu =obj;
    }

    public void setStatus(MoveStatus mov){
        if(mov==status) return;
        status=mov;
        switch(status){
            case stay:
                animFrom=0;
                animTo=7;
                curFrameNumber=animFrom;
                break;
            case moveLeft:
                animFrom=8;
                animTo=15;
                curFrameNumber=animFrom;
                break;
            case moveRight:
                animFrom=16;
                animTo=23;
                curFrameNumber=animFrom;
                break;
        }
    }

    public void update(){
        ++time;
        if(time>=everyAnimFrameTime){
            ++curFrameNumber;
            time=0;
        }
        if(curFrameNumber>animTo){
            curFrameNumber=animFrom+5;
        }
        myPlaneReimu.image.setDrawable(ResourcesManager.textures.get("pl00"+curFrameNumber));
       // myPlaneReimu.image.setDrawable(ResourcesManager.playerDrawabls.get(curFrameNumber));
    }
}
