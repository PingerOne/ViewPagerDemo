# ViewPager详解（一）简单介绍
### 一、什么是ViewPager？
#### 1.1 谷歌官方解释
![introduction](http://upload-images.jianshu.io/upload_images/2786991-8f0b39a90dffa7b5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 1.2 大致意思
* 布局管理器允许左右翻转带数据的页面，你想要显示的视图可以通过实现PagerAdapter来显示。这个类其实是在早期设计和开发的，它的API在后面的更新之中可能会被改变，当它们在新版本之中编译的时候可能还会改变源码。

* ViewPager经常用来连接Fragment，它很方便管理每个页面的生命周期，使用ViewPager管理Fragment是标准的适配器实现。最常用的实现一般有FragmentPagerAdapter和FragmentStatePagerAdapter。

* FragmentPagerAdapter和FragmentStatePagerAdapter是ViewPager和Fragment一起使用时才会用到，后面会详细介绍，这里就不介绍了。

#### 1.3 介绍总结
* ViewPager是v4包中的一个类。
* ViewPager继承自ViewGroup，其实是一个容器。
* ViewPager类似于ListView，也有自己的适配器，里面用来填充数据页面。
* ViewPager一般和Fragment一起使用，它更方面的管理页面中Fragment的生命周期。


### 二、ViewPager的简单使用
#### 布局文件中申明控件
* 由于ViewPager是一个类似ListView的容器，一般使用单标签

		 <!--填充整个页面的ViewPager-->
		 <android.support.v4.view.ViewPager
	        android:id="@+id/viewpager"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"/>
		


#### 代码中设置显示数据

* 获取控件对象

		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

* 自定义类继承PagerAdapter填充页面和数据
	* int getCount()：返回显示多少个页面 
	* boolean isViewFromObject(View view, Object object)：判断初始化返回的Object是不是一个View对象
	* Object instantiateItem(ViewGroup container, int position)：初始化显示的条目对象
	* void destroyItem(ViewGroup container, int position, Object object)：销毁条目对象
			
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
		



* 设置适配器

	  viewPager.setAdapter(new MyAdapter());
		
		
#### 效果图

![Demo1效果图](http://upload-images.jianshu.io/upload_images/2786991-55423d9eeddeef1b.gif?imageMogr2/auto-orient/strip)


<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/><br/>

---


# ViewPager详解（二）广告轮播图案例

### 效果图
![效果图](http://upload-images.jianshu.io/upload_images/2786991-e958ba9933115ff2.gif?imageMogr2/auto-orient/strip)


### 一、ViewPager填充图片
#### 1.1 布局中申明
* 由于是显示广告条，所以高度要固定住

		<android.support.v4.view.ViewPager
		    android:id="@+id/viewpager"
		    android:layout_width="match_parent"
		    android:layout_height="120dp"/>
		    
#### 1.2 代码中设置页面数据
* 准备显示图片控件的集合

		// 准备显示的图片集合
        mList = new ArrayList<>();
        for (int i = 0; i < mImages.length; i++) {
            ImageView imageView = new ImageView(this);
            // 将图片设置到ImageView控件上
            imageView.setImageResource(mImages[i]);

            // 将ImageView控件添加到集合
            mList.add(imageView);
        }

* 自定义类书写适配器

		@Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);
            // 将图片控件添加到容器
            container.addView(mList.get(position));

            // 返回
            return mList.get(position);
        }

### 二、底部小圆点显示逻辑
> 原理分析：底部的小圆点时浮动在ViewPager上面的的，所以应该是一个RelativeLayout布局。
> ViewPager页面切换时小圆点的颜色不一样，所以需要对小圆点做选择器，并且对ViewPager进行监听。

#### 2.1 布局申明
* 需要用一个RelativeLayout将ViewPager和包裹圆点的LinearLayout包裹起来


		<RelativeLayout
	        android:layout_width="match_parent"
	        android:layout_height="120dp">
	
	        <android.support.v4.view.ViewPager
	            android:id="@+id/viewpager"
	            android:layout_width="match_parent"
	            android:layout_height="120dp"/>
	
	        <LinearLayout
	            android:id="@+id/pointgroup"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:layout_alignParentBottom="true"
	          android:layout_centerHorizontal="true"
	            android:layout_marginBottom="3dp"
	            android:orientation="horizontal">
	        </LinearLayout>
	
	
	    </RelativeLayout>
		
			
#### 2.2 制作小圆点颜色选择器
* 选择器的选中状态应该设置为selected，因为对ViewPager监听时可以设置selected的属性

		<selector xmlns:android="http://schemas.android.com/apk/res/android">
		    <item android:drawable="@drawable/shape_point_normal" android:state_selected="false"/>
		    <item android:drawable="@drawable/shape_point_selected" android:state_selected="true"/>
		</selector>
		
		<shape xmlns:android="http://schemas.android.com/apk/res/android"
               android:shape="oval">
			<solid android:color="#66000000"/>
		</shape
		
		<shape xmlns:android="http://schemas.android.com/apk/res/android"
		    android:shape="oval">
		    <solid android:color="#FFFFFF"/>
		</shape>



#### 2.3 将小圆点添加到LinearLayout容器
* 小圆点其实就是一个ImageView，所以在做出ViewPager的页面图片时，一起把小圆点也做了
* 初始化ImageView添加到LinearLayout之前，需要设置小圆点的布局参数，包括位置和大小

		LinearLayout pointGroup = (LinearLayout) findViewById(R.id.pointgroup);

        for (int i = 0; i < mImages.length; i++) {
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




### 三、小圆点随着ViewPager切换移动
* 其实就是对ViewPager设置滑动监听，当滑动到每一页时就设置小圆点为选中状态，这样小圆点就显示白色，其他页面就设置为未选中状态显示灰色。

		// 对ViewPager设置滑动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            int lastPosition;
            @Override
            public void onPageSelected(int position) {
                // 页面被选中
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
* 这里设置选中和未选中除了可以用替换position的方法之外，还可以再定义一个ImageView来记录选中的ImageView，然后在监听里替换，思想都是一样的。
        
        
---
#### 经过前面的三步设置后就能显示一个简单的广告条了，这里再对其添加一个滑动到最后一页后再滑还能滑动到首页的功能。

### 四、无限滑动的ViewPager
> 实现原理：	ViewPager之所以滑动到左右能显示页面，其实是因为左右都存在即将要显示的页面。当左右有很多页面时我们就能一直滑动，没有时就不能滑动。所以原理就是让ViewPager的左右都有很多的页面。

#### 4.1 修改getCount方法
* ViewPager能显示多少个页面全由getCount方法说了算，所以我们首先要改造它。

		@Override
        public int getCount() {
            // 返回整数的最大值
            return Integer.MAX_VALUE;
        }

#### 4.2 修改instantiateItem方法
* 因为position变了，所以显示的位置也变了，这里需要进行取%运算，来还原position。

		// 修改position
        position = position % mList.size();
        
#### 4.3 修改ViewPager监听器里的onPageSelected
* 用到了position就要修改。

        position = position % mList.size();

#### 修改之后，ViewPager当前页的右边就有了无数的页面，但是因为%了mList.size()，就只会显示mList.size()的大小，这样就实现了无限滑动轮播


### 五、无限自动轮播的广告图
> 实现原理：在前面四步的基础上，在代码里添加一个Handler，不断的给自己发消息就好了。

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
                mHandler.postDelayed(this,5000);
            }
        },5000);	



