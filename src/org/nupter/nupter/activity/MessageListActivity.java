package org.nupter.nupter.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;



public class MessageListActivity extends ListActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] strs = {"学校化粪池沼气爆炸","装空调的梦泡汤啦","下学期学费上涨400%","南京明天最高气温50度，创历史新高","教务系统发现漏洞，所有学生成绩被清空"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,strs);
        setListAdapter(adapter);
    }


}