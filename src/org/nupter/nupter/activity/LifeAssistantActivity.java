package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.List;
import org.nupter.nupter.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;




/**
 *  生活小秘书板块主界面
 *
 *  @author panlei
*/



public class LifeAssistantActivity extends Activity {
    
    private ViewPager viewPager;
    private PagerTitleStrip pagerTitleStrip;
    private List<View> list;
    private List<String> titleList;
    
    
    @SuppressLint("NewApi")

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifeassistant);
        
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerTitleStrip = (PagerTitleStrip)findViewById(R.id.pagerTitle);
       
       View viewSchoolGuide = LayoutInflater.from(LifeAssistantActivity.this).inflate(R.layout.view_schoolguide, null);
       View viewBusRoute = LayoutInflater.from(LifeAssistantActivity.this).inflate(R.layout.view_busroute, null);
       View viewLocalInformation = LayoutInflater.from(LifeAssistantActivity.this).inflate(R.layout.view_localinformation, null);
       View viewRestTime = LayoutInflater.from(LifeAssistantActivity.this).inflate(R.layout.view_resttime, null);

        
       list = new ArrayList<View>();
       list.add(viewSchoolGuide);
       list.add(viewBusRoute);
       list.add(viewLocalInformation);
       list.add(viewRestTime);
       
       
       titleList = new ArrayList<String>();
       titleList.add("校园指南");
       titleList.add("公交出行");
       titleList.add("周边信息");
       titleList.add("作息时间");
       
            
       viewPager.setAdapter(new MyPaperAdapter());
       this.getActionBar().setDisplayHomeAsUpEnabled(true);
       
      
            
      }
    
    
    class MyPaperAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }
        
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager)container).removeView(list.get(position));
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titleList.get(position);
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager)container).addView(list.get(position));
            return list.get(position);
        }
        
        

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        
    }
    
    
    
    
  
        
        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                case android.R.id.home:
                    onBackPressed();
                    break;

                default:
                    return super.onOptionsItemSelected(item);
            }
            return true;
        }


    

}