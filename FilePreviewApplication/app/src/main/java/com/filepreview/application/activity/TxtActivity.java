package com.filepreview.application.activity;

import android.content.Context;
import android.content.Intent;

import com.filepreview.application.R;
import com.filepreview.application.constant.ConstValue;
import com.filepreview.application.databinding.ActivityTxtBinding;

public class TxtActivity extends BaseActivity<ActivityTxtBinding> {

    String mContent;

    public static void launch(Context context, String path) {
        Intent intent = new Intent(context, TxtActivity.class);
        intent.putExtra(ConstValue.FILE_INFO, path);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_txt;
    }

    @Override
    protected void init() {
        mContent = getIntent().getStringExtra(ConstValue.FILE_INFO);
        if (mContent != null) {
            mBinding.tvContent.setText(mContent);
        }
    }
}