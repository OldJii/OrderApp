package com.oldjii.ordering.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.oldjii.ordering.R;
import com.oldjii.ordering.activity.ImageDetailActivity;
import com.oldjii.ordering.bmob.CommentBean;
import com.oldjii.ordering.common.CommonAdapter;
import com.oldjii.ordering.common.CommonViewHolder;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.utils.GlideUtils;
import com.oldjii.ordering.views.CustomGridView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * 评价列表适配器
 *
 * （商家评价页面（用户端））
 */
public class OrderCommentsAdapter extends CommonAdapter<CommentBean> {

    public OrderCommentsAdapter(Context context, List<CommentBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, CommentBean commentBean) {
        GlideUtils.load(commentBean.userPortrait, holder.getView(R.id.id_commen_profile_image));

        holder.setTvText(R.id.id_commen_nickname, commentBean.isInonymous ? "匿名用户" : commentBean.userName)
                .setTvText(R.id.id_commen_time, commentBean.getCreatedAt())
                .setTvText(R.id.id_commen_body, commentBean.content);
        MaterialRatingBar ratingBar = holder.getView(R.id.id_ratingbar);
        ratingBar.setRating(Float.valueOf(commentBean.score));

        CustomGridView gridView = holder.getView(R.id.girdview);
        try {
            JSONArray jsonArray = new JSONArray(commentBean.imgs);
            if (jsonArray != null && jsonArray.length() > 0) {
                ArrayList<String> imgs = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    imgs.add(jsonArray.getString(i));
                }
                gridView.setAdapter(new GridImgAdapter(mContext, imgs, R.layout.item_img));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(mContext, ImageDetailActivity.class);
                        intent.putExtra(Constant.POSITION, position);
                        intent.putStringArrayListExtra(Constant.IMAGES, imgs);
                        mContext.startActivity(intent);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
