package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Log;

import java.io.IOException;

/**
 * 校园地图的列表
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 */


public class MapListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity);

        ListView mListView = (ListView) findViewById(R.id.listView);
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                Intent intent = new Intent(MapListActivity.this, demos[index].demoClass);
                MapListActivity.this.startActivity(intent);
            }
        });

    }


    private static final ActivityInfo[] demos = {
            new ActivityInfo(R.string.title_activity_baidumap, R.string.subtitle_activity_baidumap, BaiduMapActivity.class),
            new ActivityInfo(R.string.title_activity_image, R.string.subtitle_activity_image, TouchImageViewActivity.class),
            new ActivityInfo(R.string.title_activity_image2, R.string.subtitle_activity_image2, TouchImageActivity.class)

    };


    private class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(MapListActivity.this, R.layout.item_activty_info, null);
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