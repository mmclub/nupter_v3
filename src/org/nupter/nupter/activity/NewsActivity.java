package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nupter.nupter.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 校园新闻板块和教务公告板块的主界面
 *
 * 
 * @author panlei     e-mail:121531863@qq.com
 *
 */
@SuppressLint({ "ValidFragment", "NewApi" })
public class NewsActivity extends FragmentActivity {
    private final static int EDU_NOTICE = 1;
    private final static int SCH_NEWS = 2;

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewpager);
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        fragmentList.add(new NoticeAndNewsFragment(EDU_NOTICE));
        fragmentList.add(new NoticeAndNewsFragment(SCH_NEWS));
        titleList.add("教务公告");
        titleList.add("校园新闻");
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                fragmentList, titleList));

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,
                List<String> titleList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null
                    : fragmentList.get(arg0);
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

    public class NoticeAndNewsFragment extends Fragment {

        private JSONObject jsonObject;
        private int frameState;
        private Intent intent;
        private String URL_NEWS = "https://trello-attachments.s3.amazonaws.com/517694e75a3d555d0d000609/51f4bc8ddcd2956544001f62/416f3cd54f4c89af3abe42b64516344b/document_(1).json";
        private SimpleAdapter noticeAdapter;
        private ListView listView = null;
        private String myUrl = null;
        ArrayList<HashMap<String, Object>> noticeList = null;
        private HashMap<String, Object> noticeMap;
        private JSONArray jsonArray;

        public NoticeAndNewsFragment(int frameState) {
            super();
            this.frameState = frameState;

        }

        /**
         * 覆盖此函数，先通过inflater inflate函数得到view最后返回
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_news,
                    container, false);
            listView = (ListView) v.findViewById(R.id.newsListview);
            intent = new Intent();
            noticeList = new ArrayList<HashMap<String, Object>>();

            AsyncHttpClient client = new AsyncHttpClient();
            if (frameState == EDU_NOTICE) {
                myUrl = URL_NEWS;
            } else {
                myUrl = URL_NEWS;
            }
            Toast.makeText(getActivity(),"玩命加载中...",Toast.LENGTH_LONG).show();

            client.get(myUrl, null, new AsyncHttpResponseHandler() {

                public void onSuccess(String response) {

                    try {


                        jsonArray = new JSONObject(response)
                                .getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonObject = (JSONObject) jsonArray.get(i);
                            noticeMap = new HashMap<String, Object>();
                            
                            noticeMap.put("Title",
                                    jsonObject.getString("title"));
                            noticeMap.put("Time", 
                                    jsonObject.getString("time"));
                            noticeList.add(noticeMap);

                            noticeAdapter = new SimpleAdapter(getActivity(),
                                    noticeList, R.layout.view_notice_news,
                                    new String[] { "Title", "Time" },
                                    new int[] { R.id.Title, R.id.Time });
                            listView.setAdapter(noticeAdapter);

                            listView.setOnItemClickListener(new OnItemClickListener() {
                                private String str;
                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                        View arg1, int arg2, long arg3) {
                                    // TODO Auto-generated method stub
                                    try {
                                        jsonObject = (JSONObject)jsonArray.get(arg2);
                                        str = jsonObject.getString("content");
                                        Log.d("TAG", jsonObject.getString("content")); 
                                        
                                       
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    intent.putExtra("content",str);
                                            
                                    intent.setClass(NewsActivity.this, NewsDetailActivity.class);
                                    startActivity(intent);
                                } 

                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_LONG).show();
                }

            });

            return v;
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(1, 1, 1, "Refresh");
        item.setIcon(android.R.drawable.ic_menu_rotate);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            break;
        default:
            Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();

            break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
