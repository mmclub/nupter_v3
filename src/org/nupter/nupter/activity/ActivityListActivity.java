package org.nupter.nupter.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.nupter.nupter.R;


/**
 * 所有Activity的列表
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 * 开发人员在完成一个Activity的雏形的时候，将其加入demos数组即可。
 */

public class ActivityListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity);

        ListView mListView = (ListView) findViewById(R.id.listView);
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                Intent intent = new Intent(ActivityListActivity.this, demos[index].demoClass);
                ActivityListActivity.this.startActivity(intent);
            }
        });

    }


    private static final ActivityInfo[] demos = {
            new ActivityInfo(R.string.title_activity_main, R.string.subtitle_activity_main, MainActivity.class),
            new ActivityInfo(R.string.title_activity_baidumap, R.string.subtitle_activity_baidumap, BaiduMapActivity.class),
            new ActivityInfo(R.string.title_activity_welcome, R.string.subtitle_activity_welcome, WelcomeActivity.class),
            new ActivityInfo(R.string.title_activity_settings, R.string.subtitle_activity_settings, SettingActivity.class),


    };


    private class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(ActivityListActivity.this, R.layout.item_activty_info, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            if (demos[index].demoClass == MainActivity.class
                    ) {
                title.setTextColor(Color.YELLOW);
                desc.setTextColor(Color.YELLOW);
            }
            title.setText(demos[index].title);
            desc.setText(demos[index].desc);
            return convertView;
        }

        @Override
        public int getCount() {
            return demos.length;
        }

        @Override
        public Object getItem(int index) {
            return demos[index];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }


    private static class ActivityInfo {
        private final int title;
        private final int desc;
        private final Class<? extends android.app.Activity> demoClass;

        public ActivityInfo(int title, int desc, Class<? extends android.app.Activity> demoClass) {
            this.title = title;
            this.desc = desc;
            this.demoClass = demoClass;
        }
    }
}