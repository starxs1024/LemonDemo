package com.nova.Lemon.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;

import com.nova.Lemon.R;
import com.nova.Lemon.util.VPApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar_main_reight)
    LinearLayout toolbarMainReight;

    // @Bind(R.id.toolbar)
    // Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 取消标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // 取消状态栏
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 标题栏
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (VPApplication.instance.VideoPlaying != null) {
            if (VPApplication.instance.VideoPlaying.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                VPApplication.instance.VideoPlaying.startButton.performClick();
            } else if (VPApplication.instance.VideoPlaying.currentState == JCVideoPlayer.CURRENT_STATE_PREPAREING) {
                JCVideoPlayer.releaseAllVideos();
            }
        }
    }
}
