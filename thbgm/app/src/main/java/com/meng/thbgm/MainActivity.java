package com.meng.thbgm;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); // 设置使用的布局内容
        //根据采样率，采样精度，单双声道来得到frame的大小。
        int bufsize = AudioTrack.getMinBufferSize(8000,//每秒8K个点
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,//双声道
                AudioFormat.ENCODING_PCM_16BIT);//一个采样点16比特-2个字节
//注意，按照数字音频的知识，这个算出来的是一秒钟buffer的大小。
//创建AudioTrack

        AudioTrack trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufsize,
                AudioTrack.MODE_STREAM);//
        trackplayer.play();//开始
        //    trackplayer.write(bytes_pkg, 0, bytes_pkg.length);//往track中写数据
        byte[] bs = new byte[1];
        trackplayer.write(bs, 0, bs.length);//往track中写数据
        trackplayer.stop();//停止播放
        trackplayer.release();//释放底层资源。
    }

    // 创建一个继承Activity的内部类，用于在手机界面中，通过Activity显示详细内容


}