package com.filepreview.application.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.filepreview.application.R;
import com.filepreview.application.adapter.DocumentListAdapter;
import com.filepreview.application.databinding.ActivityDocumentListBinding;
import com.filepreview.application.direction.SpaceItemDecoration;
import com.filepreview.application.util.PermissionUtil;
import com.filepreview.application.util.ScreenUtil;

import java.lang.ref.SoftReference;


/**
 * local document list activity
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class DocumentListActivity extends BaseActivity<ActivityDocumentListBinding> {
    private DocumentListAdapter mAdapter;
    private static final int SEARCH_CLEAR = 0;
    private static final int SEARCH_WHAT = 1;
    private static final int SEARCH_RESET = 2;
    private static final int DATA_CLEAR = 3;
    private static final int REQUEST_CODE = 100;
    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_document_list;
    }

    @Override
    protected void init() {
        mBinding.llSearch.etSearchContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String content = v.getText().toString();
                if (content.length() != 0 && content.trim().length() != 0) {
                    //clear data list
                    mHandler.sendEmptyMessage(DATA_CLEAR);
                    //filter data list
                    Message msg = Message.obtain();
                    msg.what = SEARCH_WHAT;
                    msg.obj = content;
                    mHandler.sendMessageDelayed(msg, 200);
                }
                return true;
            }
            return false;
        });

        mBinding.llSearch.etSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 || s.toString().trim().length() == 0) {
                    mHandler.sendEmptyMessageDelayed(SEARCH_CLEAR, 200);
                }
            }
        });

        mBinding.llSearch.ivClear.setOnClickListener(v -> {
            mHandler.sendEmptyMessageDelayed(SEARCH_CLEAR, 200);
        });

        //request permission Must be done during an initialization phase like onCreate
        PermissionUtil.requestManageExternalStoragePermission(this);
        // set adapter
        initAdapter();
    }

    private void initAdapter() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new DocumentListAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtil.dip2px(this
                , 32), 3));
    }

    public static class MyHandler extends Handler {

        private final SoftReference<DocumentListActivity> softReference;

        public MyHandler(DocumentListActivity activity) {
            softReference = new SoftReference(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == SEARCH_WHAT) {
                if (!msg.obj.toString().isEmpty() && msg.obj.toString().equals(softReference.get().mBinding.llSearch.etSearchContent.getText().toString())) {
                    softReference.get().mAdapter.filter(msg.obj
                            .toString());
                    softReference.get().mBinding.llSearch.ivClear.setVisibility(View.VISIBLE);
                } else {
                    sendEmptyMessage(SEARCH_RESET);
                }
            } else if (msg.what == SEARCH_RESET) {
                softReference.get().mAdapter.reset();
                softReference.get().mBinding.llSearch.ivClear.setVisibility(View.GONE);
            } else if (msg.what == SEARCH_CLEAR) {
                softReference.get().mBinding.llSearch.etSearchContent.getText().clear();
                sendEmptyMessage(SEARCH_RESET);
            } else if (msg.what == DATA_CLEAR) {
                softReference.get().mAdapter.clear();
            }
        }
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
