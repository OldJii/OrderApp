package com.oldjii.ordering.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.oldjii.ordering.R;
import com.oldjii.ordering.bmob.FoodBean;
import com.oldjii.ordering.common.CommonAdapter;
import com.oldjii.ordering.common.CommonViewHolder;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.utils.GlideUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * 订单 - ListView适配器
 *
 * （提交订单页面（用户端））
 */
public class OrderListAdapter extends CommonAdapter<FoodBean> {

    public OrderListAdapter(Context context, List<FoodBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, FoodBean foodBean) {
        ImageView iv = holder.getView(R.id.id_cart_item_food_iv);
        try {
            JSONArray jsonArray = new JSONArray(foodBean.imgs);
            GlideUtils.load(jsonArray.get(0).toString(), iv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.setTvText(R.id.id_cart_item_food_name, foodBean.name)
                .setTvText(R.id.id_cart_item_food_count, "x" + foodBean.total)
                .setTvText(R.id.id_cart_item_food_price, Constant.PRICE_MARK + foodBean.price);
    }
}
