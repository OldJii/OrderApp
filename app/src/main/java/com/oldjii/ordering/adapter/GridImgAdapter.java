package com.oldjii.ordering.adapter;

import android.content.Context;

import com.oldjii.ordering.R;
import com.oldjii.ordering.common.CommonAdapter;
import com.oldjii.ordering.common.CommonViewHolder;
import com.oldjii.ordering.utils.GlideUtils;

import java.util.List;

/**
 * GridView适配器
 *
 * （base：评价列表适配器）
 */
public class GridImgAdapter extends CommonAdapter<String> {

    public GridImgAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, String s) {
        GlideUtils.load(s, holder.getView(R.id.id_img));
    }
}
