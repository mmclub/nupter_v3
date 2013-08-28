package org.nupter.nupter.activity;


import android.annotation.SuppressLint;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onStart() {
        super.onStart();
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        fragmentList.add(new LostInfoFragment());
        fragmentList.add(new PublishFragment());
        titleList.add("瞄一眼");
        titleList.add("发布");
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //三个Fragment滑动的Adapter
    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
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


    //发布信息的Fragment
    public class PublishFragment extends Fragment {
        EditText infoEditText, publisherEditText, phoneEditText;
        String contnet, publisher, phone, url, timeStamp;
        Calendar calendar = Calendar.getInstance();
        Date a = new Date();

        Button publishButton;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_publishinfo, container, false);

            infoEditText = (EditText) v.findViewById(R.id.publishInfoEditText);
            publisherEditText = (EditText) v.findViewById(R.id.publisherEditText);
            phoneEditText = (EditText) v.findViewById(R.id.publishPhoneEditText);
            publishButton = (Button) v.findViewById(R.id.publishButton);

            publishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contnet = infoEditText.getText().toString();
                    publisher = publisherEditText.getText().toString();
                    phone = phoneEditText.getText().toString();
                    timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
                    Log.d("Calendar_test", timeStamp);
                    url = "http://nuptapi.nupter.org/lost/new";
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("time", timeStamp);
                    params.put("title", "");
                    params.put("content", contnet);
                    params.put("url", "");
                    params.put("key", "llpzqxh");
                    if (NetUtils.isNewworkConnected()) {
                        client.get("http://nuptapi.nupter.org/lost/new", params, new AsyncHttpResponseHandler() {
                            public void onSuccess(String response) {
                                Log.d("RE_lost", response);
                                if (response.contains("ok")) {

                                    Toast toast = Toast.makeText(LostAndFoundActivity.this, "OK了", Toast.LENGTH_SHORT);
                                    toast.show();

                                } else {
                                    Toast toast = Toast.makeText(LostAndFoundActivity.this, response, 1000);
                                    toast.show();
                                }

                            }


                        });
                    } else {
                        Toast toast = Toast.makeText(LostAndFoundActivity.this, "网络还没连接哦", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            });


            return v;
        }
    }

    //展示寻物的Fragment
    public class LostInfoFragment extends Fragment {

        private List<String> lostList;
        private ListView listView;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_lost, container, false);
            listView = (ListView) v.findViewById(R.id.lostListView);
            lostURL = "https://api.renren.com/restserver.do?call_id=204763&api_key=e4e12cd61ab542f3a6e45fee619c46f3&secret_key=1e7a17db78e74ed6964601ab89ea6444&format=json&count=10&v=1.0&method=status.gets&page_id=601408737&page=1";
            lostList = new ArrayList<String>();
            AsyncHttpClient client = new AsyncHttpClient();
            if (NetUtils.isNewworkConnected()) {
                client.post(lostURL, null, new AsyncHttpResponseHandler() {
                    public void onSuccess(String response) {
                        try {
                            Toast toast = Toast.makeText(LostAndFoundActivity.this, "努力加载ing", Toast.LENGTH_SHORT);
                            toast.show();
                            Log.d("lostAPI", response);
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++)
                                lostList.add(jsonArray.getJSONObject(i).getString("message"));
                            Log.d("lostList",lostList.toString());
                            listView.setAdapter(new ArrayAdapter<String>(LostAndFoundActivity.this, R.layout.item_lost, lostList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    public void onFailure(Throwable throwable, String s) {
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

    //展示招领的Fragment
    public class FoundInfoFragment extends Fragment {

        private List<String> foundList;
        private ListView listView;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_pager_fragment, container, false);
            listView = (ListView) v.findViewById(R.id.textView);
            foundList = new ArrayList<String>();
            foundURL = "http://nuptapi.nupter.org/lost/";
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(foundURL, null, new AsyncHttpResponseHandler() {
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            foundList.add(jsonArray.getString(i));
                        }
                        Log.d("Test_json", foundList.toString());
                        //listView.setAdapter(new lostListAdapter(LostAndFoundActivity.this));
                        listView.setAdapter(new ArrayAdapter<String>(LostAndFoundActivity.this, R.layout.view_found, foundList));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                public void onFailure(Throwable throwable, String s) {

                }
            });


//            SimpleAdapter adapter = new SimpleAdapter(LostAndFoundActivity.this, list,
//                    R.layout.item_publish_info, new String[]{"lostName",
//                    "owner", "phone"},
//                    new int[]{R.id.lostNameTextView, R.id.ownerTextView,
//                            R.id.publishPhoneTextView});
            return v;
        }
//        public final class LostViewHolder {
//            public TextView title;
//        }
//        private class lostListAdapter extends BaseAdapter {
//            private LayoutInflater lostInflater;
//
//            public lostListAdapter(Context context) {
//                this.lostInflater = LayoutInflater.from(context);
//            }
//
//            @Override
//            public int getCount() {
//                return lostList.size();  //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public Object getItem(int i) {
//                return lostList.get(i);  //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public long getItemId(int i) {
//                return 0;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup viewGroup) {
//                LostViewHolder holder = null;
//                if (convertView == null) {
//
//                    holder = new LostViewHolder();
//
//                    convertView = lostInflater.inflate(R.layout.view_lost, null);
//                    holder.title = (TextView) convertView.findViewById(R.id.TextView);
//                    convertView.setTag(holder);
//
//                } else {
//
//                    holder = (LostViewHolder) convertView.getTag();
//                }
//                holder.title.setText(lostList.get(position));
//
//                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//        }
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
