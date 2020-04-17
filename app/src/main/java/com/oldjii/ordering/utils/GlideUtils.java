package com.oldjii.ordering.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oldjii.ordering.R;

/**
 * 统一图片加载
 */
public class GlideUtils {
    public static void load(String url, ImageView imageView) {
        Glide.with(UIUtils.getContext())
                .load(url)
                .error(R.mipmap.logo)
                .into(imageView);
    }
}
