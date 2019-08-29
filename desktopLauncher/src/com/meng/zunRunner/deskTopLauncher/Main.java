package com.meng.zunRunner.deskTopLauncher;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class Main {

    public static void main(String[] args) {
          GameMain.baseEclPath = "F:\\project\\ZunRunner\\2un\\ecl\\";
          GameMain.baseShtPath = "F:\\project\\ZunRunner\\2un\\sht\\";
          GameMain.baseAnmPath = "F:\\project\\ZunRunner\\2un\\anm\\";
        /*  GameMain.baseEclPath = "F:\\project\\ZunRunner\\subed\\";*/
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SJF";
        config.width = GameMain.width;
        config.height = GameMain.height;
        config.vSyncEnabled = true;
        config.resizable = true;
        config.foregroundFPS = 60;
        config.addIcon("textures/beammid2.png", Files.FileType.Internal);
        new LwjglApplication(new GameMain(), config);

        // new zunRunner() ;
        //  EclFile eclFile=new EclFile("st06bs.ecl");
        //  System.out.println(eclFile.toString());
    }
}
