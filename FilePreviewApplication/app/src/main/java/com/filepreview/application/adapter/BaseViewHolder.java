package com.filepreview.application.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


/**
 * basic class of all view holder
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class BaseViewHolder<D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    D binding;

    public BaseViewHolder(@NonNull D dataBinding) {
        super(dataBinding.getRoot());
        this.binding = dataBinding;
    }
}
