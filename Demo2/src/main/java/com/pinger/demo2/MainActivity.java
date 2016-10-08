package com.pinger.demo2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 图片资源
    private int[] mImages = {R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4};
    private List<ImageView> mList;
    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final LinearLayout pointGroup = (LinearLayout) findViewById(R.id.pointgroup);

        // 准备显示的图片集合
        mList = new ArrayList<>();
            for (int i = 0; i < mImages.length; i++) {
            ImageView imageView = new ImageView(this);
            // 将图片设置到ImageView控件上
            imageView.setImageResource(mImages[i]);

            // 将ImageView控件添加到集合
            mList.add(imageView);

            // 制作底部小圆点
            ImageView pointImage = new ImageView(this);
            pointImage.setImageResource(R.drawable.shape_point_selector);

            // 设置小圆点的布局参数
            int PointSize = getResources().getDimensionPixelSize(R.dimen.point_size);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PointSize, PointSize);

            if (i > 0) {
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.point_margin);
                pointImage.setSelected(false);
            } else {
                pointImage.setSelected(true);
            }
            pointImage.setLayoutParams(params);
            // 添加到容器里
            pointGroup.addView(pointImage);
        }

        viewPager.setAdapter(new MyAdapter());

        // 对ViewPager设置滑动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            int lastPosition;
            @Override
            public void onPageSelected(int position) {
                // 页面被选中

                // 修改position
                position = position % mList.size();

                // 设置当前页面选中
                pointGroup.getChildAt(position).setSelected(true);
                // 设置前一页不选中
                pointGroup.getChildAt(lastPosition).setSelected(false);

                // 替换位置
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentPosition = viewPager.getCurrentItem();

                if(currentPosition == viewPager.getAdapter().getCount() - 1){
                    // 最后一页
                    viewPager.setCurrentItem(0);
                }else{
                    viewPager.setCurrentItem(currentPosition + 1);
                }

                // 一直给自己发消息
                mHandler.postDelayed(this,3000);
            }
        },3000);

    }



    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 返回整数的最大值
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);
            // 修改position
            position = position % mList.size();
            // 将图片控件添加到容器
            container.addView(mList.get(position));

            // 返回
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
