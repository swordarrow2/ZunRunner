package com.meng.zunRunner;

import android.os.*;
import android.widget.*;
import com.InsProcess.*;
import com.badlogic.gdx.backends.android.*;
import java.io.*;
import com.meng.TaiHunDanmaku.ui.*;

public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		FightScreen.baseEclPath="/storage/emulated/0/AppProjects/ZunRunner/subed/";
        setContentView(initializeForView(new GameMain(), new AndroidApplicationConfiguration()));
		/*ScrollView s=new ScrollView(this);
		TextView t=new TextView(this);
		t.setText(new UnpackedEclParser(new File(Environment.getExternalStorageDirectory()+"/AppProjects/ZunRunner/subed/BossCard7_at4.txt")).read());
		s.addView(t);
		setContentView(s);*/
	  }
  }
