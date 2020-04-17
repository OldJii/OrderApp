package com.oldjii.ordering.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.kongzue.dialog.v2.SelectDialog;
import com.oldjii.ordering.R;
import com.oldjii.ordering.base.BaseActivity;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.confige.MySharePreference;
import com.oldjii.ordering.utils.AppInfoManagerUtils;
import com.oldjii.ordering.utils.GlideCacheUtil;
import com.oldjii.ordering.utils.ToasUtils;
import com.oldjii.ordering.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们页面
 */
public class AboutOurActivity extends BaseActivity {
    @BindView(R.id.about_our_toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_logo_image)
    ImageView appLogoIv;
    @BindView(R.id.about_our_appname)
    TextView appNameText;
    @BindView(R.id.about_our_version_name)
    TextView appVersionNameText;
    @BindView(R.id.id_cache_tv)
    TextView chacheSizeTv;
//    @BindView(R.id.about_our_history_version_text)
//    TextView historyVersionText;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_aboutour;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height = lp.height + UIUtils.getStateBarHeight();
            toolbar.setPadding(0, UIUtils.getStateBarHeight(), 0, 0);
            toolbar.setLayoutParams(lp);
        }
        //init logo,app name,app version name
        AppInfoManagerUtils appInfoManagerUtils = new AppInfoManagerUtils(this);
        appLogoIv.setImageDrawable(appInfoManagerUtils.getAppLogo());
        appNameText.setText(appInfoManagerUtils.getAppName());
        appVersionNameText.setText("version " + appInfoManagerUtils.getAppVersionName());
        String size = GlideCacheUtil.getInstance().getCacheSize(this);
        chacheSizeTv.setText(size);

        //history varsion
//        String html = "<font color='blue'><u><i>历史版本</i></u></font><br/>";
//        historyVersionText.setText(Html.fromHtml(html));
//        historyVersionText.setMovementMethod(LinkMovementMethod.getInstance());
//        historyVersionText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("https://www.pgyer.com/pKwt");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });
    }

    @OnClick({R.id.user_info_ll, R.id.yjfk_ll, R.id.clear_cache_ll})
    void click(View view) {
        switch (view.getId()) {
            case R.id.user_info_ll:
                ckeck();
                break;
            case R.id.yjfk_ll:
                Intent intent = new Intent(this, OpinionFeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.clear_cache_ll:
                clearImageChche();
                break;
        }
    }

    private void ckeck() {
        Intent intent;
        if (MySharePreference.getCurrentLoginState() == Constant.STATE_NORMAL_LOGIN_SUC) {
            intent = new Intent(this, UserInfoActivity.class);
        } else {
            intent = new Intent(this, UserLoginActivity.class);
        }
        startActivity(intent);
    }

    private void clearImageChche() {
        SelectDialog.show(this, "提示", "确定要清除缓存吗?", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlideCacheUtil.getInstance().clearImageAllCache(AboutOurActivity.this);
                chacheSizeTv.setText("0.0M");
            }
        }, "取消", null).setCanCancel(true);
    }

    /**
     * 判断 Uri是否有效
     */
    public static boolean isValidIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }
}
