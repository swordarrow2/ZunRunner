package com.meng.thbgm;

import android.app.ListFragment;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.meng.thbgm.fileRead.Helper;
import com.meng.thbgm.fileRead.THfmt;

import java.io.File;

public class MusicFragment extends ListFragment {

    public String name;
    public AudioTrack trackplayer = null;
    private THfmt thfmt = null;

    public MusicFragment() {
        this("");
    }

    public MusicFragment(String name) {
        this.name = name;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        File fmt = new File(MainActivity.mainFloder + name + "/thbgm.fmt");
        thfmt = new THfmt(fmt);
        thfmt.load();
        //    Toast.makeText(getActivity(), fmt.getAbsolutePath(), Toast.LENGTH_LONG).show();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, thfmt.names));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        getListView().setItemChecked(position, true);
        if (trackplayer != null && trackplayer.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            trackplayer.stop();
            trackplayer.release();
            trackplayer = null;
        }
        THfmt.MusicInfo musicInfo = thfmt.musicInfos[position];
        int start = musicInfo.start;
        int bufsize = musicInfo.length;
        byte[] data = new byte[bufsize];
        Helper.readFile(data, start, name);
        trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC,
                musicInfo.rate,
                musicInfo.channels == 2 ? AudioFormat.CHANNEL_CONFIGURATION_STEREO : AudioFormat.CHANNEL_CONFIGURATION_MONO,
                musicInfo.bitsPerSample == 16 ? AudioFormat.ENCODING_PCM_16BIT : AudioFormat.ENCODING_PCM_8BIT, bufsize, AudioTrack.MODE_STATIC);
        trackplayer.write(data, 0, data.length);
        // 1 frame = bitsPerSample * channels
        int frameLength = musicInfo.bitsPerSample * musicInfo.channels;
        int startFrame = musicInfo.repeatStart / frameLength;
        int endFrame = musicInfo.length / frameLength;
        Toast.makeText(getActivity(), "fileOffset:" + musicInfo.start + " len:" + musicInfo.length + " loopAtMusicOffset:" + musicInfo.repeatStart + "(" + (1f * (startFrame / endFrame * 100)) + "%)", Toast.LENGTH_LONG).show();
        trackplayer.setLoopPoints(startFrame, endFrame, 1000);
        trackplayer.play();
    }
}