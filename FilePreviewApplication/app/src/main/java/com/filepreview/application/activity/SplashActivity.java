package com.filepreview.application.activity;

import com.filepreview.application.R;
import com.filepreview.application.constant.ConstValue;
import com.filepreview.application.databinding.ActivitySplashBinding;
import com.filepreview.application.util.PreViewUtil;

import java.util.ArrayList;


/**
 * Apply external pull-up interface
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/25
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    ArrayList<String> mPaths;
    int mFileType = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        mPaths = getIntent().getStringArrayListExtra(ConstValue.OUT_FILE_LIST);
        mFileType = getIntent().getIntExtra(ConstValue.OUT_FILE_TYPE, -1);
        if (mFileType != -1 && mPaths != null && !mPaths.isEmpty()) {
            //splice the file path
            splicePath();
            //open file
            switch (mFileType) {
                case ConstValue.DOCUMENT:
                    PreViewUtil.open(SplashActivity.this, mPaths.get(0));
                    break;

                case ConstValue.PICTURE:
                    ImagePreviewActivity.launch(SplashActivity.this, mPaths);
                    break;
            }
        } else {
            showToast("空值传递!");
        }
    }

    /**
     * The name is passed in from the outside. You need to manually splice the path address
     */
    private void splicePath() {
        for (int i = 0; i < mPaths.size(); i++) {
            String name = mPaths.get(i);
            if (mFileType == ConstValue.DOCUMENT) {
                name = ConstValue.OUT_DOCUMENT_FILE_PATH_ROOT + name;
            } else if (mFileType == ConstValue.PICTURE) {
                name = ConstValue.OUT_PICTURE_FILE_PATH_ROOT + name;
            }
            mPaths.set(i, name);
        }
    }

}