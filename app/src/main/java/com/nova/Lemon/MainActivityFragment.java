package com.nova.Lemon;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nova.Lemon.CustomView.HVideoPlayer;
import com.nova.Lemon.Dao.PythonData;
import com.nova.Lemon.adapter.VideoListAdapter;
import com.nova.Lemon.bean.PythonBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @BindView(R.id.videoList)
    ListView videoList;
    VideoListAdapter videoListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        return view;

    }

    public int firstVisible = 0, visibleCount = 0, totalCount = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // rvJoke.setOnScrollChangeListener();
        // 设置数据
        PythonData pythonData = new PythonData();
        List<PythonBean> list = pythonData.getData();

        videoList.setAdapter(new VideoListAdapter(getActivity(), list));
        videoListAdapter = new VideoListAdapter(getActivity(), list);
        // videoList.setAdapter(new CommonAdapter<List>(getActivity(),
        // R.layout.item_videoview, list) {
        // @Override
        // protected void convert(ViewHolder viewHolder, List item, int
        // position) {
        // }
        // });
        // videoList.setAdapter(new CommonAdapter<PythonBean>(
        // getActivity(), R.layout.item_videoview, list) {
        // @Override
        // protected void convert(ViewHolder viewHolder, PythonBean item, int
        // position) {
        //
        // }
        // });
        // ListView的滚动监听
        videoList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                    int scrollState) {
                switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    // 当手指离开屏幕，并且产生惯性滑动的时候调用，可能会调用<=1次
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    // 滚动事件结束的时候调用，调用一次
                    autoPlayVideo(view);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    // 开始滚动的时候调用，调用一次
                    break;
                default:
                    break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                // 在滑动屏幕的过程中，onScroll方法会一直调用
                if (firstVisible == firstVisibleItem) {
                    return;
                }
                // firstVisibleItem 当前屏幕显示的第一个item的位置（下标从0开始）
                firstVisible = firstVisibleItem;
                Log.d("tts",
                        "firstVisible : " + firstVisible + "visibleCount : "
                                + visibleCount + "  totalCount : " + totalCount
                                + "all = " + (firstVisible + visibleCount));
                // visibleItemCount 当前屏幕可以见到的item总数，包括没有完整显示的item
                visibleCount = visibleItemCount;
                // Item的总数， 包括通过addFooterView添加的那个item
                totalCount = totalItemCount;
                /*
                 * // 判断是否是最后一个item，可以实现加载更多的功能 if (firstVisibleItem +
                 * visibleItemCount >= totalItemCount && totalItemCount > 0) {
                 * isLastRow = true; } else { isLastRow = false; }
                 */

            }
        });
    }

    /**
     * 滑动停止自动播放视频
     */
    void autoPlayVideo(AbsListView view) {
        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i)
                    .findViewById(R.id.videoplayer) != null) {
                JCVideoPlayerStandard videoPlayerStandard1 = (JCVideoPlayerStandard) view
                        .getChildAt(i).findViewById(R.id.videoplayer);
                Rect rect = new Rect();
                videoPlayerStandard1.getLocalVisibleRect(rect);
                int videoheight3 = videoPlayerStandard1.getHeight();
                if (rect.top == 0 && rect.bottom == videoheight3) {
                    if (videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                            || videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {

                        videoPlayerStandard1.startButton.performClick();
                        VPApplication.instance.VideoPlaying = videoPlayerStandard1;
                    }
                    return;
                }

            }
        }
        // 释放其他视频资源
        JCVideoPlayer.releaseAllVideos();
        VPApplication.instance.VideoPlaying = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // ButterKnife.unbind(this);
        // null.unbind();
    }
}
