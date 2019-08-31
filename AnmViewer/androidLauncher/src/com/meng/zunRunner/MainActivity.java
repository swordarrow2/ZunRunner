package com.meng.zunRunner;

import android.os.*;
import com.badlogic.gdx.backends.android.*;
import com.meng.gui.ui.*;
import android.view.*;

public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//PicMain.pathBase = "/storage/emulated/0/AppProjects/ZunRunner/2un/";
		//PicMain.anmPath="/storage/emulated/0/AppProjects/ZunRunner/2un/textures/th14_sakuya.anm";
        setContentView(initializeForView(new PicMain(), new AndroidApplicationConfiguration()));
	  }  
  }
