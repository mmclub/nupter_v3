package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.CornerListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置板块主界面
 * @author panlei
 *
 */
public class SettingActivity extends Activity {
    private CornerListView cornerListView = null;

    private List<Map<String, String>> listData = null;
    private SimpleAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cornerListView = (CornerListView)findViewById(R.id.settinglistview);
        setListData();

        adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.view_tab_setting_list_item,
                new String[]{"text"}, new int[]{R.id.setting_list_item_text});
        cornerListView.setAdapter(adapter);

         getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListData(){
        listData = new ArrayList<Map<String,String>>();

        Map<String,String> map = new HashMap<String, String>();
        map.put("text", "帐号设置");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "分享");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "关于");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "版本更新");
        listData.add(map);
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


}
