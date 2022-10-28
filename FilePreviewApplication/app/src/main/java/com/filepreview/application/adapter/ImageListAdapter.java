package com.filepreview.application.adapter;

import android.app.Activity;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.filepreview.application.R;
import com.filepreview.application.bean.FileVO;
import com.filepreview.application.databinding.AdapterImageListBinding;
import com.filepreview.application.util.AlbumUtil;
import com.filepreview.application.util.ImageLoader;

import java.util.ArrayList;


/**
 * picture list adapter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class ImageListAdapter extends BaseFileListAdapter<AdapterImageListBinding> {

    private boolean mIsEditModel;

    public ImageListAdapter(Activity context, OnItemClickListener<FileVO> callBack) {
        super(context);
        setOnItemClickListener(callBack);
    }

    public void updateEditModel(boolean isEdit) {
        this.mIsEditModel = isEdit;
    }

    @Override
    public void loadImages() {
        AlbumUtil.initData(mContext);
        ImageLoader.getInstance().loadImages(mContext, AlbumUtil.getValue(mContext.getString(R.string.camera)), new UpdateUI());
        ImageLoader.getInstance().loadImages(mContext, AlbumUtil.getValue(mContext.getString(R.string.shortcut)), new UpdateUI());
        ImageLoader.getInstance().loadImages(mContext, AlbumUtil.getValue(mContext.getString(R.string.wechat)), new UpdateUI());
        ImageLoader.getInstance().loadImages(mContext, AlbumUtil.getValue(mContext.getString(R.string.other)), new UpdateUI());
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_image_list;
    }

    @Override
    protected void bindHolder(BaseViewHolder<AdapterImageListBinding> holder, int position) {
        Glide.with(mContext)
                .applyDefaultRequestOptions(mOptions)
                .load(mData.get(position).getFile().getPath())
                .into(holder.binding.ivFileLogo);

        if (mIsEditModel) {
            holder.binding.ivChecked.setVisibility(View.VISIBLE);
        } else {
            holder.binding.ivChecked.setVisibility(View.GONE);
        }
        if (mData.get(position).isSelected()) {
            holder.binding.ivChecked.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_checked));
        } else {
            holder.binding.ivChecked.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_unchecked));
        }
    }

    @Override
    public void onUpdateData(ArrayList<FileVO> files) {

    }
}
