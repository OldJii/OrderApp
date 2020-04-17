package com.oldjii.ordering.adapter;

import android.content.Context;

import com.oldjii.ordering.R;
import com.oldjii.ordering.bmob.ShopCateBean;
import com.oldjii.ordering.common.CommonAdapter;
import com.oldjii.ordering.common.CommonViewHolder;

import java.util.List;

/**
 * "商家页面Fragment（从底部icon进入）"的筛选列表的适配器
 */
public class SchoolCateListAdapter extends CommonAdapter<ShopCateBean> {

    public SchoolCateListAdapter(Context context, List<ShopCateBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, ShopCateBean categoryBean) {
        holder.setTvText(R.id.id_cate_tv, categoryBean.name);
    }
}
