package org.nupter.nupter.utils;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-20
 * Time: 下午10:51
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import org.nupter.nupter.R;

import java.util.ArrayList;

public class ScheduleWidgetContent extends Activity {

    int mAppWidgetId;
    private ArrayList<ArrayList<String>> tableList = new ArrayList<ArrayList<String>>();
    private int[] color = new int[]{R.drawable.table_yellow, R.drawable.table_blue, R.drawable.table_green, R.drawable.table_orange, R.drawable.table_pink, R.drawable.table_red};
    private int[] linearLayoutId1 = {R.id.one_1, R.id.one_2, R.id.one_3, R.id.one_4, R.id.one_5};
    private int[] linearLayoutId2 = {R.id.two_1, R.id.two_2, R.id.two_3, R.id.two_4, R.id.two_5};
    private int[] linearLayoutId3 = {R.id.three_1, R.id.three_2, R.id.three_3, R.id.three_4, R.id.three_5};
    private int[] linearLayoutId4 = {R.id.four_1, R.id.four_2, R.id.four_3, R.id.four_4, R.id.four_5};
    private int[] linearLayoutId5 = {R.id.five_1, R.id.five_2, R.id.five_3, R.id.five_4, R.id.five_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_schedule);
        setResult(RESULT_CANCELED);
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String schedule = preferences.getString("schedule", "null");
        if ((!schedule.equals("null"))) {
            JsoupTable jsoupTable = new JsoupTable();
            tableList = jsoupTable.parse(schedule);
            RemoteViews views = new RemoteViews(ScheduleWidgetContent.this
                    .getPackageName(), R.layout.widget_schedule);
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(ScheduleWidgetContent.this);
            //第一二节课
            for (int i = 0; i < 5; i++) {
                if (!tableList.get(0).get(i).equals(" ")) {
                    RemoteViews view1 = new RemoteViews(ScheduleWidgetContent.this.getPackageName(), R.layout.widget_remoteview3);
                    view1.setTextViewText(R.id.scheduleName, getClassName(tableList.get(0).get(i)));
                    view1.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(0).get(i)));
                    view1.setImageViewResource(R.id.background,color[i]);
                    views.addView(linearLayoutId1[i], view1);
                }
            }
            //第三四五节课
            for (int i = 0; i < 5; i++) {
                if (!tableList.get(1).get(i).equals(" ")) {
                    if (isTwoClass(tableList.get(1).get(i))) {
                        RemoteViews view2 = new RemoteViews(ScheduleWidgetContent.this.getPackageName(), R.layout.widget_remoteview2);
                        view2.setTextViewText(R.id.scheduleName, getClassName(tableList.get(1).get(i)));
                        view2.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(1).get(i)));
                        view2.setImageViewResource(R.id.background,color[i>1?i-2:i+4]);
                        views.addView(linearLayoutId2[i], view2);
                    } else {
                        RemoteViews view2 = new RemoteViews(ScheduleWidgetContent.this.getPackageName(), R.layout.widget_remoteview3);
                        view2.setTextViewText(R.id.scheduleName, getClassName(tableList.get(1).get(i)));
                        view2.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(1).get(i)));
                        view2.setImageViewResource(R.id.background,color[i>1?i-2:i+4]);
                        views.addView(linearLayoutId2[i], view2);
                    }
                }
            }
            //第六七节课
            for (int i = 0; i < 5; i++) {
                if (!tableList.get(2).get(i).equals(" ")) {
                    RemoteViews view3 = new RemoteViews(ScheduleWidgetContent.this.getPackageName(), R.layout.widget_remoteview2);
                    view3.setTextViewText(R.id.scheduleName, getClassName(tableList.get(2).get(i)));
                    view3.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(2).get(i)));
                    view3.setImageViewResource(R.id.background,color[i>3?i-4:i+2]);
                    views.addView(linearLayoutId3[i], view3);
                }
            }
            //第八九节课
            for (int i = 0; i < 5; i++) {
                if (!tableList.get(3).get(i).equals(" ")) {
                    if (isOneClass(tableList.get(3).get(i))) {
                        RemoteViews view4 = new RemoteViews(ScheduleWidgetContent.this.getPackageName(), R.layout.widget_remoteview1);
                        view4.setImageViewResource(R.id.background,color[i>3?i-4:i+2]);
                        views.addView(linearLayoutId4[i], view4);
                    }else {
                        RemoteViews view4 = new RemoteViews(ScheduleWidgetContent.this.getPackageName(), R.layout.widget_remoteview3);
                        view4.setTextViewText(R.id.scheduleName, getClassName(tableList.get(3).get(i)));
                        view4.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(3).get(i)));
                        view4.setImageViewResource(R.id.background,color[i]);
                        views.addView(linearLayoutId4[i>0?i-1:i+5], view4);
                    }
                }
            }
            //第十十一十二节课
            for (int i = 0; i < 5; i++) {
                if (!tableList.get(4).get(i).equals(" ")) {
                    RemoteViews view5 = new RemoteViews(ScheduleWidgetContent.this.getPackageName(), R.layout.widget_remoteview3);
                    view5.setTextViewText(R.id.scheduleName, getClassName(tableList.get(4).get(i)));
                    view5.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(4).get(i)));
                    view5.setImageViewResource(R.id.background,color[i]);
                    views.addView(linearLayoutId5[i>2?i-3:i+3], view5);
                }
            }
            appWidgetManager.updateAppWidget(mAppWidgetId, views);
        } else {
            finish();
        }

        // return OK
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
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

    private Boolean isTwoClass(String s) {
        String a[] = format(s);
        if (a[1].substring(0, 8).endsWith("5")) {
            return false;
        }
        return true;
    }

    private Boolean isOneClass(String s) {
        String a[] = format(s);
        if (!a[1].substring(0, 6).endsWith("9")) {
            return true;
        }
        return false;
    }
}
