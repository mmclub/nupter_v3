package org.nupter.nupter.utils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.content.Context;
import android.widget.RemoteViews;
import org.nupter.nupter.R;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-20
 * Time: 下午8:47
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleWidgetCreate extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.i("TAG", "this is [" + appWidgetId + "] onUpdate!");
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.i("TAG", "this is [" + appWidgetId + "] onDelete!");
        }
    }
}
