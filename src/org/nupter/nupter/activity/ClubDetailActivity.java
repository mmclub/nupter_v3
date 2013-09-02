package org.nupter.nupter.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 社团模块 二级菜单
 *
 * @author sudongsheng
 */
public class ClubDetailActivity extends FragmentActivity {
    private final static int status = 1;
    private final static int blog = 2;
    public Intent intent;
    public int page_id;
    private int position;
    private String[] clubName = {"南京移动互联网开发者俱乐部", "青春南邮", "校学生会", "社团联合会", "校科学技术协会",
            "青志联", "南京邮电大学校研究生会", "南邮之声", "南邮青年", "南邮大艺团办公室", "南邮红会", "学通社"};
    private int[] clubId = {601415670, 600907477, 601017224, 600889745, 601003549, 600490284, 601052072, 601534154, 600989734, 601482336, 601593100, 601317519};
    private int[] clubImage = {R.drawable.shetuan_nyydhlwkfzjlb, R.drawable.shetuan_qcny, R.drawable.shetuan_xxsh, R.drawable.shetuan_sl, R.drawable.shetuan_xkx, R.drawable.shetuan_qzl, R.drawable.shetuan_njyddxxyjsh, R.drawable.shetuan_nyzs, R.drawable.shetuan_nyqn, R.drawable.shetuan_nydyt, R.drawable.shetuan_nyhh, R.drawable.shetuan_xts};
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();
    private boolean rawString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubdetail);
        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        page_id = clubId[position];
        this.setTitle(clubName[position]);
        ViewPager vp = (ViewPager) findViewById(R.id.mViewPager);
        titleList.add("状态");
        titleList.add("日志");
        titleList.add("相册");
        fragmentList.add(new StatusAndBlogFragment("status.gets", status));
        fragmentList.add(new StatusAndBlogFragment("blog.gets", blog));
        fragmentList.add(new PhotosFragment());
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
        PagerTabStrip pts = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        pts.setDrawFullUnderline(true);
        pts.setTextSpacing(50);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        rawString = preferences.getBoolean("SoundFlag",true);
}

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.size() > position) ? titleList.get(position) : "";
        }


        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    public class StatusAndBlogFragment extends Fragment implements AbsListView.OnScrollListener{

        private ProgressDialog progressDialog;
        private JSONArray jsonArray;
        private JSONObject jsonObject;
        private int frameState;
        private String url = "https://api.renren.com/restserver.do?call_id=204763&" +
                "api_key=e4e12cd61ab542f3a6e45fee619c46f3&secret_key=1e7a17db78e74ed6964601ab89ea6444&" +
                "format=json&count=10&v=1.0";
        private ListView listView = null;
        private SimpleAdapter adapter = null;
        ArrayList<HashMap<String, Object>> msg;
        private HashMap<String, Object> map;
        private int img;
        private int lastItem;
        private int scrollState;

        public StatusAndBlogFragment(String text, int frameState) {
            super();
            this.url = this.url + "&method=" + text + "&page_id=" + page_id + "&page=1";
            this.frameState = frameState;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_status_blog_fragment, container, false);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("努力加载ing");
            progressDialog.setMessage("人人网API调皮了。。。");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            listView = (ListView) v.findViewById(R.id.fragment_listView);
            msg = new ArrayList<HashMap<String, Object>>();
            img = clubImage[position];
            new AsyncHttpClient().post(url, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            msg(response);
                            adapter = new SimpleAdapter(getActivity(), msg, R.layout.view_club_listview,
                                    new String[]{"img", "msg", "time"},
                                    new int[]{R.id.headimg, R.id.msg, R.id.time});
                            listView.setAdapter(adapter);
                            listView.setOnScrollListener(StatusAndBlogFragment.this);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            Toast.makeText(getActivity(), "获取人人数据失败", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
            return v;
        }

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            this.scrollState = i;
            if (lastItem >= adapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                url = url.substring(0, url.length() - 1) + (adapter.getCount() / 10 + 1);
                new AsyncHttpClient().post(url, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                msg(response);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Throwable throwable, String s) {
                                Toast.makeText(getActivity(), "获取人人数据失败", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            lastItem = i + i2;
        }

        public void msg(String response) {
            try {
                if (frameState == status)
                    jsonArray = new JSONArray(response);
                else
                    jsonArray = new JSONObject(response).getJSONArray("blogs");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    map = new HashMap<String, Object>();
                    map.put("img", img);
                    if (frameState == status)
                        map.put("msg", jsonObject.getString("message"));
                    else {
                        map.put("msg", jsonObject.getString("content"));
                    }
                    map.put("time", jsonObject.getString("time"));
                    msg.add(map);
                }
            } catch (Exception e) {
            }
        }
    }

    public class PhotosFragment extends Fragment {
        private String url = "https://api.renren.com/restserver.do?call_id=204763&" +
                "api_key=e4e12cd61ab542f3a6e45fee619c46f3&secret_key=1e7a17db78e74ed6964601ab89ea6444&" +
                "format=json&count=12&v=1.0";
        private MyAdapter adapter;
        private ProgressDialog progressDialog;
        private PullToRefreshGridView mPullRefreshGridView;
        private int page, before_page = 1;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_photos_fragment, container, false);
            url = url + "&page_id=" + page_id + "&method=photos.getAlbums";
            mPullRefreshGridView = (PullToRefreshGridView) v.findViewById(R.id.pull_refresh_grid);
            mPullRefreshGridView.setOnItemClickListener(onItemClickListener);
            /**
             * Add Sound Event Listener
             */
            if (rawString) {
                SoundPullEventListener<GridView> soundListener = new SoundPullEventListener<GridView>(getActivity());
                soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
                soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
                soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
                mPullRefreshGridView.setOnPullEventListener(soundListener);
            }
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("努力加载ing");
            progressDialog.setMessage("人人网API调皮了。。。");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            if (!NetUtils.isNewworkConnected()) {
                mPullRefreshGridView.setPullToRefreshEnabled(false);
            }
            new AsyncHttpClient().post(url, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            adapter = new MyAdapter(getActivity(), response);
                            mPullRefreshGridView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            Toast.makeText(getActivity(), "获取人人相片失败，请检查网络", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
            // Set a listener to be invoked when the list should be refreshed.
            mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                    mPullRefreshGridView.onRefreshComplete();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                    page = adapter.getCount() / 12 + 1;
                    if (page != (before_page + 1)) {
                        page++;
                    }
                    String url_update = url + "&page=" + page;
                    new AsyncHttpClient().post(url_update, null,
                            new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(String response) {
                                    if (response.equals("[]")) {
                                        Toast.makeText(getActivity(), "木有更多了。。。亲", Toast.LENGTH_LONG).show();
                                    } else {
                                        adapter.Update(response);
                                        adapter.notifyDataSetChanged();
                                    }
                                    mPullRefreshGridView.onRefreshComplete();
                                }

                                @Override
                                public void onFailure(Throwable throwable, String s) {
                                    Toast.makeText(getActivity(), "获取人人相片失败，请检查网络", Toast.LENGTH_LONG).show();
                                    mPullRefreshGridView.onRefreshComplete();
                                }
                            });
                    before_page = page;
                }

            });
            return v;
        }

        class ViewHolder {
            public ImageView image;
            public TextView name;
            public TextView size;

        }

        private GridView.OnItemClickListener onItemClickListener = new GridView.OnItemClickListener() {
            private String get_url_photo;
            private String aid;

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aid = adapter.getItem(position);
                get_url_photo = url.substring(0, url.length() - 6) + "&aid=" + aid;
                Intent photo_intent = new Intent();
                photo_intent.putExtra("get_url_photo", get_url_photo);
                photo_intent.setClass(ClubDetailActivity.this, ClubDetailPhotoActivity.class);
                startActivity(photo_intent);
            }
        };

        public class MyAdapter extends BaseAdapter {

            private LayoutInflater inflater;
            private JSONArray jsonArray, jsonArray2;
            private JSONObject jsonObject;


            public MyAdapter(Context context, String response) {
                this.inflater = LayoutInflater.from(context);
                try {
                    jsonArray = new JSONArray(response);
                } catch (Exception e) {
                }
            }

            public void Update(String result) {
                try {
                    jsonArray2 = new JSONArray(result);
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        jsonArray.put(jsonArray2.getJSONObject(i));
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public int getCount() {
                try {
                    return jsonArray.length();
                } catch (Exception e) {
                    return 0;
                }
            }

            @Override
            public String getItem(int arg0) {
                try {
                    return jsonArray.getJSONObject(arg0).getString("aid");
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public long getItemId(int arg0) {
                return arg0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder holder = null;
                if (convertView == null) {

                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.view_photos_image_fragment, null);
                    holder.image = (ImageView) convertView.findViewById(R.id.AlbumsImage);
                    holder.name = (TextView) convertView.findViewById(R.id.AlbumsName);
                    holder.size = (TextView) convertView.findViewById(R.id.AlbumsSize);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                try {
                    jsonObject = jsonArray.getJSONObject(position);
                    ImageLoader.getInstance().displayImage(jsonObject.getString("url"), holder.image);
                    holder.name.setText(jsonObject.getString("name"));
                    holder.size.setText(jsonObject.getString("size") + "张相片");
                } catch (Exception e) {
                }
                return convertView;
            }


        }

    }

    @Override
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
}