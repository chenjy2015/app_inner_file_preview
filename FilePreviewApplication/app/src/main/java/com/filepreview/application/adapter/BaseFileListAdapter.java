package com.filepreview.application.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.filepreview.application.R;
import com.filepreview.application.bean.FileVO;
import com.filepreview.application.util.AlbumUtil;
import com.filepreview.application.util.ImageLoader;

import java.util.ArrayList;


/**
 * Basic adapter for media file list
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/14
 */
public abstract class BaseFileListAdapter<D extends ViewDataBinding> extends BaseAdapter<D, FileVO> {

    protected Activity mContext;
    protected RequestOptions mOptions;
    protected int mWidth, mHeight;

    public abstract void loadImages();

    public abstract void onUpdateData(ArrayList<FileVO> files);

    public ArrayList<FileVO> getSelectedData() {
        ArrayList<FileVO> data = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            FileVO fileVO = mData.get(i);
            if (fileVO.isSelected()) {
                data.add(fileVO);
            }
        }
        return data;
    }

    public ArrayList<String> getSelectedPaths() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            FileVO fileVO = mData.get(i);
            if (fileVO.isSelected()) {
                data.add(fileVO.getFile().getPath());
            }
        }
        return data;
    }

    public void reset() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public BaseFileListAdapter(Activity context) {
        this.mContext = context;
        AlbumUtil.initData(context);
        loadImages();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ImageLoader.setScale(dm.widthPixels);
        mWidth = (dm.widthPixels - 168) >> 2;
        mHeight = mWidth;
        this.mOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.album_default_picture)
                .placeholder(R.drawable.album_default_picture)
                .error(R.drawable.album_default_picture)
                .fallback(R.drawable.album_default_picture)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }


    public class UpdateUI implements ImageLoader.UpdateUI {
        @Override
        public void updateUI(Bundle bundle) {
            ArrayList<FileVO> files = bundle.getParcelableArrayList(ImageLoader.KEY);
            mContext.runOnUiThread(() -> {
                if (files != null && files.size() > 0) {
                    mData.addAll(files);
                    notifyDataSetChanged();
                    onUpdateData(files);
                }
            });
        }
    }
}
