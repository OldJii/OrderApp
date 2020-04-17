package com.oldjii.ordering.adapter;

import android.content.Context;

import com.makeramen.roundedimageview.RoundedImageView;
import com.oldjii.ordering.R;
import com.oldjii.ordering.bmob.FoodBean;
import com.oldjii.ordering.common.CommonAdapter;
import com.oldjii.ordering.common.CommonViewHolder;
import com.oldjii.ordering.utils.GlideUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * 商家端订单页面
 *
 * (实现的最上层用于ShopOrderActivity.java 商家端订单页面）
 */
public class ListItemListAdapter extends CommonAdapter<FoodBean> {
    private String createTime;

    public ListItemListAdapter(Context context, List<FoodBean> datas, int layoutId, String createTime) {
        super(context, datas, layoutId);
        this.createTime = createTime;
    }

    @Override
    public void convert(CommonViewHolder helper, FoodBean foodBean) {
        RoundedImageView iv = helper.getView(R.id.logo);
        try {
            JSONArray jsonArray = new JSONArray(foodBean.imgs);
            GlideUtils.load(jsonArray.get(0).toString(), iv);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        helper.setTvText(R.id.name_tv, foodBean.name)
                .setTvText(R.id.price, "￥ " + foodBean.price)
                .setTvText(R.id.desc, "下单时间 : " + createTime)
                .setTvText(R.id.count_tv, "x " + foodBean.total);
    }
}
