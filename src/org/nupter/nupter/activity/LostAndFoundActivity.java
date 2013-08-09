package org.nupter.nupter.activity;


import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.nupter.nupter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 寻物平台
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
@SuppressLint({ "NewApi", "ValidFragment" })
public class LostAndFoundActivity extends FragmentActivity {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);

        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        fragmentList.add(new ViewPagerFragment(BookDataActivity.getLostList()));
        fragmentList.add(new ViewPagerFragment(BookDataActivity.getLostList()));
        fragmentList.add(new PublishFragment());
        titleList.add("寻物");
        titleList.add("招领");
        titleList.add("发布");
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));

        getActionBar().setDisplayHomeAsUpEnabled(true);


    }


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


    //相当于写一个展示列表的ListActivity
    public class PublishFragment extends Fragment {
        EditText infoEditText, publisherEditText, phoneEditText;
        String info, publisher, phone;
        Button publishButton;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_publishinfo, container, false);

            infoEditText = (EditText) v.findViewById(R.id.publishInfoEditText);
            publisherEditText = (EditText) v.findViewById(R.id.publisherEditText);
            phoneEditText = (EditText) v.findViewById(R.id.publishPhoneTextView);
            publishButton = (Button) v.findViewById(R.id.publishButton);

            publishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    info = infoEditText.getText().toString();
                    publisher = publisherEditText.getText().toString();
                    phone = phoneEditText.getText().toString();
                }
            });


            return v;
        }
    }

    public class ViewPagerFragment extends Fragment {

        private List<Map<String, String>> list;
        private ListView listView;

        public ViewPagerFragment(List<Map<String, String>> list) {
            super();
            this.list = list;
        }

        /**
         * 覆盖此函数，先通过inflater inflate函数得到view最后返回
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.view_pager_fragment, container, false);
            listView = (ListView) v.findViewById(R.id.showListView);
            SimpleAdapter adapter = new SimpleAdapter(LostAndFoundActivity.this, list,
                    R.layout.item_publish_info, new String[]{"lostName",
                    "owner", "phone"},
                    new int[]{R.id.lostNameTextView, R.id.ownerTextView,
                            R.id.publishPhoneTextView});
            listView.setAdapter(adapter);
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


}
