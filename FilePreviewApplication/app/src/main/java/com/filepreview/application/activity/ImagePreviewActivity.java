package com.filepreview.application.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.filepreview.application.R;
import com.filepreview.application.adapter.BannerImagePreviewAdapter;
import com.filepreview.application.constant.ConstValue;
import com.filepreview.application.databinding.ActivityImagePreviewBinding;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;


/**
 * Large Image Preview Activity
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/28
 */
public class ImagePreviewActivity extends BaseActivity<ActivityImagePreviewBinding> implements OnPageChangeListener {

    private ArrayList<String> mImagePaths;
    private BannerImagePreviewAdapter mAdapter;

    public static void launch(Context context, ArrayList<String> imagePaths) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putStringArrayListExtra(ConstValue.OUT_FILE_LIST, imagePaths);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void init() {
        mImagePaths = getIntent().getStringArrayListExtra(ConstValue.OUT_FILE_LIST);
        if (mImagePaths == null) {
            mImagePaths = new ArrayList<>();
        }
        Log.d("ImagePreviewActivity", "paths : " + mImagePaths.size());
        mAdapter = new BannerImagePreviewAdapter(this, mImagePaths);
        mBinding.banner.addBannerLifecycleObserver(ImagePreviewActivity.this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(this))
                .addOnPageChangeListener(this)
                .setAdapter(mAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}