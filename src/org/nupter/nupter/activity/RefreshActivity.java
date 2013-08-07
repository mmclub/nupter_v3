package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.nupter.nupter.R;




/**
 * 刷新程序演示
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 * 演示如何用ActionBar，后续会加入下拉刷新。
 */


public class RefreshActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(1,1,1,"Refresh");
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