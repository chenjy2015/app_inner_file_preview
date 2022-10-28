package com.filepreview.application.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.filepreview.application.R;
import com.filepreview.application.adapter.BaseAdapter;
import com.filepreview.application.adapter.ImageListAdapter;
import com.filepreview.application.bean.FileVO;
import com.filepreview.application.databinding.ActivityImageListBinding;
import com.filepreview.application.direction.SpaceItemDecoration;
import com.filepreview.application.util.PermissionUtil;
import com.filepreview.application.util.ScreenUtil;
import com.filepreview.application.util.VibratorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * local image list activity
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class ImageListActivity extends BaseActivity<ActivityImageListBinding> implements BaseAdapter.OnItemClickListener<FileVO> {
    private ImageListAdapter mAdapter;
    private boolean mIsEditModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_list;
    }

    @Override
    protected void init() {
        mBinding.btnPlayer.setOnClickListener(v -> {
            showToast(String.format(getString(R.string.phone_selected), mAdapter.getSelectedData().size()));
            ImagePreviewActivity.launch(ImageListActivity.this, mAdapter.getSelectedPaths());
        });

        mBinding.btnSelectCancel.setOnClickListener(v -> {
            mBinding.tvSelectNum.setText(String.format(getString(R.string.phone_selected), 0));
            mBinding.clSelectedParent.setVisibility(View.GONE);
            mIsEditModel = false;
            mAdapter.updateEditModel(false);
            mAdapter.reset();
        });

        mBinding.clSelectedParent.getBackground().setAlpha(12);
        mBinding.tvSelectNum.setText(String.format(getString(R.string.phone_selected), 0));

        //request permission Must be done during an initialization phase like onCreate
        PermissionUtil.requestManageExternalStoragePermission(this);
        //set adapter
        initAdapter();
    }

    private void initAdapter() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageListAdapter(this, this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtil.dip2px(this
                , 32), 3));
    }

    @Override
    public void onItemClick(FileVO o, int position) {
        VibratorUtil.getInstance().vibrate();
        FileVO fileVO = mAdapter.getData().get(position);
        if (mIsEditModel) {
            fileVO.setSelected(!fileVO.isSelected());
            mAdapter.updateData(position, fileVO);
            mBinding.tvSelectNum.setText(String.format(getString(R.string.phone_selected), mAdapter.getSelectedData().size()));
        } else {
            ImagePreviewActivity.launch(ImageListActivity.this, new ArrayList<String>() {{
                add(o.getFile().getPath());
            }});
        }
    }

    @Override
    public void onItemLongClickListener(FileVO o, int position) {
        VibratorUtil.getInstance().vibrate();
        mBinding.clSelectedParent.setVisibility(View.VISIBLE);
        mIsEditModel = true;
        mAdapter.updateEditModel(true);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionUtil.REQUEST_CODE) {
            //refresh data
            mAdapter.loadImages();
        }
    }
}
