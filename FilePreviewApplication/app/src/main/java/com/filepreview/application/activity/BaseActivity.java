package com.filepreview.application.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


/**
 * basic class of all activity
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public abstract class BaseActivity<VD extends ViewDataBinding> extends AppCompatActivity {

    protected VD mBinding;

    protected abstract int getLayoutId();

    protected abstract void init();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        init();
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
