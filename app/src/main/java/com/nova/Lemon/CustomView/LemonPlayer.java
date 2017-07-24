package com.nova.Lemon.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nova.Lemon.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Paraselene on 2017/7/23. Email ：15616165649@163.com
 */

public class LemonPlayer extends JCVideoPlayerStandard {
    public ImageView start;

    public LemonPlayer(Context context) {
        super(context);
    }

    public LemonPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public void init(Context context) {
        super.init(context);
        start = (ImageView) findViewById(R.id.start);
        start.setOnClickListener(this);

    }


    @Override
    public int getLayoutId() {
        return R.layout.lemon_player;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.start) {
            Toast.makeText(getContext(), "Whatever the icon means" ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            start.setVisibility(View.VISIBLE);
        } else {
            start.setVisibility(View.INVISIBLE);
        }
    }
}
