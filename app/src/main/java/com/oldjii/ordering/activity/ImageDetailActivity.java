package com.oldjii.ordering.activity;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oldjii.ordering.R;
import com.oldjii.ordering.base.BaseActivity;
import com.oldjii.ordering.confige.Constant;
import com.oldjii.ordering.utils.GlideUtils;

import java.util.ArrayList;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

/**
 * 图片详情页
 * 需传参数：
 * TITLE 非必传
 * IMAGES 必传
 * POSITION 必传
 */

public class ImageDetailActivity extends BaseActivity {
    @BindView(R.id.id_img_detail_vp)
    ViewPager viewPager;
    @BindView(R.id.id_img_detail_title)
    TextView mTitleTv;
    @BindView(R.id.id_img_detail_count)
    TextView mIndexTv;

    private ArrayList<String> images;
    private int position;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected int setStatueBarColor() {
        return R.color.img_statue_bar_color;
    }

    @Override
    protected void initView() {
        String title = getIntent().getStringExtra(Constant.TITLE);
        mTitleTv.setText(title != null ? title : "图片详情");
        images = getIntent().getStringArrayListExtra(Constant.IMAGES);
        position = getIntent().getIntExtra(Constant.POSITION, 0);
        viewPager.setAdapter(new VpAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndexTv.setText(position + 1 + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(position);
        mIndexTv.setText(position + 1 + "/" + images.size());
    }

    @Override
    public void finish() {
        super.finish();
    }

    class VpAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            PhotoView photoView = new PhotoView(ImageDetailActivity.this);
            GlideUtils.load(images.get(position), photoView);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
