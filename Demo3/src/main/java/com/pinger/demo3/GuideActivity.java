package com.pinger.demo3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * Created by Pinger on 2016/9/15.
 */
public class GuideActivity extends Activity {

    private ViewPager mViewPager;
    private int[] mIcons = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4};
    private List<ImageView> mImageList;
    private LinearLayout mPointGroup;
    private Button mBtnDown;
    private Button mBtnSkip;
    private ImageButton mIbNext;
    private int mPointMargin;
    private ImageView mWhitePoint;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        // 状态栏透明
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mSp = getSharedPreferences("config", MODE_PRIVATE);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPointGroup = (LinearLayout) findViewById(R.id.point_group);
        mBtnDown = (Button) findViewById(R.id.btn_down);
        mBtnSkip = (Button) findViewById(R.id.btn_skip);
        mIbNext = (ImageButton) findViewById(R.id.ib_guide_next);
        mWhitePoint = (ImageView) findViewById(R.id.white_point);


        mImageList = new ArrayList<>();
        for (int i = 0; i < mIcons.length; i++) {
            // 准备好显示的图片
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mIcons[i]);

            // 添加到集合
            mImageList.add(image);

            // 设置底部小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_normal);

            // 设置白点的布局参数
            int pointSize = getResources().getDimensionPixelSize(R.dimen.point_size);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(pointSize, pointSize);
            mWhitePoint.setLayoutParams(params1);

            // 设置灰色点的布局参数
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(pointSize, pointSize);
            if (i > 0) {
                params2.leftMargin = getResources().getDimensionPixelSize(R.dimen.point_margin);
            }
            point.setLayoutParams(params2);
            mPointGroup.addView(point);
        }

        // 设置适配器
        GuideAdapter adapter = new GuideAdapter();
        mViewPager.setAdapter(adapter);


        // 获取视图树对象，通过监听白点布局的显示，然后获取两个圆点之间的距离
        mWhitePoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 此时layout布局已经显示出来了，可以获取小圆点之间的距离了
                mPointMargin = mPointGroup.getChildAt(1).getLeft() - mPointGroup.getChildAt(0).getLeft();

                // 将自己移除掉
                mWhitePoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        /**
         * 对View Pager添加监听
         */
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 页面滑动的时候，动态的获取小圆点的左边距
                int leftMargin = (int) (mPointMargin * (position + positionOffset));
                // Log.d("GuideActivity", "leftMargin:" + leftMargin);

                // 获取布局参数，然后设置布局参数
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mWhitePoint.getLayoutParams();
                // 修改参数
                params.leftMargin = leftMargin;
                // 重新设置布局参数
                mWhitePoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

                // 最后一页
                if (position == mIcons.length - 1) {
                    mBtnSkip.setVisibility(View.GONE);
                    mIbNext.setVisibility(View.GONE);
                    mBtnDown.setVisibility(View.VISIBLE);

                } else {
                    // 不是最后一页
                    mBtnDown.setVisibility(View.GONE);
                    mBtnSkip.setVisibility(View.VISIBLE);
                    mIbNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // SKIP跳过按钮的点击事件
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMain();
            }
        });


        // 下一页点击按钮的点击事件
        mIbNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下一页
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });

        // 完成引导按钮的点击事件
        mBtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMain();
            }
        });
    }


    /**
     * 进入主页
     */
    private void enterMain() {
        // 设置不是第一次进入主页
        mSp.edit().putBoolean("guide", false).apply();

        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 将图片添加到ViewPager容器
            container.addView(mImageList.get(position));
            return mImageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
