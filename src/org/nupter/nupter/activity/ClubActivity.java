package org.nupter.nupter.activity;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Log;

import static org.nupter.nupter.utils.NetworkUtils.isNewworkConnected;

/*
 *
 * 社团模块 一级菜单
 */
public class ClubActivity extends Activity implements Runnable {

    private Intent chooseIntent;
    private boolean check;
    private Thread checknet;
    private ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter simple = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        checknet = new Thread(ClubActivity.this);
        checknet.start();

        // 生成动态数组，并且传入数据
        setAssociations();
        // 实例化GridView
        GridView mGridView = (GridView) findViewById(R.id.gridview);
        // 构建一个适配器
        simple = new SimpleAdapter(ClubActivity.this, lstImageItem, R.layout.activity_club_image,
                new String[]{"ItemImage", "ItemText"}, new int[]{
                R.id.ItemImage, R.id.ItemText});
        mGridView.setAdapter(simple);
        mGridView.setOnItemClickListener(onItemClickListener);
    }/*---------------------------------------------------------------------*/

    private GridView.OnItemClickListener onItemClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            long page_id = -1;
            switch (position) {
                case 0:
                    page_id = 600907477;
                    break;
                case 1:
                    page_id = 601003549;
                    break;
                case 2:
                    page_id = 600889745;
                    break;
                case 3:
                    page_id = 601017224;
                    break;
                case 4:
                    page_id = 600490284;
                    break;
                default:
                    break;
            }

            if (check) {
                chooseIntent = new Intent();
                chooseIntent.setClass(ClubActivity.this, ClubDetailActivity.class);
                chooseIntent.putExtra("page_id",page_id);
                startActivity(chooseIntent);
            } else
                Log.i("目前没有网络连接");
        }
    };
    public void run() {
        while (true) {
            check = isNewworkConnected();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void setAssociations() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.img);// 添加图像资源的ID
        map.put("ItemText", "青春南邮");// 按序号做ItemText
        lstImageItem.add(map);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("ItemImage", R.drawable.img1);
        map1.put("ItemText", "青志联");
        lstImageItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("ItemImage", R.drawable.img2);
        map2.put("ItemText", "社团联合会");
        lstImageItem.add(map2);
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("ItemImage", R.drawable.img3);
        map3.put("ItemText", "校科学技术协会");
        lstImageItem.add(map3);
        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("ItemImage", R.drawable.img4);
        map4.put("ItemText", "校学生会");
        lstImageItem.add(map4);
    }

}