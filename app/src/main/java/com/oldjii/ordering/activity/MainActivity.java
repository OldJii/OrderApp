package com.oldjii.ordering.activity;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jaeger.library.StatusBarUtil;
import com.oldjii.ordering.R;
import com.oldjii.ordering.base.BaseActivity;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.confige.MySharePreference;
import com.oldjii.ordering.fragment.FragmentMine;
import com.oldjii.ordering.fragment.FragmentOrder;
import com.oldjii.ordering.fragment.FragmentShop;
import com.oldjii.ordering.fragment.FragmentHome;
import com.oldjii.ordering.utils.GaoDeMapUtils;
import com.oldjii.ordering.utils.ToasUtils;
import com.oldjii.ordering.utils.UIUtils;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * 首页（主界面）
 */
public class MainActivity extends BaseActivity {

    static RadioGroup radioGroup;
    public int currentTabPosition;
    private GaoDeMapUtils gaoDeMapUtils;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        radioGroup = findViewById(R.id.id_mian_activity_radiogroup);
        gaoDeMapUtils = new GaoDeMapUtils();
        changeMianUI(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int position = 0;
                switch (i) {
                    case R.id.radio_home:
                        position = 0;
                        break;
                    case R.id.radio_shop:
                        position = 1;
                        break;
                    case R.id.radio_order:
                        position = 2;
                        break;
                    case R.id.radio_mine:
                        position = 3;
                        break;
                }
                if (currentTabPosition == position) return;//优化
                changeMianUI(position);
            }
        });
        if (MySharePreference.getCurrentLoginState() == Constant.STATE_BUSINESS_LOGIN_SUC) {
            orderButtonOption(true);
        }
    }

    private FragmentHome fragmentHome;
    private FragmentShop fragmentShop;
    private FragmentOrder fragmentOrder;
    private FragmentMine fragmentMine;

    private void changeMianUI(int id) {
        if (id == 0 || id == 2) {
            //设置状态栏文字颜色及图标为浅色
            StatusBarUtil.setDarkMode(this);
        } else {
            //设置状态栏文字颜色及图标为深色
            StatusBarUtil.setLightMode(this);
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();//获取事物
        hindMainFourFragment(ft);

        currentTabPosition = id;
        switch (id) {
            case 0:
                if (fragmentHome == null) {
                    ft.add(R.id.id_mian_activity_framelayout, fragmentHome = new FragmentHome());
                } else {
                    ft.show(fragmentHome);
                }
                break;
            case 1:
                if (fragmentShop == null) {
                    ft.add(R.id.id_mian_activity_framelayout, fragmentShop = new FragmentShop());
                } else {
                    ft.show(fragmentShop);
                }
                break;
            case 2:
                if (fragmentOrder == null) {
                    ft.add(R.id.id_mian_activity_framelayout, fragmentOrder = new FragmentOrder());
                } else {
                    ft.show(fragmentOrder);
                }
                break;
            case 3:
                if (fragmentMine == null) {
                    ft.add(R.id.id_mian_activity_framelayout, fragmentMine = new FragmentMine());
                } else {
                    ft.show(fragmentMine);
                }
                break;
        }
        ft.commit();//提交事物
        //视频操作
        if (currentTabPosition == 2) {
            JzvdStd.goOnPlayOnResume();
        } else {
            JzvdStd.goOnPlayOnPause();
        }
    }

    /**
     * 隐藏4个Fragment
     */
    private void hindMainFourFragment(FragmentTransaction ft) {
        if (fragmentHome != null) {
            ft.hide(fragmentHome);
        }
        if (fragmentShop != null) {
            ft.hide(fragmentShop);
        }
        if (fragmentOrder != null) {
            ft.hide(fragmentOrder);
        }
        if (fragmentMine != null) {
            ft.hide(fragmentMine);
        }
    }

    //返回界面继续播放
    @Override
    protected void onResume() {
        super.onResume();
        //home back
        if (currentTabPosition == 2) JzvdStd.goOnPlayOnResume();
    }

    //Home键退出界面暂停播放
    @Override
    protected void onPause() {
        super.onPause();
        //home back
        if (currentTabPosition == 2) JzvdStd.goOnPlayOnPause();
    }

    /**
     * 直接监听back返回
     */
    private static boolean exit;

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) { //Jz视频能否退出(是否是横屏播放暂停)
            return;
        } else {
            if (currentTabPosition != 0) {
                RadioButton radio0 = (RadioButton) radioGroup.getChildAt(0);
                radio0.setChecked(true);
                return;
            }
            if (!exit) {
                ToasUtils.showToastMessage("再按一次退出");
                exit = true;
                UIUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 2000);
                return;
            }
        }
        super.onBackPressed();
    }


    @Override
    protected void statueBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消handler发送的信息
        UIUtils.getHandler().removeCallbacksAndMessages(null);
        gaoDeMapUtils.destory();
        //释放所有视频资源
        JzvdStd.releaseAllVideos();
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 主界面不需要支持滑动返回，重写该方法永久禁用当前界面的滑动返回功能
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    public void orderButtonOption(boolean isHide) {
        radioGroup.getChildAt(2).setVisibility(isHide ? View.GONE : View.VISIBLE);
    }
}
