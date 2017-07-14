package com.nova.Lemon.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nova.Lemon.R;
import com.nova.Lemon.util.CacheDataUtils;
import com.nova.Lemon.util.VPApplication;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nv_left)
    NavigationView nvReight;
    @Bind(R.id.drawerlayout_activity_main)
    DrawerLayout drawerlayoutActivityMain;
    @Bind(R.id.toolbar_main_reight)
    LinearLayout toolbarMainReight;

    private String mDirSize = "";
    private MyHandler mHandler;
    public static final int SUCESS = 0;
    public static final int FAILED = 1;

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

    /****************************** 左侧侧滑菜单open操作 ***********************************/
    @OnClick(R.id.toolbar_main_reight)
    public void onViewClicked() {
        // mdrawerLayout.openDrawer(Gravity.LEFT);//这里设置的方向应该跟下面xml文件里面的gravity方向相同，不然会报错,start和LEFT都为从左边出现
        if (drawerlayoutActivityMain.isDrawerOpen(Gravity.RIGHT)) {
            drawerlayoutActivityMain.closeDrawer(Gravity.RIGHT);
        } else {
            drawerlayoutActivityMain.openDrawer(Gravity.RIGHT);
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

    /************************************************************/
}