<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/><br/>

---
# ViewPager详解（三）引导页

#### 效果图
* 部分素材来自互联网，侵权什么的请及时告知，谢谢。

![引导页效果图](http://upload-images.jianshu.io/upload_images/2786991-8a609dc5e4cc25d1.gif?imageMogr2/auto-orient/strip)



#### 一、引导页简介
* 引导页一般是在用户第一次进入app时给用户的友好提示，包括介绍app的基本功能，最近更新的功能等等。

* 目前市场上的app的引导页大部分都是采用ViewPager滑动的方式实现，每一个页面采用图片或者素材加图片的方式填充。



#### 一、Splash界面的实现
* Splash界面通常在app启动时弹出的界面，一般在这时做一些数据的初始化操作。
* 这里进入Splash时，获取用户是否第一次进入，是则进入引导页，不是则进入主页。

##### 1.1 Splash界面逻辑分析图

![Splash界面逻辑分析图](http://upload-images.jianshu.io/upload_images/2786991-bd60c84720bc6e79.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 1.2 代码实现
* 先读取出用户是否是第一次进入app的状态，根据状态做不同的处理
	* true：延迟3秒进入引导页
	* false：延迟3秒进入主页

			mSp = getSharedPreferences("config", MODE_PRIVATE);
	
	        // 默认进入主页
	        boolean firstEnter = mSp.getBoolean("guide", false);
	        if (firstEnter) {
	            // 进入引导页
	            mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
	                    finish();
	                }
	            }, 3000);
	        } else {
	            // 进入主页
	            mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
	                    finish();
	                }
	            }, 3000);
	
	        }

