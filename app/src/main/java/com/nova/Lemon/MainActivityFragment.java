package com.nova.Lemon;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.nova.Lemon.Dao.PythonData;
import com.nova.Lemon.adapter.VideoListAdapter;
import com.nova.Lemon.adapter.VideoRecyclerAdapter;
import com.nova.Lemon.bean.Constant;
import com.nova.Lemon.bean.LemonVideoBean;
import com.nova.Lemon.bean.PythonBean;
import com.nova.Lemon.net.LemonVideoClient;
import com.nova.Lemon.net.LemonVideoService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    // @BindView(R.id.videoList)
    // ListView videoList;
    // VideoListAdapter videoListAdapter;
    VideoRecyclerAdapter videoRecyclerAdapter;
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    @BindView(R.id.srl_video)
    SwipeRefreshLayout srlVideo;

    // 计数器
    private int mCurrentCounter;
    private int mTotalCounter = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        return view;

    }

    public int firstVisible = 0, visibleCount = 0, totalCount = 0, allItems;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置数据
        PythonData pythonData = new PythonData();
        List<PythonBean> list = pythonData.getData();

        // videoList.setAdapter(new VideoListAdapter(getActivity(), list));
        // videoListAdapter = new VideoListAdapter(getActivity(), list);

        /**************************
         * 配置SwipeRefreshLayout + RecyclerView效果
         **************************/
        videoRecyclerAdapter = new VideoRecyclerAdapter(getContext());

        // videoRecyclerAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);//
        // 动画效果
        // 滑动到底部自动加载,BaseQuickAdapter加载更多的一种方法
        videoRecyclerAdapter.setOnLoadMoreListener(
                new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        // 延迟操作
                        rvVideo.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LemonVideoClient.getInstance()
                                        .create(LemonVideoService.class,
                                                Constant.LEMON_VIDEO)
                                        .getLemonVideo(mTotalCounter)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(
                                                AndroidSchedulers.mainThread())
                                        .subscribe(
                                                new Consumer<LemonVideoBean>() {
                                                    @Override
                                                    public void accept(
                                                            @NonNull LemonVideoBean lemonVideoBean)
                                                            throws Exception {
                                                        List<LemonVideoBean.SublistBean> data = lemonVideoBean
                                                                .getSublist();
                                                        Log.d("Mydata",
                                                                data + "");
                                                        videoRecyclerAdapter
                                                                .addData(data);
                                                        mCurrentCounter = mTotalCounter;
                                                        mTotalCounter += 1;
                                                        videoRecyclerAdapter
                                                                .loadMoreComplete();
                                                    }
                                                });
                            }
                        }, 0);
                    }
                });
        // 使用(SwipeRefreshLayout + RecyclerView)方式实现简单的下拉刷新
        srlVideo.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN,
                Color.YELLOW);
        srlVideo.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        srlVideo.setRefreshing(false);// 取消进度框
                        Toast.makeText(getActivity(), "刷新成功",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        rvVideo.setAdapter(videoRecyclerAdapter);
        rvVideo.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        /*********************************** RecyclerView事件监听 ***********************************/
        rvVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                    int newState) {
                /*
                 * recyclerView : 当前在滚动的RecyclerView newState : 当前滚动状态.
                 */
                switch (newState) {
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    // 正在被外部拖拽,一般为用户正在用手指滚动
                    // 当手指离开屏幕，并且产生惯性滑动的时候调用，可能会调用<=1次
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    // 滚动事件结束的时候调用，调用一次
                    Log.d("SCROLL_STATE_IDLE", "SCROLL_STATE_IDLE" + "");
                    autoPlayVideo(recyclerView);
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    // 自动滚动开始 滚动的时候调用，调用一次
                    break;
                default:
                    break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*
                 * 其中，visibleCount 就是当前的item位置， allItems 则是recyclerView中的所有条目数量。
                 * 
                 */
                LinearLayoutManager l = (LinearLayoutManager) recyclerView
                        .getLayoutManager();
                visibleCount = l.findFirstVisibleItemPosition();
                allItems = l.getItemCount();
                /*
                 * recyclerView : 当前滚动的view dx : 水平滚动距离 dy : 垂直滚动距离
                 */

                //
                // if (firstVisible == firstVisibleItem) {
                // return;
                // }
                // // firstVisibleItem 当前屏幕显示的第一个item的位置（下标从0开始）
                // firstVisible = firstVisibleItem;
                // // visibleItemCount 当前屏幕可以见到的item总数，包括没有完整显示的item
                // visibleCount = visibleItemCount;
                // // Item的总数， 包括通过addFooterView添加的那个item
                // totalCount = totalItemCount;
            }
        });
        getHttp();
        // ListView的滚动监听
        // videoList.setOnScrollListener(new AbsListView.OnScrollListener() {
        //
        // @Override
        // public void onScrollStateChanged(AbsListView view,
        // int scrollState) {
        // switch (scrollState) {
        // case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
        // // 当手指离开屏幕，并且产生惯性滑动的时候调用，可能会调用<=1次
        // break;
        // case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
        // // 滚动事件结束的时候调用，调用一次
        // autoPlayVideo(view);
        // break;
        // case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
        // // 开始滚动的时候调用，调用一次
        // break;
        // default:
        // break;
        // }
        // }
        //
        // @Override
        // public void onScroll(AbsListView view, int firstVisibleItem,
        // int visibleItemCount, int totalItemCount) {
        // // 在滑动屏幕的过程中，onScroll方法会一直调用
        // if (firstVisible == firstVisibleItem) {
        // return;
        // }
        // // firstVisibleItem 当前屏幕显示的第一个item的位置（下标从0开始）
        // firstVisible = firstVisibleItem;
        // // visibleItemCount 当前屏幕可以见到的item总数，包括没有完整显示的item
        // visibleCount = visibleItemCount;
        // // Item的总数， 包括通过addFooterView添加的那个item
        // totalCount = totalItemCount;
        // /*
        // * // 判断是否是最后一个item，可以实现加载更多的功能 if (firstVisibleItem +
        // * visibleItemCount >= totalItemCount && totalItemCount > 0) {
        // * isLastRow = true; } else { isLastRow = false; }
        // */
        //
        // }
        // });
    }

    /**
     * 滑动停止自动播放视频
     */
    void autoPlayVideo(RecyclerView view) {
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

    // 进行网络请求
    private void getHttp() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.LEMON_VIDEO)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        LemonVideoService myService = retrofit.create(LemonVideoService.class);
        myService.getLemonVideo(1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LemonVideoBean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LemonVideoBean lemonVideoBean) {
                        videoRecyclerAdapter
                                .setNewData(lemonVideoBean.getSublist());

                        srlVideo.setRefreshing(false); // 让SwipeRefreshLayout关闭刷新

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // ButterKnife.unbind(this);
        // null.unbind();
    }
}
