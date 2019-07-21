package com.aide.trainer.mylibgdxgame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.meng.TaiHunDanmaku.ui.*;
import com.badlogic.gdx.*;

public class MainActivity extends AndroidApplication {

	@Override
	public void setApplicationLogger(ApplicationLogger p1) {
		// TODO: Implement this method
	  }

	@Override
	public ApplicationLogger getApplicationLogger() {
		// TODO: Implement this method
		return null;
	  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
        initialize(new GameMain(), cfg);
    }
}
