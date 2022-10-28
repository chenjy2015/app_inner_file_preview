package com.filepreview.application.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.filepreview.application.R;
import com.filepreview.application.databinding.AdapterSmallImageBinding;
import com.filepreview.application.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * image banner adapter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/24
 */
public class SmallBannerImagePreviewAdapter extends BaseAdapter<AdapterSmallImageBinding, String> {

    protected RequestOptions mOptions;
    protected int mCheckedIndex;
    private BaseAdapter.OnItemClickListener mCallBack;

    public SmallBannerImagePreviewAdapter(List<String> datas, int checkedIndex, BaseAdapter.OnItemClickListener onItemClickListener) {
        setData((ArrayList<String>) datas);
        mCheckedIndex = checkedIndex;
        mCallBack = onItemClickListener;
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

    private void scaleAnim(View view, boolean enlarge) {
        final ScaleAnimation animation;
        if (enlarge) {
            animation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            view.getLayoutParams().width = ScreenUtil.dip2px(view.getContext(),60);
            view.getLayoutParams().height = ScreenUtil.dip2px(view.getContext(),44);
        } else {
            animation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            view.getLayoutParams().width = ScreenUtil.dip2px(view.getContext(),44);
            view.getLayoutParams().height = ScreenUtil.dip2px(view.getContext(),36);
        }
        animation.setDuration(200);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public void updateSelectedIndex(int position) {
//        notifyItemChanged(mCheckedIndex,false);
//        this.mCheckedIndex = position;
//        notifyItemChanged(position);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_small_image;
    }

    @Override
    protected void bindHolder(BaseViewHolder<AdapterSmallImageBinding> holder, int position) {
        Glide.with(holder.binding.ivFileLogo.getContext())
                .applyDefaultRequestOptions(mOptions)
                .load(mData.get(position))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        scaleAnim(holder.binding.ivFileLogo, mCheckedIndex == position);
                        return false;
                    }
                })
                .into(holder.binding.ivFileLogo);
        holder.binding.ivFileLogo.setOnClickListener(v -> mCallBack.onItemClick(mData.get(position), position));
    }
}
