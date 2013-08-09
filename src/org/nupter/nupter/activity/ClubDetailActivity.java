package org.nupter.nupter.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nupter.nupter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 社团模块 二级菜单
 * 
 * @author sudongsheng
 */
@SuppressLint({ "ValidFragment", "NewApi" })
public class ClubDetailActivity extends FragmentActivity {
    private final static int status = 1;
    private final static int blog = 2;
    public Intent intent;
    public Long page_id;
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);

        intent = getIntent();
        page_id = intent.getLongExtra("page_id", 0);
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        titleList.add("状态");
        titleList.add("日志");
        titleList.add("相册");
        fragmentList.add(new StatusAndBlogFragment("status.gets", status));
        fragmentList.add(new StatusAndBlogFragment("blog.gets", blog));
        fragmentList.add(new PhotosFragment());
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

    public class StatusAndBlogFragment extends Fragment implements
            AbsListView.OnScrollListener {

        private ProgressDialog progressDialog;
        private JSONArray jsonArray;
        private JSONObject jsonObject;
        private int frameState;
        private int lastItem;
        private int scrollState;
        private String url = "https://api.renren.com/restserver.do?call_id=204763&"
                + "api_key=e4e12cd61ab542f3a6e45fee619c46f3&secret_key=1e7a17db78e74ed6964601ab89ea6444&"
                + "format=json&count=10&v=1.0";
        private ListView listView = null;
        private SimpleAdapter adapter = null;
        ArrayList<HashMap<String, Object>> msg;
        private HashMap<String, Object> map;
        private int img;

        public StatusAndBlogFragment(String text, int frameState) {
            super();
            this.url = this.url + "&method=" + text + "&page_id=" + page_id
                    + "&page=1";
            this.frameState = frameState;
        }

        /**
         * 覆盖此函数，先通过inflater inflate函数得到view最后返回
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_status_blog_fragment,
                    container, false);
            listView = (ListView) v.findViewById(R.id.listview);
            listView.setOnScrollListener(this);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("努力加载ing");
            progressDialog.setMessage("人人网API调皮了。。。");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            msg = new ArrayList<HashMap<String, Object>>();

            setimg();
            new AsyncHttpClient().post(url, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            msg(response);
                            adapter = new SimpleAdapter(getActivity(), msg,
                                    R.layout.view_club_listview, new String[] {
                                            "img", "msg", "time" }, new int[] {
                                            R.id.headimg, R.id.msg, R.id.time });
                            listView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            Toast.makeText(getActivity(), "获取人人数据失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            return v;
        }

        private void setimg() {
            if (page_id == 600490284)
                img = R.drawable.shetuan_xxsh;
            else if (page_id == 600889745)
                img = R.drawable.shetuan_sl;
            else if (page_id == 601003549)
                img = R.drawable.shetuan_qzl;
            else if (page_id == 601017224)
                img = R.drawable.shetuan_xkx;
            else if (page_id == 600907477)
                img = R.drawable.shetuan_qcny;
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            lastItem = i + i2;

        }

        @Override
        public void onScrollStateChanged(AbsListView absListView,
                int scrollState) {
            this.scrollState = scrollState;
            if (lastItem >= adapter.getCount()
                    && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                url = url.substring(0, url.length() - 1)
                        + (adapter.getCount() / 10 + 1);
                new AsyncHttpClient().post(url, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                msg(response);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Throwable throwable, String s) {
                                Toast.makeText(getActivity(), "获取人人数据失败",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
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
        private String url = "https://api.renren.com/restserver.do?call_id=204763&"
                + "api_key=e4e12cd61ab542f3a6e45fee619c46f3&secret_key=1e7a17db78e74ed6964601ab89ea6444&"
                + "format=json&count=18&v=1.0";
        private GridView gridView;
        private MyAdapter adapter;
        private ProgressDialog progressDialog;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_photos_fragment, container,
                    false);
            url = url + "&page_id=" + page_id + "&method=photos.getAlbums";
            gridView = (GridView) v.findViewById(R.id.gridView);
            gridView.setOnItemClickListener(onItemClickListener);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("努力加载ing");
            progressDialog.setMessage("人人网API调皮了。。。");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            new AsyncHttpClient().post(url, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            adapter = new MyAdapter(getActivity(), response);
                            gridView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            Toast.makeText(getActivity(), "获取人人数据失败",
                                    Toast.LENGTH_LONG).show();
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

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                aid = adapter.getItem(position);
                get_url_photo = url.substring(0, url.length() - 6) + "&aid="
                        + aid;
                Intent photo_intent = new Intent();
                photo_intent.putExtra("get_url_photo", get_url_photo);
                photo_intent.setClass(ClubDetailActivity.this,
                        ClubDetail_Photo_Activity.class);
                startActivity(photo_intent);
            }
        };

        public class MyAdapter extends BaseAdapter {

            private LayoutInflater inflater;
            private JSONArray jsonArray;
            private JSONObject jsonObject;

            public MyAdapter(Context context, String response) {
                this.inflater = LayoutInflater.from(context);
                try {
                    jsonArray = new JSONArray(response);
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
                    convertView = inflater.inflate(
                            R.layout.view_photos_image_fragment, null);
                    holder.image = (ImageView) convertView
                            .findViewById(R.id.AlbumsImage);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.AlbumsName);
                    holder.size = (TextView) convertView
                            .findViewById(R.id.AlbumsSize);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                try {
                    jsonObject = jsonArray.getJSONObject(position);
                    ImageLoader.getInstance().displayImage(
                            jsonObject.getString("url"), holder.image);
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
