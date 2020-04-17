package com.oldjii.ordering.adapter;

import android.content.Context;

import com.oldjii.ordering.R;
import com.oldjii.ordering.base.ItemBean;
import com.oldjii.ordering.common.CommonAdapter;
import com.oldjii.ordering.common.CommonViewHolder;

import java.util.List;

/**
 * Item - ListView适配器
 */
public class ItemLvAdapter extends CommonAdapter<ItemBean> {

    public ItemLvAdapter(Context context, List<ItemBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, ItemBean itemBean) {
        holder.setImageResource(R.id.id_iv, itemBean.imgRes)
                .setTvText(R.id.id_title, itemBean.title);
    }
}
