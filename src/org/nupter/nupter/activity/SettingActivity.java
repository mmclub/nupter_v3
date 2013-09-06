package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.AppUtils;
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
    private ToggleButton  refreshsoundTB;
    private SharedPreferences mySharedPreferences;
    private boolean soundFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cornerListView = (CornerListView)findViewById(R.id.settinglistview);
        refreshsoundTB =(ToggleButton)findViewById(R.id.refreshsoundTB);
        refreshsoundTB.setOnCheckedChangeListener(checkedListener);

        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        boolean getNewsFlog =sharedPreferences.getBoolean("NewsFlag",true);
        boolean getSoundFlog = sharedPreferences.getBoolean("SoundFlag",true);
        refreshsoundTB.setChecked(getSoundFlog);

        setListData();
        adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.view_tab_setting_list_item,
                new String[]{"text"}, new int[]{R.id.setting_list_item_text});
        cornerListView.setAdapter(adapter);
        cornerListView.setOnItemClickListener(LVlistener);


         getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //To change body of implemented methods use File | Settings | File Templates.
            mySharedPreferences= getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();


                if(isChecked){
                    soundFlag = true;
                }else {
                    soundFlag = false;
                }
            //editor.putBoolean("NewsFlag",newsFlag) ;
            editor.putBoolean("SoundFlag",soundFlag);
            editor.commit();

        }
    } ;

    private AdapterView.OnItemClickListener LVlistener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //To change body of implemented methods use File | Settings | File Templates.

            Intent intent = new Intent();
            switch (position){
                case 0:
                    intent.setClass(SettingActivity.this,LoginNumberSaveActivity.class);
                    startActivity(intent);
                    break;
                case  1:
                    intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    intent.putExtra(Intent.EXTRA_TEXT, "掌上南邮是我们南邮学生自己开发的一款校园助手App，可以随时随地查课表，查成绩，查图书，浏览校园最新资讯～下载链接 http://host1.nupter.org/nupter.apk");
                    startActivity(Intent.createChooser(intent, getTitle()));

                    break;

                case 2:
                    intent.setClass(SettingActivity.this, WebviewActivity.class);
                    intent.putExtra(WebviewActivity.EXTRA_TITLE, "掌上南邮与开发团队");
                    intent.putExtra(WebviewActivity.EXTRA_URL, "file:///android_asset/about_us.html");
                    startActivity(intent);
                    break;


                case 3:
                    FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
                    agent.startFeedbackActivity();
                    agent.sync();
                    break;
                default:
                    break;
            }
        }
    };


    private void setListData(){
        listData = new ArrayList<Map<String,String>>();

        Map<String,String> map = new HashMap<String, String>();
        map.put("text", "帐号设置");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "分享");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "掌上南邮与开发团队");
        listData.add(map);


        map = new HashMap<String, String>();
        map.put("text", "反馈");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "版本号" + AppUtils.getVersionName(this));
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
