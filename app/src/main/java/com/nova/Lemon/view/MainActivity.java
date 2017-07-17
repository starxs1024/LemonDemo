package com.nova.Lemon.view;

import android.app.Service;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nova.Lemon.R;
import com.nova.Lemon.util.CacheDataUtils;
import com.nova.Lemon.VPApplication;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.iv_sound)
    ImageView ivSound;
    private AudioManager audioManager = null; // 音频

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nv_left)
    NavigationView nvReight;
    @Bind(R.id.drawerlayout_activity_main)
    DrawerLayout drawerlayoutActivityMain;
    @Bind(R.id.toolbar_main_reight)
    LinearLayout toolbarMainReight;

    @Bind(R.id.toolbar_main_left)
    LinearLayout toolbarMainLeft;

    private String mDirSize = "";
    private MyHandler mHandler;
    public static final int SUCESS = 0;
    public static final int FAILED = 1;
    // 默认声音 为静音
    boolean isSound = true;
    // 当前音量
    int currentVolume;

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

        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);// 开局静音
        // // 当前音量
        // currentVolume = audioManager
        // .getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置静音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        if (isSound) {
            ivSound.setImageResource(R.drawable.sound_off);
            isSound = false;
        }

        /*************************** 左侧 侧滑菜单 设置选择事件 ***************************/
        nvReight.setCheckedItem(R.id.nav_clear_cache);
        nvReight.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        nvReight.setCheckedItem(item.getItemId());
                        drawerlayoutActivityMain.closeDrawers();
                        switch (item.getItemId()) {
                        case R.id.nav_clear_cache:
                            clearCache();
                            break;

                        default:
                            break;
                        }
                        return false;
                    }
                });
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

    /****************************** 右侧侧滑菜单open操作 ***********************************/
    @OnClick(R.id.toolbar_main_reight)
    public void onViewClicked() {
        // mdrawerLayout.openDrawer(Gravity.LEFT);//这里设置的方向应该跟下面xml文件里面的gravity方向相同，不然会报错,start和LEFT都为从左边出现
        if (drawerlayoutActivityMain.isDrawerOpen(Gravity.RIGHT)) {
            drawerlayoutActivityMain.closeDrawer(Gravity.RIGHT);
        } else {
            drawerlayoutActivityMain.openDrawer(Gravity.RIGHT);
        }
    }

    /****************************** 左侧声音的open操作 ***********************************/

    @OnClick({ R.id.iv_sound, R.id.toolbar_main_left })
    public void onViewClicked(View view) {
        switch (view.getId()) {
        case R.id.iv_sound:
            setSound();
            break;
        case R.id.toolbar_main_left:
            // 全局静音和开音
            setSound();
            break;
        }
    }

    /****************************** 缓存清理操作 ***********************************/
    private void clearCache() {
        try {
            // 清理前的缓存大小
            mDirSize = CacheDataUtils
                    .getTotalCacheSize(getApplicationContext());
            new AlertDialog.Builder(MainActivity.this).setTitle("确定要清理缓存")
                    .setMessage("缓存大小：" + mDirSize).setPositiveButton("清理",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CacheDataUtils.clearAllCache(
                                                    getApplicationContext());
                                            try {
                                                if (CacheDataUtils
                                                        .getTotalCacheSize(
                                                                getApplicationContext())
                                                        .startsWith("0")) {
                                                    mHandler.sendEmptyMessage(
                                                            SUCESS);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();

                                }
                            })
                    .setNegativeButton("取消", null).show();
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    class MyHandler extends Handler {
        WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity theActivity = mActivity.get();
            if (theActivity == null || theActivity.isFinishing()) {
                return;
            }
            // 消息处理
            switch (msg.what) {
            case SUCESS:
                // ActivityUtils.showToast("清理成功");
                Toast.makeText(getApplicationContext(), "清理完成",
                        Toast.LENGTH_SHORT).show();
                break;
            case FAILED:

                break;
            default:
                break;
            }
        }
    }

    /**************************** 设置声音 ********************************/
    private void setSound() {
        if (!isSound) {
            ivSound.setImageResource(R.drawable.sound_open);
            isSound = true;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 50, 0);// tempVolume:音量绝对值
        } else {
            ivSound.setImageResource(R.drawable.sound_off);
            isSound = false;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);// 静音

        }
    }

    /**************************** 强制设置媒体声音 ********************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN://游戏音量减小
//                AudioPlayUtils.getInstance().lowerVoice();
                lowerVoice();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP://游戏音量增大
//                AudioPlayUtils.getInstance().raiseVoice();
                raiseVoice();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 调高音量(多媒体音量)
     */
    public void raiseVoice() {
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
    public void lowerVoice() {
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
