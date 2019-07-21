package com;

import com.InsProcess.*;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SJF";
        config.width = 386;
        config.height = 450;
        config.resizable = false;
        config.foregroundFPS = 60;
        config.addIcon("textures/beammid1.png", Files.FileType.Internal);
        new LwjglApplication(new GameMain(), config);

        System.out.println(new zunRunner().toString());

    }

}