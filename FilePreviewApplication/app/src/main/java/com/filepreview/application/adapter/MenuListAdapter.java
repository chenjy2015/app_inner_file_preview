package com.filepreview.application.adapter;

import com.filepreview.application.R;
import com.filepreview.application.bean.MenuVO;
import com.filepreview.application.databinding.AdapterMenuListBinding;


/**
 * menu list adapter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class MenuListAdapter extends BaseAdapter<AdapterMenuListBinding, MenuVO> {

    public MenuListAdapter(OnItemClickListener<MenuVO> listener) {
        setOnItemClickListener(listener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_menu_list;
    }

    @Override
    protected void bindHolder(BaseViewHolder<AdapterMenuListBinding> holder, int position) {
//        holder.binding.setMenu(mData.get(position));
        holder.binding.tvTitle.setText(mData.get(position).getTitle());
    }
}
