package org.nupter.nupter.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.nupter.nupter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 寻物平台
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 */
public class LostAndFoundActivity extends Activity {

    ViewPager mViewPager;
    View view1, view2, view3;
    PagerTitleStrip pagerTitleStrip;
    PagerTabStrip pagerTabStrip;
    List<View> viewList;
    List<String> titleList;
    Button button;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);
        initView();
    }

    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        //pagerTitleStrip = (PagerTitleStrip)findViewById(R.id.pagertitle);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextSpacing(50);
        //  pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.gold));
        //  pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.azure));


        view1 = findViewById(R.layout.view_lost);
        view2 = findViewById(R.layout.view_found);

        LayoutInflater layoutInflater = getLayoutInflater().from(this);
        view1 = layoutInflater.inflate(R.layout.view_lost, null);
        view2 = layoutInflater.inflate(R.layout.view_found, null);

        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);

        titleList = new ArrayList<String>();
        titleList.add("Lost");
        titleList.add("found");

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };

        mViewPager.setAdapter(pagerAdapter);


    }
}