package com.meng.zunRunner;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.meng.TaiHunDanmaku.ui.*;
import com.badlogic.gdx.*;
import com.InsProcess.*;
import android.widget.*;

public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initializeForView(new MyGame(), new AndroidApplicationConfiguration()));
		ScrollView s=new ScrollView(this);
		TextView t=new TextView(this);
		t.setText(new zunRunner().toString());
		s.addView(t);
		setContentView(s);
	  }
  }
