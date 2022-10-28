package com.filepreview.application.activity;

import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.filepreview.application.R;
import com.filepreview.application.adapter.VideoListAdapter;
import com.filepreview.application.databinding.ActivityVideoListBinding;


/**
 * local video list activity
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/24
 */
public class VideoListActivity extends BaseActivity<ActivityVideoListBinding> {


    private VideoListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void init() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VideoListAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }
}