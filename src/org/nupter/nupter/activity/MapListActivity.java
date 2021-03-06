package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.R;

/**
 * 校园地图的列表
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */


public class MapListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity);
        ListView mListView = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.mButton);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.app_list_corner_round);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.login_edit);
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapListActivity.this, MapBaiduActivity.class);
                MapListActivity.this.startActivity(intent);
            }
        });
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                Intent intent = new Intent(MapListActivity.this, MapImageActivity.class);
                intent.putExtra(MapImageActivity.EXTRA_IMAGE_ID, images[index]);
                intent.putExtra(MapImageActivity.EXTRA_IMAGE_TITLE, demos[index].title);
                MapListActivity.this.startActivity(intent);
            }
        });


        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private static final ActivityInfo[] demos = {
            new ActivityInfo(R.string.title_activity_xianlin_area, R.string.subtitle_activity_xianlin_area),
            new ActivityInfo(R.string.title_activity_nupt_draw, R.string.subtitle_activity_nupt_draw),
            new ActivityInfo(R.string.title_activity_nupt_map, R.string.subtitle_activity_nupt_map)

    };

    private static final int[] images = {
            R.drawable.xianlin_area,
            R.drawable.nupt_draw,
            R.drawable.nupt_map
    };


    private class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(MapListActivity.this, R.layout.item_map_list, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText(demos[index].title);
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

        public ActivityInfo(int title, int desc) {
            this.title = title;
            this.desc = desc;
        }
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}