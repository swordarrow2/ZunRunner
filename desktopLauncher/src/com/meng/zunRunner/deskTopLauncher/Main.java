package com.meng.zunRunner.deskTopLauncher;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.meng.gui.ui.GameMain;
import com.meng.gui.ui.PicMain;
import com.meng.gui.ui.PicScreen;
import com.meng.insLogic.helper.DataHelper;
import com.meng.zunRunner.anm.AnmBean;
import com.meng.zunRunner.anm.AnmFile;

public class Main {

    public static void main(String[] args) {

        boolean showPic = true;
        String game = "th15";
        if (showPic) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "SJF";
            config.width = PicMain.width;
            config.height = PicMain.height;
            config.vSyncEnabled = false;
            config.resizable = true;
            config.foregroundFPS = 60;
            config.addIcon("textures/beammid2.png", Files.FileType.Internal);
            new LwjglApplication(new PicMain(), config);
        } else {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "SJF";
            config.width = GameMain.width;
            config.height = GameMain.height;
            config.vSyncEnabled = false;
            config.resizable = true;
            config.foregroundFPS = 60;
            config.addIcon("textures/beammid2.png", Files.FileType.Internal);
            new LwjglApplication(new GameMain(), config);
        }
    }
}
