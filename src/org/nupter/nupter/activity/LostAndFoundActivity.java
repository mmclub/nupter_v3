package org.nupter.nupter.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.protocol.ResponseDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.NetUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 寻物平台
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */

@SuppressLint({"NewApi", "ValidFragment"})

public class LostAndFoundActivity extends FragmentActivity {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();
    private JSONArray jsonArray;
    private String lostURL, foundURL;
    ArrayAdapter adapter;
    private RadioGroup myRadioGroup;
    private ViewPager vp;
    private RadioButton btn_0,btn_1;
    private ImageView imageView;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentList.add(new LostInfoFragment());
        fragmentList.add(new PublishFragment());
        myRadioGroup = (RadioGroup)findViewById(R.id.myRadiogroup);
        vp = (ViewPager) findViewById(R.id.viewPager);
        btn_0 = (RadioButton)findViewById(R.id.btn_0);
        btn_1 = (RadioButton)findViewById(R.id.btn_1);
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        imageView=(ImageView)findViewById(R.id.scrollImageView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, 5));
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList));
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (i2!=0) {
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
                if (checkedId ==R.id.btn_0){
                    vp.setCurrentItem(0);
                }
                else if (checkedId == R.id.btn_1){
                    vp.setCurrentItem(1);
                }
            }
        });
    }

    //过滤人人客户端
    public Intent findRenrenClient() {
        final String renrenApps = "com.renren.mobile.android";
        Intent tweetIntent = new Intent();
        tweetIntent.setType("text/plain");
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : list) {
            String p = resolveInfo.activityInfo.packageName;
            if (p != null && p.startsWith(renrenApps)) {
                tweetIntent.setPackage(p);
                return tweetIntent;
            } else {
                Toast toast = Toast.makeText(LostAndFoundActivity.this, "您的手机上没有人人，不能发布", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        return null;

    }


    //三个Fragment滑动的Adapter
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


    //发布信息的Fragment
    class PublishFragment extends Fragment {
        EditText infoEditText, publisherEditText, phoneEditText;
        String contnet, publisher, phone, url, timeStamp;
        Calendar calendar = Calendar.getInstance();
        Date a = new Date();

        Button publishButton;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_publishinfo, container, false);

            infoEditText = (EditText) v.findViewById(R.id.publishInfoEditText);

            publishButton = (Button) v.findViewById(R.id.publishButton);

            publishButton.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    contnet = infoEditText.getText().toString();
                    timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
                    Log.d("Calendar_test", timeStamp);
                    url = "http://nuptapi.nupter.org/lost/new/";
                    RequestParams params = new RequestParams();
                    params.put("content", contnet);
                    params.put("time", timeStamp);
                    params.put("title", "");
                    params.put("key", "llpzqxh");
                    params.put("url", "");
                    Log.d("Calendar_test", contnet);
                    if (contnet.isEmpty()) {
                        Toast.makeText(LostAndFoundActivity.this, "还没填写内容哦", Toast.LENGTH_SHORT).show();
                    } else {
                        if (NetUtils.isNewworkConnected()) {
                            new AsyncHttpClient().get(url, params,
                                    new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(String response) {
                                            try {
                                                Log.d("Calendar_test", response);
                                                if (response.matches("(.*)ok(.*)")) {
                                                    Toast.makeText(LostAndFoundActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(LostAndFoundActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(LostAndFoundActivity.this, "发送失败，请检查网络", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else

                        {
                            Toast.makeText(LostAndFoundActivity.this, "网络还没连接哦", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            );


            return v;
        }
    }

    //展示寻物的Fragment
    public class LostInfoFragment extends Fragment {

        private List<String> lostList;
        private PullToRefreshListView listView;



        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LostAndFoundActivity.this.setTitle("玩命加载中...");
            lostURL = "https://api.renren.com/restserver.do?call_id=204763&api_key=e4e12cd61ab542f3a6e45fee619c46f3&secret_key=1e7a17db78e74ed6964601ab89ea6444&format=json&count=10&v=1.0&method=status.gets&page_id=601408737&page=1";
            View v = inflater.inflate(R.layout.view_lost, container, false);
            listView = (PullToRefreshListView) v.findViewById(R.id.lostListView);
            listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                @Override
                public void onLastItemVisible() {
                    lostURL = lostURL.substring(0, lostURL.length() - 1) + (adapter.getCount() / 10 + 1);

                    new AsyncHttpClient().post(lostURL, null,
                            new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(String response) {
                                    try {
                                        jsonArray = new JSONArray(response);
                                        for (int i = 0; i < jsonArray.length(); i++)
                                            lostList.add(jsonArray.getJSONObject(i).getString("message"));
                                        LostAndFoundActivity.this.setTitle("寻物平台");
                                    } catch (Exception e) {

                                    }
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onFailure(Throwable throwable, String s) {
                                    Toast.makeText(getActivity(), "获取人人数据失败", Toast.LENGTH_LONG).show();
                                    LostAndFoundActivity.this.setTitle("寻物平台");
                                }
                            });
                }
            });

            lostList = new ArrayList<String>();
            AsyncHttpClient client = new AsyncHttpClient();
            if (NetUtils.isNewworkConnected()) {
                client.post(lostURL, null, new AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        try {
                            Log.d("lostAPI", response);
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++)
                                lostList.add(jsonArray.getJSONObject(i).getString("message"));
                            Log.d("lostList", lostList.toString());
                            adapter = new ArrayAdapter<String>(LostAndFoundActivity.this, R.layout.item_lost, lostList);
                            listView.setAdapter(adapter);
                            LostAndFoundActivity.this.setTitle("寻物平台");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFailure(Throwable throwable, String s) {
                        LostAndFoundActivity.this.setTitle("寻物平台");
                        Toast toast = Toast.makeText(LostAndFoundActivity.this, "服务器歇菜了", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            } else {
                Toast toast = Toast.makeText(LostAndFoundActivity.this, "网络没有连接啊", Toast.LENGTH_SHORT);
                toast.show();
            }

//            SimpleAdapter adapter = new SimpleAdapter(LostAndFoundActivity.this, list,
//                    R.layout.item_publish_info, new String[]{"lostName",
//                    "owner", "phone"},
//                    new int[]{R.id.lostNameTextView, R.id.ownerTextView,
//                            R.id.publishPhoneTextView});
            return v;
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
