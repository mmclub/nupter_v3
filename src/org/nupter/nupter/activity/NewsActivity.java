package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v4.view.PagerTabStrip;
import android.widget.*;
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

/**
 * 校园新闻板块和教务公告板块的主界面
 *
 * @author panlei     e-mail:121531863@qq.com
 */
@SuppressLint({"ValidFragment", "NewApi"})
public class NewsActivity extends FragmentActivity {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    private RadioGroup myRadioGroup;
    private ViewPager vp;
    private RadioButton btn_0, btn_1;
    private ImageView imageView;
    private int screenWidth;
    private ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewpager);
        vp = (ViewPager) findViewById(R.id.viewPager);
        btn_0 = (RadioButton) findViewById(R.id.btn_0);
        btn_1 = (RadioButton) findViewById(R.id.btn_1);
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        imageView = (ImageView) findViewById(R.id.scrollImageView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, 5));
        myRadioGroup = (RadioGroup) findViewById(R.id.myRadiogroup);
        fragmentList.add(new NoticeFragment());
        fragmentList.add(new NewsFragment());

        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                fragmentList));

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (i2 != 0) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    params.setMargins(i2 / 2, 0, 0, 0);
                    imageView.setLayoutParams(params);
                }
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        btn_0.setChecked(true);
                        break;
                    case 1:
                        btn_1.setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_0) {
                    vp.setCurrentItem(0);
                } else if (checkedId == R.id.btn_1) {
                    vp.setCurrentItem(1);
                }
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;


        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;

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
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    public class NoticeFragment extends Fragment {
        //        private ProgressDialog progressDialog;
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
            }
            NewsActivity.this.setTitle("玩命加载中...");
            new Thread() {
                @Override
                public void run() {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(10);
                        sleep(500);
                        progressBar.setProgress(20);
                        sleep(500);
                        progressBar.setProgress(50);
                    } catch (Exception e) {
                    }
                }
            }.start();
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL_NOTICE, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    NoticeList(response);
                    noticeAdapter = new SimpleAdapter(NewsActivity.this, noticeList, R.layout.view_notice_news,
                            new String[]{"Title","Time"},
                            new int[]{R.id.Title,R.id.Time});
                    noticelistView.setAdapter(noticeAdapter);
                    NewsActivity.this.setTitle("南邮新闻");
                    progressBar.setVisibility(View.INVISIBLE);
                    noticelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        String str;
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                jsonObject = (JSONObject) jsonArray.get(position - 1);

                                str = jsonObject.getString("content");
                            } catch (JSONException e) {
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
//                    progressDialog.dismiss();
                    NewsActivity.this.setTitle("掌上南邮");
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "网络不给力啊...", Toast.LENGTH_SHORT).show();
                }

            });
            noticelistView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    noticeList = new ArrayList<HashMap<String, Object>>();
                    new AsyncHttpClient().get(URL_NOTICE, null, new AsyncHttpResponseHandler() {
                        public void onSuccess(String response) {

                            NoticeList(response);

                            noticeAdapter = new SimpleAdapter(NewsActivity.this, noticeList, R.layout.view_notice_news,
                                    new String[]{"Title","Time"},
                                    new int[]{R.id.Title,R.id.Time});
                            noticelistView.setAdapter(noticeAdapter);
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
                    noticeMap.put("Title", jsonObject.getString("title"));
                    noticeMap.put("Time",jsonObject.getString("time"));
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
                    newsAdapter = new SimpleAdapter(NewsActivity.this, newsList, R.layout.view_notice_news,
                            new String[]{"Title","Time"},
                            new int[]{R.id.Title,R.id.Time});
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
                    newsList = new ArrayList<HashMap<String, Object>>();
                    new AsyncHttpClient().get(URL_NEWS, null, new AsyncHttpResponseHandler() {
                        public void onSuccess(String response) {
                            NewsList(response);
                            newsAdapter = new SimpleAdapter(NewsActivity.this, newsList, R.layout.view_notice_news,
                                    new String[]{"Title","Time"},
                                    new int[]{R.id.Title,R.id.Time});
                            newslistView.setAdapter(newsAdapter);
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
                    newsMap.put("Time",jsonObject.getString("time"));
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