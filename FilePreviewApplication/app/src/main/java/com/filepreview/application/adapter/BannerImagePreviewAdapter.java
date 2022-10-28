package com.filepreview.application.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.filepreview.application.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;


/**
 * image banner adapter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/24
 */
public class BannerImagePreviewAdapter extends BannerAdapter<String, BannerImagePreviewAdapter.BannerViewHolder> {

    protected RequestOptions mOptions;
    private Activity mContext;

    public BannerImagePreviewAdapter(Activity activity,List<String> datas) {
        super(datas);
        mContext = activity;
        mOptions = new RequestOptions()
                .fitCenter()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.album_default_picture)
                .placeholder(R.drawable.album_default_picture)
                .error(R.drawable.album_default_picture)
                .fallback(R.drawable.album_default_picture)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        PhotoView imageView = new PhotoView(parent.getContext());
        //Note that it must be set to match_ Parent, this is mandatory for viewpager2
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Enable picture zooming
        imageView.enable();
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        Glide.with(holder.imageView.getContext())
                .applyDefaultRequestOptions(mOptions)
                .load(mDatas.get(position))
                .into(holder.imageView);
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}
