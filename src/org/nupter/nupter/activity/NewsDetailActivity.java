package org.nupter.nupter.activity;

import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * 校园新闻与教务公告的二级菜单
 * 主要用于显示新闻内容
 *
 * @author panlei       e-mail:121531863@qq.com
 *
 */

@SuppressLint("NewApi")
public class NewsDetailActivity extends Activity{
    
    private TextView contentTV;
    private Intent contentIntent;
    private String getContent;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_content);
        contentIntent = getIntent();
        getContent =  contentIntent.getStringExtra("content");
        contentTV = (TextView)findViewById(R.id.contentTV);
        contentTV.setText(getContent);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
       @Override
       public boolean onMenuItemSelected(int featureId, MenuItem item) {
           switch (item.getItemId()) {
           case android.R.id.home:
               onBackPressed();
               break;
           default:
               Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();

               break;
           }
           return super.onMenuItemSelected(featureId, item);
       }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}
