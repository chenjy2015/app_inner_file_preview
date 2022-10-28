package com.filepreview.application;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.filepreview.application.activity.BaseActivity;
import com.filepreview.application.activity.DocumentListActivity;
import com.filepreview.application.activity.ImageListActivity;
import com.filepreview.application.adapter.BaseAdapter;
import com.filepreview.application.adapter.MenuListAdapter;
import com.filepreview.application.bean.MenuType;
import com.filepreview.application.bean.MenuVO;
import com.filepreview.application.databinding.ActivityMainBinding;
import com.filepreview.application.util.VibratorUtil;

import java.util.ArrayList;


/**
 * FilePreview Main Activity
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> implements BaseAdapter.OnItemClickListener<MenuVO> {

    private ArrayList<MenuVO> mMenus = new ArrayList<>();
    private MenuListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mAdapter = new MenuListAdapter(this);
        mMenus.add(new MenuVO("picture preview", MenuType.PICTURE));
        mMenus.add(new MenuVO("document preview", MenuType.DOCUMENT));
//        mMenus.add(new MenuVO("video preview", MenuType.VIDEO));
        mAdapter.setData(mMenus);
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycler.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        mBinding.btnPicturePreview.setOnClickListener(v -> {
            VibratorUtil.getInstance().vibrate();
            startActivity(new Intent(MainActivity.this, ImageListActivity.class));
        });

        mBinding.btnDocumentPreview.setOnClickListener(v -> {
            VibratorUtil.getInstance().vibrate();
            startActivity(new Intent(MainActivity.this, DocumentListActivity.class));
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 0);
        }
    }


    @Override
    public void onItemClick(MenuVO o, int position) {
        showToast(o.getTitle());
        switch (o.getType()) {
            case PICTURE:
                startActivity(new Intent(MainActivity.this, ImageListActivity.class));
                break;
            case DOCUMENT:
                startActivity(new Intent(MainActivity.this, DocumentListActivity.class));
                break;
        }
    }

    @Override
    public void onItemLongClickListener(MenuVO o, int position) {
        VibratorUtil.getInstance().vibrate();
    }
}