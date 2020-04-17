package com.oldjii.ordering.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kongzue.dialog.v2.SelectDialog;
import com.oldjii.ordering.R;
import com.oldjii.ordering.activity.FoodManageActivity;
import com.oldjii.ordering.activity.MainActivity;
import com.oldjii.ordering.activity.ShopInfoActivity;
import com.oldjii.ordering.activity.ShopOrderActivity;
import com.oldjii.ordering.adapter.ItemLvAdapter;
import com.oldjii.ordering.base.ItemBean;
import com.oldjii.ordering.bmob.ShopBean;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.confige.MySharePreference;
import com.oldjii.ordering.fragment.FragmentMine;
import com.oldjii.ordering.utils.CustomListView;
import com.oldjii.ordering.utils.GlideUtils;
import com.oldjii.ordering.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商家登陆成功显示界面
 */
public class ShopPage extends FrameLayout {
    @BindView(R.id.id_photo)
    CircleImageView circleImageView;
    @BindView(R.id.id_name)
    TextView nameTv;
    @BindView(R.id.id_entry_time_tv)
    TextView timeTv;
    @BindView(R.id.listview)
    CustomListView customListView;

    private MainActivity mainActivity;

    public ShopPage(@NonNull Context context) {
        super(context);
        this.mainActivity = (MainActivity) context;
        init();
    }

    private void init() {
        View view = UIUtils.inflater(R.layout.page_shop);
        this.addView(view);
        ButterKnife.bind(this);
        final List<ItemBean> list = new ArrayList<>();
        list.add(new ItemBean(R.mipmap.icon_bj, "信息编辑"));
        list.add(new ItemBean(R.mipmap.ic_dd, "我的订单"));
        list.add(new ItemBean(R.mipmap.ic_food_manage, "菜品管理"));
        list.add(new ItemBean(R.mipmap.ic_exit, "退出登录"));
        customListView.setAdapter(new ItemLvAdapter(mainActivity, list, R.layout.item_mine_lv));
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(mainActivity, ShopInfoActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(mainActivity, ShopOrderActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(mainActivity, FoodManageActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 3:
                        shopLogout();
                        break;
                }
            }
        });
    }

    public void updateUi() {
        mainActivity.orderButtonOption(true);
        ShopBean shopBean = MySharePreference.getShopBean();
        if (shopBean.logo != null) GlideUtils.load(shopBean.logo, circleImageView);
        nameTv.setText(shopBean.name);
        timeTv.setText("入驻时间 : " + shopBean.createdTime);
    }

    private void shopLogout() {
        SelectDialog.show(mainActivity, "提示", "确定要退出登录吗?", "确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mainActivity.orderButtonOption(false);
                MySharePreference.setCurrentLoginState(Constant.STATE_UN_LOING);
                MySharePreference.saveShopBean(new ShopBean());
                FragmentMine.initState();
            }
        }).setCanCancel(true);
    }
}
