package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.JsoupTable;

import java.util.ArrayList;


/**
 * 课表模块
 *
 * @author sudongsheng
 */


public class ScheduleActivity extends Activity {
    private int height;
    private int skin;
    private MorningAdapter morningAdapter;
    private AfternoonAdapter afternoonAdapter;
    private EveningAdapter eveningAdapter;
    private GridView morningGridView;
    private GridView afternoonGridView;
    private GridView eveningGridView;
    private SharedPreferences preferences;
    private LinearLayout linearLayout;
    private String schedule;
    private ArrayList<ArrayList<String>> tablelist = new ArrayList<ArrayList<String>>();
    private int[][] color = new int[][]{{R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5, R.drawable.color_6},
            {R.drawable.pink_1, R.drawable.pink_2, R.drawable.pink_3, R.drawable.pink_1, R.drawable.pink_2, R.drawable.pink_3},
            {R.drawable.green_1, R.drawable.green_2, R.drawable.green_3, R.drawable.green_1, R.drawable.green_2, R.drawable.green_3},
            {R.drawable.blue_1, R.drawable.blue_2, R.drawable.blue_3, R.drawable.blue_1, R.drawable.blue_2, R.drawable.blue_3},
            {R.drawable.table_yellow, R.drawable.table_blue, R.drawable.table_green, R.drawable.table_orange, R.drawable.table_pink, R.drawable.table_red}};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        height = getWindowManager().getDefaultDisplay().getHeight();
        preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        schedule = preferences.getString("schedule", null);
        skin = preferences.getInt("skin", 0);
        //解析网页，tableList存放5组数据，（从星期一到星期五）早上1、2节，3、4、5节，下午6、7节，7、8节，晚上9，10，11节
        tablelist = new JsoupTable().parse(schedule);
        linearLayout = (LinearLayout) findViewById(R.id.postLinearLayout);
        if (skin == 0)
            linearLayout.setBackgroundResource(R.drawable.colorbackground);
        if (skin == 1)
            linearLayout.setBackgroundResource(R.drawable.pink_background);
        if (skin == 2)
            linearLayout.setBackgroundResource(R.drawable.green_background);
        if (skin == 3)
            linearLayout.setBackgroundResource(R.drawable.blue_background);
        linearLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT)
                                .getTop();
                        height = height - contentViewTop - 20;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                morningGridView = (GridView) findViewById(R.id.morningGridView);
                                afternoonGridView = (GridView) findViewById(R.id.afternoonGridView);
                                eveningGridView = (GridView) findViewById(R.id.eveningGridView);
                                morningAdapter = new MorningAdapter(ScheduleActivity.this, tablelist.get(0), tablelist.get(1));
                                morningGridView.setAdapter(morningAdapter);
                                morningGridView.setOnItemClickListener(MorningClickListener);
                                afternoonAdapter = new AfternoonAdapter(ScheduleActivity.this, tablelist.get(2), tablelist.get(3));
                                afternoonGridView.setAdapter(afternoonAdapter);
                                afternoonGridView.setOnItemClickListener(AfternoonClickListener);
                                eveningAdapter = new EveningAdapter(ScheduleActivity.this, tablelist.get(4));
                                eveningGridView.setAdapter(eveningAdapter);
                                eveningGridView.setOnItemClickListener(EveningClickListener);
                            }
                        });
                    }
                });
    }

    private String[] format(String s) {
        String a[] = s.split(" ");
        if ((!a[1].startsWith("周")) && a[1].length() < 5) {
            a[0] = a[0] + a[1];
            a[1] = a[2];
            a[2] = a[3];
            if (a.length > 4) {
                a[3] = a[4];
            }
        }
        return a;
    }

    private String getClassName(String s) {
        String a[] = format(s);
        return a[0];
    }

    private String getClassLocation(String s) {
        String a[] = format(s);
        if (a.length >= 4) {
            if (a[3].startsWith("教")) {
                return a[3];
            }
        }
        return null;
    }

    private String getClassTime(String s) {
        String a[] = format(s);
        return a[1];
    }

    private String getClassTeacher(String s) {
        String a[] = format(s);
        return a[2];
    }

    private Boolean isOneClass(String s) {
        String a[] = format(s);
        if (!a[1].substring(0, 6).endsWith("9")) {
            return true;
        }
        return false;
    }

    private Boolean isTwoClass(String s) {
        String a[] = format(s);
        if (a[1].substring(0, 8).endsWith("5")) {
            return false;
        }
        return true;
    }

    public class MorningAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<String> list1 = new ArrayList<String>();
        private ArrayList<String> list2 = new ArrayList<String>();


        public MorningAdapter(Context context, ArrayList<String> list1, ArrayList<String> list2) {
            this.inflater = LayoutInflater.from(context);
            this.list1 = list1;
            this.list2 = list2;
        }

        @Override
        public int getCount() {
            return list1.size() + list2.size();
        }

        @Override
        public String getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.view_schedule, null);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) morningGridView.getLayoutParams();
            TextView className = (TextView) convertView.findViewById(R.id.className);
            TextView classLocation = (TextView) convertView.findViewById(R.id.classLocation);
            classLocation.setTextSize(10);
            //设置textView的内容
            if (position < 5) {
                if (!list1.get(position).equals(" ")) {
                    convertView.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 6));
                    className.setText(getClassName(list1.get(position)));
                    classLocation.setText(getClassLocation(list1.get(position)));
                    convertView.setBackgroundResource(color[skin][position]);
                } else {
                    convertView.setLayoutParams(new GridView.LayoutParams(linearParams.width, height/6));
                }
            } else if (position >= 5 && position < 10) {
                if (!list2.get(position - 5).equals(" ")) {
                    if (isTwoClass(list2.get(position - 5))) {
                        convertView.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 6));
                    } else {
                        convertView.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 4));
                    }
                    className.setText(getClassName(list2.get(position - 5)));
                    classLocation.setText(getClassLocation(list2.get(position - 5)));
                    convertView.setBackgroundResource(color[skin][position > 6 ? position - 7 : position - 1]);
                } else {
                    convertView.setLayoutParams(new GridView.LayoutParams(linearParams.width, 0));
                }
            }
            return convertView;
        }
    }

    public class AfternoonAdapter extends BaseAdapter {
        private LayoutInflater inflate;
        private ArrayList<String> list3 = new ArrayList<String>();
        private ArrayList<String> list4 = new ArrayList<String>();

        public AfternoonAdapter(Context context, ArrayList<String> list3, ArrayList<String> list4) {
            this.inflate = LayoutInflater.from(context);
            this.list3 = list3;
            this.list4 = list4;
        }

        @Override
        public int getCount() {
            return list3.size() + list4.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflate.inflate(R.layout.view_schedule, null);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) afternoonGridView.getLayoutParams();
            TextView className = (TextView) view.findViewById(R.id.className);
            TextView classLocation = (TextView) view.findViewById(R.id.classLocation);
            classLocation.setTextSize(10);
            if (i < 5) {
                if (!list3.get(i).equals(" ")) {
                    if (!list4.get(i).equals(" ")) {
                        if (isOneClass(list4.get(i))) {
                            view.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 4));
                        }
                    }else {
                        view.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 6));
                    }
                    className.setText(getClassName(list3.get(i)));
                    classLocation.setText(getClassLocation(list3.get(i)));
                    view.setBackgroundResource(color[skin][i > 3 ? i - 4 : i + 2]);
                }else {
                    view.setLayoutParams(new GridView.LayoutParams(linearParams.width, height/6));
                }
            } else if (i >= 5 && i < 10) {
                if (!list4.get(i - 5).equals(" ")) {
                    if (!isOneClass(list4.get(i - 5))) {
                        view.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 6));
                        className.setText(getClassName(list4.get(i - 5)));
                        classLocation.setText(getClassLocation(list4.get(i - 5)));
                        view.setBackgroundResource(color[skin][i - 4]);
                    }
                } else {
                    view.setLayoutParams(new GridView.LayoutParams(linearParams.width, 0));
                }
            }
            return view;
        }
    }

    public class EveningAdapter extends BaseAdapter {
        private LayoutInflater inflate;
        private ArrayList<String> list5 = new ArrayList<String>();

        public EveningAdapter(Context context, ArrayList<String> list5) {
            this.inflate = LayoutInflater.from(context);
            this.list5 = list5;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public int getCount() {
            return list5.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflate.inflate(R.layout.view_schedule, null);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) afternoonGridView.getLayoutParams();
            TextView className = (TextView) view.findViewById(R.id.className);
            TextView classLocation = (TextView) view.findViewById(R.id.classLocation);
            classLocation.setTextSize(10);
            if (!list5.get(i).equals(" ")) {
                view.setLayoutParams(new GridView.LayoutParams(linearParams.width, height / 4));
                className.setText(getClassName(list5.get(i)));
                classLocation.setText(getClassLocation(list5.get(i)));
                view.setBackgroundResource(color[skin][i > 2 ? i - 3 : i + 3]);
            }  else {
                view.setLayoutParams(new GridView.LayoutParams(linearParams.width, 0));
            }
            return view;
        }
    }

    private GridView.OnItemClickListener MorningClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if ((position < 5 && (!tablelist.get(0).get(position).equals(" "))) || (position >= 5 && (!tablelist.get(1).get(position - 5).equals(" ")))) {
                Dialog dialog = new Dialog(ScheduleActivity.this, R.style.classDialog) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        this.dismiss();
                        return true;
                    }
                };
                dialog.setContentView(R.layout.view_dialog);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                TextView class_name = (TextView) dialog.getWindow().findViewById(R.id.class_name);
                TextView class_time = (TextView) dialog.getWindow().findViewById(R.id.class_time);
                TextView class_teacher = (TextView) dialog.getWindow().findViewById(R.id.class_teacher);
                TextView class_location = (TextView) dialog.getWindow().findViewById(R.id.class_location);
                class_name.setText(position <= 4 ? getClassName(tablelist.get(0).get(position)) : getClassName(tablelist.get(1).get(position - 5)));
                class_time.setText(position <= 4 ? getClassTime(tablelist.get(0).get(position)) : getClassTime(tablelist.get(1).get(position - 5)));
                class_teacher.setText(position <= 4 ? getClassTeacher(tablelist.get(0).get(position)) : getClassTeacher(tablelist.get(1).get(position - 5)));
                class_location.setText(position <= 4 ? getClassLocation(tablelist.get(0).get(position)) : getClassLocation(tablelist.get(1).get(position - 5)));
            }
        }
    };
    private GridView.OnItemClickListener AfternoonClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if ((position < 5 && (!tablelist.get(2).get(position).equals(" "))) || (position >= 5 && (!tablelist.get(3).get(position - 5).equals(" ")))) {
                Dialog dialog = new Dialog(ScheduleActivity.this, R.style.classDialog) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        this.dismiss();
                        return true;
                    }
                };
                dialog.setContentView(R.layout.view_dialog);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                TextView class_name = (TextView) dialog.getWindow().findViewById(R.id.class_name);
                TextView class_time = (TextView) dialog.getWindow().findViewById(R.id.class_time);
                TextView class_teacher = (TextView) dialog.getWindow().findViewById(R.id.class_teacher);
                TextView class_location = (TextView) dialog.getWindow().findViewById(R.id.class_location);
                class_name.setText(position <= 4 ? getClassName(tablelist.get(2).get(position)) : getClassName(tablelist.get(3).get(position - 5)));
                class_time.setText(position <= 4 ? getClassTime(tablelist.get(2).get(position)) : getClassTime(tablelist.get(3).get(position - 5)));
                class_teacher.setText(position <= 4 ? getClassTeacher(tablelist.get(2).get(position)) : getClassTeacher(tablelist.get(3).get(position - 5)));
                class_location.setText(position <= 4 ? getClassLocation(tablelist.get(2).get(position)) : getClassLocation(tablelist.get(3).get(position - 5)));
            }
        }
    };
    private GridView.OnItemClickListener EveningClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < 5 && (!tablelist.get(4).get(position).equals(" "))) {
                Dialog dialog = new Dialog(ScheduleActivity.this, R.style.classDialog) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        this.dismiss();
                        return true;
                    }
                };
                dialog.setContentView(R.layout.view_dialog);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                TextView class_name = (TextView) dialog.getWindow().findViewById(R.id.class_name);
                TextView class_time = (TextView) dialog.getWindow().findViewById(R.id.class_time);
                TextView class_teacher = (TextView) dialog.getWindow().findViewById(R.id.class_teacher);
                TextView class_location = (TextView) dialog.getWindow().findViewById(R.id.class_location);
                class_name.setText(getClassName(tablelist.get(4).get(position)));
                class_time.setText(getClassTime(tablelist.get(4).get(position)));
                class_teacher.setText(getClassTeacher(tablelist.get(4).get(position)));
                class_location.setText(getClassLocation(tablelist.get(4).get(position)));
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        menu.add("menu");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        String[] items = {"梦幻水晶", "粉红卡通", "绿意萦绕", "蓝色天空", "纯真浪漫"};
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.skin_icon)
                .setTitle("更换皮肤")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = preferences.edit();
                        switch (which) {
                            case 0:
                                editor.putInt("skin", 0);
                                editor.commit();
                                sendBroadcast();
                                ScheduleActivity.this.recreate();
                                break;
                            case 1:
                                editor.putInt("skin", 1);
                                editor.commit();
                                sendBroadcast();
                                ScheduleActivity.this.recreate();
                                break;
                            case 2:
                                editor.putInt("skin", 2);
                                editor.commit();
                                sendBroadcast();
                                ScheduleActivity.this.recreate();
                                break;
                            case 3:
                                editor.putInt("skin", 3);
                                editor.commit();
                                sendBroadcast();
                                ScheduleActivity.this.recreate();
                                break;
                            case 4:
                                editor.putInt("skin", 4);
                                editor.commit();
                                sendBroadcast();
                                ScheduleActivity.this.recreate();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).create().show();
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                this.finish();
                break;
            case R.id.action_login:
                Intent intent1 = new Intent(ScheduleActivity.this, LoginActivity.class);
                intent1.putExtra("JumpTo", "Schedule");
                startActivity(intent1);
                this.finish();
                break;
            case R.id.action_skin:
                String[] items = {"梦幻水晶", "粉红卡通", "绿意萦绕", "蓝色天空", "纯真浪漫"};
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.skin_icon)
                        .setTitle("更换皮肤")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = preferences.edit();
                                switch (which) {
                                    case 0:
                                        editor.putInt("skin", 0);
                                        editor.commit();
                                        sendBroadcast();
                                        ScheduleActivity.this.recreate();
                                        break;
                                    case 1:
                                        editor.putInt("skin", 1);
                                        editor.commit();
                                        sendBroadcast();
                                        ScheduleActivity.this.recreate();
                                        break;
                                    case 2:
                                        editor.putInt("skin", 2);
                                        editor.commit();
                                        sendBroadcast();
                                        ScheduleActivity.this.recreate();
                                        break;
                                    case 3:
                                        editor.putInt("skin", 3);
                                        editor.commit();
                                        sendBroadcast();
                                        ScheduleActivity.this.recreate();
                                        break;
                                    case 4:
                                        editor.putInt("skin", 4);
                                        editor.commit();
                                        sendBroadcast();
                                        ScheduleActivity.this.recreate();
                                        break;
                                }
                            }
                        })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).create().show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;// 禁止Gridview进行滑动
        }
        return super.dispatchTouchEvent(ev);
    }

    private void sendBroadcast() {
        Intent intent = new Intent("org.nupter.widget.refresh");
        this.sendBroadcast(intent);
    }
}
