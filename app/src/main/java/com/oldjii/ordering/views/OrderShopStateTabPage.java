package com.oldjii.ordering.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.oldjii.ordering.R;
import com.oldjii.ordering.activity.ShopReceiveOrderDetailActivity;
import com.oldjii.ordering.adapter.OrderShopStateListAdapter;
import com.oldjii.ordering.bmob.OrderBean;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.confige.MySharePreference;
import com.oldjii.ordering.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 商家端订单页面order - FrameLayout
 */
public class OrderShopStateTabPage extends FrameLayout {
    @BindView(R.id.id_base_smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.listview)
    ListView listView;
    private Context context;
    int state;
    private OrderShopStateListAdapter adapter;

    public OrderShopStateTabPage(@NonNull Context context) {
        super(context);
        this.context = context;
        View view = UIUtils.inflater(R.layout.layout_refresh_listview);
        this.addView(view);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                initData(state);
                smartRefreshLayout.finishRefresh();
            }
        });
    }

    //bmob复合查询-与查询
    public void initData(int state) {
        this.state = state;

        BmobQuery<OrderBean> eq0 = new BmobQuery<OrderBean>();
        eq0.addWhereEqualTo("shopId", MySharePreference.getShopBean().getObjectId());

        BmobQuery<OrderBean> eq1 = new BmobQuery<OrderBean>();
        if (state == 666) {
            //已完成-(待评价vs评价完成)
            List<Integer> list = new ArrayList<>();
            list.add(Constant.ORDER_STATE_WAIT_COMMENT);
            list.add(Constant.ORDER_STATE_COMPLETE);
            eq1.addWhereContainedIn("state", list);
        } else {
            eq1.addWhereEqualTo("state", state);
        }

        BmobQuery<OrderBean> eq2 = new BmobQuery<OrderBean>();
        eq2.addWhereEqualTo("closeOrder", false);

        List<BmobQuery<OrderBean>> queries = new ArrayList<BmobQuery<OrderBean>>();
        queries.add(eq0);
        queries.add(eq1);
        queries.add(eq2);

        BmobQuery<OrderBean> mainQuery = new BmobQuery<OrderBean>();
        mainQuery.and(queries);
        mainQuery.order("-createdAt");//最新时间查询
        mainQuery.findObjects(new FindListener<OrderBean>() {
            @Override
            public void done(List<OrderBean> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    if (adapter == null) {
                        listView.setAdapter(adapter = new OrderShopStateListAdapter(context, list, R.layout.layout_item_order_sate, OrderShopStateTabPage.this));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                OrderBean bean = adapter.getItem(i);
                                Intent intent = new Intent(context, ShopReceiveOrderDetailActivity.class);
                                intent.putExtra(Constant.OBJECTID, bean.getObjectId());
                                context.startActivity(intent);
                            }
                        });
                    } else {
                        adapter.refreshData(list);
                    }
                } else {
                    if (adapter != null)
                        adapter.clearAllData();
                }
            }
        });
    }

    public void refreshListData() {
        initData(state);
    }
}
