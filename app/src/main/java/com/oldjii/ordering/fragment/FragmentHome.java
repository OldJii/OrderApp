package com.oldjii.ordering.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kongzue.dialog.v2.SelectDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.oldjii.ordering.R;
import com.oldjii.ordering.activity.SearchShopActivity;
import com.oldjii.ordering.activity.ShopFoodActivity;
import com.oldjii.ordering.adapter.ShopListAdapter;
import com.oldjii.ordering.base.BaseFragment;
import com.oldjii.ordering.bmob.BannerData;
import com.oldjii.ordering.bmob.ShopBean;
import com.oldjii.ordering.confige.MySharePreference;
import com.oldjii.ordering.utils.CustomCodeUtils;
import com.oldjii.ordering.utils.CustomListView;
import com.oldjii.ordering.utils.GlideImageLoader;
import com.oldjii.ordering.utils.NotificationUtils;
import com.oldjii.ordering.utils.ToasUtils;
import com.oldjii.ordering.utils.UIUtils;
import com.oldjii.ordering.views.CustomeScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 首页Fragment
 */
public class FragmentHome extends BaseFragment {
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.listview)
    CustomListView listView;
    @BindView(R.id.martrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.scrollView)
    CustomeScrollView customeScrollView;
    @BindView(R.id.id_index_top_search_view)
    FrameLayout searchParentView;
    @BindView(R.id.id_location_tv)
    TextView lovationTv;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private ShopListAdapter adapter;

    Animation animation1;
    Animation animation2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        init();
        //notification通知授予
        NotificationUtils.checkNotificationIsOpen(getActivity());

        return view;
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            searchParentView.setPadding(0, UIUtils.getStateBarHeight(), 0, 0);
        }
        //scrollview滚动监听
        customeScrollView.setIonScrollListener(new CustomeScrollView.IonScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                //top search view
                searchParentView.setBackgroundColor(getResources().getColor(R.color.text_center_tab_selected));
                if (scrollY <= 255) {
                    searchParentView.getBackground().setAlpha(scrollY);//透明度范围 0-255
                } else {
                    searchParentView.getBackground().setAlpha(255);//透明度范围 0-255
                }
                //fab
                if (scrollY > 755) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
                if (scrollY <= 0) {
                    floatingActionButton.setVisibility(View.GONE);
                }
            }
        });
        refreshData();
        refreshListtener();
        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_item_scale1);
        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_item_scale2);
        String address = MySharePreference.getCurrentLocationInfo().address;
        if (address != null) lovationTv.setText("当前位置 ：" + address);
    }

    private void refreshListtener() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshData();
                smartRefreshLayout.finishRefresh();
                ToasUtils.showToastMessage("刷新成功");
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
    }

    private void refreshData() {
        getAllBannerData();
        refreshAllShopListData();
    }

    //查询banner数据
    private void getAllBannerData() {
        BmobQuery<BannerData> bmobQuery = new BmobQuery<BannerData>();
        bmobQuery.order("-sort");//根据sort降序查询
        bmobQuery.addWhereEqualTo("isShow", true);
        bmobQuery.findObjects(new FindListener<BannerData>() {
            @Override
            public void done(List<BannerData> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    initBanner(list);
                }
            }
        });
    }

    private void initBanner(final List<BannerData> list) {
        List<String> imgs = new ArrayList<>();
        for (BannerData bean : list) {
            imgs.add(bean.getImg());
        }
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(imgs);
        mBanner.setDelayTime(3000);
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToasUtils.showToastMessage(list.get(position).getUrl());
            }
        });
    }

    //获取商家数据
    private void refreshAllShopListData() {
        BmobQuery<ShopBean> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-hot");//热度降序查询
        bmobQuery.addWhereEqualTo("isPass", true);
        bmobQuery.findObjects(new FindListener<ShopBean>() {
            @Override
            public void done(List<ShopBean> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    if (adapter == null) {
                        listView.setAdapter(adapter = new ShopListAdapter(getActivity(), list, R.layout.item_list_shop));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                ShopBean bean = adapter.getItem(i);
                                Intent intent = new Intent(getActivity(), ShopFoodActivity.class);
                                intent.putExtra("shop", bean);
                                startActivity(intent);
                            }
                        });
                    } else {
                        adapter.refreshAllData(list);
                    }
                } else {
                    if (adapter != null) adapter.clearAllData();
                }
            }
        });
    }

    @OnClick({R.id.fab, R.id.top_search_tv, R.id.id_location_tv})
    void click(final View view) {
        if (view.getId() == R.id.fab) {
            customeScrollView.post(new Runnable() {
                @Override
                public void run() {
                    customeScrollView.fullScroll(View.FOCUS_UP);
                }
            });
        } else if (view.getId() == R.id.top_search_tv) {
            startActivity(new Intent(getActivity(), SearchShopActivity.class));
        } else {
            //缩放动画
            view.startAnimation(animation1);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CustomCodeUtils.OPEN_CODE_SCAN) {
            if (resultCode == CustomCodeUtils.TYPE_XC_RETURN_CODE) {
                //相册返回
                String result = data.getStringExtra(CodeUtils.RESULT_STRING);
                showCodeResultDialog(result);
            } else if (resultCode != CustomCodeUtils.TYPE_XC_RETURN_CODE) {
                //直接扫描返回
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        showCodeResultDialog(result);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        ToasUtils.showToastMessage("抱歉!请重试");
                    }
                }
            }
        }
    }

    private void showCodeResultDialog(String result) {
        if (isHttpUrl(result)) {
            urlTypeDialog(result);
        } else {
            textTypeDialog(result);
        }
    }

    private void urlTypeDialog(String result) {
        SelectDialog.show(getActivity(), "扫描结果", result, "游览器打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse(result);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }, "复制链接", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                copy(result);
            }
        }).setCanCancel(true);
    }

    private void textTypeDialog(String result) {
        SelectDialog.show(getActivity(), "扫描结果", result, "复制文本", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                copy(result);
            }
        }, "关闭弹框", null).setCanCancel(true);
    }

    private void copy(String content) {
        ClipboardManager copy = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(content);
        ToasUtils.showToastMessage("复制成功");
    }

    /**
     * 判断字符串是否为URL
     *
     * @param urls 字符串
     * @return true:是URL、false:不是URL
     */
    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))" + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }
}
