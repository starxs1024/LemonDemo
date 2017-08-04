package com.nova.Lemon.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nova.Lemon.CustomView.LemonPlayer;
import com.nova.Lemon.R;
import com.nova.Lemon.bean.LemonVideoBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Paraselene on 2017/7/31. Email ：15616165649@163.com
 */

public class VideoRecyclerAdapter
        extends BaseQuickAdapter<LemonVideoBean.SublistBean, BaseViewHolder> {
    private Context context;

    public VideoRecyclerAdapter() {
        super(R.layout.item_videoview);
    }

    public VideoRecyclerAdapter(Context context) {
        super(R.layout.item_videoview);
        this.context = context;

    }

    public VideoRecyclerAdapter(Context context,
            List<LemonVideoBean.SublistBean> data) {
        super(R.layout.item_videoview, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper,
            LemonVideoBean.SublistBean item) {

        helper.setText(R.id.jc_time, item.getV_time());
        helper.setText(R.id.jc_title, item.getV_title());

        String videolink = item.getV_videolink();
        String imaglinks = item.getV_imagelinks();

        // 设置视频地址
        LemonPlayer lemonPlayer = helper.getView(R.id.videoplayer);
        lemonPlayer.setUp(videolink, LemonPlayer.SCREEN_LAYOUT_NORMAL, "");
        // 设置缩略图
        // viewHolder.jcVideoPlayer.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
        Glide.with(context).load(imaglinks).into(lemonPlayer.thumb);
    }

}
