package com.meng.thbgm;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class GameNameFragment extends ListFragment {

    private boolean dualPane; // 是否在一屏上同时显示列表和详细内容
    private int curCheckPosition = 0; // 当前选择的索引位置
    private String[] folderName;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        folderName = new File(MainActivity.mainFloder).list();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, folderName));
        View detailFrame = getActivity().findViewById(R.id.detail);
        dualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE; // 判断是否在一屏上同时显示列表和详细内容
        if (savedInstanceState != null) {
            curCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        if (dualPane) { // 如果在一屏上同时显示列表和详细内容
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 设置列表为单选模式
            showDetails(curCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", curCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    void showDetails(int index) {
        curCheckPosition = index;
        if (dualPane) { // 当在一屏上同时显示列表和详细内容时
            getListView().setItemChecked(index, true);
            MusicFragment details = (MusicFragment) getFragmentManager().findFragmentById(R.id.detail);
            if (details != null && details.trackplayer != null && details.trackplayer.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                details.trackplayer.stop();
                details.trackplayer.release();
            }
            if (details == null || !details.name.equals(folderName[index])) {
                details = new MusicFragment(folderName[index]);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.detail, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("name", folderName[index]);
            startActivity(intent);
        }
    }
}
