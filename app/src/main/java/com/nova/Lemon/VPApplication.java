package com.nova.Lemon;

import android.app.Application;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class VPApplication extends Application {
    public static VPApplication instance;
    public JCVideoPlayerStandard VideoPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
