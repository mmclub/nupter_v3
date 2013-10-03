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
 *
 * @author panlei
 */
public class SettingActivity extends Activity {
    private CornerListView cornerListView = null;
    private List<Map<String, String>> listData = null;
    private SimpleAdapter adapter = null;
    private ToggleButton refreshsoundTB, classNoticeTB;
    private SharedPreferences mySharedPreferences;
    private boolean soundFlag, classNoticeFlag;
    private TextView classNoticeTV;
    private ArrayList<ArrayList<String>> alarmList = new ArrayList<ArrayList<String>>();
    private String getSchedule;
    private Calendar calendar;
    private boolean alarmFlag;
    private int alarmTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        classNoticeTV = (TextView) findViewById(R.id.classNoticeTV);
        cornerListView = (CornerListView) findViewById(R.id.settinglistview);
        refreshsoundTB = (ToggleButton) findViewById(R.id.refreshsoundTB);
        classNoticeTB = (ToggleButton) findViewById(R.id.classNoticeTB);
        refreshsoundTB.setOnCheckedChangeListener(checkedListener);
        classNoticeTB.setOnCheckedChangeListener(checkedListener);

        SharedPreferences sharedPreferences = getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        boolean getSoundFlag = sharedPreferences.getBoolean("SoundFlag", true);
        boolean getClassNoticeFlag = sharedPreferences.getBoolean("ClassNoticeFlag", false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getSchedule = sharedPreferences.getString("schedule", null);
        refreshsoundTB.setChecked(getSoundFlag);
        classNoticeTB.setChecked(getClassNoticeFlag);

        setListData();
        adapter = new SimpleAdapter(SettingActivity.this, listData, R.layout.view_tab_setting_list_item,
                new String[]{"text"}, new int[]{R.id.setting_list_item_text});
        cornerListView.setAdapter(adapter);
        cornerListView.setOnItemClickListener(LVlistener);
        getActionBar().setDisplayHomeAsUpEnabled(true);


    }


