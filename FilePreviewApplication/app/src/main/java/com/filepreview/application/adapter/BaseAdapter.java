package com.filepreview.application.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * Basic class of All Adapter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public abstract class BaseAdapter<D extends ViewDataBinding, B> extends RecyclerView.Adapter<BaseViewHolder<D>> {

    protected D mDataBinding;
    protected ArrayList<B> mData = new ArrayList<>();
    protected OnItemClickListener<B> mOnItemClickListener;

    protected abstract int getLayoutId();

    protected abstract void bindHolder(BaseViewHolder<D> holder, int position);

    public void setOnItemClickListener(OnItemClickListener<B> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder<D> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutId(), parent, false);
        return new BaseViewHolder<>(mDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<D> holder, int position) {
        bindHolder(holder, position);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mData.get(position), position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemLongClickListener(mData.get(position), position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<B> getData(){
        return (ArrayList<B>) mData;
    }

    public void setData(ArrayList<B> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void updateData(int position, B bean) {
        this.mData.set(position, bean);
        notifyItemChanged(position);
    }

    public interface OnItemClickListener<O> {
        void onItemClick(O o, int position);

        void onItemLongClickListener(O o, int position);
    }

}
