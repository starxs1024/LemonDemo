package com.nova.Lemon.view;

import android.app.Service;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nova.Lemon.R;
import com.nova.Lemon.VPApplication;
import com.nova.Lemon.util.ActivityUtils;
import com.nova.Lemon.util.CacheDataUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.iv_sound)
    ImageView ivSound;
    @BindView(R.id.toolbar_main_left)
    LinearLayout toolbarMainLeft;
    @BindView(R.id.toolbar_main_reight)
    LinearLayout toolbarMainReight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nv_right)
    NavigationView nvRight;
    @BindView(R.id.drawerlayout_activity_main)
    DrawerLayout drawerlayoutActivityMain;

    private ActivityUtils utils;
    private AudioManager audioManager = null; // 音频

    private String mDirSize = "";
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

        /*
         * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 透明状态栏
         * getWindow().addFlags(
         * WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); }
         */
        // 设置软键盘的模式为适应屏幕模式
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 标题栏
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);// 开局静音

        // 设置静音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        if (isSound) {
            ivSound.setImageResource(R.drawable.sound_off);
            isSound = false;
        }

        /*************************** 右侧 侧滑菜单 设置选择事件 ***************************/
        // 设置MenuItem的字体颜色
        Resources resource = getBaseContext().getResources();
        ColorStateList csl = resource
                .getColorStateList(R.color.navigation_menu_item_color);
        nvRight.setItemTextColor(csl);
        nvRight.setCheckedItem(R.id.nav_clear_cache);
        nvRight.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        nvRight.setCheckedItem(item.getItemId());
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
            } else if (VPApplication.instance.VideoPlaying.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                JCVideoPlayer.releaseAllVideos();
            }
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
                                                    MainActivity.this);
                                            SystemClock.sleep(1000);
                                            try {

                                                if (CacheDataUtils
                                                        .getTotalCacheSize(
                                                                MainActivity.this)
                                                        .startsWith("0")) {
                                                    handler.sendEmptyMessage(
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

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SUCESS:
                Toast.makeText(MainActivity.this, "清理完成", Toast.LENGTH_SHORT)
                        .show();
                try {
                    // 显示的TextView上显示清理前的缓存大小
                    // txtCacheSize.setText(CacheDataManager.getTotalCacheSize(SettingsActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    };

    /**************************** 设置声音 ********************************/
    private void setSound() {
        if (!isSound) {
            ivSound.setImageResource(R.drawable.sound_open);
            isSound = true;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);// tempVolume:音量绝对值
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
        case KeyEvent.KEYCODE_VOLUME_DOWN:// 游戏音量减小
            AudioPlayUtils.getInstance().lowerVoice(audioManager);
            // 当前音量
            int currentVolume = audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume == 0) {
                ivSound.setImageResource(R.drawable.sound_off);
                isSound = false;
            }
            return true;
        case KeyEvent.KEYCODE_VOLUME_UP:// 游戏音量增大
            AudioPlayUtils.getInstance().raiseVoice(audioManager);
            ivSound.setImageResource(R.drawable.sound_open);
            isSound = true;
            return true;
        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({ R.id.iv_sound, R.id.toolbar_main_left, R.id.toolbar_main_reight,
            R.id.drawerlayout_activity_main })
    public void onViewClicked(View view) {
        switch (view.getId()) {
        /****************************** 左侧声音的open操作 ***********************************/
        case R.id.iv_sound:
            setSound();
            break;
        case R.id.toolbar_main_left:
            // 全局静音和开音
            setSound();
            break;
        /****************************** 右侧侧滑菜单open操作 ***********************************/
        case R.id.toolbar_main_reight:
            // mdrawerLayout.openDrawer(Gravity.LEFT);//这里设置的方向应该跟下面xml文件里面的gravity方向相同，不然会报错,start和LEFT都为从左边出现
            if (drawerlayoutActivityMain.isDrawerOpen(Gravity.RIGHT)) {
                drawerlayoutActivityMain.closeDrawer(Gravity.RIGHT);
            } else {
                drawerlayoutActivityMain.openDrawer(Gravity.RIGHT);
            }
            break;
        case R.id.drawerlayout_activity_main:
            break;
        }
    }

    @OnClick(R.id.toolbar_main_left)
    public void onViewClicked() {
    }
}

class AudioPlayUtils {
    private static class AudioPlayUtilsHolder {
        private static AudioPlayUtils instance = new AudioPlayUtils();
    }

    /**
     * 私有的构造函数
     */
    private AudioPlayUtils() {

    }

    public static AudioPlayUtils getInstance() {
        return AudioPlayUtils.AudioPlayUtilsHolder.instance;
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
