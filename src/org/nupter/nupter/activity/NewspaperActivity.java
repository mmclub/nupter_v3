package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import org.nupter.nupter.R;


/**
 * 团委手机报
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */


public class NewspaperActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] strs = {"手机报第1期-学校化粪池沼气爆炸",
                "手机报第2期-装空调的梦泡汤啦",
                "手机报第3期-下学期学费上涨400%",
                "手机报第4期-南京明天最高气温50度，创历史新高",
                "手机报第5期-教务系统发现漏洞，所有学生成绩被清空"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strs);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(1, 1, 1, "Refresh");
        item.setIcon(android.R.drawable.ic_menu_rotate);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Toast.makeText(this, "刷新中", Toast.LENGTH_SHORT).show();
        return super.onMenuItemSelected(featureId, item);
    }
}