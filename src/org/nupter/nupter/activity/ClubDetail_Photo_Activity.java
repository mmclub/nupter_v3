package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;


/**
 * 从网络加载图片演示
 *
 * @author sudongsheng
 *         <p/>
 *         参见 https://github.com/nostra13/Android-Universal-Image-Loader
 */
public class ClubDetail_Photo_Activity extends Activity {
    private Intent intent;
    private String get_url_photo;
    private PullToRefreshGridView mGridView;
    private MyAdapter myAdapter;
    private ProgressDialog progressDialog;
    private int page, before_page = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubdetail_photo);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        get_url_photo = intent.getStringExtra("get_url_photo");
        mGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid_photo);
        mGridView.setOnItemClickListener(onItemClickListener);
        /**
         * Add Sound Event Listener
         */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String rawString = preferences.getString("SoundFlag", "null");
        if (rawString.equals("")) {
            SoundPullEventListener<GridView> soundListener = new SoundPullEventListener<GridView>(this);
            soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
            soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
            soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
            mGridView.setOnPullEventListener(soundListener);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("努力加载ing");
        progressDialog.setMessage("人人网API调皮了。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new AsyncHttpClient().post(get_url_photo, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        myAdapter = new MyAdapter(ClubDetail_Photo_Activity.this, response);
                        mGridView.setAdapter(myAdapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        Toast.makeText(ClubDetail_Photo_Activity.this, "获取人人数据失败", Toast.LENGTH_LONG).show();
                    }
                });
        // Set a listener to be invoked when the list should be refreshed.
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mGridView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = myAdapter.getCount() / 12 + 1;
                if (page != (before_page + 1)) {
                    page++;
                }
                String url_update = get_url_photo + "&page=" + page;
                new AsyncHttpClient().post(url_update, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                if (response.equals("[]")) {
                                    Toast.makeText(ClubDetail_Photo_Activity.this, "木有更多了。。。亲", Toast.LENGTH_LONG).show();
                                } else {
                                    myAdapter.Update(response);
                                    myAdapter.notifyDataSetChanged();
                                }
                                mGridView.onRefreshComplete();
                            }

                            @Override
                            public void onFailure(Throwable throwable, String s) {
                                Toast.makeText(ClubDetail_Photo_Activity.this, "获取人人数据失败", Toast.LENGTH_LONG).show();
                                mGridView.onRefreshComplete();
                            }
                        });
                before_page = page;
            }

        });
    }

    class ViewHolder {
        public ImageView image;
    }

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
            // TODO Auto-generated method stub
            try {
                return jsonArray.length();
            } catch (Exception e) {
                return 0;
            }
        }

        @Override
        public String getItem(int arg0) {
            // TODO Auto-generated method stub
            try {
                return jsonArray.getJSONObject(arg0).getString("url_large");
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
                convertView = inflater.inflate(R.layout.view_clubdetail_photo_image, null);
                holder.image = (ImageView) convertView.findViewById(R.id.ImageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                jsonObject = jsonArray.getJSONObject(position);
                ImageLoader.getInstance().displayImage(jsonObject.getString("url_main"), holder.image);
            } catch (Exception e) {
            }
            return convertView;
        }
    }

    private GridView.OnItemClickListener onItemClickListener = new GridView.OnItemClickListener() {
        private String large_url;

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            large_url = myAdapter.getItem(position);
            ImageView img = new ImageView(ClubDetail_Photo_Activity.this);
            ImageLoader.getInstance().displayImage(large_url, img);

            new AlertDialog.Builder(ClubDetail_Photo_Activity.this)
                    .setView(img)
                    .show();
        }
    };

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