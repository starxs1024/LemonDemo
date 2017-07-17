package com.nova.Lemon.util;

import android.media.AudioManager;

/**
 * Created by Paraselene on 2017/7/17.
 * Email ：15616165649@163.com
 */

public class AudioPlayUtils {

    private static class AudioPlayUtilsHolder {
        private static AudioPlayUtils instance = new AudioPlayUtils();
    }

    /**
     * 私有的构造函数
     */
    private AudioPlayUtils() {

    }

    public static AudioPlayUtils getInstance() {
        return AudioPlayUtilsHolder.instance;
    }

    /**
     * 调高音量(多媒体音量)
     */
    protected void raiseVoice(AudioManager audioManager) {
        // 强制调用多媒体音量
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
        // //本地保存多媒体音量
        // int nowVol2 =
        // PreferenceHelper.getMyPreference().getSetting().getInt("music", 0);
        // if (nowVol2 != 15) {
        // PreferenceHelper.getMyPreference().getEditor().putInt("music",
        // nowVol2 + 1);
        // PreferenceHelper.getMyPreference().getEditor().commit();
        // AudioPlayUtils.getInstance().SetVoice(PreferenceHelper.getMyPreference().getSetting().getInt("music",
        // 0));
        // }
    }

    /**
     * 调小音量(多媒体音量)
     */
    protected void lowerVoice(AudioManager audioManager) {
        // 强制调用多媒体音量
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
        // //本地保存多媒体音量
        // int nowVol =
        // PreferenceHelper.getMyPreference().getSetting().getInt("music", 0);
        // if (nowVol != 0) {
        // PreferenceHelper.getMyPreference().getEditor().putInt("music", nowVol
        // - 1);
        // PreferenceHelper.getMyPreference().getEditor().commit();
        // AudioPlayUtils.getInstance().SetVoice(PreferenceHelper.getMyPreference().getSetting().getInt("music",
        // 0));
        // }
    }

}
