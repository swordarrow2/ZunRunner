package com.meng.zunRunner.deskTopLauncher;

import com.InsProcess.parse.zunRunner;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SJF";
        config.width = GameMain.width;
        config.height = GameMain.height;
        config.vSyncEnabled=true;
        config.resizable = true;
        config.foregroundFPS = 60;
        config.addIcon("textures/beammid2.png", Files.FileType.Internal);
      new LwjglApplication(new GameMain(), config);

      // new zunRunner() ;
    }
}
