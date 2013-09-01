package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
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
    private ToggleButton  refreshsoundTB;
    private SharedPreferences mySharedPreferences;
    private boolean newsFlag;
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
                    intent.putExtra(Intent.EXTRA_TEXT, "嗨！童鞋们，快来使用‘掌上南邮’吧，太给力了！" +
                            "我和小伙伴们都惊呆了。。。 ");
                    startActivity(Intent.createChooser(intent, getTitle()));

                    break;

                case  2:
                    intent.setClass(SettingActivity.this, WebviewActivity.class);
                    intent.putExtra(WebviewActivity.EXTRA_TITLE, "关于掌上南邮");
                    intent.putExtra(WebviewActivity.EXTRA_URL, "file:///android_asset/about_us.html");
                    startActivity(intent);
                    break;

                case 3:
                    intent.setClass(SettingActivity.this, WebviewActivity.class);
                    intent.putExtra(WebviewActivity.EXTRA_TITLE, "加入我们");
                    intent.putExtra(WebviewActivity.EXTRA_URL, "file:///android_asset/join_us.html");
                    startActivity(intent);
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
        map.put("text", "关于掌上南邮");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "加入开发团队");
        listData.add(map);


    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent1 = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent1);
                this.finish();
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }


}
