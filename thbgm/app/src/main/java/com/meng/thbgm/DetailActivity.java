package com.meng.thbgm;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否为横屏，如果为横屏，则结束当前Activity，准备使用Fragment显示详细内容
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        if (savedInstanceState == null) {
            Toast.makeText(this,getIntent().getStringExtra("name"),Toast.LENGTH_LONG).show();
            MusicFragment details = new MusicFragment(getIntent().getStringExtra("name"));
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}
