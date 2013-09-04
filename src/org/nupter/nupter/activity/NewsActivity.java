package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v4.view.PagerTabStrip;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.umeng.analytics.MobclickAgent;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 校园新闻板块和教务公告板块的主界面
 *
 * @author panlei     e-mail:121531863@qq.com
 */
@SuppressLint({"ValidFragment", "NewApi"})
public class NewsActivity extends FragmentActivity {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewpager);
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        PagerTabStrip pts = (PagerTabStrip) findViewById(R.id.pagerTab);

        fragmentList.add(new NoticeFragment());
        fragmentList.add(new NewsFragment());
        titleList.add("教务公告");
        titleList.add("校园新闻");
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                fragmentList, titleList));
        pts.setTabIndicatorColorResource(R.color.blue);
        pts.setTextSpacing(50);
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
            return titleList.get(position);
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    public class NoticeFragment extends Fragment {
        private ProgressDialog progressDialog;
        private Intent intent;
        private String URL_NOTICE = "http://nuptapi.nupter.org/jwc/";
        private SimpleAdapter noticeAdapter;
        private PullToRefreshListView noticelistView = null;
        private ArrayList<HashMap<String, Object>> noticeList = null;
        private HashMap<String, Object> noticeMap;
        private JSONArray jsonArray;
        private JSONObject jsonObject;


        /**
         * 覆盖此函数，先通过inflater inflate函数得到view最后返回
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_news,
                    container, false);
            noticeList = new ArrayList<HashMap<String, Object>>();
            noticelistView = (PullToRefreshListView) v.findViewById(R.id.news_pull_refresh_list);
            /**
             * Add Sound Event Listener
             */
            SharedPreferences sharedPreferences = getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            boolean getSoundFlag = sharedPreferences.getBoolean("SoundFlag", true);
            if (getSoundFlag == true) {
                SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(getActivity());
                soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
                soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
                soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
                noticelistView.setOnPullEventListener(soundListener);
            } else {
            }
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("玩命加载ing");
            progressDialog.setMessage("别着急啊。。。");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL_NOTICE, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    NoticeList(response);
                    noticeAdapter = new SimpleAdapter(getActivity(), noticeList, R.layout.view_notice_news,
                            new String[]{"Title"},
                            new int[]{R.id.Title});
                    noticelistView.setAdapter(noticeAdapter);
                    progressDialog.dismiss();
                    noticelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        String str;

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //To change body of implemented methods use File | Settings | File Templates.

                            try {
                                jsonObject = (JSONObject) jsonArray.get(position - 1);

                                str = jsonObject.getString("content");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            intent = new Intent();
                            intent.putExtra("content", str);
                            intent.setClass(NewsActivity.this, NewsDetailActivity.class);
                            startActivity(intent);
                        }
                    });

                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "网络不给力啊...", Toast.LENGTH_SHORT).show();
                }

            });
            noticelistView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    noticeList.clear();
                    new AsyncHttpClient().get(URL_NOTICE, null, new AsyncHttpResponseHandler() {
                        public void onSuccess(String response) {

                            NoticeList(response);

                            noticeAdapter.notifyDataSetChanged();
                            noticelistView.onRefreshComplete();

                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            noticelistView.onRefreshComplete();
                            Toast.makeText(getActivity(), "网络不给力啊...", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            });
            return v;
        }

        public void NoticeList(String response) {
            try {
                jsonArray = new JSONObject(response).getJSONArray("array");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    noticeMap = new HashMap<String, Object>();
                    noticeMap.put("Title",
                            jsonObject.getString("title"));
                    noticeList.add(noticeMap);
                }
            } catch (Exception e) {

            }
        }
    }

    public class NewsFragment extends Fragment {
        private JSONObject jsonObject;
        private Intent intent;
        private String URL_NEWS = "http://nuptapi.nupter.org/news/";
        private SimpleAdapter newsAdapter;
        private PullToRefreshListView newslistView = null;

        private ArrayList<HashMap<String, Object>> newsList = null;
        private HashMap<String, Object> newsMap;
        private JSONArray jsonArray;


        /**
         * 覆盖此函数，先通过inflater inflate函数得到view最后返回
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_news,
                    container, false);
            newsList = new ArrayList<HashMap<String, Object>>();
            newslistView = (PullToRefreshListView) v.findViewById(R.id.news_pull_refresh_list);
            SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(getActivity());
            SharedPreferences sharedPreferences = getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            boolean getSoundFlag = sharedPreferences.getBoolean("SoundFlag", true);
            if (getSoundFlag == true) {

                soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
                soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
                soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
                newslistView.setOnPullEventListener(soundListener);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL_NEWS, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    NewsList(response);
                    newsAdapter = new SimpleAdapter(getActivity(), newsList, R.layout.view_notice_news,
                            new String[]{"Title"},
                            new int[]{R.id.Title});
                    newslistView.setAdapter(newsAdapter);

                    newslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        String str;

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //To change body of implemented methods use File | Settings | File Templates.
                            try {
                                jsonObject = (JSONObject) jsonArray.get(position - 1);
                                str = jsonObject.getString("content");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            intent = new Intent();
                            intent.putExtra("content", str);
                            intent.setClass(NewsActivity.this, NewsDetailActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    Toast.makeText(getActivity(), "网络不给力啊...", Toast.LENGTH_SHORT).show();
                }

            });
            newslistView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    // Do work to refresh the list here.
                    newsList.clear();
                    new AsyncHttpClient().get(URL_NEWS, null, new AsyncHttpResponseHandler() {
                        public void onSuccess(String response) {
                            NewsList(response);
                            newsAdapter.notifyDataSetChanged();
                            newslistView.onRefreshComplete();

                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            newslistView.onRefreshComplete();
                            Toast.makeText(getActivity(), "网络不给力啊...", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            });

            return v;
        }

        public void NewsList(String response) {
            try {
                jsonArray = new JSONObject(response).getJSONArray("array");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    newsMap = new HashMap<String, Object>();
                    newsMap.put("Title",
                            jsonObject.getString("title"));
                    newsList.add(newsMap);
                }
            } catch (Exception e) {

            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}