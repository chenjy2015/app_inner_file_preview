package com.filepreview.application.adapter;

import android.app.Activity;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.filepreview.application.R;
import com.filepreview.application.bean.FileVO;
import com.filepreview.application.databinding.AdapterVideoListBinding;
import com.filepreview.application.util.AlbumUtil;
import com.filepreview.application.util.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Video list adapter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class VideoListAdapter extends BaseFileListAdapter<AdapterVideoListBinding> implements BaseAdapter.OnItemClickListener<FileVO> {

    private SimpleDateFormat mDateFormatter;

    public VideoListAdapter(Activity context) {
        super(context);
        setOnItemClickListener(this);
        mDateFormatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        mDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    @Override
    public void loadImages() {
        AlbumUtil.initData(mContext);
        ImageLoader.getInstance().loadVideoDetails(mContext, new UpdateUI());
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_video_list;
    }

    @Override
    public void bindHolder(BaseViewHolder<AdapterVideoListBinding> holder, int position) {
        Glide.with(mContext)
                .applyDefaultRequestOptions(mOptions)
                .load(mData.get(position).getFile().getPath())
                .into(holder.binding.ivFileLogo);
        holder.binding.tvFileName.setText(mData.get(position).getFile().getName());
        holder.binding.tvFileDuration.setText(mDateFormatter.format(mData.get(position).getDuration()));
    }

    @Override
    public void onUpdateData(ArrayList<FileVO> files) {

    }


    @Override
    public void onItemClick(FileVO o, int position) {
        Log.d("VideoListAdapter", "onItemClick position : " + position);
    }

    @Override
    public void onItemLongClickListener(FileVO o, int position) {

    }
}
