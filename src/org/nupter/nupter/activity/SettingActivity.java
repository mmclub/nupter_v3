package org.nupter.nupter.activity;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.AppUtils;
import org.nupter.nupter.utils.CallAlarm;
import org.nupter.nupter.utils.CornerListView;
import org.nupter.nupter.utils.JsoupTable;

import java.util.*;

/**
 * 设置板块主界面
 * @author panlei
 *
 */
public class SettingActivity extends Activity {
    private CornerListView cornerListView = null;
    private List<Map<String, String>> listData = null;
    private SimpleAdapter adapter = null;
    private ToggleButton  refreshsoundTB,classNoticeTB;
    private SharedPreferences mySharedPreferences;
    private boolean soundFlag,classNoticeFlag;
    private TextView classNoticeTV;
    private ArrayList<ArrayList<String>> alarmList = new ArrayList<ArrayList<String>>();
    private String getSchedule;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        classNoticeTV = (TextView)findViewById(R.id.classNoticeTV);
        cornerListView = (CornerListView)findViewById(R.id.settinglistview);
        refreshsoundTB =(ToggleButton)findViewById(R.id.refreshsoundTB);
        classNoticeTB = (ToggleButton)findViewById(R.id.classNoticeTB);
        refreshsoundTB.setOnCheckedChangeListener(checkedListener);
        classNoticeTB.setOnCheckedChangeListener(checkedListener);

        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        boolean getSoundFlag = sharedPreferences.getBoolean("SoundFlag",true);
        boolean getClassNoticeFlag = sharedPreferences.getBoolean("ClassNoticeFlag",false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getSchedule = sharedPreferences.getString("schedule",null);
        refreshsoundTB.setChecked(getSoundFlag);
        classNoticeTB.setChecked(getClassNoticeFlag);

        setListData();
        adapter = new SimpleAdapter(SettingActivity.this, listData, R.layout.view_tab_setting_list_item,
                new String[]{"text"}, new int[]{R.id.setting_list_item_text});
        cornerListView.setAdapter(adapter);
        cornerListView.setOnItemClickListener(LVlistener);


        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void alarm(String string){

        alarmList   = new JsoupTable().parse(string);

        Intent intent = new Intent(SettingActivity.this,
                CallAlarm.class);

        calendar = Calendar.getInstance();

        /*AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent0 = PendingIntent
                .getBroadcast(SettingActivity.this, 0,
                        intent, 0);
        calendar.set(Calendar.DAY_OF_WEEK, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 52);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        }
        am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                , (60 * 60 * 24 * 7 * 1000),
                pendingIntent0);


        AlarmManager am1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent
                .getBroadcast(SettingActivity.this, 1,
                        intent, 0);
        calendar.set(Calendar.DAY_OF_WEEK, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 54);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        }
        am1.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent1);
        am1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                , (60 * 60 * 24 * 7 * 1000),
                pendingIntent1); */

       //周一

      AlarmManager am0 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent0 = PendingIntent
                .getBroadcast(SettingActivity.this, 0,
                        intent, 0);
        if(!alarmList.get(0).get(0).equals(" ")){
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        }
        am0.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                , (60 * 60 * 24 * 7 * 1000),
                pendingIntent0);

        }
           else if (!alarmList.get(1).get(0).equals(" ")){
               calendar.set(Calendar.DAY_OF_WEEK, 2);
               calendar.set(Calendar.HOUR, 9);
               calendar.set(Calendar.MINUTE, 35);
               calendar.set(Calendar.SECOND, 0);
               calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
               am0.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent0);
           }

      AlarmManager am1 = (AlarmManager) getSystemService(ALARM_SERVICE);
      PendingIntent pendingIntent1 = PendingIntent
                .getBroadcast(SettingActivity.this, 1,
                        intent, 0);
       if (!alarmList.get(2).get(0).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 2);
            calendar.set(Calendar.HOUR, 13);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
           if(System.currentTimeMillis() > calendar.getTimeInMillis()){
               calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
           }

            am1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent1);
        }
        else if (!alarmList.get(3).get(0).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 1);
            calendar.set(Calendar.HOUR, 15);
            calendar.set(Calendar.MINUTE, 20);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
           if(System.currentTimeMillis() > calendar.getTimeInMillis()){
               calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
           }

            am1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent1);
        }

        AlarmManager am2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent2 = PendingIntent
                .getBroadcast(SettingActivity.this, 2,
                        intent, 0);
        if (!alarmList.get(4).get(0).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 2);
            calendar.set(Calendar.HOUR, 18);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent2);
        }


        //周二
        AlarmManager am3 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent3 = PendingIntent
                .getBroadcast(SettingActivity.this, 3,
                        intent, 0);
        if(!alarmList.get(0).get(1).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 3);
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 45);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am3.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , (60 * 60 * 24 * 7 * 1000),
                    pendingIntent3);

        }
        else if (!alarmList.get(1).get(1).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 3);
            calendar.set(Calendar.HOUR, 9);
            calendar.set(Calendar.MINUTE, 35);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am3.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent3);
        }

        AlarmManager am4 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent4 = PendingIntent
                .getBroadcast(SettingActivity.this, 4,
                        intent, 0);
        if (!alarmList.get(2).get(1).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 3);
            calendar.set(Calendar.HOUR, 13);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am4.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent4);
        }
        else if (!alarmList.get(3).get(1).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 3);
            calendar.set(Calendar.HOUR, 15);
            calendar.set(Calendar.MINUTE, 20);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am4.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent4);
        }


        AlarmManager am5 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent5 = PendingIntent
                .getBroadcast(SettingActivity.this, 5,
                        intent, 0);
        if (!alarmList.get(4).get(1).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 3);
            calendar.set(Calendar.HOUR, 18);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am5.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent5);
        }

        //周三
        AlarmManager am6 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent6 = PendingIntent
                .getBroadcast(SettingActivity.this, 6,
                        intent, 0);
        if(!alarmList.get(0).get(2).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 4);
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 45);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am6.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , (60 * 60 * 24 * 7 * 1000),
                    pendingIntent6);

        }
        else if (!alarmList.get(1).get(2).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 4);
            calendar.set(Calendar.HOUR, 9);
            calendar.set(Calendar.MINUTE, 35);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am6.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent6);
        }

        AlarmManager am7 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent7 = PendingIntent
                .getBroadcast(SettingActivity.this, 7,
                        intent, 0);
        if (!alarmList.get(2).get(2).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 4);
            calendar.set(Calendar.HOUR, 13);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am7.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent7);
        }
        else if (!alarmList.get(3).get(2).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 4);
            calendar.set(Calendar.HOUR, 15);
            calendar.set(Calendar.MINUTE, 20);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am7.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent7);
        }
        AlarmManager am8 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent8 = PendingIntent
                .getBroadcast(SettingActivity.this, 8,
                        intent, 0);
        if (!alarmList.get(4).get(2).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 4);
            calendar.set(Calendar.HOUR, 18);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am8.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent8);
        }


        //周四
        AlarmManager am9 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent9 = PendingIntent
                .getBroadcast(SettingActivity.this, 9,
                        intent, 0);
        if(!alarmList.get(0).get(3).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 5);
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 45);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am9.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , (60 * 60 * 24 * 7 * 1000),
                    pendingIntent9);

        }
        else if (!alarmList.get(1).get(3).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 5);
            calendar.set(Calendar.HOUR, 9);
            calendar.set(Calendar.MINUTE, 35);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am9.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent9);
        }
        AlarmManager am10 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent10 = PendingIntent
                .getBroadcast(SettingActivity.this, 10,
                        intent, 0);
        if (!alarmList.get(2).get(3).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 5);
            calendar.set(Calendar.HOUR, 13);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am10.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent10);
        }
        else if (!alarmList.get(3).get(3).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 5);
            calendar.set(Calendar.HOUR, 15);
            calendar.set(Calendar.MINUTE, 20);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am10.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent10);
        }

        AlarmManager am11 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent11 = PendingIntent
                .getBroadcast(SettingActivity.this, 11,
                        intent, 0);
        if (!alarmList.get(4).get(3).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 5);
            calendar.set(Calendar.HOUR, 18);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am11.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent11);
        }

        //周五
        AlarmManager am12 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent12 = PendingIntent
                .getBroadcast(SettingActivity.this, 12,
                        intent, 0);
        if(!alarmList.get(0).get(4).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 45);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am12.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , (60 * 60 * 24 * 7 * 1000),
                    pendingIntent12);

        }
        else if (!alarmList.get(1).get(4).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR, 9);
            calendar.set(Calendar.MINUTE, 35);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }
            am12.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent12);
        }
        AlarmManager am13 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent13 = PendingIntent
                .getBroadcast(SettingActivity.this, 13,
                        intent, 0);
        if (!alarmList.get(2).get(4).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR, 13);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am13.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent13);
        }
        else if (!alarmList.get(3).get(4).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR, 15);
            calendar.set(Calendar.MINUTE, 20);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am13.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent13);
        }
        AlarmManager am14 = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent14 = PendingIntent
                .getBroadcast(SettingActivity.this, 14,
                        intent, 0);
        if (!alarmList.get(4).get(4).equals(" ")){
            calendar.set(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR, 18);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            }

            am14.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*60*24*7*1000, pendingIntent14);
        }




    }
    public void alarmCancel(){
        Intent intent = new Intent(SettingActivity.this,
                CallAlarm.class);
        AlarmManager am;
          for (int i = 0;i<15;i++){
              PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this,i,intent,0);
              am = (AlarmManager)getSystemService(ALARM_SERVICE);
              am.cancel(pendingIntent);
          }

    }


    CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //To change body of implemented methods use File | Settings | File Templates.
            mySharedPreferences= getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
           if (buttonView.getId() == R.id.refreshsoundTB ){

            if(isChecked){
                soundFlag = true;
            }else {
                soundFlag = false;
            }
           }
           else if (buttonView.getId() == R.id.classNoticeTB){

               if (isChecked){
                   classNoticeFlag = true;
                   if (getSchedule == null){
                       Toast.makeText(SettingActivity.this,"请先登录课表！！！",Toast.LENGTH_SHORT).show();
                       classNoticeTB.setChecked(false);
                   }else {
                   alarm(getSchedule);
                   }
               }else {
                   classNoticeFlag = false;
                   alarmCancel();
               }
           }

            editor.putBoolean("ClassNoticeFlag",classNoticeFlag);
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
                    intent.putExtra(Intent.EXTRA_TEXT, "掌上南邮是我们南邮学生自己开发的一款校园助手App，可以随时随地查课表，查成绩，查图书，浏览校园最新资讯～下载连接 http://host1.nupter.org/nupter.apk");
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