#### 二、引导页的实现
> 需求分析：这里的引导页是使用四张图片来进行填充，并且在底部添加了一个小的操作平台，包括了页面指示器，跳过按钮，下一页按钮，滑动时指示器能动态指示当前的页面，点击跳过按钮进入主页，点击下一页进行翻页，最后一页有进入主页的按钮。


> 实现原理：做一个ViewPager容器填充图片，底部做一个RelativeLayout容器存放按钮和指示器。其中不同按钮的显示是根据当前的页面位置，做隐藏和显示处理。按钮的点击是通过监听做不同的操作，指示器是根据当前页面的偏移来计算白点距离左边的位置，达到实时更新位置的效果。

##### 2.1 ViewPager的编写
* 布局中申明

	    <android.support.v4.view.ViewPager
	        android:id="@+id/viewpager"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
	    </android.support.v4.view.ViewPager>

* 代码中使用，获取对象，设置适配器，显示图片，这些基本的操作前面都已经介绍过了，这里就不累述了，不知道的请上传送门。
[ViewPager详解（一）简单介绍和使用](http://www.jianshu.com/p/3f9cc4beb0ae)   
[ViewPager详解（二）广告轮播图](http://www.jianshu.com/p/48f63c537ae5)


##### 2.2 底部圆点的指示器的实现
> 逻辑分析：看效果图可以知道，白点是浮动在上面的，也就是说，底下灰点是背景，上面白点随着页面的偏移而偏移。那就可以这样设计，灰点用LinearLayout包裹 ，白点用ImageView或View包裹并且浮在LinearLayout上面，在填充图片的时候初始化这两种类型的点，然后监听ViewPager页面的滑动，动态的设置白点的位置。

> 白点的位置分析：监听ViewPager的滑动，我们会得到一个页面的偏移百分比，有了百分比，我们还差一个白点之间的距离，他俩相乘后累加就会得到白点随着页面滑动的移动轨迹，那我们现在还缺白点之间的距离。白点之间的距离可以在布局完成之后用第二个点的左边距减去第一个点的左边距得到，所以需要的数据就基本分析出来了。

* 初始化两种类型的圆点

		for (int i = 0; i < mIcons.length; i++) {
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

			// 灰点添加到容器
            mPointGroup.addView(point);
        }

* 获取白点之间的距离，因为要测量边距，所以要保证布局已经完成，这里使用视图树对象来添加一个全局的布局监听。

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


* 监听ViewPager的滑动，在onPageScrolled方法中动态修改白点的左边距，这样白点就会随着页面的滑动而移动。		

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


#### 2.2 底部按钮的显示隐藏和点击事件
* 监听ViewPager的滑动，在onPageSelected方法中判断position的位置来显示隐藏控件。

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

* 实现按钮的点击事件，进入主页需要保存进入app的状态。

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


##### 经过以上的分析和编码之后，引导页这一块大家自己做出来应该基本没什么问题了，市面上还有一些引导页是动画的效果，包括ViewPager滑动的动画，获取页面上的素材随ViewPager移动的动画等等，这些其实都没什么难的，仔细分析一下就知道怎么做的了。


---
[个人主页](http://www.jianshu.com/users/64f479a1cef7/latest_articles)

[Demo下载地址](https://github.com/PingerWan/ViewPagerDemo)


####  以上纯属于个人平时工作和学习的一些总结分享，如果有什么错误欢迎随时指出，大家可以讨论一起进步。如果你觉得对你有帮助，请到[GitHub](https://github.com/PingerWan/ViewPagerDemo)上点个赞，谢谢。
