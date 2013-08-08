package org.nupter.nupter.activity;


import android.content.Intent;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.nupter.nupter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 社团模块 二级菜单
 *
 * @author sudongsheng
 *
 */
public class ClubDetailActivity extends FragmentActivity {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);

        ViewPager vp = (ViewPager)findViewById(R.id.viewPager);
        titleList.add("状态");
        titleList.add("日志");
        titleList.add("相册");
        fragmentList.add(new ViewPagerFragment("status.gets"));
        fragmentList.add(new ViewPagerFragment("blog.gets"));
        fragmentList.add(new ViewPagerFragment("photos.getAlbums"));
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

        private Intent intent;
        private Long page_id;
        private String url = "https://api.renren.com/restserver.do?call_id=204763&" +
                "api_key=e4e12cd61ab542f3a6e45fee619c46f3&secret_key=1e7a17db78e74ed6964601ab89ea6444&" +
                "format=json&count=10&v=1.0&count=10&page=1";
        private String   text;
        private TextView tv = null;

        public ViewPagerFragment(String text){
            super();
            intent=getIntent();
            page_id=intent.getLongExtra("page_id",0);
            this.url = this.url+"&method="+text+"&page_id="+page_id;
        }

        /**
         * 覆盖此函数，先通过inflater inflate函数得到view最后返回
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_pager_fragment, container, false);
            tv = (TextView)v.findViewById(R.id.textView);
            new AsyncHttpClient().post(url, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            tv.setText(response);
 /*                       try {
                            jsonArray = new JSONArray(response);
                        } catch (Exception e) {
                        }   */
                        }
                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            //jsonArray = "获取失败";
                        }
                    });
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
