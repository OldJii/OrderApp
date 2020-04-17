package com.oldjii.ordering.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.oldjii.ordering.R;
import com.oldjii.ordering.base.BaseActivity;
import com.oldjii.ordering.bmob.UserBean;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.confige.MySharePreference;
import com.oldjii.ordering.fragment.FragmentMine;
import com.oldjii.ordering.utils.CallPhoneUtils;
import com.oldjii.ordering.utils.ToasUtils;
import com.oldjii.ordering.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 用户注册页面
 */
public class UserLoginActivity extends BaseActivity {
    @BindView(R.id.username_mobile_edit)
    EditText usernameMobileEdit;
    @BindView(R.id.pass)
    EditText passEdit;
    @BindView(R.id.see)
    ImageView seeIv;
    @BindView(R.id.user_mobile_view)
    RelativeLayout userMobileView;
    @BindView(R.id.id_change_login_tv)
    TextView loginTypeTv;
    @BindView(R.id.flage_iv)
    ImageView flageIv;
    @BindView(R.id.back_colse_view)
    LinearLayout backLinearLayout;

    Animation animation1;
    Animation animation2;

    public static final int WX_LOGIN = 100;
    public static Handler mHandler;
    private boolean isSee;
    private int loginType;//0用户名登陆 1手机号码登陆

    @Override
    protected int setContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.translate_none, R.anim.translate_between_to_down);
    }

    @Override
    protected void statueBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) backLinearLayout.getLayoutParams();
            lp.topMargin = UIUtils.getStateBarHeight();
            backLinearLayout.setLayoutParams(lp);
        }

        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_item_scale1);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_item_scale2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.login, R.id.register, R.id.see, R.id.id_change_login_tv})
    void click(View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.register:
                startActivity(new Intent(this, UserRegisterActivity.class));
                break;
            case R.id.see:
                if (isSee) {
                    seeIv.setImageResource(R.mipmap.icon_see_n);
                    passEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    seeIv.setImageResource(R.mipmap.icon_see_p);
                    passEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                isSee = !isSee;
                //光标靠后
                Editable etable = passEdit.getText();
                Selection.setSelection(etable, etable.length());
                break;
            case R.id.id_change_login_tv:
                changeLoginType();
                break;
        }
    }

    private void changeLoginType() {
        usernameMobileEdit.setText(null);
        if (loginType == 0) {
            //手机号码登陆
            loginType = 1;
            loginTypeTv.setText("用户名登陆");
            usernameMobileEdit.setHint("请输入手机号码");
            flageIv.setImageResource(R.mipmap.icon_phone);
            usernameMobileEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            //用户名登陆
            loginType = 0;
            loginTypeTv.setText("手机号码登陆");
            usernameMobileEdit.setHint("请输入用户名");
            flageIv.setImageResource(R.mipmap.icon_count);
            usernameMobileEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);//旋转时间
        rotateAnimation.setFillAfter(true);//旋转后停止在最后,保持动画结束状态
        userMobileView.startAnimation(rotateAnimation);
    }

    private void login() {
        String username = usernameMobileEdit.getText().toString().trim();
        String pass = passEdit.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            shake(usernameMobileEdit);
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            shake(passEdit);
            return;
        }
        closeSoftware();
        if (loginType == 0) {
            loginByUsername(username, pass);
        } else {
            if (!CallPhoneUtils.isPhone(username)) {
                shake(usernameMobileEdit);
                ToasUtils.showToastMessage("请输入正确的手机号码");
                return;
            }
            loginByPhone(username, pass);
        }
    }

    private void shake(EditText editText) {
        //控件横向来回抖动
        ObjectAnimator anim = ObjectAnimator.ofFloat(editText, "translationX", -30, 30, -25, 25, -20, 20, -15, 15, -10, 10, -5, 5, 0);
        anim.setDuration(1000);
        anim.start();
    }

    /**
     * 账号密码登录
     */
    private void loginByUsername(String username, String password) {
        final UserBean user = new UserBean();
        //此处替换为你的用户名
        user.setUsername(username);
        //此处替换为你的密码
        user.setPassword(password);
        user.login(new SaveListener<UserBean>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void done(UserBean bmobUser, BmobException e) {
                if (e == null) {
                    //登录成功
                    MySharePreference.setCurrentLoginState(Constant.STATE_NORMAL_LOGIN_SUC);
                    FragmentMine.initState();
                    finish();
                } else {
                    //登录失败
                    shake(usernameMobileEdit);
                    shake(passEdit);
                    ToasUtils.showToastMessage("用户名或密码错误");
                }
            }
        });
    }

    /**
     * 手机号码密码登录
     */
    private void loginByPhone(String phone, String password) {
        //TODO 此处替换为你的手机号码和密码
        BmobUser.loginByAccount(phone, password, new LogInListener<UserBean>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void done(UserBean user, BmobException e) {
                if (e == null && user != null) {
                    //登录成功
                    ToasUtils.showToastMessage("登陆成功");
                    MySharePreference.setCurrentLoginState(Constant.STATE_NORMAL_LOGIN_SUC);
                    FragmentMine.initState();
                    finish();
                } else {
                    //登录失败
                    shake(usernameMobileEdit);
                    shake(passEdit);
                    ToasUtils.showToastMessage("手机号或密码错误");
                }
            }
        });
    }
}
