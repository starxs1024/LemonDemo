package com.nova.Lemon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nova.Lemon.R;
import com.nova.Lemon.CustomView.LemonPlayer;
import com.nova.Lemon.bean.PythonBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Paraselene on 2017/7/19. Email ：15616165649@163.com
 */

public class VideoListAdapter extends BaseAdapter {
    private Context context;
    private List<PythonBean> list;

    LayoutInflater mInflater;
    @BindView(R.id.videoList)
    ListView videoList;
    public boolean isLucency;

    public VideoListAdapter(Context context, List<PythonBean> list) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // This is the point
        if (convertView != null && convertView.getTag() != null
                && convertView.getTag() instanceof VideoHolder) {
            ((VideoHolder) convertView.getTag()).jcVideoPlayer.release();
        }

        VideoHolder viewHolder;
        if (convertView != null && convertView.getTag() != null
                && convertView.getTag() instanceof VideoHolder) {
            viewHolder = (VideoHolder) convertView.getTag();
        } else {

            viewHolder = new VideoHolder();
            convertView = mInflater.inflate(R.layout.item_videoview, null);
            viewHolder.jcVideoPlayer = (LemonPlayer) convertView
                    .findViewById(R.id.videoplayer);
            viewHolder.jc_title = (TextView) convertView
                    .findViewById(R.id.jc_title);
            viewHolder.layout = (LinearLayout) convertView
                    .findViewById(R.id.ll_alpha);
            viewHolder.jc_time = (TextView) convertView
                    .findViewById(R.id.jc_time);
            convertView.setTag(viewHolder);
        }
        // Log.d("isLucency", isLucency + "");
        // viewHolder.layout.setAlpha(0.1f);
        // if (isLucency) {
        // viewHolder.layout.setAlpha(0.1f);
        // isLucency = false;
        // } else {
        // viewHolder.layout.setAlpha(1f);
        // isLucency = true;
        // }

        viewHolder.jc_title.setText(list.get(position).getTitle());
        viewHolder.jc_time.setText(list.get(position).getTime());

        String videolink = list.get(position).getVideolink();
        String imaglinks = list.get(position).getImaglinks();

        // 设置视频地址
        // boolean setUp = viewHolder.jcVideoPlayer.setUp(videolink,
        // JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
        viewHolder.jcVideoPlayer.setUp(videolink,
                LemonPlayer.SCREEN_LAYOUT_NORMAL, "");
        // 设置缩略图
        // viewHolder.jcVideoPlayer.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");

        // if (setUp) {
        Glide.with(context).load(imaglinks)
                .into(viewHolder.jcVideoPlayer.thumb);
        // }
        /*
         * } else {
         *
         * ImageViewHolder imageViewHolder; if (convertView != null &&
         * convertView.getTag() != null && convertView.getTag() instanceof
         * ImageViewHolder) { imageViewHolder = (ImageViewHolder)
         * convertView.getTag(); } else { imageViewHolder = new
         * ImageViewHolder(); LayoutInflater mInflater =
         * LayoutInflater.from(context); convertView =
         * mInflater.inflate(R.layout.item_textview, null);
         * imageViewHolder.imageView = (ImageView) convertView
         * .findViewById(R.id.image_view); Glide.with(context)
         * .load("http://img04.tooopen.com/images/20131019/sy_43185978222.jpg")
         * .into(imageViewHolder.imageView);
         * convertView.setTag(imageViewHolder); }
         *
         * }
         */
        return convertView;
    }

    class VideoHolder {
        LemonPlayer jcVideoPlayer;
        TextView jc_title;
        TextView jc_time;
        LinearLayout layout;
    }

    public void ChangeAlpha(boolean isLucency) {
        this.isLucency = isLucency;

    }

    class ImageViewHolder {
        ImageView imageView;
    }
}
