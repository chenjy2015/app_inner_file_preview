package com.filepreview.application.adapter;

import android.app.Activity;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.filepreview.application.R;
import com.filepreview.application.bean.FileVO;
import com.filepreview.application.databinding.AdapterDocumentListBinding;
import com.filepreview.application.util.AlbumUtil;
import com.filepreview.application.util.ImageLoader;
import com.filepreview.application.util.MediaFileUtil;
import com.filepreview.application.util.PinyinUtils;
import com.filepreview.application.util.PreViewUtil;
import com.filepreview.application.util.VibratorUtil;

import java.util.ArrayList;


/**
 * document list adapter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class DocumentListAdapter extends BaseFileListAdapter<AdapterDocumentListBinding> implements BaseAdapter.OnItemClickListener<FileVO> {

    public ArrayList<FileVO> mCacheFileVOList = new ArrayList<>();

    public DocumentListAdapter(Activity context) {
        super(context);
        setOnItemClickListener(this);
    }

    @Override
    public void loadImages() {
        AlbumUtil.initData(mContext);
        ImageLoader.getInstance().loadDocument(new UpdateUI());
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_document_list;
    }

    @Override
    protected void bindHolder(BaseViewHolder<AdapterDocumentListBinding> holder, int position) {
        Glide.with(mContext)
                .applyDefaultRequestOptions(mOptions)
                .load(mData.get(position).getDrawableId())
                .into(holder.binding.ivFileLogo);

        holder.binding.tvFileName.setText(subTitle(mData.get(position).getFile().getName()));
    }

    @Override
    public void onUpdateData(ArrayList<FileVO> files) {
        this.mCacheFileVOList.addAll(files);
    }

    /**
     * set name max length 5
     *
     * @param name
     * @return
     */
    private String subTitle(String name) {
        String suffix = MediaFileUtil.getSuffix(name);
        int index = name.lastIndexOf(".");
        String n = "";
        if (index >= 0) {
            n = name.substring(0, index);
            if (n.length() > 5) {
                n = n.substring(0, 5);
            }
        }
        return n + "..." + suffix;
    }

    public void filter(String key) {
        ArrayList<FileVO> tempList = new ArrayList<>();
        for (int i = 0; i < mCacheFileVOList.size(); i++) {
            FileVO fileVO = mCacheFileVOList.get(i);
            String keyPY = PinyinUtils.ccs2Pinyin(key);
            String fileNamePY = PinyinUtils.ccs2Pinyin(fileVO.getFile().getName());
            Log.d(this.getClass().getSimpleName(), "key : " + key + "; keyPY : " + keyPY + "; filename : " + fileVO.getFile().getName() + "; fileNamePY : " + fileNamePY);
            if (fileNamePY.contains(keyPY)) {
                tempList.add(fileVO);
            }
        }
        Log.d(this.getClass().getSimpleName(), "tempList.size : " + tempList.size());
        if (tempList.size() > 0) {
            mData.addAll(tempList);
            notifyDataSetChanged();
        }
    }

    public void reset() {
        mData.clear();
        notifyDataSetChanged();
        mData.addAll(mCacheFileVOList);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(FileVO o, int position) {
//        VibratorUtil.getInstance().vibrate();
        PreViewUtil.open(mContext, o.getFile());
    }

    @Override
    public void onItemLongClickListener(FileVO o, int position) {
        VibratorUtil.getInstance().vibrate();
    }
}
