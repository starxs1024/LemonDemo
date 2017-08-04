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
    public ImageView start, thumb, iv_coverage;

    public boolean isLigth = false;
    public boolean isChange = false;

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
        thumb = (ImageView) findViewById(R.id.thumb);
        iv_coverage = (ImageView) findViewById(R.id.iv_coverage);

    }

    @Override
    public int getLayoutId() {
        return R.layout.lemon_player;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.start) {
            // Toast.makeText(getContext(), "Whatever the icon means",
            // Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        thumb.setAlpha(0.1f);
//        iv_coverage.setAlpha(0.1f);
//        iv_coverage.setVisibility(VISIBLE);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            start.setVisibility(View.VISIBLE);
        } else {
            start.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        thumb.setAlpha(1f);
        iv_coverage.setVisibility(INVISIBLE);

    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        setTransparencyVisible(VISIBLE);
//        iv_coverage.setAlpha(0.1f);
        // thumb.setImageResource(R.mipmap.ic_launcher);

    }

    @Override
    public void onPrepared() {
        super.onPrepared();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        thumb.setAlpha(0.1f);
//        iv_coverage.setAlpha(0.1f);
        // iv_coverage.setVisibility(VISIBLE);
    }

    public void setLigth(boolean isLigth) {
        isChange = isLigth;

    }

    // @Override
    // public void setAllControlsVisible(int topCon, int bottomCon, int
    // startBtn,
    // int loadingPro, int thumbImg, int coverImg, int bottomPro) {
    // super.setAllControlsVisible(topCon, bottomCon, startBtn, loadingPro,
    // thumbImg, coverImg, bottomPro);
    // }
    public void setTransparencyVisible(int thumbImg) {
        thumb.setVisibility(thumbImg);

    }

    @Override
    public void onStatePreparing() {
        // 视频预加载时期
        super.onStatePreparing();
        thumb.setAlpha(1f);
//        iv_coverage.setAlpha(0.1f);

    }
}
