package org.nupter.nupter.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.Context;
import android.widget.RemoteViews;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.activity.LoginActivity;
import org.nupter.nupter.activity.ScheduleActivity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-20
 * Time: 下午8:47
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleWidget extends AppWidgetProvider {
    private int skin;
    private String schedule;
    private ArrayList<ArrayList<String>> tableList = new ArrayList<ArrayList<String>>();
    private int[] background_big = new int[]{R.drawable.widget_background1, R.drawable.widget_background2, R.drawable.widget_background3, R.drawable.widget_background4};
    private int[][] color = new int[][]{{R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5, R.drawable.color_6},
            {R.drawable.pink_1, R.drawable.pink_2, R.drawable.pink_3, R.drawable.pink_1, R.drawable.pink_2, R.drawable.pink_3},
            {R.drawable.green_1, R.drawable.green_2, R.drawable.green_3, R.drawable.green_1, R.drawable.green_2, R.drawable.green_3},
            {R.drawable.blue_1, R.drawable.blue_2, R.drawable.blue_3, R.drawable.blue_1, R.drawable.blue_2, R.drawable.blue_3},
            {R.drawable.table_yellow, R.drawable.table_blue, R.drawable.table_green, R.drawable.table_orange, R.drawable.table_pink, R.drawable.table_red}};
    private int[][] linearLayoutId = new int[][]{{R.id.one_1, R.id.one_2, R.id.one_3, R.id.one_4, R.id.one_5},
            {R.id.two_1, R.id.two_2, R.id.two_3, R.id.two_4, R.id.two_5},
            {R.id.three_1, R.id.three_2, R.id.three_3, R.id.three_4, R.id.three_5},
            {R.id.four_1, R.id.four_2, R.id.four_3, R.id.four_4, R.id.four_5},
            {R.id.five_1, R.id.five_2, R.id.five_3, R.id.five_4, R.id.five_5}};
    private ArrayList<ArrayList<Integer>> colors = new ArrayList<ArrayList<Integer>>();

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        skin = preferences.getInt("skin", 0);
        for (int i = 0; i < 5; i++) {
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (int j = 0; j <= 5; j++) {
                arrayList.add(color[i][j]);
            }
            colors.add(arrayList);
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        if (preferences.getInt("color_1", 0) != 0) {
            arrayList.add(preferences.getInt("color_1", 0));
            arrayList.add(preferences.getInt("color_2", 0));
            arrayList.add(preferences.getInt("color_3", 0));
            arrayList.add(preferences.getInt("color_4", 0));
            arrayList.add(preferences.getInt("color_5", 0));
            arrayList.add(preferences.getInt("color_6", 0));
            colors.add(arrayList);
        }
        schedule = preferences.getString("schedule", "null");
        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                    context, ScheduleWidget.class));
            if ((!schedule.equals("null"))) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
                Intent intent1 = new Intent(context, ScheduleActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
                views.setOnClickPendingIntent(R.id.background, pendingIntent);
                views = Update(context, views, skin);
//              for (int i = 0; i < appWidgetIds.length; i++)
//                  Log.i("str", appWidgetIds[i] + "create");
                appWidgetManager.updateAppWidget(appWidgetIds, views);
            } else {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_empty_view);
                Intent intent1 = new Intent(context, LoginActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
                views.setOnClickPendingIntent(R.id.empty, pendingIntent);
                appWidgetManager.updateAppWidget(appWidgetIds, views);
            }
        }
        if (intent.getAction().equals("org.nupter.widget.refresh")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                    context, ScheduleWidget.class));
            //     Log.i("str", appWidgetIds.length + "refresh");
            if (appWidgetIds.length != 0) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
                Intent intent1 = new Intent(context, ScheduleActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
                views.setOnClickPendingIntent(R.id.background, pendingIntent);
                views.removeAllViews(R.id.background);
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++)
                        views.removeAllViews(linearLayoutId[i][j]);
                }
                Update(context, views, skin);
                appWidgetManager.updateAppWidget(appWidgetIds, views);
            }
        }
    }

    private RemoteViews Update(Context context, RemoteViews views, int skin) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        schedule = preferences.getString("schedule", "null");
        tableList = new JsoupTable().parse(schedule);
        //第一二节课
        for (int i = 0; i < 5; i++) {
            if (!tableList.get(0).get(i).equals(" ")) {
                RemoteViews view1 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview);
                view1.setTextViewText(R.id.scheduleName, getClassName(tableList.get(0).get(i)));
                view1.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(0).get(i)));
                view1.setImageViewResource(R.id.background, colors.get(skin).get(i));
                views.addView(linearLayoutId[0][i], view1);
            }
        }
        //第三四五节课
        for (int i = 0; i < 5; i++) {
            if (!tableList.get(1).get(i).equals(" ")) {
                if (isTwoClass(tableList.get(1).get(i))) {
                    RemoteViews view2 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_half);
                    view2.setTextViewText(R.id.scheduleName, getClassName(tableList.get(1).get(i)));
                    view2.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(1).get(i)));
                    view2.setImageViewResource(R.id.background, colors.get(skin).get(i > 1 ? i - 2 : i + 4));
                    views.addView(linearLayoutId[1][i], view2);
                } else {
                    RemoteViews view2 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview);
                    view2.setTextViewText(R.id.scheduleName, getClassName(tableList.get(1).get(i)));
                    view2.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(1).get(i)));
                    view2.setImageViewResource(R.id.background, colors.get(skin).get(i > 1 ? i - 2 : i + 4));
                    views.addView(linearLayoutId[1][i], view2);
                }
            }
        }
        //第六七节课
        for (int i = 0; i < 5; i++) {
            if (!tableList.get(2).get(i).equals(" ")) {
                RemoteViews view3 = null;
                if (!tableList.get(3).get(i).equals(" ")) {
                    if (isOneClass(tableList.get(3).get(i)))
                        view3 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview);
                } else
                    view3 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_half);
                view3.setTextViewText(R.id.scheduleName, getClassName(tableList.get(2).get(i)));
                view3.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(2).get(i)));
                view3.setImageViewResource(R.id.background, colors.get(skin).get(i > 3 ? i - 4 : i + 2));
                views.addView(linearLayoutId[2][i], view3);
            }
        }
        //第八九节课
        for (int i = 0; i < 5; i++) {
            if (!tableList.get(3).get(i).equals(" ")) {
                if (!isOneClass(tableList.get(3).get(i))) {
                    RemoteViews view4 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview);
                    view4.setTextViewText(R.id.scheduleName, getClassName(tableList.get(3).get(i)));
                    view4.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(3).get(i)));
                    view4.setImageViewResource(R.id.background, colors.get(skin).get(i + 1));
                    views.addView(linearLayoutId[3][i], view4);
                }
            }
        }
        //第十十一十二节课
        for (int i = 0; i < 5; i++) {
            if (!tableList.get(4).get(i).equals(" ")) {
                RemoteViews view5 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview);
                view5.setTextViewText(R.id.scheduleName, getClassName(tableList.get(4).get(i)));
                view5.setTextViewText(R.id.scheduleLocation, getClassLocation(tableList.get(4).get(i)));
                view5.setImageViewResource(R.id.background, colors.get(skin).get(i > 2 ? i - 3 : i + 3));
                views.addView(linearLayoutId[4][i], view5);
            }
        }
        switch (skin) {
            case 0:
                RemoteViews views_background = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_background);
                views.addView(R.id.background, views_background);
            case 1:
                RemoteViews views_background1 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_background1);
                views.addView(R.id.background, views_background1);
            case 2:
                RemoteViews views_background2 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_background2);
                views.addView(R.id.background, views_background2);
            case 3:
                RemoteViews views_background3 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_background3);
                views.addView(R.id.background, views_background3);
            case 4:
                RemoteViews views_background4 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_background4);
                views.addView(R.id.background, views_background4);
            case 5:
                if (preferences.getInt("custom_bigBackground", 0) != 0){
                    RemoteViews views_background5 = new RemoteViews(context.getPackageName(), R.layout.widget_remoteview_background5);
                    views_background5.setImageViewResource(R.id.background5,preferences.getInt("custom_bigBackground",0));
                    views.addView(R.id.background,views_background5);
                }
        }
        return views;
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
