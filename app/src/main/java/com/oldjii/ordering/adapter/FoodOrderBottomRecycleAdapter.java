package com.oldjii.ordering.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oldjii.ordering.R;
import com.oldjii.ordering.activity.ShopFoodActivity;
import com.oldjii.ordering.bmob.FoodBean;
import com.oldjii.ordering.utils.NetworkUtil;
import com.oldjii.ordering.utils.ToasUtils;

import java.util.List;

/**
 * 底部弹出购物车列表适配器
 *
 * （商家详情页）
 */

public class FoodOrderBottomRecycleAdapter extends RecyclerView.Adapter<FoodOrderBottomRecycleAdapter.ViewHolder> {
    private ShopFoodActivity activity;
    public List<FoodBean> list;
    private LayoutInflater inflater;
    public FoodOrderBottomRecycleAdapter(ShopFoodActivity activity, List<FoodBean> list){
        this.activity = activity;
        this.list = list;
        this.inflater = LayoutInflater.from(activity);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bottom_dc_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodBean bean = list.get(position);
        holder.bindData(bean);
    }
    public void refreshRecycleData(List<FoodBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private FoodBean goodsData;
        private TextView foodNameTv,priceTv,minusTv,countTv,addTv;
        public ViewHolder(View itemView) {
            super(itemView);
            foodNameTv = itemView.findViewById(R.id.id_bottom_item_foodname_tv);
            priceTv = itemView.findViewById(R.id.id_bottom_item_price_tv);
            minusTv = itemView.findViewById(R.id.id_bottom_item_jing_tv);
            countTv = itemView.findViewById(R.id.id_bottom_item_count_tv);
            addTv = itemView.findViewById(R.id.id_bottom_item_add_tv);
            minusTv.setOnClickListener(this);
            addTv.setOnClickListener(this);
        }
        public void bindData(FoodBean cartFoodBean){
            FoodBean gd = new FoodBean();
            gd.total = cartFoodBean.total;
            this.goodsData = gd;
            foodNameTv.setText(cartFoodBean.name);
            priceTv.setText("￥"+(cartFoodBean.total* Float.valueOf(cartFoodBean.price)));
            countTv.setText(String.valueOf(cartFoodBean.total) + "份");
        }

        @Override
        public void onClick(View view) {
            if(!NetworkUtil.checkedNetWork(activity)){
                ToasUtils.showToastMessage("网络异常请重试!");
                return;
            }
        }
    }
}
