package com.pinger.demo1;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter());
    }


    /**
     * 自定义类实现PagerAdapter，填充显示数据
     */
    class MyAdapter extends PagerAdapter {

        // 显示多少个页面
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化显示的条目对象
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);
            // 准备显示的数据，一个简单的TextView
            TextView tv = new TextView(MainActivity.this);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);
            tv.setText("我是天才" + position + "号");
            switch (position) {
                case 0:
                    tv.setBackgroundColor(Color.GREEN);
                    break;
                case 1:
                    tv.setBackgroundColor(Color.RED);
                    break;
                case 2:
                    tv.setBackgroundColor(Color.YELLOW);
                    break;
                case 3:
                    tv.setBackgroundColor(Color.BLUE);
                    break;
                case 4:
                    tv.setBackgroundColor(Color.CYAN);
                    break;

            }

            // 添加到ViewPager容器
            container.addView(tv);

            // 返回填充的View对象
            return tv;
        }


        // 销毁条目对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