    private void alarm(String string) {
        alarmList = new JsoupTable().parse(string);
        Intent intent = new Intent(SettingActivity.this, CallAlarm.class);
        calendar = Calendar.getInstance();
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //calendar.setTimeInMillis(System.currentTimeMillis());


        /*PendingIntent pendingInten0 = PendingIntent.getBroadcast(SettingActivity.this, 0, intent, 1);
        setAlarm(5, 10, 35);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                ,60*60*24*7*1000,
                pendingInten0);
        PendingIntent pendingInten1 = PendingIntent.getBroadcast(SettingActivity.this, 1, intent, 1);
        setAlarm(5, 10, 36);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                ,60*60*24*7*1000,
                pendingInten1); */


        //周一
        PendingIntent pendingIntent0 = PendingIntent.getBroadcast(SettingActivity.this, 0, intent, 1);
        if (!alarmList.get(0).get(0).equals(" ")) {
            setAlarm(2, 7, 45);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent0);

        } else if (!alarmList.get(1).get(0).equals(" ")) {
            setAlarm(2, 9, 35);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent0);

        }

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(SettingActivity.this, 1, intent, 1);
        if (!alarmList.get(2).get(0).equals(" ")) {
            setAlarm(2, 13, 30);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent1);
        } else if (!alarmList.get(3).get(0).equals(" ")) {
            setAlarm(2, 15, 20);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent1);
        }

        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(SettingActivity.this, 2, intent, 1);
        if (!alarmList.get(4).get(0).equals(" ")) {
            setAlarm(2, 18, 15);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent2);
        }

        //周二
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(SettingActivity.this, 3, intent, 1);
        if (!alarmList.get(0).get(1).equals(" ")) {
            setAlarm(3, 7, 45);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent3);

        } else if (!alarmList.get(1).get(1).equals(" ")) {
            setAlarm(3, 9, 35);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent3);
        }

        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(SettingActivity.this, 4, intent, 1);
        if (!alarmList.get(2).get(1).equals(" ")) {
            setAlarm(3, 13, 30);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent4);
        } else if (!alarmList.get(3).get(1).equals(" ")) {
            setAlarm(3, 15, 20);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent4);
        }

        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(SettingActivity.this, 5, intent, 1);
        if (!alarmList.get(4).get(1).equals(" ")) {
            setAlarm(3, 18, 15);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent5);
        }

        //周三
        PendingIntent pendingIntent6 = PendingIntent.getBroadcast(SettingActivity.this, 6, intent, 1);
        if (!alarmList.get(0).get(2).equals(" ")) {
            setAlarm(4, 7, 45);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent6);

        } else if (!alarmList.get(1).get(2).equals(" ")) {
            setAlarm(4, 9, 35);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent6);
        }

        PendingIntent pendingIntent7 = PendingIntent.getBroadcast(SettingActivity.this, 7, intent, 1);
        if (!alarmList.get(2).get(2).equals(" ")) {
            setAlarm(4, 13, 30);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent7);
        } else if (!alarmList.get(3).get(2).equals(" ")) {
            setAlarm(4, 15, 20);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent7);
        }

        PendingIntent pendingIntent8 = PendingIntent.getBroadcast(SettingActivity.this, 8, intent, 1);
        if (!alarmList.get(4).get(2).equals(" ")) {
            setAlarm(4, 18, 15);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent8);
        }

        //周四
        PendingIntent pendingIntent9 = PendingIntent.getBroadcast(SettingActivity.this, 9, intent, 1);
        if (!alarmList.get(0).get(3).equals(" ")) {
            setAlarm(5, 7, 45);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent9);

        } else if (!alarmList.get(1).get(3).equals(" ")) {
            setAlarm(5, 9, 35);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent9);
        }
        PendingIntent pendingIntent10 = PendingIntent.getBroadcast(SettingActivity.this, 10, intent, 1);
        if (!alarmList.get(2).get(3).equals(" ")) {
            setAlarm(5, 13, 30);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent10);
        } else if (!alarmList.get(3).get(3).equals(" ")) {
            setAlarm(5, 15, 20);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent10);
        }

        PendingIntent pendingIntent11 = PendingIntent.getBroadcast(SettingActivity.this, 11, intent, 1);
        if (!alarmList.get(4).get(3).equals(" ")) {
            setAlarm(5, 18, 15);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent11);
        }

        //周五
        PendingIntent pendingIntent12 = PendingIntent.getBroadcast(SettingActivity.this, 12, intent, 1);
        if (!alarmList.get(0).get(4).equals(" ")) {
            setAlarm(6, 7, 45);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent12);
        } else if (!alarmList.get(1).get(4).equals(" ")) {
            setAlarm(6, 9, 35);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent12);
        }

        PendingIntent pendingIntent13 = PendingIntent.getBroadcast(SettingActivity.this, 13, intent, 1);
        if (!alarmList.get(2).get(4).equals(" ")) {
            setAlarm(6, 13, 30);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent13);
        } else if (!alarmList.get(3).get(4).equals(" ")) {
            setAlarm(6, 15, 20);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent13);
        }

        PendingIntent pendingIntent14 = PendingIntent.getBroadcast(SettingActivity.this, 14, intent, 1);
        if (!alarmList.get(4).get(4).equals(" ")) {
            setAlarm(6, 18, 15);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60 * 24 * 7 * 1000, pendingIntent14);
        }
    }

    private void setAlarm(int week, int hour, int minute) {
        calendar.set(Calendar.DAY_OF_WEEK, week);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute + 15 - alarmTime);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        }
        Log.i("TAG", calendar.getTime() + "");
    }

    public void alarmCancel() {
        Intent intent = new Intent(SettingActivity.this,
                CallAlarm.class);
        AlarmManager am;
        for (int i = 0; i < 15; i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, i, intent, 1);
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.cancel(pendingIntent);
        }
    }


    CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //To change body of implemented methods use File | Settings | File Templates.
            mySharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);
            alarmFlag = mySharedPreferences.getBoolean("alarmFlag", true);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            if (buttonView.getId() == R.id.refreshsoundTB) {
                if (isChecked) {
                    soundFlag = true;
                } else {
                    soundFlag = false;
                }
            } else if (buttonView.getId() == R.id.classNoticeTB) {
                if (isChecked) {
                    classNoticeFlag = true;
                    if (getSchedule == null) {
                        Toast.makeText(SettingActivity.this, "请先登录课表！！！", Toast.LENGTH_SHORT).show();
                        classNoticeTB.setChecked(false);
                    } else {
                        if (alarmFlag == true) {
                            final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).setIcon(R.drawable.alarm).setTitle("设置课前提醒时间:").setPositiveButton("确定", null).create();
                            dialog.show();
                            dialog.getWindow().setContentView(R.layout.view_alarm_dialog);
                            Spinner spinner = (Spinner) dialog.getWindow().findViewById(R.id.spinner);
                            String spinner_text[] = new String[31];
                            for (int i = 0; i <= 30; i++) {
                                spinner_text[i] = i + "分钟";
                            }
                            ;
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, spinner_text);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    alarmTime=i;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    alarmTime=0;
                                }
                            });
                            dialog.getWindow().findViewById(R.id.spinner_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alarm(getSchedule);
                                    Toast.makeText(SettingActivity.this, "课前提醒设置成功！！！", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                            alarmFlag = false;
                        }
                    }
                } else {
                    alarmFlag = true;
                    classNoticeFlag = false;
                    alarmCancel();
                }
            }

            editor.putBoolean("alarmFlag", alarmFlag);
            editor.putBoolean("ClassNoticeFlag", classNoticeFlag);
            editor.putBoolean("SoundFlag", soundFlag);
            editor.commit();

        }
    };

    private AdapterView.OnItemClickListener LVlistener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //To change body of implemented methods use File | Settings | File Templates.

            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(SettingActivity.this, LoginNumberSaveActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(Intent.ACTION_SEND);
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


    private void setListData() {
        listData = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();
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