package org.nupter.nupter.activity;

import java.util.ArrayList;
import android.view.*;
import org.nupter.nupter.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;




/**
 *  生活小秘书板块主界面
 *
 *  @author panlei e-mail:121531863@qq.com
 *
*/



public class LifeAssistantActivity extends Activity {

    // 定义ViewPager对象
    private ViewPager viewPager;

    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;

    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    //定义各个界面View对象
    private View view1,view2,view3,view4;

    // 定义底部小点图片
    private ImageView pointImage0, pointImage1, pointImage2, pointImage3;

    // 当前的位置索引值
    private int currIndex = 0;
    @SuppressLint("NewApi")

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifeassistant);
        initView();

        initData();

  }
    private void initView() {
        //实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.view_schoolguide, null);
        view2 = mLi.inflate(R.layout.view_busroute, null);
        view3 = mLi.inflate(R.layout.view_localinformation, null);
        view4 = mLi.inflate(R.layout.view_resttime, null);


        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 实例化ArrayList对象
        views = new ArrayList<View>();

        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);

        // 实例化底部小点图片对象
        pointImage0 = (ImageView) findViewById(R.id.page0);
        pointImage1 = (ImageView) findViewById(R.id.page1);
        pointImage2 = (ImageView) findViewById(R.id.page2);
        pointImage3 = (ImageView) findViewById(R.id.page3);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 设置监听
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        // 设置适配器数据
        viewPager.setAdapter(vpAdapter);

        //将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);

    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    pointImage0.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_gray));
                    pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_blue));
                    break;
                case 1:
                    pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_gray));
                    pointImage0.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_blue));
                    pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_blue));
                    break;
                case 2:
                    pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_gray));
                    pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_blue));
                    pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_blue));
                    break;
                case 3:
                    pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_gray));
                    pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.guide_dot_blue));
                    break;

            }
            currIndex = position;
            // animation.setFillAfter(true);// True:图片停在动画结束位置
            // animation.setDuration(300);
            // mPageImg.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }


}

    class ViewPagerAdapter extends PagerAdapter {

        //界面列表
        private ArrayList<View> views;

        public ViewPagerAdapter (ArrayList<View> views){
            this.views = views;
        }

        /**
         * 获得当前界面数
         */
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        /**
         * 初始化position位置的界面
         */
        @Override
           public  Object instantiateItem(ViewGroup container , int position){
            ((ViewPager) container).addView(views.get(position), 0);

            return views.get(position);


        }

        /**
         * 判断是否由对象生成界面
         */
        @Override
        public boolean isViewFromObject(View view, Object arg1) {
            return (view == arg1);
        }

        /**
         * 销毁position位置的界面
         */
        @Override
        public void destroyItem(ViewGroup  view, int position, Object arg2) {
            ((ViewPager) view).removeView(views.get(position));
        }

    }

        


    
  



    

