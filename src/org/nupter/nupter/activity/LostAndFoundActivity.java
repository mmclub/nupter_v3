package org.nupter.nupter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.nupter.nupter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 寻物平台
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 */
public class LostAndFoundActivity extends FragmentActivity {

   List<Fragment> fragmentList = new ArrayList<Fragment>();
   List<String> titleList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);

        ViewPager vp = (ViewPager)findViewById(R.id.viewPager);
        fragmentList.add(new ViewPagerFragment("Page1"));
        fragmentList.add(new ViewPagerFragment("Page2"));
        titleList.add("title 1");
        titleList.add("title 2");
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));

        getActionBar().setDisplayHomeAsUpEnabled(true);


    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String>   titleList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList){
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.size() > position) ? titleList.get(position) : "";
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }





    public class ViewPagerFragment extends Fragment {

        private String   text;
        private TextView tv = null;

        public ViewPagerFragment(String text){
            super();
            this.text = text;
        }

        /**
         * 覆盖此函数，先通过inflater inflate函数得到view最后返回
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_pager_fragment, container, false);
            tv = (TextView)v.findViewById(R.id.textView);
            tv.setText(text);
            return v;
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
