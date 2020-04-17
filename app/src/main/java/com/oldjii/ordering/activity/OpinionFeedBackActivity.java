package com.oldjii.ordering.activity;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kongzue.dialog.v2.MessageDialog;
import com.oldjii.ordering.R;
import com.oldjii.ordering.base.BaseActivity;
import com.oldjii.ordering.bmob.OpinionBean;
import com.oldjii.ordering.utils.ToasUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 意见反馈页面
 * 直接提交到Bmob数据库中
 */
public class OpinionFeedBackActivity extends BaseActivity {
    @BindView(R.id.id_base_title_tv)
    TextView mTitleTv;
    @BindView(R.id.msg_cntancet)
    EditText lxfsEdit;
    @BindView(R.id.msg_edit)
    EditText contentEdit;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_yjfk;
    }

    @Override
    protected boolean setStatusBarFontColorIsBlack() {
        return true;
    }

    @Override
    protected void initView() {
        mTitleTv.setText("意见反馈");
    }

    @OnClick(R.id.commit_tv)
    void click(View view) {
        closeSoftware();
        String xlfs = lxfsEdit.getText().toString().trim();
        String content = contentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(xlfs)) {
            ToasUtils.showToastMessage("请填写你的联系方式");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToasUtils.showToastMessage("请填写您宝贵的意见");
            return;
        }

        //表名及为OpinionBean
        OpinionBean opinionBean = new OpinionBean();
        opinionBean.number = xlfs;
        opinionBean.msg = content;
        opinionBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    MessageDialog.show(OpinionFeedBackActivity.this, "提示", "数据提交成功，感谢您的反馈！", "关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                } else {
                    ToasUtils.showToastMessage("数据提交失败：" + e.getMessage());
                }
            }
        });
    }
}
