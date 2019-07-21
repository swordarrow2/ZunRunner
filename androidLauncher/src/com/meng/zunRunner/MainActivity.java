package com.meng.zunRunner;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.meng.TaiHunDanmaku.ui.*;
import com.badlogic.gdx.*;
import com.InsProcess.*;

public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new GameMain(), new AndroidApplicationConfiguration());
    }
}
